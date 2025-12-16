<template>
  <el-dialog
    title="人脸验证"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="false"
    center>
    
    <div class="face-verify-content">
      <!-- 摄像头视频区域 -->
      <div class="video-container">
        <video ref="video" width="640" height="480" autoplay></video>
        <canvas ref="canvas" width="640" height="480" style="display: none;"></canvas>
      </div>

      <!-- 提示信息 -->
      <div class="tips">
        <el-alert
          title="请进行人脸验证以开始考试"
          type="warning"
          :closable="false"
          show-icon>
          <p>请正面面对摄像头，确保光线充足</p>
        </el-alert>
      </div>

      <!-- 验证结果 -->
      <div class="verify-result" v-if="verifyResult">
        <el-alert
          :title="verifyResult.message"
          :type="verifyResult.type"
          :closable="false"
          show-icon>
        </el-alert>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="cancelVerify">取消</el-button>
      <el-button type="primary" @click="verifyFace" :loading="verifying">
        开始验证
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'FaceVerify',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    examId: {
      type: [String, Number],
      required: false,
      default: null
    },
    userId: {
      type: [String, Number],
      required: false,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      stream: null,
      verifying: false,
      verifyResult: null
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.$nextTick(() => {
          this.startCamera()
        })
      } else {
        this.stopCamera()
      }
    }
  },
  beforeDestroy() {
    this.stopCamera()
  },
  methods: {
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

    // 验证人脸
    async verifyFace() {
      // 验证必要参数
      if (!this.userId || !this.examId) {
        this.$message.error('缺少必要参数，无法进行验证')
        return
      }

      this.verifying = true
      this.verifyResult = null

      try {
        const imageBase64 = this.captureFrame()
        const res = await this.$http.post(
          `${this.API.faceVerifyExam}?userId=${this.userId}&examId=${this.examId}`,
          { image: imageBase64 }
        )

        console.log('人脸验证响应:', res.data);
        
        if (res.data.code === 200 && res.data.data.matched) {
          this.verifyResult = {
            type: 'success',
            message: `验证通过！相似度：${(res.data.data.similarity * 100).toFixed(2)}%`
          }
          
          this.$message.success('人脸验证通过')
          
          console.log('准备触发 verify-success 事件');
          
          // 1秒后关闭对话框并触发成功回调
          setTimeout(() => {
            this.stopCamera()
            console.log('触发 verify-success 事件');
            this.$emit('verify-success')
            this.$emit('update:visible', false)
          }, 1000)
        } else {
          this.verifyResult = {
            type: 'error',
            message: res.data.message || '人脸验证失败，请重试'
          }
          this.$message.error('人脸验证失败')
        }
      } catch (error) {
        console.error('验证错误:', error)
        this.verifyResult = {
          type: 'error',
          message: '验证失败：' + (error.response?.data?.message || error.message)
        }
        this.$message.error('验证失败，请重试')
      } finally {
        this.verifying = false
      }
    },

    // 取消验证
    cancelVerify() {
      this.stopCamera()
      this.$emit('update:visible', false)
      this.$emit('verify-cancel')
    }
  }
}
</script>

<style scoped>
.face-verify-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.video-container {
  margin-bottom: 20px;
  border: 2px solid #E6A23C;
  border-radius: 8px;
  overflow: hidden;
}

.tips {
  width: 100%;
  margin-bottom: 15px;
}

.tips p {
  margin: 10px 0 0 0;
  color: #606266;
}

.verify-result {
  width: 100%;
  margin-top: 15px;
}
</style>
