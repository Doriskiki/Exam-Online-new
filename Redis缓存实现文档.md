# Redis 缓存实现文档（“千人同考”场景）

## 1. 业务场景

- 高并发考试（千人同考）时，试卷结构、题库、题目详情等热点数据频繁被读，直接查 MySQL 会造成压力与响应抖动。
- 目标：将试卷结构和题目热点数据缓存到 Redis，降低 DB 读压力，提升首屏与交卷查询速度。

## 2. 技术方案

- 技术栈：Spring Boot + Spring Data Redis（已在 `application.yml` 配置）。
- 策略：读缓存命中返回；未命中查询 DB 后写入缓存，TTL=5 分钟 + 随机 0-120 秒，防雪崩。更新/删除时按业务点位清理相关 Key。
- 关注点：
  - 数据一致性：变更题库/题目/试卷后显式删除对应缓存。
  - 防击穿：短 TTL + 预热/异步填充可选；已有基础 TTL+随机抖动。

## 3. 关键配置

```yaml
# exam-admin/src/main/resources/application.yml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 1800000
    password: 123456
```

## 4. 关键代码（粘贴可复用）

### 4.1 题库列表缓存

```java
// exam-admin/src/main/java/com/wzz/controller/TeacherController.java
@GetMapping("/getQuestionBank")
public CommonResult<Object> getQuestionBank() {
    if (redisUtil.get("questionBanks") != null) {
        return new CommonResult<>(200, "success", redisUtil.get("questionBanks"));
    } else {
        List<QuestionBank> questionBanks = questionBankService.list(new QueryWrapper<>());
        int ttl = 60 * 5 + new Random().nextInt(120);
        redisUtil.set("questionBanks", questionBanks, ttl);
        return new CommonResult<>(200, "success", questionBanks);
    }
}
```

### 4.2 题目详情缓存

```java
// exam-admin/src/main/java/com/wzz/controller/TeacherController.java
@GetMapping("/getQuestionById/{id}")
public CommonResult<Object> getQuestionById(@PathVariable Integer id) {
    if (redisUtil.get("questionVo:" + id) != null) {
        return new CommonResult<>(200, "查询题目信息成功", redisUtil.get("questionVo:" + id));
    }
    // ... 组装 QuestionVo ...
    int ttl = 60 * 5 + new Random().nextInt(120);
    redisUtil.set("questionVo:" + id, questionVo, ttl);
    return new CommonResult<>(200, "查询成功", questionVo);
}
```

### 4.3 按题库拉取题目列表缓存

```java
// exam-admin/src/main/java/com/wzz/controller/TeacherController.java
@GetMapping("/getQuestionByBank")
public CommonResult<Object> getQuestionByBank(Integer bankId) {
    if (redisUtil.get("questionBankQuestion:" + bankId) != null) {
        return new CommonResult<>(200, "当前题库题目查询成功", redisUtil.get("questionBankQuestion:" + bankId));
    }
    // ... 组装 questionVos ...
    int ttl = 60 * 5 + new Random().nextInt(120);
    redisUtil.set("questionBankQuestion:" + bankId, questionVos, ttl);
    return new CommonResult<>(200, "当前题库题目查询成功", questionVos);
}
```

### 4.4 考试结构缓存（试卷题目 + 元信息）

```java
// exam-admin/src/main/java/com/wzz/controller/TeacherController.java
@GetMapping("/getExamInfoById")
public CommonResult<Object> getExamInfoById(@RequestParam Integer examId) {
    if (redisUtil.get("examInfo:" + examId) != null) {
        return new CommonResult<>(200, "查询成功", redisUtil.get("examInfo:" + examId));
    }
    // ... 读取 Exam 与 ExamQuestion，组装 AddExamByQuestionVo ...
    int ttl = 60 * 5 + new Random().nextInt(120);
    redisUtil.set("examInfo:" + examId, addExamByQuestionVo, ttl);
    return new CommonResult<>(200, "查询成功", addExamByQuestionVo);
}
```

### 4.5 考试题目分值缓存

```java
// exam-admin/src/main/java/com/wzz/controller/TeacherController.java
@GetMapping("/getExamQuestionByExamId/{examId}")
public CommonResult<Object> getExamQuestionByExamId(@PathVariable Integer examId) {
    if (redisUtil.get("examQuestion:" + examId) != null) {
        return new CommonResult<>(200, "查询考试中题目和分值成功", redisUtil.get("examQuestion:" + examId));
    }
    ExamQuestion examQuestion = examQuestionService.getOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examId));
    int ttl = 60 * 5 + new Random().nextInt(120);
    redisUtil.set("examQuestion:" + examId, examQuestion, ttl);
    return new CommonResult<>(200, "查询考试中题目和分值成功", examQuestion);
}
```

### 4.6 考试记录查询缓存（查询历史记录时减压 DB）

```java
// exam-admin/src/main/java/com/wzz/controller/TeacherController.java
@GetMapping("/getExamRecordById/{recordId}")
public CommonResult<Object> getExamRecordById(@PathVariable Integer recordId) {
    if (redisUtil.get("examRecord:" + recordId) != null) {
        return new CommonResult<>(200, "考试信息查询成功", redisUtil.get("examRecord:" + recordId));
    }
    ExamRecord examRecord = examRecordService.getOne(new QueryWrapper<ExamRecord>().eq("record_id", recordId));
    int ttl = 60 * 5 + new Random().nextInt(120);
    redisUtil.set("examRecord:" + recordId, examRecord, ttl);
    return new CommonResult<>(200, "考试信息查询成功", examRecord);
}
```

### 4.7 失效与同步

- 题目/题库/考试更新时会调用 `redisUtil.del(...)` 清理相关 key（示例：更新题库/题目后删除 `questionBanks`、`questionVo:{id}`、`questionBankQuestion:{bankId}`；更新考试后删除 `examInfo:{examId}`）。

## 5. 遇到的问题与解决

- 雪崩/击穿：采用 TTL + 随机抖动；可在大考前预热关键 Key。
- 数据一致性：通过更新/删除操作显式 `del` 缓存，必要时可加版本号或双删策略（先删缓存 → 写库 → 延迟再删）。
- 热点搬迁：key 前缀清晰，便于按考试 ID/题库 ID 做定向清理。

## 6. 优化方案

- 使用 Redis Hash/JSON（如 RedisJSON）存储大对象，减少序列化开销。
- 引入本地缓存二级（Caffeine + Redis）降低网络开销。
- 对超热点 Key 加入互斥锁（Redis 分布式锁）防止并发击穿。
- 预热脚本：大考开始前批量加载考试结构与题库到缓存。

## 7. 效果（定量估计）

- 命中率假设 80%：DB 读 QPS 可下降约 4-5 倍，主库压力显著降低。
- 单次查询延迟：Redis 命中 ~1-3ms，DB 查询 ~30-80ms（本地口径），首屏/进入考试页面的 P95 可下降一个数量级。
- 可用性：Redis 持久化/主从可进一步提升；宕机时自动回退到 DB 查询，功能不受阻断。
