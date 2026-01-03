<template>
    <view class="exam-detail-container">
        <view class="exam-info">
            <view class="exam-title">
                {{ exam.title }}
            </view>
            <view class="exam-stats">
                <view class="stat-item">
                    <text class="stat-lable">考试时间</text>
                    <text class="stat-value">{{ exam.time }}小时</text>
                </view>
                <view class="stat-item">
                    <text class="stat-lable">题目数量</text>
                    <text class="stat-value">{{ exam.eqCount }}题</text>
                </view>
                <view class="stat-item">
                    <text class="stat-lable">总分</text>
                    <text class="stat-value">{{ exam.totalScore }}分</text>
                </view>
                <view class="stat-item">
                    <text class="stat-lable">及格分</text>
                    <text class="stat-value">{{ exam.passScore }}分</text>
                </view>
            </view>

            <view class="exam-rules">
                <view class="section-title">考试规则</view>
                <view class="rule-item">
                    <text class="iconfont icon-right"></text>
                    <text class="rule-text">考试时长为 {{ exam.time }}小时，时间到自动提交</text>
                </view>
                <view class="rule-item">
                    <text class="iconfont icon-right"></text>
                    <text class="rule-text">考试过程中请勿切换应用，否则可能影响成绩</text>
                </view>
                <view class="rule-item">
                    <text class="iconfont icon-right"></text>
                    <text class="rule-text">考试结束后将显示成绩和答案解析</text>
                </view>
                <view class="rule-item">
                    <text class="iconfont icon-right"></text>
                    <text class="rule-text">考试成绩将记录在个人档案中</text>
                </view>
            </view>

            <view class="start-section">
                <button class="start-btn" @click="startExam">
                    开始考试
                </button>
            </view>

        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onLoad} from '@dcloudio/uni-app'
import {getExam} from "@/pages/api/exam/exam";

const exam = ref([])

onLoad((options) => {
    getExam(options.id).then(res => {
        exam.value = res.data
    })
})

const startExam = () => {
  uni.showModal({
      title: '开始考试',
      content: '考试即将开始, 确认后将进入答题页面 请确保已了解考试桂策并准备就绪',
      confirmText: '开始考试',
      cancelText: '再看看',
      success: function (res) {
          if (res.confirm) {
              uni.navigateTo({
                  url: '/pages/exam/questions?id=' + exam.value.examId
              })
          }
      }
  })
}

</script>

<style lang="scss">
page {
    background-color: #f8f9fa;
}

.exam-detail-container {
    padding-top: 20px;

    .header-section {
        position: relative;
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        padding: 60px 20px 20px;
        color: white;
        text-align: center;

        .back-btn {
            position: absolute;
            left: 20px;
            top: 60px;
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.2);

            .iconfont {
                font-size: 20px;
            }
        }

        .header-title {
            font-size: 22px;
            font-weight: 600;
        }
    }

    .exam-info {
        padding: 20px;
        margin-top: -30px;

        .exam-title {
            background: white;
            border-radius: 15px;
            padding: 20px;
            font-size: 20px;
            font-weight: 600;
            color: #333;
            margin-bottom: 15px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            display: flex;
            justify-content: space-between;
            align-items: center;

            .exam-status {
                font-size: 12px;
                padding: 3px 8px;
                border-radius: 10px;
                color: white;

                &.in_progress {
                    background-color: #ffcf5c;
                    color: #333;
                }

                &.completed {
                    background-color: #92fe9d;
                    color: #333;
                }
            }
        }

        .exam-stats {
            background: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 15px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;

            .stat-item {
                display: flex;
                flex-direction: column;

                .stat-label {
                    font-size: 14px;
                    color: #999;
                    margin-bottom: 5px;
                }

                .stat-value {
                    font-size: 18px;
                    font-weight: 600;
                    color: #333;
                }
            }
        }

        .exam-rules {
            background: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 15px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

            .section-title {
                font-size: 18px;
                font-weight: 600;
                color: #333;
                margin-bottom: 15px;
            }

            .rule-item {
                display: flex;
                align-items: flex-start;
                margin-bottom: 12px;

                .iconfont {
                    color: #4facfe;
                    font-size: 16px;
                    margin-right: 10px;
                    margin-top: 2px;
                }

                .rule-text {
                    font-size: 14px;
                    color: #666;
                    flex: 1;
                    line-height: 1.5;
                }
            }
        }

        .start-section {
            padding: 20px 0 30px;
            text-align: center;

            .start-btn {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                border: none;
                color: white;
                font-size: 15px;
                font-weight: 500;
                border-radius: 8px;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                width: 80%;
                margin: 0 auto;
            }
        }
    }
}
</style>
