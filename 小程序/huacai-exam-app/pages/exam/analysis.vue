<template>
    <view class="analysis-container">
        <view class="analysis-content">
            <view class="question-item" v-for="(question, index) in questionList" :key="index">
                <view class="question-header">
                    <view class="question-type">{{ question.type }}</view>
                </view>

                <view class="question-title">
                    {{ question.title }}
                </view>

                <!-- 单选题或判断题 -->
                <view class="options-list" v-if="question.type === '单选题' || question.type === '判断题'">
                    <view class="option-item"
                          v-for="(option, optionIndex) in question.options.split(',')"
                          :key="optionIndex"
                          :class="{
                                    correct: question.correctAnswer.split(',').map(Number).includes(optionIndex),
                                    wrong: question.userAnswer == optionIndex && !question.correctAnswer.split(',').map(Number).includes(optionIndex),
                                    selected: question.userAnswer == optionIndex
                                  }"
                    >
                        <view class="option-prefix">{{ optionPrefix[optionIndex] }}</view>
                        <text class="option-text">{{ option }}</text>
                    </view>
                </view>

                <!-- 多选题 -->
                <view class="options-list" v-else-if="question.type === '多选题'">
                    <view
                      class="option-item"
                      v-for="(option, optionIndex) in question.options.split(',')"
                      :key="optionIndex"
                      :class="{
              correct: question.correctAnswer.split(',').map(Number).includes(optionIndex),
              wrong: (question.userAnswer || '').split(',').map(Number).includes(optionIndex) && !question.correctAnswer.split(',').map(Number).includes(optionIndex),
              selected: (question.userAnswer || '').split(',').map(Number).includes(optionIndex)
            }"
                    >
                        <view class="option-prefix">{{ optionPrefix[optionIndex] }}</view>
                        <text class="option-text">{{ option }}</text>
                    </view>
                </view>

                <view class="answer-section">
                    <view class="answer-lable">正确答案: </view>
                    <view class="answer-content">
                        {{ formatCorrectAnswer(question.correctAnswer) }}
                    </view>
                </view>

                <view class="analysis-section">
                    <view class="answer-lable">解析: </view>
                    <view class="answer-content">
                        {{ question.analysis }}
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onLoad} from "@dcloudio/uni-app";
import {selectErListByExamIdAndUserId} from "@/pages/api/exam/er";

const questionList = ref([])

//选项前缀数组
const optionPrefix = ['A', 'B', 'C', 'D']

//正确答案映射
const formatCorrectAnswer = (correctAnswer) => {
  return correctAnswer.split(',').map(i => optionPrefix[Number(i)]).join(',')
}

const load = (options) => {
    selectErListByExamIdAndUserId(options.examId).then(res => {
        questionList.value = res.data
    })
}

onLoad((options) => {
    load(options)
})

</script>

<style lang="scss">
page {
  background-color: #f8f9fa;
}

.analysis-container {
  .header-section {
    background: white;
    padding: 15px 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    position: relative;

    .back-btn {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      background: #f1f3f4;
      margin-right: 10px;

      .iconfont {
        font-size: 20px;
        color: #333;
      }
    }

    .header-title {
      font-size: 18px;
      font-weight: 500;
      color: #333;
    }
  }

  .analysis-content {
    padding: 20px;

    .question-item {
      background: white;
      border-radius: 10px;
      padding: 20px;
      margin-bottom: 20px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

      .question-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .question-number {
          font-size: 14px;
          color: #666;
        }

        .question-type {
          display: inline-block;
          padding: 4px 12px;
          background: linear-gradient(90deg, #4facfe, #00f2fe);
          color: white;
          font-size: 12px;
          border-radius: 15px;
        }
      }

      .question-title {
        font-size: 16px;
        color: #333;
        margin-bottom: 20px;
        line-height: 1.5;
      }

      .options-list {
        margin-bottom: 20px;

        .option-item {
          display: flex;
          align-items: center;
          padding: 12px 15px;
          margin-bottom: 10px;
          background: #f8f9fa;
          border-radius: 8px;
          transition: all 0.3s;

          &.correct {
            background: #e8f5e9;
            border: 1px solid #4caf50;
          }

          &.wrong {
            background: #ffebee;
            border: 1px solid #f44336;
          }

          &.selected {
            background: #e3f2fd;
            border: 1px solid #4facfe;
          }

          .option-prefix {
            width: 26px;
            height: 26px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #e9ecef;
            border-radius: 50%;
            font-weight: 500;
            font-size: 12px;
            margin-right: 12px;
            flex-shrink: 0;
          }

          .option-text {
            flex: 1;
            font-size: 14px;
            line-height: 1.5;
            color: #555;
          }
        }
      }

      .blank-answer {
        margin-bottom: 20px;
        padding: 15px;
        background-color: #f8f9fa;
        border-radius: 10px;

        .user-answer, .correct-answer {
          display: flex;
          margin-bottom: 10px;

          &:last-child {
            margin-bottom: 0;
          }

          .label {
            font-weight: 500;
            margin-right: 10px;
          }

          .value {
            flex: 1;
          }
        }

        .user-answer.correct {
          .label, .value {
            color: #4caf50;
          }
        }
      }

      .answer-section {
        display: flex;
        align-items: flex-start;
        padding: 15px;
        background: #f1f8ff;
        border-radius: 8px;
        margin-bottom: 15px;

        .answer-label {
          font-size: 14px;
          color: #333;
          font-weight: 500;
          margin-right: 10px;
          white-space: nowrap;
        }

        .answer-content {
          font-size: 14px;
          color: #555;
          flex: 1;
        }
      }

      .analysis-section {
        padding: 15px;
        background: #fff8e1;
        border-radius: 8px;

        .analysis-label {
          font-size: 14px;
          color: #333;
          font-weight: 500;
          margin-bottom: 5px;
        }

        .analysis-content {
          font-size: 14px;
          color: #555;
          line-height: 1.6;
        }
      }
    }
  }
}
</style>
