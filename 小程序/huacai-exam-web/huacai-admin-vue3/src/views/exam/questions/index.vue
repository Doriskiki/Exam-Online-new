<template>
    <div class="app-container">
        <!-- 顶部搜索 -->
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="题型" prop="type">
                <el-select style="width: 200px;" v-model="queryParams.type" placeholder="请选择题型" clearable>
                    <el-option
                            v-for="dict in question_type"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="题目分类" prop="categoryId">
                <el-select v-model="queryParams.categoryId" placeholder="请选择题目分类" style="width: 200px">
                    <el-option
                            v-for="category in categoryList"
                            :key="category.categoryId"
                            :label="category.name"
                            :value="category.categoryId"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="题目标题" prop="title">
                <el-input
                        v-model="queryParams.title"
                        placeholder="请输入题目标题"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 顶部按钮 -->
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        plain
                        icon="Plus"
                        @click="handleAdd"
                        v-hasPermi="['exam:questions:add']"
                >新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="Edit"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['exam:questions:edit']"
                >修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="Delete"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['exam:questions:remove']"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['exam:questions:export']"
                >导出
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="questionsList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="题型" align="center" prop="type">
                <template #default="scope">
                    <dict-tag :options="question_type" :value="scope.row.type"/>
                </template>
            </el-table-column>
            <el-table-column label="题目分类" align="center" prop="categoryName"/>
            <el-table-column label="题目标题" align="center" prop="title"/>
            <el-table-column label="选项" align="center" prop="options">
                <template #default="scope">
                    <div v-if="scope.row.options">
                        <div v-for="(option, index) in scope.row.options.split(',')" :key="index">
                            {{ String.fromCharCode(65 + index) }}. {{ option }}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="正确答案" align="center" prop="answer">
                <template #default="scope">
                    <span v-if="scope.row.type === '判断题'">
                        {{ scope.row.answer == 0 ? '正确' : '错误' }}
                    </span>
                    <span v-else>
                        {{ formatAnswer(scope.row.answer, scope.row.type) }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="题目解析" align="center" prop="analysis"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPermi="['exam:questions:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPermi="['exam:questions:remove']">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <pagination
                v-show="total>0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
        />

        <!-- 添加或修改题目对话框 -->
        <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
            <el-form ref="questionsRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="题型" prop="type">
                    <el-select v-model="form.type" placeholder="请选择题型" @change="handleTypeChange">
                        <el-option
                                v-for="dict in question_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="题目分类" prop="categoryId">
                    <el-select v-model="form.categoryId" placeholder="请选择题目分类" style="width: 100%">
                        <el-option
                                v-for="category in categoryList"
                                :key="category.categoryId"
                                :label="category.name"
                                :value="category.categoryId"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="题目标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入题目标题"/>
                </el-form-item>
                <el-form-item label="选项" prop="options" v-if="form.type !== '判断题'">
                    <!--                    <el-input v-model="form.options" placeholder="请输入选项"/>-->
                    <div v-for="(option, index) in optionList" :key="index"
                         style="display: flex; align-items: center; margin-bottom: 10px">
                        <el-input v-model="optionList[index]"
                                  :placeholder="'选项' + String.fromCharCode(65 + index)"
                                  style="width: 100%; margin-right: 10px"
                        >
                            <template #prepend>{{ String.fromCharCode(65 + index) }}</template>
                        </el-input>
                    </div>
                </el-form-item>

                <!-- 判断题选项 -->
                <el-form-item label="选项" prop="options" v-if="form.type === '判断题'">
                    <div style="display: flex; gap: 20px">
                        <el-tag type="info" size="large">A.正确</el-tag>
                        <el-tag type="info" size="large">B.错误</el-tag>
                    </div>
                </el-form-item>

                <!-- 答案选择区域 -->
                <el-form-item label="正确答案" prop="answer">
                    <!-- 单选题答案 -->
                    <el-radio-group v-model="form.answer" v-if="form.type === '单选题'">
                        <el-radio v-for="(option, index) in optionList" :key="index"
                                  :label="index" :disabled="!option">
                            {{ String.fromCharCode(65 + index) }}
                        </el-radio>
                    </el-radio-group>

                    <!-- 多选题答案 -->
                    <el-checkbox-group v-model="multiAnswer" v-if="form.type === '多选题'">
                        <el-checkbox v-for="(option, index) in optionList" :key="index"
                                     :label="index" :disabled="!option">
                            {{ String.fromCharCode(65 + index) }}
                        </el-checkbox>
                    </el-checkbox-group>

                    <!-- 判断题答案 -->
                    <el-radio-group v-model="form.answer" v-if="form.type === '判断题'">
                        <el-radio :label="0">正确</el-radio>
                        <el-radio :label="1">错误</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="题目解析" prop="analysis">
                    <el-input v-model="form.analysis" type="textarea" placeholder="请输入内容"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </vxe-modal>
    </div>
</template>

<script setup name="Questions">
import {listQuestions, getQuestions, delQuestions, addQuestions, updateQuestions} from "@/api/exam/questions"
import {getToken} from "@/utils/auth.js";
import {selectAllCategoryList} from "@/api/exam/category.js";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()
const {question_type} = proxy.useDict('question_type')

const questionsList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const selectedRow = ref(null)

//初始四个选项
const optionList = ref(["", "", "", ""])
//多选题答案数组
const multiAnswer = ref([])

//处理题型变化
const handleTypeChange = (type) => {
    //充值选项和答案
    if (type === '判断题') {
        optionList.value = ["正确", "错误"]
    } else {
        optionList.value = ["", "", "", ""]
    }
    form.value.answer = null
    multiAnswer.value = []
}

//格式化答案显示
const formatAnswer = (answer, type) => {
    if (!answer) return ''
    if (type === '单选题') {
        return String.fromCharCode(65 + parseInt(answer))
    } else if (type === '多选题') {
        return answer.split(',').map(a => String.fromCharCode(65 + parseInt(a))).join(',')
    }
}

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
        categoryId: null,
        title: null,
    },
    rules: {
        type: [
            {required: true, message: "题型不能为空", trigger: "change"}
        ],
        categoryId: [
            {required: true, message: "分类ID不能为空", trigger: "blur"}
        ],
        title: [
            {required: true, message: "题目标题不能为空", trigger: "blur"}
        ],
        analysis: [
            {required: true, message: "题目解析不能为空", trigger: "blur"}
        ],
    },
})

