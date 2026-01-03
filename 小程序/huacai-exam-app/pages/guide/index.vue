<template>
    <view class="guide-container">
        <view class="header-section">
            <text class="title">学习指南与技巧</text>
            <text class="subtitle">提升学习效率，掌握考试要点</text>
        </view>

        <view class="guide-content">
            <view class="section-title">
                <text>指南与技巧</text>
            </view>

            <view class="guide-list">
                <view class="guide-item" v-for="guide in guideList" :key="guide.guideId" @click="handleDetail(guide.guideId)">
                    <view class="guide-type type">{{ guide.type }}</view>
                    <text class="guide-title">{{ guide.title }}</text>
                    <view class="guide-meta">
                        <text class="createTime">{{ guide.createTime }}</text>
                    </view>
                </view>
            </view>

        </view>

    </view>
</template>

<script setup >
import {onMounted, ref} from 'vue'
import {selectAllGuide} from "@/pages/api/exam/guide";

const guideList = ref([])

onMounted(() => {
    selectAllGuide().then(res => {
        guideList.value = res.data
    })
})

//跳转到指南详情页面
const handleDetail = (guideId) => {
  uni.navigateTo({
      url: '/pages/guide/detail?id=' + guideId
  })
}

</script>

<style lang="scss">
page {
    background-color: #f8f9fa;
}

.guide-container {
    .header-section {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        padding: 20px 20px 10px;
        color: white;
        text-align: center;

        .title {
            font-size: 20px;
            font-weight: 600;
            display: block;
            margin-bottom: 10px;
        }

        .subtitle {
            font-size: 14px;
            opacity: 0.9;
        }
    }

    .guide-content {
        padding: 20px;
        margin-top: -20px;
        position: relative;
        z-index: 10;

        .stats-card {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            display: flex;
            justify-content: space-around;
            margin-bottom: 20px;

            .stat-item {
                text-align: center;

                .stat-number {
                    font-size: 24px;
                    font-weight: 700;
                    display: block;
                    margin-bottom: 5px;
                    color: #4facfe;
                }

                .stat-label {
                    font-size: 12px;
                    color: #999;
                }
            }
        }

        .section-title {
            font-size: 18px;
            font-weight: 600;
            color: #333;
            margin: 25px 0 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .more-link {
                font-size: 14px;
                font-weight: normal;
                color: #999;
            }
        }

        .guide-list {
            display: flex;
            flex-direction: column;
            gap: 15px;

            .guide-item {
                background: white;
                border-radius: 15px;
                padding: 15px;
                margin-bottom: 15px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                position: relative;
                border-left: 4px solid #4facfe;

                .guide-type {
                    font-size: 12px;
                    padding: 2px 8px;
                    border-radius: 8px;
                    color: white;
                    display: inline-block;
                    margin-bottom: 10px;

                    &.type {
                        background-color: #4facfe;
                    }
                }

                .guide-title {
                    font-size: 15px;
                    color: #333;
                    margin-bottom: 15px;
                    display: block;
                    line-height: 1.4;
                }

                .guide-meta {
                    display: flex;
                    justify-content: space-between;
                    font-size: 12px;
                    color: #999;
                    margin-bottom: 10px;

                    .createTime {
                        font-size: 12px;
                        color: #999;
                    }
                }
            }
        }
    }
}
</style>
