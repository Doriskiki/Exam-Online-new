<template>
  <div class="face-register-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: bold;">人脸注册</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回</el-button>
      </div>

      <div class="content">
        <!-- 摄像头视频区域 -->
        <div class="video-container">
          <video ref="video" width="640" height="480" autoplay></video>
          <canvas ref="canvas" width="640" height="480" style="display: none;"></canvas>
        </div>

        <!-- 提示信息 -->
        <div class="tips">
          <el-alert
            title="温馨提示"
            type="info"
            :closable="false"
            show-icon>
            <ul>
              <li>请确保光线充足，面部清晰可见</li>
              <li>请正面面对摄像头，保持自然表情</li>
              <li>注册成功后，考试前需要进行人脸验证</li>
            </ul>
          </el-alert>
        </div>

        <!-- 操作按钮 -->
        <div class="actions">
          <el-button type="primary" @click="startCamera" v-if="!cameraStarted" icon="el-icon-video-camera">
            打开摄像头
          </el-button>
          <el-button type="success" @click="captureAndRegister" v-if="cameraStarted" icon="el-icon-camera">
            拍照并注册
          </el-button>
          <el-button type="warning" @click="stopCamera" v-if="cameraStarted" icon="el-icon-switch-button">
            关闭摄像头
          </el-button>
        </div>

        <!-- 注册状态 -->
        <div class="status" v-if="registerStatus">
          <el-alert
            :title="registerStatus.message"
            :type="registerStatus.type"
            :closable="false"
            show-icon>
          </el-alert>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'FaceRegister',
  data() {
    return {
      cameraStarted: false,
      stream: null,
      registerStatus: null,
      currentUser: null
    }
  },
  mounted() {
    this.getCurrentUserInfo()
  },
  beforeDestroy() {
    this.stopCamera()
  },
  methods: {
    // 获取当前用户信息
    async getCurrentUserInfo() {
      try {
        console.log('🔍 正在获取用户信息...')
        const res = await this.$http.get(this.API.getCurrentUser)
        console.log('📥 getCurrentUser 响应:', res.data)
        
        if (res.data.code === 200) {
          this.currentUser = res.data.data
          console.log('✓ 当前用户信息:', this.currentUser)
          console.log('📋 可用字段:', Object.keys(this.currentUser))
          
          // 尝试多种方式获取 userId
          const userId = this.currentUser.userId || 
                        this.currentUser.id || 
                        this.currentUser.user_id ||
                        localStorage.getItem('userId')
          
          if (userId) {
            this.currentUser.userId = userId
            console.log('✓ 成功获取 userId:', userId)
          } else {
            console.error('❌ 无法获取 userId，请检查后端返回的数据结构')
            this.$message.warning('无法获取用户ID，请尝试重新登录或手动设置')
          }
        }
      } catch (error) {
        this.$message.error('获取用户信息失败')
        console.error('❌ 获取用户信息错误:', error)
      }
    },

    // 打开摄像头
    async startCamera() {
      try {
        this.stream = await navigator.mediaDevices.getUserMedia({ 
          video: { 
            width: 640, 
            height: 480,
            facingMode: 'user'
          } 
        })
        this.$refs.video.srcObject = this.stream
        this.cameraStarted = true
        this.$message.success('摄像头已打开')
      } catch (error) {
        this.$message.error('无法访问摄像头，请检查权限设置')
        console.error('摄像头错误:', error)
      }
    },

    // 关闭摄像头
    stopCamera() {
      if (this.stream) {
        this.stream.getTracks().forEach(track => track.stop())
        this.stream = null
        this.cameraStarted = false
        this.$message.info('摄像头已关闭')
      }
    },

    // 捕获当前帧
    captureFrame() {
      const video = this.$refs.video
      const canvas = this.$refs.canvas
      const ctx = canvas.getContext('2d')
      
      ctx.drawImage(video, 0, 0, 640, 480)
      return canvas.toDataURL('image/jpeg', 0.8)
    },

    // 拍照并注册人脸
    async captureAndRegister() {
      if (!this.currentUser) {
        this.$message.error('用户信息未加载')
        return
      }

      // 获取用户ID和用户名，支持多种字段名
      const userId = this.currentUser.userId || 
                     this.currentUser.id || 
                     this.currentUser.user_id ||
                     localStorage.getItem('userId')
      const userName = this.currentUser.username || 
                      this.currentUser.trueName || 
                      this.currentUser.name ||
                      this.currentUser.userName
      
      console.log('🔍 尝试获取用户信息...')
      console.log('  - userId:', userId)
      console.log('  - userName:', userName)
      console.log('  - currentUser:', this.currentUser)
      console.log('  - localStorage userId:', localStorage.getItem('userId'))
      
      if (!userId || !userName) {
        console.error('❌ 用户信息不完整!')
        console.error('  - 可用字段:', Object.keys(this.currentUser))
        console.error('  - localStorage:', {
          userId: localStorage.getItem('userId'),
          authorization: localStorage.getItem('authorization')
        })
        
        this.$message.error('无法获取用户信息，请重新登录或查看控制台日志')
        
        // 显示详细的错误提示
        this.$alert(
          `无法获取用户信息。请按 F12 打开控制台查看详细日志，或尝试以下操作：\n\n` +
          `1. 清除缓存：localStorage.clear()\n` +
          `2. 重新登录\n` +
          `3. 或手动设置：localStorage.setItem('userId', '你的用户ID')`,
          '错误提示',
          { type: 'error' }
        )
        return
      }

      console.log('✓ 准备注册人脸 - userId:', userId, 'userName:', userName)

      const loading = this.$loading({
        lock: true,
        text: '正在注册人脸...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      try {
        // 1. 先检测人脸
        const imageBase64 = this.captureFrame()
        const detectRes = await this.$http.post(this.API.faceDetect, {
          image: imageBase64
        })

        if (detectRes.data.code !== 200 || !detectRes.data.data.success) {
          this.registerStatus = {
            type: 'error',
            message: '未检测到人脸，请调整位置后重试'
          }
          loading.close()
          return
        }

        const faceCount = detectRes.data.data.count
        if (faceCount === 0) {
          this.registerStatus = {
            type: 'error',
            message: '未检测到人脸，请确保面部清晰可见'
          }
          loading.close()
          return
        } else if (faceCount > 1) {
          this.registerStatus = {
            type: 'warning',
            message: `检测到${faceCount}张人脸，请确保只有一人`
          }
          loading.close()
          return
        }

        // 2. 注册人脸
        console.log('开始注册人脸，URL:', `${this.API.faceRegister}?userId=${userId}&userName=${userName}`)
        const registerRes = await this.$http.post(
          `${this.API.faceRegister}?userId=${userId}&userName=${userName}`,
          { image: imageBase64 }
        )

        if (registerRes.data.code === 200 && registerRes.data.data.success) {
          this.registerStatus = {
            type: 'success',
            message: '人脸注册成功！'
          }
          this.$message.success('人脸注册成功')
          
          // 3秒后关闭摄像头并返回
          setTimeout(() => {
            this.stopCamera()
            this.goBack()
          }, 3000)
        } else {
          this.registerStatus = {
            type: 'error',
            message: registerRes.data.message || '人脸注册失败'
          }
        }
      } catch (error) {
        console.error('注册错误:', error)
        this.registerStatus = {
          type: 'error',
          message: '注册失败：' + (error.response?.data?.message || error.message)
        }
      } finally {
        loading.close()
      }
    },

    // 返回上一页
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.face-register-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.video-container {
  margin-bottom: 20px;
  border: 2px solid #409EFF;
  border-radius: 8px;
  overflow: hidden;
}

.tips {
  width: 100%;
  margin-bottom: 20px;
}

.tips ul {
  margin: 10px 0 0 0;
  padding-left: 20px;
}

.tips li {
  margin: 5px 0;
  color: #606266;
}

.actions {
  margin-bottom: 20px;
}

.actions .el-button {
  margin: 0 10px;
}

.status {
  width: 100%;
}
</style>
