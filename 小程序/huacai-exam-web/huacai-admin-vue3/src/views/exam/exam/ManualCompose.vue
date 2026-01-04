<template>
  <!-- 组件弹窗 -->
    <vxe-modal :title="composeTitle" v-model="open" width="80%" height="80%" show-maximize showFooter resize>
        <!-- 加载状态 -->
        <div v-loading="composeLoading">
            <el-row :gutter="20">
                <!-- 左侧题目库区域 -->
                <el-col :span="16">
                    <div>
                        <!-- 题目库头部: 分类筛选 -->
                        <div class="question-bank-header">
                            <el-select v-model="questionQuery.categoryId"
                                       placeholder="请选择分类"
                                       @change="handleQuestionsQuery"
                                       clearable
                            >
                                <el-option v-for="item in categoryList"
                                           :key="item.categoryId"
                                           :label="item.name"
                                           :value="item.categoryId"
                                >
                                </el-option>
                            </el-select>
                        </div>
                        <!-- 题目列表 -->
                        <div style="margin-top: 10px">
                            <el-table :data="questionList" height="500">
                                <el-table-column label="题型" prop="type" width="80"/>
                                <el-table-column label="题目标题" prop="title" show-overflow-tooltip/>
                                <el-table-column label="操作" width="80">
                                    <template #default="scope">
                                        <!-- 添加按钮 -->
                                        <el-button v-if="!isQuestionSelected(scope.row.questionId)"
                                                   size="small"
                                                   type="primary"
                                                   @click="addQuestionToPaper(scope.row)">
                                            添加
                                        </el-button>
                                        <!-- 已添加提示 -->
                                        <span v-else style="color: #918f8f">已添加</span>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <!-- 分页组件 -->
                            <pagination
                                    v-show="questionTotal>0"
                                    :total="questionTotal"
                                    v-model:page="questionQuery.pageNum"
                                    v-model:limit="questionQuery.pageSize"
                                    @pagination="getQuestionList"
                            />
                        </div>
                    </div>
                </el-col>

                <!-- 右侧试卷题目区域 -->
                <el-col :span="8">
                    <div>
                        <!-- 试卷头部: 标题和总分显示 -->
                        <div class="question-bank-header">
                            <h3>试卷题目</h3>
                            <span>
                                总分: {{ totalScore }}
                                <span v-if="examData">(考试总分: {{ examData.totalScore }})</span>
                            </span>
                        </div>
                        <!-- 已选题目列表 -->
                        <div>
                            <!-- 遍历已选题目 -->
                            <div v-for="(element, index) in selectedQuestions"
                                 :key="element.questionId"
                                 class="question-item">
                                <!-- 序号 题型 操作 -->
                                <div style="display: flex; justify-content: space-between;
                                align-items: center; margin-bottom: 10px">
                                    <span>
                                        {{ index + 1 }}
                                        {{ element.type }}
                                    </span>
                                    <div>
                                        <!-- 分数输入 -->
                                        <el-input-number v-model="element.score"
                                                         :min="1"
                                                         :max="100"
                                                         size="small"
                                                         @change="selectTotalScore"
                                        />
                                        <span>分</span>
                                        <!-- 移除按钮 -->
                                        <el-button type="danger"
                                                   size="small"
                                                   @click="removeQuestion(index)"
                                                   circle
                                                   icon="Delete"
                                        />
                                    </div>
                                </div>
                                <!-- 题目 -->
                                <div class="question-item-content">{{ element.title }}</div>
                            </div>
                            <!-- 空状态提示 -->
                            <div style="text-align: center; color: #6e7070; padding: 20px"
                                 v-if="selectedQuestions.length === 0">
                                暂无题目, 请从题目库中添加
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>

        <!-- 底部按钮区域 -->
        <template #footer>
            <div>
                <el-button type="primary" @click="submitCompose">确 定</el-button>
                <el-button @click="open = false">取 消</el-button>
            </div>
        </template>

    </vxe-modal>
</template>

<script setup>
import {deleteEqByExamId, getExam} from "@/api/exam/exam.js";
import {addQuestions, getQuestions, listQuestions} from "@/api/exam/questions.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {addEq, listEq} from "@/api/exam/eq.js";

//定义组件属性
const props = defineProps({
    examId: {
        type: String,
        required: true
    },
    categoryList: {
        type: Array,
        required: true
    },
    visible: {
        type: Boolean,
        default: false
    }
})

//定义组件事件
const emit = defineEmits(['update:visible', 'success'])

//弹窗标题
const composeTitle = ref('手动组卷')
//弹窗是否打开(默认关闭状态)
const open = ref(false)
//考试数据
const examData = ref(null)
//当前分类ID
const currentCategoryId = ref('')
//已选中的题目列表
const selectedQuestions = ref([])
//试卷总分
const totalScore = ref(0)
//加载状态
const composeLoading = ref(false)
//题目列表数据
const questionList = ref([])
//题目总数
const questionTotal = ref(0)

