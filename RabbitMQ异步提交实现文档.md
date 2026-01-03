# RabbitMQ 异步提交流程实现文档

## 1. 业务场景

- 考生点击“提交试卷”时高峰并发，直接写库易造成数据库压力与响应超时。
- 目标：前端快速返回“已提交”，后台通过 MQ 异步落库与评分，削峰解耦。

## 2. 技术方案

- 技术选型：RabbitMQ + Spring AMQP（`spring-boot-starter-amqp`）。
- 路由模型：Direct Exchange + 持久化队列。
- 消息格式：JSON（Jackson2JsonMessageConverter）。
- 消息体：`ExamSubmitMessage`，包含 `username` + `examRecord`（题目 ID、答案、考试 ID 等）。

## 3. 配置

- 依赖：`exam-admin/pom.xml` 引入 `spring-boot-starter-amqp`。
- 连接配置（application.yml）：

  ```yaml
  spring:
    rabbitmq:
      host: localhost
      port: 5672
      username: guest
      password: guest
      virtual-host: /

  mq:
    exam:
      exchange: exam.submit.exchange
      queue: exam.submit.queue
      routing-key: exam.submit.key
  ```

- Bean 声明（RabbitConfig.java）：

  ```java
  @Configuration
  public class RabbitConfig {
      @Value("${mq.exam.exchange:exam.submit.exchange}")
      private String exchangeName;
      @Value("${mq.exam.queue:exam.submit.queue}")
      private String queueName;
      @Value("${mq.exam.routing-key:exam.submit.key}")
      private String routingKey;

      @Bean
      public DirectExchange examSubmitExchange() {
          return new DirectExchange(exchangeName, true, false);
      }

      @Bean
      public Queue examSubmitQueue() {
          return QueueBuilder.durable(queueName).build();
      }

      @Bean
      public Binding examSubmitBinding(Queue examSubmitQueue, DirectExchange examSubmitExchange) {
          return BindingBuilder.bind(examSubmitQueue).to(examSubmitExchange).with(routingKey);
      }

      @Bean
      public MessageConverter messageConverter() {
          return new Jackson2JsonMessageConverter();
      }

      @Bean
      public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter mc) {
          RabbitTemplate rabbitTemplate = new RabbitTemplate(cf);
          rabbitTemplate.setMessageConverter(mc);
          return rabbitTemplate;
      }
  }
  ```

## 4. 发送流程（提交入口）

- 入口：`POST /teacher/addExamRecord`。
- 关键代码（TeacherController.addExamRecord）：

  ```java
  @PostMapping("/addExamRecord")
  public CommonResult<Integer> addExamRecord(@RequestBody ExamRecord examRecord, HttpServletRequest request) {
      String token = request.getHeader("authorization");
      TokenVo tokenVo = TokenUtils.verifyToken(token);
      List<ExamRecord> examRecords = examRecordService.list(new QueryWrapper<>());
      int id = examRecords.isEmpty() ? 1 : examRecords.get(examRecords.size() - 1).getRecordId() + 1;
      examRecord.setRecordId(id);

      ExamSubmitMessage message = new ExamSubmitMessage();
      message.setUsername(tokenVo.getUsername());
      message.setExamRecord(examRecord);

      rabbitTemplate.convertAndSend(examSubmitExchange, examSubmitRoutingKey, message);
      return new CommonResult<>(200, "考试记录已提交，正在异步保存", id);
  }
  ```

- 步骤：token 取 username → 预生成 recordId → 组装消息 → MQ 投递 → 立即返回。

## 5. 消费流程（异步落库）

