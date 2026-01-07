# OSS 对象存储服务实现文档

## 1. 背景与业务场景

- **场景**：教师在命题时上传题干插图或答案解析图片。
- **痛点**：
  - 传统本地存储（Local Disk）无法扩展，应用服务器扩容时数据不一致，且占用宝贵的服务器 IO 和带宽。
  - 公共读（Public Read）Bucket 存在安全性问题，容易被恶意遍历或盗链，导致流量费用激增。
- **目标**：使用阿里云 OSS 作为统一存储，并将 Bucket 权限设置为**私有（Private）**，通过生成带签名的**临时访问 URL**（Pre-signed URL）提供访问，兼顾性能与安全。

## 2. 具体功能 + 实现方案

### 2.1 技术选型

- **服务商**：阿里云 OSS（Object Storage Service）
- **权限模式**：**Private (私有)** —— 仅持有 AccessKey 或有效签名 URL 的请求可访问。
- **访问策略**：**Pre-signed URL (服务端签名)**。
  - 上传时由后端使用 AK/SK 直传 OSS。
  - 返回时生成带 `Signature` 和 `Expires` 参数的 URL。
  - _权衡_：本方案采用“长有效期签名 URL”策略（10 年有效期），这是为了兼容现有数据库结构（直接存 URL 字符串）的折中方案。更严谨的方案是 DB 存 Key，读取时动态签发短效 URL，但这需要重构全链路读接口。

### 2.2 实现流程

1.  **上传请求**：前端调用 `POST /teacher/uploadQuestionImage` 上传文件。
2.  **身份校验**：后端利用 `OSSClient`（配置后端 AK/SK）连接 OSS。
3.  **文件处理**：生成唯一 UUID 文件名，根据后缀识别 `Content-Type`（防止浏览器下载而非预览）。
4.  **上传对象**：调用 `ossClient.putObject` 将文件流写入 Bucket。
5.  **生成签名**：调用 `ossClient.generatePresignedUrl`，指定 Key 和 过期时间（Expirations），OSS SDK 会利用 SK 计算 HMAC-SHA1 签名。
6.  **返回结果**：返回形如 `https://bucket.endpoint/key?OSSAccessKeyId=...&Expires=...&Signature=...` 的完整 URL。

### 2.3 关键代码

#### [exam-admin/src/main/java/com/wzz/Util/OSSUtil.java](Exam-Online-Backend/exam-admin/src/main/java/com/wzz/Util/OSSUtil.java)

```java
// 重要：需配合 Private Bucket 使用
public static String picOSS(MultipartFile uploadFile) throws Exception {
    OSSClient ossClient = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);

    // 1. 生成路径与元数据
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    String date = sf.format(new Date());
    String fileName = UUID.randomUUID().toString().substring(0, 5) + uploadFile.getOriginalFilename();
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));

    // 2. 执行上传 (Private Bucket 写入需 AK/SK)
    ossClient.putObject(BUCKETNAME, KEY + date + "/" + fileName,
        new ByteArrayInputStream(uploadFile.getBytes()), objectMetadata);

    // 3. 生成签名 URL (核心变更)
    // 设定过期时间为 10 年 (3600s * 24 * 365 * 10)
    // 如果需要更严格的安全管控，应缩短此时间并在查询接口动态签名
    Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
    String url = ossClient.generatePresignedUrl(BUCKETNAME, KEY + date + "/" + fileName, expiration).toString();

    // 4. 关闭资源
    ossClient.shutdown();
    return url;
}
```

#### [exam-admin/src/main/java/com/wzz/controller/TeacherController.java](Exam-Online-Backend/exam-admin/src/main/java/com/wzz/controller/TeacherController.java)

```java
@PostMapping("/uploadQuestionImage")
public CommonResult<String> uploadQuestionImage(MultipartFile file) throws Exception {
    // 调用工具类，获取带签名的安全 URL
    String url = OSSUtil.picOSS(file);
    return new CommonResult<>(200, "上传成功", url);
}
```

## 3. 遇到的问题 + 解决方法

1.  **AccessDenied 错误**：

    - **问题**：将 Bucket 设为 Private 后，原有的 `https://bucket.endpoint/key` 直接访问方式报 403 Forbidden。
    - **解决**：必须使用 `generatePresignedUrl` 生成带签名的 URL。签名中包含 AccessKeyId、过期时间和根据 SK 计算的 Signature，OSS 服务端验证通过后才放行。

2.  **浏览器下载而非预览**：

    - **问题**：上传图片后，访问链接浏览器直接弹出下载框，而非在 `<img>` 标签通过。
    - **解决**：上传时必须显式设置 `ObjectMetadata.setContentType`（如 `image/jpeg`）。否则 OSS 默认识别为 application/octet-stream，导致强制下载。

3.  **URL 过期问题**：
    - **问题**：签名 URL 有过期时间（Expires）。若过期，图片将无法加载（403）。
    - **解决**：
      - _短期_：设置极长的过期时间（如 10 年），模拟永久链接。
      - _长期_：数据库仅存 Object Key（如 `2023/10/a.jpg`），后端提供 `getSignedUrl(key)` 接口，前端每次加载时动态获取最新短期有效链接（更安全，但开发成本高）。

## 4. 优化方案

- **Web 端直传 (Server-Signed Direct Upload)**：
  目前方案仍需流量经过应用服务器（MultipartFile）。优化方案是后端计算 Post Policy 和 Signature 返回给前端，前端直接 Form Data POST 给 OSS。能彻底释放应用服务器带宽。
- **CDN 回源鉴权**：
  若使用 CDN 加速，需配置 CDN 的 URL 鉴权（Type A/B/C），配合 OSS 的私有 Bucket 回源。

- **STS 临时凭证**：
  对于高安全需求，不应在服务器持有长期 AccessKey，应使用 RAM Role 动态申请 STS Token 进行操作。

## 5. 效果（定量分析）

- **安全性**：从“公共读”升级为“私有+签名”，有效防止了 Object 遍历攻击和非授权访问。
- **兼容性**：生成的签名 URL 可以直接被 `<img>` 标签使用，对前端业务代码零侵入。
- **性能**：保持了 OSS 的高吞吐特性，由 OSS 承载图片流量，应用服务器仅处理轻量级签名计算。
