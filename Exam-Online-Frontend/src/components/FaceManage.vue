<template>
  <div class="face-manage-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: bold;">人脸管理</span>
      </div>

      <div class="content">
        <el-alert
          title="人脸识别功能说明"
          type="info"
          :closable="false"
          show-icon>
          <p>为了保证考试的公平性，系统启用了人脸识别功能。</p>
          <p>在参加考试前，您需要先注册人脸信息。考试开始前会进行人脸验证。</p>
        </el-alert>

        <div class="face-status">
          <el-card shadow="hover">
            <div class="status-content">
              <i :class="faceRegistered ? 'el-icon-success' : 'el-icon-warning'" 
                 :style="{color: faceRegistered ? '#67C23A' : '#E6A23C', fontSize: '48px'}"></i>
              <div class="status-text">
                <h3>{{ faceRegistered ? '已注册人脸' : '未注册人脸' }}</h3>
                <p>{{ faceRegistered ? '您已完成人脸注册，可以参加考试' : '请先注册人脸信息才能参加考试' }}</p>
              </div>
            </div>
          </el-card>
        </div>

        <div class="actions">
          <el-button 
            type="primary" 
            icon="el-icon-camera" 
            @click="goToRegister"
            v-if="!faceRegistered">
            立即注册人脸
          </el-button>
          <el-button 
            type="success" 
            icon="el-icon-refresh" 
            @click="goToRegister"
            v-if="faceRegistered">
            重新注册人脸
          </el-button>
          <el-button 
            type="danger" 
            icon="el-icon-delete" 
            @click="deleteFace"
            v-if="faceRegistered">
            删除人脸数据
          </el-button>
        </div>

        <div class="tips">
          <el-divider></el-divider>
          <h4>注意事项：</h4>
          <ul>
            <li>注册人脸时请确保光线充足，面部清晰可见</li>
            <li>请正面面对摄像头，保持自然表情</li>
            <li>每个账号只能注册一张人脸</li>
            <li>重新注册会覆盖原有的人脸数据</li>
            <li>删除人脸数据后将无法参加考试，需要重新注册</li>
          </ul>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'FaceManage',
  data() {
    return {
      faceRegistered: false,
      currentUser: null
    }
  },
  mounted() {
    this.checkFaceStatus()
  },
  methods: {
    // 检查人脸注册状态
    async checkFaceStatus() {
      try {
        const userRes = await this.$http.get(this.API.getCurrentUser)
        if (userRes.data.code === 200) {
          this.currentUser = userRes.data.data
          const userId = userRes.data.data.userId || userRes.data.data.id || localStorage.getItem('userId')
          
          console.log('检查人脸注册状态，用户ID:', userId)
          
          // 调用人脸识别接口检查是否已注册
          // 使用一个测试图片（1x1像素的透明图片）来检查
          const testImage = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=='
          
          try {
            const checkRes = await this.$http.post(
              `${this.API.faceRecognize}?userId=${userId}`,
              { image: testImage }
            )
            
            console.log('人脸识别检查响应:', checkRes.data)
            
            // 如果返回200且有匹配结果，说明已注册
            if (checkRes.data.code === 200 && checkRes.data.data) {
              this.faceRegistered = true
              console.log('检测到已注册人脸')
            } else {
              this.faceRegistered = false
              console.log('未检测到注册人脸')
            }
          } catch (error) {
            // 如果识别失败（404或其他错误），说明未注册
            console.log('人脸识别检查失败，判定为未注册:', error.response?.status)
            this.faceRegistered = false
          }
        }
      } catch (error) {
        console.error('检查人脸状态失败:', error)
        this.faceRegistered = false
      }
    },

    // 跳转到人脸注册页面
    goToRegister() {
      this.$router.push('/faceRegister')
    },

    // 删除人脸数据
    deleteFace() {
      this.$confirm('删除人脸数据后将无法参加考试，确定要删除吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const userId = this.currentUser.userId || this.currentUser.id || localStorage.getItem('userId')
          console.log('删除人脸数据，用户ID:', userId)
          
          const res = await this.$http.delete(
            `${this.API.faceDelete}/${userId}`
          )
          
          console.log('删除响应:', res.data)
          
          if (res.data.code === 200) {
            this.$message.success('人脸数据删除成功')
            this.faceRegistered = false
          } else {
            this.$message.error(res.data.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败：' + (error.response?.data?.message || error.message))
        }
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }
  }
}
</script>

<style scoped>
.face-manage-container {
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
}

.content {
  padding: 20px 0;
}

.face-status {
  margin: 30px 0;
}

.status-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.status-text {
  margin-left: 30px;
}

.status-text h3 {
  margin: 0 0 10px 0;
  font-size: 20px;
}

.status-text p {
  margin: 0;
  color: #606266;
}

.actions {
  text-align: center;
  margin: 30px 0;
}

.actions .el-button {
  margin: 0 10px;
}

.tips {
  margin-top: 30px;
}

.tips h4 {
  margin-bottom: 15px;
  color: #303133;
}

.tips ul {
  padding-left: 20px;
}

.tips li {
  margin: 8px 0;
  color: #606266;
  line-height: 1.6;
}
</style>
