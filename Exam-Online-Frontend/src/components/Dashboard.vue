<template>
  <div class="dashboard-container">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1 class="welcome-title">欢迎使用在线考试系统</h1>
          <p class="welcome-subtitle">智能化考试管理平台，助力教育数字化转型</p>
        </div>
        <div class="welcome-image">
          <img src="../assets/imgs/logo.jpg" alt="Logo">
        </div>
      </div>
    </div>

    <!-- 快捷操作卡片 -->
    <div class="quick-actions">
      <div class="action-card primary-card" @click="goToFaceRegister">
        <div class="card-icon">
          <i class="el-icon-camera"></i>
        </div>
        <div class="card-content">
          <h3>人脸注册</h3>
          <p>完成身份验证，开启考试之旅</p>
        </div>
        <div class="card-arrow">
          <i class="el-icon-right"></i>
        </div>
      </div>

      <div class="action-card secondary-card" @click="goToFaceManage">
        <div class="card-icon">
          <i class="el-icon-setting"></i>
        </div>
        <div class="card-content">
          <h3>人脸管理</h3>
          <p>管理您的人脸识别信息</p>
        </div>
        <div class="card-arrow">
          <i class="el-icon-right"></i>
        </div>
      </div>

      <div class="action-card accent-card" @click="goToExamOnline">
        <div class="card-icon">
          <i class="el-icon-document"></i>
        </div>
        <div class="card-content">
          <h3>在线考试</h3>
          <p>参与考试，检验学习成果</p>
        </div>
        <div class="card-arrow">
          <i class="el-icon-right"></i>
        </div>
      </div>

      <div class="action-card info-card" @click="goToMyGrade">
        <div class="card-icon">
          <i class="el-icon-data-analysis"></i>
        </div>
        <div class="card-content">
          <h3>成绩查询</h3>
          <p>查看考试成绩与详细分析</p>
        </div>
        <div class="card-arrow">
          <i class="el-icon-right"></i>
        </div>
      </div>
    </div>


    <!-- 功能特性展示 -->
    <div class="features-section">
      <h2 class="section-title">
        <span class="title-icon">✨</span>
        核心功能特性
      </h2>
      
      <div class="features-grid">
        <div class="feature-card" v-for="(feature, index) in features" :key="index" :style="{ animationDelay: index * 0.1 + 's' }">
          <div class="feature-icon" :class="feature.iconClass">
            <i :class="feature.icon"></i>
          </div>
          <h3 class="feature-title">{{ feature.title }}</h3>
          <p class="feature-desc">{{ feature.description }}</p>
        </div>
      </div>
    </div>

    <!-- 系统优势 -->
    <div class="advantages-section">
      <h2 class="section-title">
        <span class="title-icon">🎯</span>
        系统优势
      </h2>
      
      <div class="advantages-grid">
        <div class="advantage-item" v-for="(adv, index) in advantages" :key="index">
          <div class="advantage-number">{{ index + 1 }}</div>
          <div class="advantage-content">
            <h4>{{ adv.title }}</h4>
            <p>{{ adv.desc }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 数据统计 -->
    <div class="stats-section">
      <div class="stat-card" v-for="(stat, index) in stats" :key="index">
        <div class="stat-icon" :style="{ background: stat.color }">
          <i :class="stat.icon"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Dashboard',
    data() {
      return {
        features: [
          {
            icon: 'el-icon-user',
            iconClass: 'icon-blue',
            title: '权限控制',
            description: '基于JWT的多角色权限管理，支持教师、管理员、学生三种用户角色'
          },
          {
            icon: 'el-icon-edit',
            iconClass: 'icon-green',
            title: '在线考试',
            description: '支持单选、多选、判断、简答题，实时监控考试状态，自动抓拍考生'
          },
          {
            icon: 'el-icon-data-line',
            iconClass: 'icon-orange',
            title: '成绩分析',
            description: '自动批改客观题，智能统计分析，生成详细的成绩报表'
          },
          {
            icon: 'el-icon-collection',
            iconClass: 'icon-purple',
            title: '题库管理',
            description: '支持题库分类管理，多种练习模式，错题解析功能'
          },
          {
            icon: 'el-icon-document-checked',
            iconClass: 'icon-red',
            title: '试卷管理',
            description: '灵活组卷方式，支持题库组卷和自由选题，设置考试时间段'
          },
          {
            icon: 'el-icon-pie-chart',
            iconClass: 'icon-cyan',
            title: '数据统计',
            description: '可视化数据报表，直观了解考试情况和学习进度'
          }
        ],
        advantages: [
          { title: '智能化管理', desc: '自动化流程，减轻教师工作负担' },
          { title: '安全可靠', desc: '人脸识别技术，确保考试公平公正' },
          { title: '灵活便捷', desc: '随时随地在线考试，不受时间地点限制' },
          { title: '数据分析', desc: '详细的数据统计，助力教学质量提升' }
        ],
        stats: [
          { icon: 'el-icon-user-solid', value: '1000+', label: '注册用户', color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
          { icon: 'el-icon-document', value: '500+', label: '题库数量', color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
          { icon: 'el-icon-edit-outline', value: '2000+', label: '考试场次', color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
          { icon: 'el-icon-trophy', value: '98%', label: '满意度', color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' }
        ]
      }
    },
    created () {
      this.$emit('showSystemNotice')
    },
    methods: {
      goToFaceRegister() {
        this.$router.push('/faceRegister');
      },
      goToFaceManage() {
        this.$router.push('/faceManage');
      },
      goToExamOnline() {
        this.$router.push('/examOnline');
      },
      goToMyGrade() {
        this.$router.push('/myGrade');
      }
    }
  }
</script>

<style scoped lang="scss">
.dashboard-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #F7FAFC 0%, #EDF2F7 100%);
  padding: 30px;
  animation: fadeIn 0.6s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 欢迎区域
.welcome-section {
  background: linear-gradient(135deg, #38B2AC 0%, #319795 100%);
  border-radius: 24px;
  padding: 40px;
  margin-bottom: 30px;
  box-shadow: 0 10px 40px rgba(56, 178, 172, 0.2);
  animation: slideInDown 0.8s ease-out;
}

@keyframes slideInDown {
  from {
    opacity: 0;
    transform: translateY(-50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text {
  flex: 1;
  color: white;
}

.welcome-title {
  font-size: 36px;
  font-weight: 700;
  margin: 0 0 15px 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.welcome-subtitle {
  font-size: 18px;
  opacity: 0.9;
  margin: 0;
}

.welcome-image {
  img {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    border: 5px solid rgba(255, 255, 255, 0.3);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
    animation: float 3s ease-in-out infinite;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

// 快捷操作卡片
.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.action-card {
  background: white;
  border-radius: 20px;
  padding: 25px;
  display: flex;
  align-items: center;
  gap: 20px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(0, 217, 255, 0.12);
  animation: slideInUp 0.6s ease-out;
  animation-fill-mode: both;

  &:nth-child(1) { animation-delay: 0.1s; }
  &:nth-child(2) { animation-delay: 0.2s; }
  &:nth-child(3) { animation-delay: 0.3s; }
  &:nth-child(4) { animation-delay: 0.4s; }

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 35px rgba(0, 217, 255, 0.2);

    .card-icon {
      transform: scale(1.1) rotate(5deg);
    }

    .card-arrow {
      transform: translateX(5px);
    }
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  transition: all 0.3s ease;

  i {
    color: white;
  }
}

.primary-card .card-icon {
  background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
}

.secondary-card .card-icon {
  background: linear-gradient(135deg, #4FD1C5 0%, #00D9FF 100%);
}

.accent-card .card-icon {
  background: linear-gradient(135deg, #38B2AC 0%, #319795 100%);
}

.info-card .card-icon {
  background: linear-gradient(135deg, #4299E1 0%, #3182CE 100%);
}

.card-content {
  flex: 1;

  h3 {
    margin: 0 0 8px 0;
    font-size: 18px;
    font-weight: 600;
    color: #2D3748;
  }

  p {
    margin: 0;
    font-size: 14px;
    color: #718096;
  }
}

.card-arrow {
  font-size: 20px;
  color: #cbd5e0;
  transition: all 0.3s ease;
}

// 功能特性
.features-section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #2D3748;
  margin: 0 0 30px 0;
  display: flex;
  align-items: center;
  gap: 10px;

  .title-icon {
    font-size: 32px;
  }
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 25px;
}

.feature-card {
  background: white;
  border-radius: 20px;
  padding: 30px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(0, 217, 255, 0.1);
  animation: fadeInScale 0.6s ease-out;
  animation-fill-mode: both;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 35px rgba(0, 217, 255, 0.15);
  }
}

@keyframes fadeInScale {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.feature-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 36px;
  transition: all 0.3s ease;

  i {
    color: white;
  }

  &.icon-blue {
    background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
  }

  &.icon-green {
    background: linear-gradient(135deg, #48BB78 0%, #38A169 100%);
  }

  &.icon-orange {
    background: linear-gradient(135deg, #ED8936 0%, #DD6B20 100%);
  }

  &.icon-purple {
    background: linear-gradient(135deg, #9F7AEA 0%, #805AD5 100%);
  }

  &.icon-red {
    background: linear-gradient(135deg, #F56565 0%, #E53E3E 100%);
  }

  &.icon-cyan {
    background: linear-gradient(135deg, #4FD1C5 0%, #38B2AC 100%);
  }
}

.feature-card:hover .feature-icon {
  transform: rotateY(360deg);
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  color: #2D3748;
  margin: 0 0 12px 0;
}

.feature-desc {
  font-size: 14px;
  color: #718096;
  line-height: 1.6;
  margin: 0;
}

// 系统优势
.advantages-section {
  margin-bottom: 40px;
}

.advantages-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.advantage-item {
  background: white;
  border-radius: 20px;
  padding: 25px;
  display: flex;
  gap: 20px;
  align-items: flex-start;
  box-shadow: 0 4px 20px rgba(0, 217, 255, 0.1);
  transition: all 0.3s ease;
  animation: slideInLeft 0.6s ease-out;
  animation-fill-mode: both;

  &:nth-child(1) { animation-delay: 0.1s; }
  &:nth-child(2) { animation-delay: 0.2s; }
  &:nth-child(3) { animation-delay: 0.3s; }
  &:nth-child(4) { animation-delay: 0.4s; }

  &:hover {
    transform: translateX(10px);
    box-shadow: 0 8px 30px rgba(0, 217, 255, 0.15);
  }
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.advantage-number {
  width: 50px;
  height: 50px;
  border-radius: 14px;
  background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 700;
  flex-shrink: 0;
}

.advantage-content {
  flex: 1;

  h4 {
    margin: 0 0 8px 0;
    font-size: 18px;
    font-weight: 600;
    color: #2D3748;
  }

  p {
    margin: 0;
    font-size: 14px;
    color: #718096;
    line-height: 1.6;
  }
}

// 数据统计
.stats-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
}

.stat-card {
  background: white;
  border-radius: 20px;
  padding: 30px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 4px 20px rgba(0, 217, 255, 0.1);
  transition: all 0.3s ease;
  animation: zoomIn 0.6s ease-out;
  animation-fill-mode: both;

  &:nth-child(1) { animation-delay: 0.1s; }
  &:nth-child(2) { animation-delay: 0.2s; }
  &:nth-child(3) { animation-delay: 0.3s; }
  &:nth-child(4) { animation-delay: 0.4s; }

  &:hover {
    transform: scale(1.05);
    box-shadow: 0 12px 35px rgba(0, 217, 255, 0.2);
  }
}

@keyframes zoomIn {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.stat-icon {
  width: 70px;
  height: 70px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #2D3748;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #718096;
}

// 响应式设计
@media (max-width: 768px) {
  .dashboard-container {
    padding: 15px;
  }

  .welcome-title {
    font-size: 24px;
  }

  .welcome-subtitle {
    font-size: 14px;
  }

  .welcome-image img {
    width: 80px;
    height: 80px;
  }

  .quick-actions {
    grid-template-columns: 1fr;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .advantages-grid {
    grid-template-columns: 1fr;
  }

  .stats-section {
    grid-template-columns: 1fr;
  }
}
</style>
