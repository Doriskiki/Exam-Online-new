// ========================================
// ExamPage.vue 人脸识别集成补丁
// ========================================
// 使用说明：将以下代码添加到 ExamPage.vue 的对应位置

// ========================================
// 1. 在 data() 中添加以下变量
// ========================================
data() {
  return {
    // ... 保留原有变量 ...
    
    // ✨ 新增：人脸识别相关变量
    faceVerifyTimer: null,           // 定时器
    faceVerifyInterval: 120000,      // 2分钟验证一次（120000毫秒 = 2分钟）
    faceVerifyCount: 0,              // 验证次数计数
    faceVerifyFailCount: 0,          // 失败次数计数
    maxFaceVerifyFail: 3,            // 最多允许3次失败
  }
},

// ========================================
// 2. 修改 created() 方法
// ========================================
created() {
  this.getExamInfo()
  this.loading = this.$Loading.service({
    body: true,
    lock: true,
    text: '数据拼命加载中,(*╹▽╹*)',
    spinner: 'el-icon-loading',
  })
  
  window.onload = () => {
    setTimeout(() => {
      this.getCamera()
      // ✨ 新增：启动定时人脸识别验证
      this.startFaceVerifyTimer()
    }, 1000)

    // 保留原有的截图代码
    let times = []
    for (let i = 0; i < 2; i++) {
      times.push(Math.ceil(Math.random() * this.duration * 1000))
    }
    times.push(10000)
    times.forEach(item => {
      window.setTimeout(() => {
        this.takePhoto()
      }, item)
    })
  }
},

// ========================================
// 3. 添加 beforeDestroy() 生命周期钩子
// ========================================
beforeDestroy() {
  // 清除人脸识别定时器
  if (this.faceVerifyTimer) {
    clearInterval(this.faceVerifyTimer)
  }
},

