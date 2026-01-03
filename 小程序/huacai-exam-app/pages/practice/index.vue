<template>
  <!-- 页面容器 -->
    <view class="practice-container">
        <!-- 内容区域 -->
        <view class="practice-content">
            <!-- 分类列表 -->
            <view class="category-list">
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
import {selectAllCategoryList} from "@/pages/api/exam/category";

//分类列表数据
const categoryList = ref([])

//根据索引生成分类颜色
const getCategoryColor = (index) => {
    //使用黄金角度近似值(137.508)
    const hue = (index * 137.508) % 360
    return `hsl(${hue}, 70%, 60%)`
}

//跳转到分类对应的练习题库
const goToQuestions = (categoryId) => {
    uni.navigateTo({
        url: '/pages/practice/questions?id=' + categoryId
    })
}

//组件挂载后执行
onMounted(() => {
    //调用api获取分类列表数据
    selectAllCategoryList().then(res => {
        categoryList.value = res.data
    })
})
</script>


<style>
/* 页面整体背景色 */
page {
    background-color: #f8f9fa;
}

/* 主容器样式 */
.practice-container {
    padding-top: 20px;
}

/* 头部区域样式 - 注释掉的代码，可能是未来扩展用 */
.practice-container .header-section {
    position: relative;
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    padding: 60px 20px 20px;
    color: white;
    text-align: center;
}

.practice-container .header-section .title {
    font-size: 22px;
    font-weight: 600;
}

.practice-container .header-section .back-btn {
    position: absolute;
    left: 20px;
    top: 60px;
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.practice-container .header-section .back-btn .iconfont {
    font-size: 20px;
}

/* 内容区域样式 */
.practice-container .practice-content {
    padding: 20px;
    margin-top: -20px;
    position: relative;
    z-index: 10;
}

/* 分类列表样式 */
.practice-container .practice-content .category-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

/* 分类项样式 */
.practice-container .practice-content .category-list .category-item {
    background: white;
    border-radius: 15px;
    padding: 15px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
}

/* 彩色圆点样式 */
.practice-container .practice-content .category-list .category-item .color-dot {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    margin-right: 15px;
}

/* 分类信息样式 */
.practice-container .practice-content .category-list .category-item .category-info {
    flex: 1;
    display: flex;
    flex-direction: column;
}

/* 分类名称样式 */
.practice-container .practice-content .category-list .category-item .category-info .category-name {
    font-size: 16px;
    font-weight: 500;
    color: #333;
    margin-bottom: 5px;
}
</style>