//题目查询参数
const questionQuery = reactive({
    pageNum: 1,
    pageSize: 10,
    categoryId: null
})

//添加题目到试卷
const addQuestionToPaper = (question) => {
    const exists = selectedQuestions.value.some(q => q.questionId === question.questionId)
    if (!exists) {
        selectedQuestions.value.push({
            ...question,
            score: 10
        })
        //计算总分
        selectTotalScore();
    } else {
        ElMessage.warning('该题目已经添加到试卷中')
    }
}

//移除题目
const removeQuestion = (index) => {
    selectedQuestions.value.splice(index, 1)
    //重新计算总分
    selectTotalScore()
}

//计算试卷总分
const selectTotalScore = () => {
    totalScore.value = selectedQuestions.value.reduce((total, q) => total + (q.score || 0), 0)
}

//检查题目是否被选中
const isQuestionSelected = (questionId) => {
    return selectedQuestions.value.some(q => q.questionId === questionId)
}

//处理题目查询(分类变化时)
const handleQuestionsQuery = () => {
    //重置到第一页
    questionQuery.pageNum = 1
    getQuestionList()
}

//监听visible属性, 控制弹窗显示
watch(() => props.visible, (newVal) => {
    open.value = newVal;
    if (newVal) {
        //加载考试数据
        loadExamData();
    }
})

//监听open状态变化, 同步到父组件
watch(open, (newVal) => {
    emit('update:visible', newVal);
    //当组件关闭时, 清空数据
    if (!newVal) {
        selectedQuestions.value = []
        totalScore.value = 0
        examData.value = null
        composeTitle.value = '手动组卷'
    }
})

//监听examId变化, 重新加载数据
watch(() => props.examId, (newVal) => {
    if (newVal && open.value) {
        //加载考试数据
        loadExamData();
    }
})

//监听open状态, 首次打开时加载题目列表
watch(open, (newVal) => {
    if (newVal) {
        getQuestionList()
    }
})

//获取题目列表
const getQuestionList = () => {
    composeLoading.value = true
    listQuestions(questionQuery).then(res => {
        questionList.value = res.rows
        questionTotal.value = res.total
        composeLoading.value = false
    })
}

//加载考试数据
const loadExamData = () => {
    getExam(props.examId).then(res => {
        examData.value = res.data
        currentCategoryId.value = res.data.categoryId
        composeTitle.value = '手动组卷 - ' + res.data.title
        //加载已存在的题目
        loadExistingQuestions()
    })
}

//加载已存在的题目
const loadExistingQuestions = () => {
  listEq({examId: props.examId, pageSize: 10000}).then(res => {
      const eqList = res.rows
      const questionIds = eqList.map(item => item.questionId)

      if (questionIds.length > 0) {
          //并行获取所有题目的详细信息
          const promises = questionIds.map(questionId => getQuestions(questionId));
          Promise.all(promises).then(results => {
              selectedQuestions.value = results.map((res, index) => {
                  return {
                      ...res.data,
                      score: eqList[index].score //保留原有的分数
                  }
              })
              //重新计算总分
              selectTotalScore()
          })
      }
  })
}

//提交组卷
const submitCompose = () => {
    //验证至少有一道题目
    if (selectedQuestions.value.length === 0) {
        ElMessage.warning('请至少添加一道题目')
        return
    }

    //验证总分是否匹配考试设置的总分
    if (examData.value && examData.value.totalScore !== null && totalScore.value !== examData.value.totalScore) {
        ElMessage.warning(`试题总分必须等于考试信息的总分${examData.value.totalScore}分`)
        return;
    }

    //确认对话框
    ElMessageBox.confirm('确认提交组卷吗? 这将覆盖之前的组卷内容.').then(() => {
        //先删除原有的题目关联
        return deleteEqByExamId(props.examId)
    }).then(() => {
        //添加新的题目关联
        const promises = selectedQuestions.value.map(question => {
            const eqData = {
                examId: props.examId,
                questionId: question.questionId,
                score: question.score
            }
            return addEq(eqData)
        })
        return Promise.all(promises)
    }).then(() => {
        ElMessage.success('组卷成功')
        //触发成功事件
        emit('success')
        open.value = false
    }).catch(() => {
        ElMessage.error('组卷失败')
    })

}


</script>

<style scoped>
/* 题目库和试卷头部样式 */
.question-bank-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    border-radius: 4px;
    margin-bottom: 10px;
}

/* 题目区域样式 */
.question-item-content {
    padding: 10px;
    border-radius: 4px;
}

/* 题目项样式 */
.question-item {
    border: 1px solid #ebeef5;
    border-radius: 4px;
    padding: 10px;
    margin-bottom: 10px;
}
</style>
