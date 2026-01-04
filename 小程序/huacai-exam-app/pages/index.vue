<template>
  <!-- 首页容器 -->
    <view class="home-container">
        <!-- 顶部欢迎区域 -->
        <view class="welcome-section">
            <!-- 欢迎内容区域 -->
            <view class="welcome-content">
                <!-- 应用logo -->
                <image class="logo" src="@/static/logo.png" mode="aspectFit"></image>
                <!-- 欢迎文本 -->
                <view class="welcome-text">
                    <text class="title">欢迎使用</text>
                    <text class="subtitle">在线考试小程序</text>
                </view>
            </view>
            <!-- 统计内容区域 -->
            <view class="stats-section">
                <!-- 考试次数统计 -->
                <view class="stat-item">
                    <text class="stat-number">{{ examStatus.examCount }}</text>
                    <text class="stat-label">考试次数</text>
                </view>
                <view class="stat-item">
                    <text class="stat-number">{{ examStatus.passCount }}</text>
                    <text class="stat-label">通过次数</text>
                </view>
                <view class="stat-item">
                    <text class="stat-number">{{ examStatus.passRate }}%</text>
                    <text class="stat-label">通过率</text>
                </view>
            </view>
        </view>

        <!-- 快捷功能区域 -->
        <view class="quick-actions">
            <!-- 区域标题 -->
            <view class="section-title">
                <text>快捷功能</text>
            </view>
            <!-- 功能网格布局 -->
            <view class="actions-grid">
                <!-- 在线练习 -->
                <view class="action-item" @click="goToPractice">
                    <view class="icon-container practice">
                        <text class="iconfont icon-edit"/>
                    </view>
                    <text class="action-text">在线练习</text>
                </view>
                <!-- 正式考试 -->
                <view class="action-item" @click="goToExam">
                    <view class="icon-container exam">
                        <text class="iconfont icon-password"/>
                    </view>
                    <text class="action-text">正式考试</text>
                </view>
                <!-- 考试记录 -->
                <view class="action-item" @click="goToRecord">
                    <view class="icon-container record">
                        <text class="iconfont icon-community"/>
                    </view>
                    <text class="action-text">考试记录</text>
                </view>
                <!-- 学习指南 -->
                <view class="action-item" @click="goToGuide">
                    <view class="icon-container wrong">
                        <text class="iconfont icon-clean"/>
                    </view>
                    <text class="action-text">学习指南</text>
                </view>
            </view>
        </view>

        <!-- 分类练习区域 -->
        <view class="categoryList-section">
            <view class="section-title">
                <text>分类练习</text>
            </view>
            <view class="categoryList-grid">
                <!-- 循环渲染分类项 -->
                <view class="category-item" v-for="(category, index) in categoryList"
                      :key="category.categoryId" @click="goToQuestions(category.categoryId)">
                    <!-- 彩色圆点标识 -->
                    <view class="color-dot" :style="{ backgroundColor: getCategoryColor(index)}"/>
                    <!-- 分类信息 -->
                    <view class="category-info">
                        <text class="category-name">{{ category.name }}</text>
                    </view>
                </view>
            </view>
        </view>

    </view>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {onLoad} from '@dcloudio/uni-app'
import {selectMyExamCountAndPassCount} from "@/pages/api/exam/eu";
import {selectAllCategoryList} from "@/pages/api/exam/category";

const examStatus = ref({})
const categoryList = ref([])

//跳转到分类对应的练习题库
const goToQuestions = (categoryId) => {
    uni.navigateTo({
        url: '/pages/practice/questions?id=' + categoryId
    })
}

//根据索引生成分类颜色
const getCategoryColor = (index) => {
    //使用黄金角度近似值(137.508)
    const hue = (index * 137.508) % 360
    return `hsl(${hue}, 70%, 60%)`
}

onMounted(() => {
    selectAllCategoryList().then(res => {
        categoryList.value = res.data
    })

    //查询个人的考试次数和通过次数
    selectMyExamCountAndPassCount().then(res => {
        examStatus.value = res.data
        //计算通过率
        if (res.data.examCount && res.data.passCount > 0) {
            examStatus.value.passRate = (res.data.passCount / res.data.examCount) * 100
        } else {
            examStatus.value.passRate = 0
        }
    })
})

//跳转到练习页面
const goToPractice = () => {
    uni.navigateTo({
        url: '/pages/practice/index'
    })
}

//跳转到正式考试页面
const goToExam = () => {
    uni.navigateTo({
        url: '/pages/exam/index'
    })
}

//跳转到考试记录页面
const goToRecord = () => {
    uni.navigateTo({
        url: '/pages/record/index'
    })
}

//跳转到学习指南页面
const goToGuide = () => {
    uni.switchTab({
        url: '/pages/guide/index'
    })
}

onLoad(() => {
    // 页面加载时的初始化逻辑
})
</script>

<style>
/* 页面整体背景色 */
page {
    background-color: #f8f9fa;
}

/* 首页容器样式 */
.home-container {
    padding: 20px; /* 内边距 */
}

/* 欢迎区域样式 */
.welcome-section {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); /* 渐变背景 */
    border-radius: 20px; /* 圆角 */
    padding: 30px 20px; /* 内边距 */
    margin-bottom: 20px; /* 底部外边距 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 阴影效果 */
    color: white; /* 文字颜色 */
}

