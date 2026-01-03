<template>
  <!-- 考试页面容器 -->
    <view class="questions-container">
        <!-- 页面头部, 进度和计时器 -->
        <view class="header-section">
            <!-- 题目进度显示: 当前题号/总题数 -->
            <view class="progress">
                <text class="progress-text">{{ currentIndex + 1 }} / {{ questionList.length }}</text>
            </view>
            <!-- 计时器 -->
            <view class="timer">
                <text class="time-text">{{ formatTime }}</text>
            </view>
        </view>

        <!-- 进度条容器 -->
        <view class="progress-bar-container">
            <view class="progress-bar">
                <!-- 进度条填充部分, 宽度根据当前进度动态计算 -->
                <view class="progress-fill" :style="{ width: progressPercent + '%' }"/>
            </view>
        </view>

        <!-- 题目内容区域 -->
        <view class="question-content" v-if="currentQuestion">
            <!-- 题目信息: 题型和题目内容 -->
            <view class="question-info">
                <!-- 题型 -->
                <view class="question-type">{{ currentQuestion.type }}</view>
                <!-- 题目内容 -->
                <view class="question-title">{{ currentQuestion.title }}</view>
            </view>

            <!-- 单选题和判断题的选项列表 -->
            <view class="options-list" v-if="currentQuestion.type === '单选题' || currentQuestion.type === '判断题'">
                <!-- 单个选项的题 -->
                <view class="option-item" v-for="(option, index) in currentQuestion.options" :key="index"
                      :class="{ selected: selectedOption === index }" @click="selectOption(index)"
                >
                    <!-- 选项前缀(A,B,C,D) -->
                    <view class="option-prefix">{{ optionPrefix[index] }}</view>
                    <!-- 选项文本内容 -->
                    <text class="option-text">{{ option }}</text>
                </view>
            </view>

            <!-- 多选题选项区域 -->
            <view class="options-list" v-else-if="currentQuestion.type === '多选题'">
                <!-- 多个选项的题 -->
                <view class="option-item" v-for="(option, index) in currentQuestion.options" :key="index"
                      :class="{ selected: isMultiSelected(index) }" @click="togMultiOption(index)"
                >
                    <!-- 选项前缀(A,B,C,D) -->
                    <view class="option-prefix">{{ optionPrefix[index] }}</view>
                    <!-- 选项文本内容 -->
                    <text class="option-text">{{ option }}</text>
                </view>
            </view>
        </view>

        <!-- 操作按钮区域 -->
        <view class="action-buttons">
            <!-- 上一题按钮: 当不是第一题时显示 -->
            <button class="btn prev-btn" v-if="currentIndex > 0" @click="prevQuestion">上一题</button>
            <!-- 下一题按钮 -->
            <button class="btn next-btn" @click="nextQuestion">
                {{ currentIndex === questionList.length - 1 ? '完成考试' : '下一题' }}
            </button>
        </view>

    </view>
</template>

<script setup>
import {ref, computed} from 'vue'
import {onLoad} from "@dcloudio/uni-app";
import {selectExamQuestionsListByExamId} from "@/pages/api/exam/eq";
import {convertQuestions} from "@/utils/huacai";
import {completeExam} from "@/pages/api/exam/er";

//选项前缀数组
const optionPrefix = ['A', 'B', 'C', 'D']
//考试ID
const examId = ref(null)
//当前题目索引
const currentIndex = ref(0)
//考试题列表数据
const questionList = ref([])
//计时器ID(用于清除定时器)
let timerInterval = null
//计时器(秒)
const timer = ref(0)
//保存每道题的选择结果
const selectedOptions = ref([])

//上一题
const prevQuestion = () => {
    //如果不是第一题, 就切换到上一题
    if (currentIndex.value > 0) {
        currentIndex.value--
    }
}

//下一题
const nextQuestion = () => {
    //如果不是最后一题, 切换到下一题
    if (currentIndex.value < questionList.value.length - 1) {
        currentIndex.value++
    } else {
        //完成考试
        uni.showModal({
            title: '考试完成',
            content: '恭喜您已经完成本次考试! 点击确定查看考试结果',
            showCancel: false,
            success: function () {
                uni.showLoading({
                    title: '提交中...',
                    mask: true
                })

                //将考试结果转换为后端需要的格式
                const examResult = questionList.value.map((question, index) => {
                    //获取用户答案
                    const userAnswer = selectedOptions.value[index];
                    let formattedAnswer;

                    //处理未作答情况
                    if (userAnswer === undefined || userAnswer === null) {
                        formattedAnswer = '未作答'
                    }
                    //处理多选题答案(数组格式)
                    else if (Array.isArray(userAnswer)) {
                        if (userAnswer.length === 0) {
                            formattedAnswer = '未作答'
                        } else {
                            //将数组排序并转换为逗号分隔的字符串
                            formattedAnswer = `${userAnswer.sort().join(',')}`
                        }
                    }
                    //处理单选题或者判断题的答案(数字格式)
                    else {
                        formattedAnswer = `${userAnswer}`
                    }

                    //返回格式化后的考试结果对象
                    return {
                        examId: examId.value,
                        questionId: question.questionId,
                        userAnswer: formattedAnswer
                    }
                })

                completeExam(examResult).then(res => {
                    //关闭加载状态
                    uni.hideLoading()
                    //准备跳转参数
                    const result = {
                        examId: examId.value,
                        euId: res.msg
                    }
                    //跳转到考试结果页面
                    uni.redirectTo({
                        url: '/pages/exam/result?result=' + encodeURIComponent(JSON.stringify(result))
                    })
                })

            }
        })
    }
}

