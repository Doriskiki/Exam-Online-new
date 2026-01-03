<template>
  <!-- 练习题页面容器 -->
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

            <!-- 单选题和判断题选项区域 -->
            <view class="options-list" v-if="currentQuestion.type === '单选题' || currentQuestion.type === '判断题'">
                <!-- 遍历选项, 为每个选项创建选项 -->
                <!-- 动态类名绑定
                     selected: 当前选项被选中(在选中选项数组中)
                     correct: 显示答案时且当前选项是正确答案
                     wrong: 显示答案时且当前选项被选中但是不是正确答案
                        -->
                <view class="option-item" v-for="(option, index) in currentQuestion.options" :key="index"
                      :class="{ selected: selectedOption === index, correct: showAnswer
                                && currentQuestion.answer.includes(index), wrong: showAnswer
                                && selectedOption === index
                                && !currentQuestion.answer.includes(index)}"
                      @click="selectOption(index)"
                >
                    <!-- 选项前缀(A,B,C,D) -->
                    <view class="option-prefix">{{ optionPrefix[index] }}</view>
                    <!-- 选项文本内容 -->
                    <text class="option-text">{{ option }}</text>
                </view>
            </view>

            <!-- 多选题选项区域 -->
            <view class="options-list" v-else-if="currentQuestion.type === '多选题'">
                <!-- 遍历选项, 为每个选项创建选项 -->
                <!-- 动态类名绑定
                     selected: 当前选项被选中(在选中选项数组中)
                     correct: 显示答案时且当前选项是正确答案
                     wrong: 显示答案时且当前选项被选中但是不是正确答案
                        -->
                <view class="option-item" v-for="(option, index) in currentQuestion.options" :key="index"
                      :class="{ selected: selectedOptions.includes(index), correct: showAnswer
                                && currentQuestion.answer.includes(index), wrong: showAnswer
                                && selectedOptions.includes(index)
                                && !currentQuestion.answer.includes(index)}"
                      @click="toggleOption(index)"
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
            <!-- 提交答案按钮: 当题目存在并且答案未提交并且已选择答案时显示 -->
            <button class="btn submit-btn" v-if="currentQuestion && !showAnswer
            && (selectOption !== null || selectedOptions.length > 0)" @click="submitAnswer">
                提交答案
            </button>
            <!-- 下一题按钮: 当答案已经提交时显示 -->
            <button class="btn next-btn" v-if="currentQuestion && showAnswer" @click="nextQuestion">
                {{ currentIndex === questionList.length - 1 ? '完成练习' : '下一题' }}
            </button>
        </view>

    </view>
</template>

<script setup>
import {ref, computed} from 'vue'
import {onLoad, onUnload} from '@dcloudio/uni-app'
import {selectAllListByCategoryId} from "@/pages/api/exam/questions";
import {convertQuestions} from "@/utils/huacai";

//选项前缀数组
const optionPrefix = ['A', 'B', 'C', 'D']

//题目列表数据
const questionList = ref([])

//当前题目索引(从0开始)
const currentIndex = ref(0)

//单选题/判断题选中选项(存储选项索引)
const selectedOption = ref(null)
//多选题选中选项数组(存储多个选项索引)
const selectedOptions = ref([])

//是否显示答案解析
const showAnswer = ref(false)

//计时器ID(用于清除定时器)
let timerInterval = null
//计时器(秒)
const timer = ref(0)

//获取当前题目
const currentQuestion = computed(() => {
    return questionList.value[currentIndex.value]
})

//计算当前答题进度的百分比
const progressPercent = computed(() => {
    return ((currentIndex.value + 1) / questionList.value.length) * 100
})

//格式化时间显示
const formatTime = computed(() => {
    const minutes = Math.floor(timer.value / 60)
    const seconds = timer.value % 60
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
})

//加载题目数据
const loadQuestions = (options) => {
    //调用api获取题目列表
    selectAllListByCategoryId(options.id).then(res => {
        questionList.value = convertQuestions(res.data)
    })
}

//选择单选题/判断题
const selectOption = (index) => {
    //只有题目存在并且未显示答案并且是单选题或判断题时才能选择
    if (currentQuestion.value && !showAnswer.value
        && (currentQuestion.value.type === '单选题' || currentQuestion.value.type === '判断题')) {
        selectedOption.value = index
    }
}

//选择多选题
const toggleOption = (index) => {
    //只有题目存在并且未显示答案并且是多选题时才能选择
    if (currentQuestion.value && !showAnswer.value && (currentQuestion.value.type === '多选题')) {
        const optionIndex = selectedOptions.value.indexOf(index)
        //如果选项已经选中, 则取消选择, 否则添加到选中数组
        if (optionIndex === -1) {
            selectedOptions.value.push(index)
        } else {
            selectedOptions.value.splice(optionIndex, 1)
        }
    }
}

//启动计时器
const startTimer = () => {
    //每秒增加计时器值
    timerInterval = setInterval(() => {
        timer.value++
    }, 1000)
}

