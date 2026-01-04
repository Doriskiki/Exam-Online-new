<template>
    <view class="result-container">
        <view class="result-content">
            <view class="score-section">
                <view class="score-circle"
                      :class=" {passed: examResult.status === '考试通过',
                                failed: examResult.status === '考试未通过'}"
                >
                    <text class="score">{{ examResult.score }}</text>
                    <text class="score-text">得分</text>
                </view>
                <view class="score-info">
                    <text class="exam-name">{{ examResult.title }}</text>
                    <view class="result-status"
                          :class="{ passed: examResult.status === '考试通过',
                                    failed: examResult.status === '考试未通过' }">
                        {{ examResult.status }}
                    </view>
                </view>
            </view>

            <view class="stats-section">
                <view class="stat-item">
                    <text class="stat-value">{{ examResult.questionCount }}</text>
                    <text class="stat-label">题目总数</text>
                </view>
                <view class="stat-item">
                    <text class="stat-value">{{ examResult.correctCount }}</text>
                    <text class="stat-label">答对题数</text>
                </view>
            </view>

            <view class="action-buttons">
                <button class="btn view-btn" @click="viewAnswers">查看答案解析</button>
                <button class="btn view-btn" @click="backToHome">返回首页</button>
            </view>


        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue';
import {onLoad} from "@dcloudio/uni-app";
import {getEu} from "@/pages/api/exam/eu";

const examResult = ref({})

//跳转到答案解析页面
const viewAnswers = () => {
    uni.redirectTo({
        url: '/pages/exam/analysis?examId=' + examId.value
    })
}

const examId = ref(null)

const load = (result) => {
    examId.value = result.examId
    getEu(result.euId).then(res => {
        examResult.value = res.data
    })
}

const backToHome = () => {
    uni.switchTab({
        url: '/pages/index'
    })
}

onLoad((options) => {
    //页面加载时执行
    const result = JSON.parse(options.result);
    load(result)
})

</script>

<style lang="scss">
page {
  background-color: #f8f9fa;
}

.result-container {
  padding-top: 20px;

  .result-content {
    padding: 20px;
    margin-top: -30px;

    .score-section {
      background: white;
      border-radius: 15px;
      padding: 30px 20px;
      text-align: center;
      margin-bottom: 20px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

      .score-circle {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        margin: 0 auto 20px;
        color: white;

        &.passed {
          background: linear-gradient(135deg, #4caf50 0%, #8bc34a 100%);
        }

        &.failed {
          background: linear-gradient(135deg, #f44336 0%, #ff9800 100%);
        }

        .score {
          font-size: 36px;
          font-weight: 700;
        }

        .score-text {
          font-size: 16px;
        }
      }

      .score-info {
        .exam-name {
          font-size: 18px;
          font-weight: 600;
          color: #333;
          display: block;
          margin-bottom: 10px;
        }

        .result-status {
          display: inline-block;
          padding: 5px 15px;
          border-radius: 20px;
          font-size: 14px;
          font-weight: 600;

          &.passed {
            background: rgba(76, 175, 80, 0.1);
            color: #4caf50;
          }

          &.failed {
            background: rgba(244, 67, 54, 0.1);
            color: #f44336;
          }
        }
      }
    }

    .stats-section {
      background: white;
      border-radius: 15px;
      padding: 20px;
      margin-bottom: 20px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      display: flex;
      justify-content: space-around;
      text-align: center;

      .stat-item {
        .stat-value {
          display: block;
          font-size: 24px;
          font-weight: 700;
          color: #333;
          margin-bottom: 5px;
        }

        .stat-label {
          font-size: 14px;
          color: #999;
        }
      }
    }

    .action-buttons {
      display: flex;
      flex-direction: column;
      gap: 12px;
      padding: 0 20px 20px;

      .btn {
        height: 40px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 15px;
        font-weight: 500;

        &[disabled] {
          opacity: 0.5;
        }
      }

      .view-btn {
        background: #f8f9fa;
        color: #333;
        border: 1px solid #e0e0e0;
      }

      .back-btn {
        background: #f8f9fa;
        color: #333;
        border: 1px solid #e0e0e0;
      }
    }
  }
}
</style>