//获取当前题目
const currentQuestion = computed(() => {
    return questionList.value[currentIndex.value]
})

//判断多选题选项是否被选中
const isMultiSelected = (index) => {
    //获取当前题目的选择数据, 如果没有则返回空数组
    const currentSelections = selectedOptions.value[currentIndex.value] || []
    //判断选项索引是否在选择数组中
    return currentSelections.includes(index)
}

//选择单选题/判断题
const selectOption = (index) => {
    //只有题目存在并且未显示答案并且是单选题或判断题时才能选择
    if (currentQuestion.value.type === '单选题' || currentQuestion.value.type === '判断题') {
        selectedOption.value = index
    }
}

//加载题目数据
const loadQuestions = (options) => {
    examId.value = options.id
    //调用API获取考试题目列表
    selectExamQuestionsListByExamId(options.id).then(res => {
        questionList.value = convertQuestions(res.data)
    })
}

//切换多选题选项的选中状态
const togMultiOption = (index) => {
    //初始化当前题目的选择数组
    if (!Array.isArray(selectedOptions.value[currentIndex.value])) {
        selectedOptions.value[currentIndex.value] = []
    }

    //获取当前题目的选择数组
    const currentSelections = selectedOptions.value[currentIndex.value];

    //查找选项索引在数组中的位置
    const optionIndex = currentSelections.indexOf(index)

    //如果选项不在数组中, 就添加
    if (optionIndex === -1) {
        currentSelections.push(index)
    } else {
        //如果选项已经在数组中了, 就移除
        currentSelections.splice(optionIndex, 1)
    }

    selectedOptions.value = [...selectedOptions.value]
}

//计算函数: 当前单选题/判断题的选中选项
const selectedOption = computed({
    //获取当前题目的选中选项
    get: () => {
        const value = selectedOptions.value[currentIndex.value]
        return (value !== undefined && value !== null) ? value : null
    },
    //设置当前题目的选中选项
    set: (value) => {
        selectedOptions.value[currentIndex.value] = value
    }
})

//格式化时间显示
const formatTime = computed(() => {
    const minutes = Math.floor(timer.value / 60)
    const seconds = timer.value % 60
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
})

//计算当前答题进度的百分比
const progressPercent = computed(() => {
    return ((currentIndex.value + 1) / questionList.value.length) * 100
})

//启动计时器
const startTimer = () => {
    //每秒增加计时器值
    timerInterval = setInterval(() => {
        timer.value++
    }, 1000)
}


//页面加载时
onLoad((options) => {
    //页面加载时加载题目
    loadQuestions(options)
    //开始计时
    startTimer()
})

</script>

<style lang="scss">
page {
  background-color: #f8f9fa;
}

.questions-container {
  display: flex;
  flex-direction: column;

  .header-section {
    background: white;
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

    .back-btn {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      background: #f1f3f4;

      .iconfont {
        font-size: 20px;
        color: #333;
      }
    }

    .progress {
      .progress-text {
        font-size: 16px;
        font-weight: 500;
        color: #333;
      }
    }

    .timer {
      display: flex;
      align-items: center;
      background: #fff5f5;
      padding: 5px 10px;
      border-radius: 20px;

      .iconfont {
        font-size: 16px;
        color: #ff6b6b;
        margin-right: 5px;
      }

      .time-text {
        font-size: 14px;
        font-weight: 500;
        color: #ff6b6b;
      }
    }
  }

  .progress-bar-container {
    padding: 0 20px;
    background: white;

    .progress-bar {
      height: 4px;
      background: #e9ecef;
      border-radius: 2px;
      overflow: hidden;

      .progress-fill {
        height: 100%;
        background: linear-gradient(90deg, #4facfe, #00f2fe);
        transition: width 0.3s ease;
      }
    }
  }

  .question-content {
    flex: 1;
    padding: 20px;
    overflow-y: auto;

    .question-info {
      margin-bottom: 25px;

      .question-type {
        display: inline-block;
        font-size: 12px;
        padding: 4px 10px;
        border-radius: 15px;
        background: linear-gradient(135deg, #4facfe, #00f2fe);
        color: white;
        margin-bottom: 15px;
      }

      .question-title {
        font-size: 18px;
        font-weight: 500;
        color: #333;
        line-height: 1.5;
      }
    }

    .options-list {
      .option-item {
        display: flex;
        align-items: center;
        padding: 15px;
        margin-bottom: 15px;
        background: white;
        border-radius: 10px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        transition: all 0.3s;
        border: 1px solid #eee;

        &:active {
          transform: scale(0.98);
        }

        &.selected {
          border-color: #4facfe;
          background: rgba(79, 172, 254, 0.1);
        }

        .option-prefix {
          width: 24px;
          height: 24px;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 50%;
          background: #f1f3f4;
          font-size: 14px;
          font-weight: 500;
          margin-right: 15px;
        }

        .option-text {
          flex: 1;
          font-size: 16px;
          color: #333;
          line-height: 1.4;
        }
      }
    }
  }

  .action-buttons {
    display: flex;
    padding: 20px;
    background: white;
    border-top: 1px solid #eee;

    .btn {
      flex: 1;
      height: 45px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 10px;
      font-size: 16px;
      font-weight: 500;
      border: none;
      outline: none;
      transition: all 0.3s;

      &.prev-btn {
        background: #f1f3f4;
        color: #333;
        margin-right: 10px;
      }

      &.next-btn {
        background: linear-gradient(135deg, #4facfe, #00f2fe);
        color: white;
        margin-left: 10px;
      }

      &:active {
        transform: scale(0.98);
      }
    }
  }
}
</style>