- 入口：`@RabbitListener` 监听队列。
- 关键代码（ExamSubmitConsumer.handleExamSubmit）：

  ```java
  @RabbitListener(queues = "${mq.exam.queue:exam.submit.queue}")
  public void handleExamSubmit(ExamSubmitMessage message) {
    if (message == null || message.getExamRecord() == null) return;
    ExamRecord examRecord = message.getExamRecord();
    String username = message.getUsername();

    User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
    if (user == null) return;
    examRecord.setUserId(user.getId());

    if (examRecord.getRecordId() == null) {
      List<ExamRecord> examRecords = examRecordService.list(new QueryWrapper<>());
      int id = examRecords.isEmpty() ? 1 : examRecords.get(examRecords.size() - 1).getRecordId() + 1;
      examRecord.setRecordId(id);
    }

    try {
      List<Answer> answers = answerService.list(new QueryWrapper<Answer>()
          .in("question_id", Arrays.asList(examRecord.getQuestionIds().split(","))));
      Map<String, String> scoreMap = new HashMap<>();
      ExamQuestion examQuestion = examQuestionService.getOne(new QueryWrapper<ExamQuestion>()
          .eq("exam_id", examRecord.getExamId()));
      if (examQuestion != null) {
        String[] ids = examQuestion.getQuestionIds().split(",");
        String[] scores = examQuestion.getScores().split(",");
        for (int i = 0; i < ids.length; i++) scoreMap.put(ids[i], scores[i]);
      }
      int logicScore = 0;
      StringBuilder errorIds = new StringBuilder();
      String[] userAnswers = examRecord.getUserAnswers().split("-");
      String[] recordQuestionIds = examRecord.getQuestionIds().split(",");
      for (int i = 0; i < recordQuestionIds.length; i++) {
        int index = SaltEncryption.getIndex(answers, Integer.parseInt(recordQuestionIds[i]));
        if (index != -1) {
          if (Objects.equals(userAnswers[i], answers.get(index).getTrueOption())) {
            logicScore += Integer.parseInt(scoreMap.getOrDefault(recordQuestionIds[i], "0"));
          } else {
            errorIds.append(recordQuestionIds[i]).append(",");
          }
        }
      }
      examRecord.setLogicScore(logicScore);
      if (errorIds.length() > 0) examRecord.setErrorQuestionIds(errorIds.substring(0, errorIds.length() - 1));
    } catch (Exception e) {
      // 记录异常，继续保存已有数据
    }

    examRecord.setExamTime(new Date());
    examRecordService.save(examRecord);
  }
  ```

## 6. 关键代码位置

- 发送端：`TeacherController.addExamRecord` [exam-admin/src/main/java/com/wzz/controller/TeacherController.java](Exam-Online-Backend/exam-admin/src/main/java/com/wzz/controller/TeacherController.java#L1093-L1121)
- 消费端：`ExamSubmitConsumer.handleExamSubmit` [exam-admin/src/main/java/com/wzz/mq/ExamSubmitConsumer.java](Exam-Online-Backend/exam-admin/src/main/java/com/wzz/mq/ExamSubmitConsumer.java)
- MQ 配置：`RabbitConfig` [exam-admin/src/main/java/com/wzz/config/RabbitConfig.java](Exam-Online-Backend/exam-admin/src/main/java/com/wzz/config/RabbitConfig.java)
- 消息模型：`ExamSubmitMessage` [exam-admin/src/main/java/com/wzz/mq/ExamSubmitMessage.java](Exam-Online-Backend/exam-admin/src/main/java/com/wzz/mq/ExamSubmitMessage.java)

## 7. 遇到的问题与解决

- 消息序列化：使用 Jackson JSON 转换器，避免 Java 序列化版本兼容问题。
- 幂等与重复消费：仍沿用 recordId 递增生成；如需更强幂等，可增加基于 `recordId` 或 `submitId` 的唯一索引/去重表。
- 评分逻辑耦合：将原同步评分逻辑迁移到消费者，保持行为一致。

## 8. 可选优化

- 延迟/重试：为队列配置 DLX + 重试间隔，失败消息进入死信队列告警。
- 监控：暴露 RabbitMQ 指标（投递成功率、消费耗时、死信量）至 Prometheus/Grafana。
- 唯一主键：改为数据库自增或基于雪花算法生成 `recordId`，降低并发冲突风险。
- 业务分片：按考试 ID 分队列/路由键，减少热点。

## 9. 预期效果（定量估计）

- 前端响应：提交接口不再等待写库，响应时间由写库 RT（~50-200ms）降为 MQ 投递 RT（~5-20ms，本地局域网场景）。
- 削峰能力：队列可平滑吸收高峰；消费端可水平扩展以匹配写库吞吐。
- 可靠性：消息持久化 + 手动 ack（当前为默认 auto，可按需改为手动）可避免宕机数据丢失。