//上一题处理
const prevQuestion = () => {
    //如果不是第一题, 切换到上一题
    if (currentIndex.value > 0) {
        currentIndex.value--
        //重置题目状态
        resetQuestion()
    }
}

//下一题处理
const nextQuestion = () => {
    //如果不是最后一题, 切换到下一题
    if (currentIndex.value < questionList.value.length - 1) {
        currentIndex.value++
        //重置题目状态
        resetQuestion()
    } else {
        //如果是最后一题, 就显示完成提示
        uni.showToast({
            title: '练习完成',
            icon: 'success'
        })
        //1.5秒跳转回练习页面
        setTimeout(() => {
            uni.redirectTo({
                url: '/pages/practice/index'
            })
        }, 1500)
    }
}

//提交答案
const submitAnswer = () => {
    //显示答案
    showAnswer.value = true
}

//重置题目状态
const resetQuestion = () => {
    //清空单选/判断题选中状态
    selectedOption.value = null
    //清空多选题选中状态
    selectedOptions.value = []
    //隐藏答案显示
    showAnswer.value = false
}

//页面加载时执行
onLoad((options) => {
    //加载题目数据
    loadQuestions(options)
    //启动计时器
    startTimer()
})

//页面卸载时执行
onUnload(() => {
    //页面卸载时清除定时器, 防止内存泄漏
    if (timerInterval) {
        clearInterval(timerInterval)
    }
})

</script>

<style>
/* 页面背景色设置 */
page {
    background-color: #f8f9fa;
}

.questions-container {
    display: flex;
    flex-direction: column;
}

.questions-container .header-section {
    background: white;
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.questions-container .header-section .back-btn {
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    background: #f1f3f4;
}

.questions-container .header-section .progress .progress-text {
    font-size: 16px;
    font-weight: 500;
    color: #333;
}

.questions-container .header-section .timer {
    display: flex;
    align-items: center;
    background: #fff5f5;
    padding: 5px 10px;
    border-radius: 20px;
}

.questions-container .header-section .timer .time-text {
    font-size: 14px;
    font-weight: 500;
    color: #ff6b6b;
}

.questions-container .progress-bar-container {
    padding: 0 20px;
    background: white;
}

.questions-container .progress-bar-container .progress-bar {
    height: 4px;
    background: #e9ecef;
    border-radius: 2px;
    overflow: hidden;
}

.questions-container .progress-bar-container .progress-bar .progress-fill {
    height: 100%;
    background: linear-gradient(90deg, #4facfe, #00f2fe);
    transition: width 0.3s ease;
}

.questions-container .question-content {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
}

.questions-container .question-content .loading-text {
    text-align: center;
    font-size: 16px;
    color: #666;
    padding: 20px;
}

.questions-container .question-content .question-info {
    margin-bottom: 25px;
}

.questions-container .question-content .question-info .question-type {
    display: inline-block;
    font-size: 12px;
    padding: 4px 10px;
    border-radius: 15px;
    background: linear-gradient(135deg, #4facfe, #00f2fe);
    color: white;
    margin-bottom: 15px;
}

.questions-container .question-content .question-info .question-title {
    font-size: 18px;
    font-weight: 500;
    color: #333;
    line-height: 1.5;
}

.questions-container .question-content .options-list {
    margin-bottom: 20px;
}

.questions-container .question-content .options-list .option-item {
    display: flex;
    align-items: center;
    padding: 15px;
    margin-bottom: 15px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    transition: all 0.3s;
    border: 1px solid #eee;
}

.questions-container .question-content .options-list .option-item:active {
    transform: scale(0.98);
}

.questions-container .question-content .options-list .option-item.selected {
    border-color: #4facfe;
    background: rgba(79, 172, 254, 0.1);
}

.questions-container .question-content .options-list .option-item.correct {
    border-color: #4caf50;
    background: rgba(76, 175, 80, 0.1);
}

.questions-container .question-content .options-list .option-item.wrong {
    border-color: #f44336;
    background: rgba(244, 67, 54, 0.1);
}

.questions-container .question-content .options-list .option-item .option-prefix {
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

.questions-container .question-content .options-list .option-item .option-text {
    flex: 1;
    font-size: 16px;
    color: #333;
    line-height: 1.4;
}

.questions-container .question-content .blank-container {
    padding: 20px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.questions-container .question-content .blank-container .blank-input {
    width: 100%;
    padding: 12px 15px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 16px;
    box-sizing: border-box;
}

.questions-container .action-buttons {
    display: flex;
    padding: 20px;
    background: white;
    border-top: 1px solid #eee;
}

.questions-container .action-buttons .btn {
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
}

.questions-container .action-buttons .btn.prev-btn {
    background: #f1f3f4;
    color: #333;
    margin-right: 10px;
}

.questions-container .action-buttons .btn.submit-btn {
    background: linear-gradient(135deg, #ff9a9e, #fad0c4);
    color: white;
    margin-left: 10px;
}

.questions-container .action-buttons .btn.next-btn {
    background: linear-gradient(135deg, #4facfe, #00f2fe);
    color: white;
    margin-left: 10px;
}

.questions-container .action-buttons .btn:active {
    transform: scale(0.98);
}
</style>
