<template>
  <div class="face-test-container">
    <el-card>
      <div slot="header">
        <h2>人脸识别功能测试</h2>
      </div>

      <el-tabs v-model="activeTab">
        <!-- 人脸检测测试 -->
        <el-tab-pane label="人脸检测" name="detect">
          <div class="test-section">
            <video ref="detectVideo" width="640" height="480" autoplay></video>
            <canvas ref="detectCanvas" width="640" height="480" style="display: none;"></canvas>
            
            <div class="actions">
              <el-button type="primary" @click="startDetectCamera" v-if="!detectCameraStarted">
                打开摄像头
              </el-button>
              <el-button type="success" @click="testDetect" v-if="detectCameraStarted">
                检测人脸
              </el-button>
              <el-button type="warning" @click="stopDetectCamera" v-if="detectCameraStarted">
                关闭摄像头
              </el-button>
            </div>

            <div class="result" v-if="detectResult">
              <el-alert :type="detectResult.type" :closable="false">
                <pre>{{ JSON.stringify(detectResult.data, null, 2) }}</pre>
              </el-alert>
            </div>
          </div>
        </el-tab-pane>

        <!-- 人脸注册测试 -->
        <el-tab-pane label="人脸注册" name="register">
          <div class="test-section">
            <el-form label-width="100px">
              <el-form-item label="用户ID">
                <el-input v-model="registerForm.userId" placeholder="输入测试用户ID"></el-input>
              </el-form-item>
              <el-form-item label="用户名">
                <el-input v-model="registerForm.userName" placeholder="输入测试用户名"></el-input>
              </el-form-item>
            </el-form>

            <video ref="registerVideo" width="640" height="480" autoplay></video>
            <canvas ref="registerCanvas" width="640" height="480" style="display: none;"></canvas>
            
            <div class="actions">
              <el-button type="primary" @click="startRegisterCamera" v-if="!registerCameraStarted">
                打开摄像头
              </el-button>
              <el-button type="success" @click="testRegister" v-if="registerCameraStarted">
                注册人脸
              </el-button>
              <el-button type="warning" @click="stopRegisterCamera" v-if="registerCameraStarted">
                关闭摄像头
              </el-button>
            </div>

            <div class="result" v-if="registerResult">
              <el-alert :type="registerResult.type" :closable="false">
                <pre>{{ JSON.stringify(registerResult.data, null, 2) }}</pre>
              </el-alert>
            </div>
          </div>
        </el-tab-pane>

        <!-- 人脸识别测试 -->
        <el-tab-pane label="人脸识别" name="recognize">
          <div class="test-section">
            <video ref="recognizeVideo" width="640" height="480" autoplay></video>
            <canvas ref="recognizeCanvas" width="640" height="480" style="display: none;"></canvas>
            
            <div class="actions">
              <el-button type="primary" @click="startRecognizeCamera" v-if="!recognizeCameraStarted">
                打开摄像头
              </el-button>
              <el-button type="success" @click="testRecognize" v-if="recognizeCameraStarted">
                识别人脸
              </el-button>
              <el-button type="warning" @click="stopRecognizeCamera" v-if="recognizeCameraStarted">
                关闭摄像头
              </el-button>
            </div>

            <div class="result" v-if="recognizeResult">
              <el-alert :type="recognizeResult.type" :closable="false">
                <pre>{{ JSON.stringify(recognizeResult.data, null, 2) }}</pre>
              </el-alert>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'FaceTest',
  data() {
    return {
      activeTab: 'detect',
      detectCameraStarted: false,
      registerCameraStarted: false,
      recognizeCameraStarted: false,
      detectStream: null,
      registerStream: null,
      recognizeStream: null,
      detectResult: null,
      registerResult: null,
      recognizeResult: null,
      registerForm: {
        userId: '',
        userName: ''
      }
    }
  },
  beforeDestroy() {
    this.stopAllCameras()
  },
  methods: {
    // 检测相关
    async startDetectCamera() {
      this.detectStream = await this.startCamera(this.$refs.detectVideo)
      if (this.detectStream) this.detectCameraStarted = true
    },
    stopDetectCamera() {
      this.stopCamera(this.detectStream)
      this.detectStream = null
      this.detectCameraStarted = false
    },
    async testDetect() {
      const image = this.captureImage(this.$refs.detectVideo, this.$refs.detectCanvas)
      try {
        const res = await this.$http.post(this.API.faceDetect, { image })
        this.detectResult = {
          type: res.data.code === 200 ? 'success' : 'error',
          data: res.data
        }
      } catch (error) {
        this.detectResult = {
          type: 'error',
          data: error.response?.data || error.message
        }
      }
    },

    // 注册相关
    async startRegisterCamera() {
      this.registerStream = await this.startCamera(this.$refs.registerVideo)
      if (this.registerStream) this.registerCameraStarted = true
    },
    stopRegisterCamera() {
      this.stopCamera(this.registerStream)
      this.registerStream = null
      this.registerCameraStarted = false
    },
    async testRegister() {
      if (!this.registerForm.userId || !this.registerForm.userName) {
        this.$message.error('请输入用户ID和用户名')
        return
      }
      const image = this.captureImage(this.$refs.registerVideo, this.$refs.registerCanvas)
      try {
        const res = await this.$http.post(
          `${this.API.faceRegister}?userId=${this.registerForm.userId}&userName=${this.registerForm.userName}`,
          { image }
        )
        this.registerResult = {
          type: res.data.code === 200 ? 'success' : 'error',
          data: res.data
        }
      } catch (error) {
        this.registerResult = {
          type: 'error',
          data: error.response?.data || error.message
        }
      }
    },

    // 识别相关
    async startRecognizeCamera() {
      this.recognizeStream = await this.startCamera(this.$refs.recognizeVideo)
      if (this.recognizeStream) this.recognizeCameraStarted = true
    },
    stopRecognizeCamera() {
      this.stopCamera(this.recognizeStream)
      this.recognizeStream = null
      this.recognizeCameraStarted = false
    },
    async testRecognize() {
      const image = this.captureImage(this.$refs.recognizeVideo, this.$refs.recognizeCanvas)
      try {
        const res = await this.$http.post(this.API.faceRecognize, { image })
        this.recognizeResult = {
          type: res.data.code === 200 ? 'success' : 'error',
          data: res.data
        }
      } catch (error) {
        this.recognizeResult = {
          type: 'error',
          data: error.response?.data || error.message
        }
      }
    },

    // 通用方法
    async startCamera(videoElement) {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({
          video: { width: 640, height: 480, facingMode: 'user' }
        })
        videoElement.srcObject = stream
        return stream
      } catch (error) {
        this.$message.error('无法访问摄像头')
        return null
      }
    },
    stopCamera(stream) {
      if (stream) {
        stream.getTracks().forEach(track => track.stop())
      }
    },
    captureImage(videoElement, canvasElement) {
      const ctx = canvasElement.getContext('2d')
      ctx.drawImage(videoElement, 0, 0, 640, 480)
      return canvasElement.toDataURL('image/jpeg', 0.8)
    },
    stopAllCameras() {
      this.stopCamera(this.detectStream)
      this.stopCamera(this.registerStream)
      this.stopCamera(this.recognizeStream)
    }
  }
}
</script>

<style scoped>
.face-test-container {
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
}

.test-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

video {
  border: 2px solid #409EFF;
  border-radius: 8px;
  margin: 20px 0;
}

.actions {
  margin: 20px 0;
}

.actions .el-button {
  margin: 0 10px;
}

.result {
  width: 100%;
  margin-top: 20px;
}

.result pre {
  margin: 10px 0;
  text-align: left;
  max-height: 300px;
  overflow: auto;
}

.el-form {
  width: 100%;
  max-width: 500px;
}
</style>