// ========================================
// 4. 在 methods 中添加以下方法
// ========================================
methods: {
  // ... 保留原有方法 ...
  
  // ✨ 新增：启动定时人脸识别验证
  startFaceVerifyTimer() {
    console.log('启动考试中人脸识别验证')
    
    // 第一次验证在30秒后进行
    setTimeout(() => {
      console.log('执行第一次人脸验证')
      this.verifyFaceDuringExam()
    }, 30000)
    
    // 之后每2分钟进行一次验证
    this.faceVerifyTimer = setInterval(() => {
      console.log('执行定时人脸验证')
      this.verifyFaceDuringExam()
    }, this.faceVerifyInterval)
  },
  
  // ✨ 新增：考试中人脸识别验证
  async verifyFaceDuringExam() {
    // 检查摄像头是否开启
    if (!this.cameraOn) {
      this.$message.warning('摄像头未开启，请开启摄像头继续考试')
      return
    }

    try {
      // 获取当前视频帧
      let video = document.getElementById('video')
      let canvas = document.getElementById('canvas')
      
      if (!video || !canvas) {
        console.error('视频或画布元素未找到')
        return
      }
      
      let ctx = canvas.getContext('2d')
      ctx.drawImage(video, 0, 0, 200, 200)
      let imageBase64 = canvas.toDataURL('image/jpeg', 0.8)

      // 获取当前用户ID（从localStorage或examInfo）
      const userId = localStorage.getItem('userId') || this.examInfo.userId
      
      if (!userId) {
        console.error('无法获取用户ID')
        return
      }

      console.log(`开始第${this.faceVerifyCount + 1}次人脸验证...`)

      // 调用人脸识别接口
      const res = await this.$http.post(
        `${this.API.faceVerifyExam}?userId=${userId}&examId=${this.examInfo.examId}`,
        { image: imageBase64 }
      )

      this.faceVerifyCount++

      if (res.data.code === 200 && res.data.data.matched) {
        // ✅ 验证通过
        const similarity = (res.data.data.similarity * 100).toFixed(2)
        console.log(`✅ 第${this.faceVerifyCount}次人脸验证通过，相似度：${similarity}%`)
        
        // 如果相似度较低，给出警告
        if (res.data.data.similarity < 0.7) {
          this.$message({
            message: `人脸识别相似度较低（${similarity}%），请保持正面面对摄像头`,
            type: 'warning',
            duration: 3000
          })
        } else {
          // 验证通过，显示成功提示（可选）
          this.$message({
            message: `人脸验证通过（${similarity}%）`,
            type: 'success',
            duration: 2000,
            showClose: true
          })
        }
        
        // 重置失败计数
        this.faceVerifyFailCount = 0
        
      } else {
        // ❌ 验证失败
        this.faceVerifyFailCount++
        console.error(`❌ 第${this.faceVerifyCount}次人脸验证失败（${this.faceVerifyFailCount}/${this.maxFaceVerifyFail}）`)
        
        this.$message({
          message: `人脸验证失败（${this.faceVerifyFailCount}/${this.maxFaceVerifyFail}），请确保本人在考试`,
          type: 'error',
          duration: 5000,
          showClose: true
        })
        
        // 如果失败次数达到上限，强制提交试卷
        if (this.faceVerifyFailCount >= this.maxFaceVerifyFail) {
          this.$alert(
            '人脸验证多次失败，为保证考试公平性，系统将自动提交试卷',
            '警告',
            {
              confirmButtonText: '确定',
              type: 'error',
              showClose: false,
              callback: () => {
                this.forceSubmitExam('人脸验证失败')
              }
            }
          )
        }
      }
    } catch (error) {
      console.error('人脸识别验证异常:', error)
      // 网络错误或服务异常，不影响考试继续进行
      // 只记录错误，不强制提交
    }
  },
  
  // ✨ 新增：强制提交试卷
  async forceSubmitExam(reason) {
    console.log(`强制提交试卷，原因：${reason}`)
    
    // 拍照并关闭摄像头
    if (this.cameraOn) {
      await this.takePhoto()
      this.closeCamera()
    }
    
    // 清除所有定时器
    if (this.faceVerifyTimer) {
      clearInterval(this.faceVerifyTimer)
    }
    if (this.timer) {
      clearInterval(this.timer)
    }

    // 构造提交数据
    let data = {}
    data.questionIds = []
    data.userAnswers = this.userAnswer.join('-')
    data.examId = parseInt(this.$route.params.examId)
    data.creditImgUrl = this.takePhotoUrl.join(',')
    data.remark = reason // 添加备注说明强制提交原因
    
    // 处理题目ID和答案
    this.questionInfo.forEach((item, index) => {
      data.questionIds.push(item.questionId)
      // 未作答的题目补充空答案
      if (index >= this.userAnswer.length) {
        data.userAnswers += '- '
      }
    })
    
    // 如果所有题目都未作答
    if (this.userAnswer.length === 0) {
      this.questionInfo.forEach(item => {
        data.userAnswers += ' -'
      })
    }
    
    data.questionIds = data.questionIds.join(',')
    
    // 提交试卷
    this.$http.post(this.API.addExamRecord, data).then((resp) => {
      if (resp.data.code === 200) {
        this.$notify({
          title: '考试已结束',
          message: reason,
          type: 'warning',
          duration: 3000
        })
        // 跳转到成绩页面
        this.$router.push('/examResult/' + resp.data.data)
      }
    }).catch(error => {
      console.error('提交试卷失败:', error)
      this.$message.error('提交试卷失败，请联系管理员')
    })
  },
}

// ========================================
// 5. 在登录时保存 userId 到 localStorage
// ========================================
// 在 Login.vue 的登录成功后添加：
// localStorage.setItem('userId', resp.data.data.userId)

// ========================================
// 使用说明
// ========================================
/*
1. 将上述代码复制到 ExamPage.vue 的对应位置
2. 确保 Python 人脸识别服务已启动（端口5000）
3. 确保 Java 后端已启动（端口8889）
4. 确保在登录时保存了 userId 到 localStorage

测试步骤：
1. 登录系统
2. 选择考试并通过人脸验证进入
3. 等待30秒，观察第一次验证
4. 继续等待2分钟，观察定时验证
5. 故意遮挡面部，测试验证失败
6. 验证失败3次后观察是否强制提交

配置参数：
- faceVerifyInterval: 120000  // 验证间隔（毫秒）
- maxFaceVerifyFail: 3         // 最多允许失败次数
- 第一次验证延迟: 30000        // 30秒后第一次验证
*/
