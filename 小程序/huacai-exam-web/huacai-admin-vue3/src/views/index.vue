<template>
    <div class="dashboard">
        <div class="welcome-section">
            <div class="welcome-content">
                <h1 class="welcome-title">欢迎使用 考试管理端</h1>
            </div>
        </div>

        <div class="data-overview">
            <el-row :gutter="20">
                <el-col :span="8" v-for="item in statsData" :key="item.title">
                    <div class="data-card">
                        <div class="data-icon" :style="{ backgroundColor: item.color }">
                            <el-icon :size="35" color="#fff">
                                <component :is="item.icon"/>
                            </el-icon>
                        </div>
                        <div class="data-content">
                            <div class="data-value">{{ item.value }}</div>
                            <div class="data-title">{{ item.title }}</div>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>

        <div class="chart-wrapper">
            <el-card class="chart-card">
                <template #header>
                    <div class="card-header">
                        <span>题目类型分布</span>
                    </div>
                </template>
                <div class="chart-container">
                    <div ref="questionChart" style="width: 100%; height: 400px"></div>
                </div>
            </el-card>
        </div>

    </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted} from 'vue'
import {User, Tickets, QuestionFilled} from "@element-plus/icons-vue";
import {listUser} from "@/api/system/user.js";
import {listExam} from "@/api/exam/exam.js";
import {listQuestions} from "@/api/exam/questions.js";
import {getQuestionTypeStats} from "@/api/exam/category.js";
import * as echarts from 'echarts'

//图表引用
const questionChart = ref(null)
let questionChartInstance = null

//统计数据
const statsData = ref([
    {
        title: '用户总数',
        value: 0,
        icon: shallowRef(User),
        color: '#24ad76'
    },
    {
        title: '考试总数',
        value: 0,
        icon: shallowRef(Tickets),
        color: '#325b91'
    },
    {
        title: '题目总数',
        value: 0,
        icon: shallowRef(QuestionFilled),
        color: '#ce3053'
    },
])

//获取统计数据
const fetchStats = async () => {
    const [userRes, examRes, questionRes] = await Promise.all([
        listUser({pageSize: 1, pageNum: 1}),
        listExam({pageSize: 1, pageNum: 1}),
        listQuestions({pageSize: 1, pageNum: 1}),
    ])

    //更新统计数据
    statsData.value[0].value = userRes.total || 0
    statsData.value[1].value = examRes.total || 0
    statsData.value[2].value = questionRes.total || 0
}

const initCharts = async () => {
    //获取题目类型统计数据
    const res = await getQuestionTypeStats();

    let chartData = []
    if (res && res.data && Array.isArray(res.data)) {
        chartData = res.data.map(item => ({
            value: parseInt(item.questionCount),
            name: item.categoryName
        }))
        console.log(chartData)
    }

    questionChartInstance = echarts.init(questionChart.value);
    const questionOption = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '5%',
            left: 'center'
        },
        series: [
            {
                name: '题目类型',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: chartData
            }
        ]
    }
    questionChartInstance.setOption(questionOption)
}

//窗口大小改变时重置图表大小
const resizeCharts = () => {
    if (questionChartInstance) {
        questionChartInstance.resize()
    }
}

onMounted(() => {
    fetchStats()
    nextTick(() => {
        initCharts()
        window.addEventListener('resize', resizeCharts)
    })
})

//组件卸载前清除事件监听器
onUnmounted(() => {
    window.removeEventListener('resize', resizeCharts)
    if (questionChartInstance) {
        questionChartInstance.dispose()
    }
})

</script>

<style scoped lang="scss">
.dashboard {
  padding: 20px;
  background-color: #f5f7fa;
}

.welcome-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 60px 40px;
  margin-bottom: 30px;
  color: #fff;

  .welcome-content {
    flex: 1;

    .welcome-title {
      font-size: 36px;
      font-weight: bold;
      margin-bottom: 20px;
    }
  }
}

.data-overview {
  margin-bottom: 20px;
}

.data-card {
  display: flex;
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
  }

  .data-icon {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 15px;
  }

  .data-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .data-value {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 5px;
      color: #333;
    }

    .data-title {
      font-size: 14px;
      color: #999;
    }
  }
}

.chart-wrapper {
  margin-top: 20px;
}

.chart-card {
  .card-header {
    font-weight: bold;
    font-size: 16px;
  }

  .chart-container {
    position: relative;
  }
}
</style>