const {queryParams, form, rules} = toRefs(data)

//点击行 获取行
const clickRow = (row) => {
    selectedRow.value = row; // 更新选中的行
    const table = proxy.$refs.table;
    // 清除所有已选中的行
    table.clearSelection();
    // 选中当前点击的行
    table.toggleRowSelection(row, true);
}

/** 自定义序号 */
const indexMethod = (index) => {
    let pageNum = queryParams.value.pageNum - 1;
    if ((pageNum !== -1 && pageNum !== 0)) {
        return (index + 1) + (pageNum * queryParams.value.pageSize);
    } else {
        return (index + 1)
    }
}

/** 查询题目列表 */
const getList = () => {
    loading.value = true
    listQuestions(queryParams.value).then(response => {
        questionsList.value = response.rows
        total.value = response.total
        loading.value = false
    })
}

// 取消按钮
const cancel = () => {
    open.value = false
    reset()
}

// 表单重置
const reset = () => {
    form.value = {
        questionId: null,
        type: null,
        categoryId: null,
        title: null,
        options: null,
        answer: null,
        analysis: null,
        createTime: null
    }
    optionList.value = ["", "", "", ""]
    multiAnswer.value = []
    proxy.resetForm("questionsRef")
}

/** 搜索按钮操作 */
const handleQuery = () => {
    queryParams.value.pageNum = 1
    getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
    proxy.resetForm("queryRef")
    handleQuery()
}

// 多选框选中数据
const handleSelectionChange = (selection) => {
    ids.value = selection.map(item => item.questionId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加题目"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _questionId = row.questionId || ids.value
    getQuestions(_questionId).then(response => {
        form.value = response.data
        open.value = true
        title.value = "修改题目"

        //处理选项数据
        if (form.value.options) {
            if (form.value.type === '判断题') {
                optionList.value = form.value.options.split(',')
            } else {
                const options = form.value.options.split(',')
                //确保有4个选项
                optionList.value = [...options, ...Array(4 - options.length).fill('')]
            }
        }
        //处理答案数据
        if (form.value.type === '多选题') {
            multiAnswer.value = form.value.answer?.split(',').map(a => parseInt(a))
        } else if (form.value.type === '单选题') {
            form.value.answer = parseInt(form.value.answer)
        } else if (form.value.type === '判断题') {
            form.value.answer = parseInt(form.value.answer)
        }
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["questionsRef"].validate(valid => {
        if (valid) {
            //处理选项数据
            if (form.value.type !== '判断题') {
                form.value.options = optionList.value.filter(opt => opt !== '').join(',')
            } else {
                form.value.options = "正确,错误"
            }

            //处理多选题答案
            if (form.value.type === '多选题') {
                form.value.answer = multiAnswer.value.sort((a, b) => a - b).join(',')
            }

            if (form.value.questionId != null) {
                updateQuestions(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addQuestions(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功")
                    open.value = false
                    getList()
                })
            }
        }
    })
}

/** 删除按钮操作 */
const handleDelete = (row) => {
    const _questionIds = row.questionId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delQuestions(_questionIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('exam/questions/export', {
        ...queryParams.value
    }, `questions_${new Date().getTime()}.xlsx`)
}

//分类列表数据
const categoryList = ref([])

//调用api查询所有分类列表数据
const getAllCategoryList = () => {
    selectAllCategoryList().then(res => {
        categoryList.value = res.data
    })
}

getList()
getAllCategoryList()
</script>
