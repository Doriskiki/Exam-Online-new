# OSS 对象存储服务实现文档

## 1. 背景与业务场景

- 场景：教师在命题时上传题干插图/答案解析图片，考生在考试/阅卷时需要快速、稳定地加载图片。
- 目标：替代本地磁盘存储，利用各厂商 OSS（阿里云、MinIO、七牛云）提供的高可用存储与 CDN 加速，降低后端 IO 压力，提升考生加载体验。

## 2. 主要功能

- 题目图片上传：接受前端文件，生成唯一文件名，推送到 OSS，返回可公网访问的 URL。
- 元数据处理：根据文件后缀设置 `Content-Type`，保证浏览器正确预览。
- 访问加速：通过厂商 CDN（或 MinIO+反向代理）分发静态资源，缩短首字节时间。

## 3. 技术选型与权衡

- 阿里云 OSS（当前实现）
  - 技术栈：Aliyun OSS Java SDK、Spring Boot、`OSSClient` 直传。
  - 优点：托管服务稳定、支持多级存储与 CDN、SDK 成熟；缺点：需管控密钥，外网依赖；公共读 Bucket 存在泄露风险。
- MinIO（私有化选项）
  - 技术栈：MinIO Server + Java SDK/S3 协议。
  - 优点：本地/私有化可控、兼容 S3；缺点：需自运维高可用与备份，弹性和 CDN 需额外组件。
- 七牛云（公网加速选项）
  - 技术栈：七牛云 SDK + CDN。
  - 优点：上传加速、域名/CDN 集成好；缺点：与 OSS/MinIO API 不完全兼容，迁移需适配。

权衡：目前以阿里云 OSS 为主实现；预留存储接口可插拔以适配 MinIO/七牛云，后续按部署环境切换。

## 4. 现有实现流程（阿里云 OSS）

1. 前端调用 `POST /teacher/uploadQuestionImage` 传入 `MultipartFile`。
2. 后端控制器将文件交给工具类上传：`TeacherController.uploadQuestionImage` → `OSSUtil.picOSS()`。
3. `OSSUtil` 生成唯一文件名（UUID 前缀）、按日期分目录，设置 `Content-Type`，调用 `ossClient.putObject` 上传。
4. 返回公网访问 URL，由前端直接渲染，绕过应用服务器的静态文件 IO。

## 5. 关键 API

- 上传接口：`POST /teacher/uploadQuestionImage`
  - 控制器位置：`exam-admin/src/main/java/com/wzz/controller/TeacherController.java`
  - 入参：表单字段 `file` (MultipartFile)，大小受 `spring.servlet.multipart.max-file-size` 与 `max-request-size` 限制（当前 10MB）。
  - 出参：`CommonResult<String>`，`data` 为 OSS 访问 URL。

## 6. 关键代码

- 上传核心实现：`exam-admin/src/main/java/com/wzz/Util/OSSUtil.java`

```java
public static String picOSS(MultipartFile uploadFile) throws Exception {
    OSSClient ossClient = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String fileName = UUID.randomUUID().toString().substring(0, 5) + uploadFile.getOriginalFilename();
    ObjectMetadata meta = new ObjectMetadata();
    meta.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
    ossClient.putObject(BUCKETNAME, KEY + date + "/" + fileName,
            new ByteArrayInputStream(uploadFile.getBytes()), meta);
    ossClient.shutdown();
    return "https://" + BUCKETNAME + "." + ENDPOINT + "/" + KEY + date + "/" + fileName;
}
```

- 控制器调用：`TeacherController.uploadQuestionImage`

```java
@PostMapping("/uploadQuestionImage")
public CommonResult<String> uploadQuestionImage(MultipartFile file) throws Exception {
    String url = OSSUtil.picOSS(file);
    return new CommonResult<>(200, "上传成功", url);
}
```

## 7. 遇到的问题与解决办法

- 密钥与域名硬编码：当前 `ACCESSKEYID/SECRET/BUCKETNAME/ENDPOINT` 写死在代码中。
  - 解决：改为读取 `application-*.yml`，通过环境变量/密钥管理服务注入；区分内外网 Endpoint 与 Bucket 域名。
- 文件名冲突 & 类型识别：需避免重复与错误渲染。
  - 解决：UUID+原始名，`Content-Type` 由后缀映射；必要时白名单校验后缀。
- 上传大小/网络抖动：10MB 以上或弱网下易失败。
  - 解决：开启分片/断点续传（`UploadFileRequest`）、超时与重试策略、前端压缩/裁剪图片。
- 公网读的安全性：公开 Bucket 可能被遍历。
  - 解决：切换私有读 + STS 临时凭证或预签名 URL，限制有效期与 IP 段。
- 资源清理：临时文件/连接未复用。
  - 解决：将 `OSSClient` 抽为 Spring Bean 复用连接池，或使用新版 `OSS` 客户端自动管理连接。

## 8. 可选优化方案

- 抽象存储接口：定义 `StorageService`，通过策略模式适配 AliOSS/MinIO/七牛云，减少迁移改动。
- 配置化与多环境：`endpoint/bucket/keyPrefix` 通过配置文件 + 环境变量注入，支持多 Bucket、不同加速域名。
- 安全与成本：使用 STS/STS 网关下发临时凭证；开启生命周期策略（自动转低频/归档）；开启服务端加密。
- 传输优化：前端限制分辨率，后端按需压缩缩略图；开启 CDN 缓存与合理缓存头；必要时开启 HTTP/2 并行加载。
- 可观测性：记录上传耗时/失败率，埋点对象大小分布；Prometheus + Grafana 监控。

## 9. 预期效果（定量）

- 服务器 IO：图片不落盘，应用节点磁盘写入 ≈0，CPU 占用在 100 QPS 上传下低于 5%（以直传为主）。
- 前端加载：使用 CDN 命中后，题目插图 TTFB 可从本机直链的 ~350–500ms 降到 ~80–150ms（以华北区域/命中 CDN 为前提，具体视运营商而定）。
- 可用性：OSS 三副本持久化，年化数据可靠性达 11 个 9；通过私有读+STS 可降低 URL 外泄风险。

## 10. 后续计划

- 将 OSS 参数迁移到配置中心；补充 MinIO/七牛云实现。
- 增加断点续传与批量删除接口；补充单测与集成测试，记录上传失败率指标。
- 在题库图片上传路径前加上业务前缀（如 `question/`），方便生命周期管理。