/* 欢迎内容区域样式 */
.welcome-content {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    margin-bottom: 20px; /* 底部外边距 */
}

/* Logo样式 */
.logo {
    width: 80px; /* 宽度 */
    height: 80px; /* 高度 */
    border-radius: 15px; /* 圆角 */
    margin-right: 20px; /* 右侧外边距 */
    background-color: white; /* 背景色 */
    padding: 10px; /* 内边距 */
}

/* 欢迎文本区域样式 */
.welcome-text {
    flex: 1; /* 占据剩余空间 */
}

/* 主标题样式 */
.title {
    font-size: 22px; /* 字体大小 */
    font-weight: 600; /* 字体粗细 */
    display: block; /* 块级显示 */
    margin-bottom: 8px; /* 底部外边距 */
}

/* 副标题样式 */
.subtitle {
    font-size: 13px; /* 字体大小 */
    color: rgba(255, 255, 255, 0.9); /* 半透明白色 */
}

/* 统计区域样式 */
.stats-section {
    display: flex; /* 弹性布局 */
    justify-content: space-around; /* 均匀分布 */
    text-align: center; /* 文字居中 */
}

/* 统计数字样式 */
.stat-item .stat-number {
    font-size: 24px; /* 字体大小 */
    font-weight: 700; /* 字体粗细 */
    display: block; /* 块级显示 */
    margin-bottom: 5px; /* 底部外边距 */
}

/* 统计标签样式 */
.stat-item .stat-label {
    font-size: 12px; /* 字体大小 */
    opacity: 0.9; /* 透明度 */
}

/* 区域标题通用样式 */
.section-title {
    font-size: 18px; /* 字体大小 */
    font-weight: 600; /* 字体粗细 */
    color: #333; /* 文字颜色 */
    margin-bottom: 20px; /* 底部外边距 */
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
}

/* "查看更多"链接样式 */
.section-title .more-link {
    font-size: 14px; /* 字体大小 */
    font-weight: normal; /* 正常字体粗细 */
    color: #999; /* 文字颜色 */
}

/* 快捷功能区域样式 */
.quick-actions {
    background: white; /* 背景色 */
    border-radius: 20px; /* 圆角 */
    padding: 20px; /* 内边距 */
    margin-bottom: 20px; /* 底部外边距 */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05); /* 阴影效果 */
}

/* 功能网格布局样式 */
.actions-grid {
    display: flex; /* 弹性布局 */
    flex-wrap: wrap; /* 允许换行 */
    justify-content: space-between; /* 两端对齐 */
}

/* 功能项样式 */
.action-item {
    flex: 0 0 22%; /* 固定宽度，占22% */
    text-align: center; /* 文字居中 */
    margin-bottom: 20px; /* 底部外边距 */
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
    align-items: center; /* 水平居中 */
}

/* 图标容器样式 */
.icon-container {
    width: 50px; /* 宽度 */
    height: 50px; /* 高度 */
    border-radius: 50%; /* 圆形 */
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
    margin-bottom: 10px; /* 底部外边距 */
    color: white; /* 图标颜色 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

/* 在线练习图标样式 */
.icon-container.practice {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); /* 渐变背景 */
}

/* 正式考试图标样式 */
.icon-container.exam {
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); /* 渐变背景 */
}

/* 考试记录图标样式 */
.icon-container.record {
    background: linear-gradient(135deg, #92fe9d 0%, #00c9ff 100%); /* 渐变背景 */
}

/* 学习指南图标样式 */
.icon-container.wrong {
    background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%); /* 渐变背景 */
}

/* 图标字体样式 */
.icon-container .iconfont {
    font-size: 22px; /* 字体大小 */
}

/* 功能文本样式 */
.action-text {
    font-size: 14px; /* 字体大小 */
    color: #333; /* 文字颜色 */
    font-weight: 500; /* 字体粗细 */
}

/* 分类练习区域样式 */
.categoryList-section {
    background: white; /* 背景色 */
    border-radius: 20px; /* 圆角 */
    padding: 20px; /* 内边距 */
    margin-bottom: 20px; /* 底部外边距 */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05); /* 阴影效果 */
}

/* 分类列表网格样式 */
.categoryList-grid {
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
    gap: 15px; /* 项间距 */
}

/* 分类项样式 */
.category-item {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    padding: 15px; /* 内边距 */
    border-radius: 15px; /* 圆角 */
    background-color: #fafafa; /* 背景色 */
}

/* 彩色圆点样式 */
.color-dot {
    width: 20px; /* 宽度 */
    height: 20px; /* 高度 */
    border-radius: 50%; /* 圆形 */
    margin-right: 15px; /* 右侧外边距 */
}

/* 分类信息区域样式 */
.category-info {
    flex: 1; /* 占据剩余空间 */
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
}

/* 分类名称样式 */
.category-name {
    font-size: 16px; /* 字体大小 */
    font-weight: 500; /* 字体粗细 */
    color: #333; /* 文字颜色 */
    margin-bottom: 5px; /* 底部外边距 */
}

/* 分类元信息样式 */
.category-meta {
    font-size: 12px; /* 字体大小 */
    color: #999; /* 文字颜色 */
}
</style>
