<template>
    <div class="app-container">
        <!-- 顶部搜索 -->
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="考试标题" prop="examTitle">
                <el-input
                        v-model="queryParams.examTitle"
                        placeholder="请输入考试标题"
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
                        v-hasPermi="['exam:eq:add']"
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
                        v-hasPermi="['exam:eq:edit']"
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
                        v-hasPermi="['exam:eq:remove']"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['exam:eq:export']"
                >导出
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="eqList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="考试标题" align="center" prop="examTitle"/>
            <el-table-column label="题型" align="center" prop="type"/>
            <el-table-column label="题目" align="center" prop="title"/>
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
            <el-table-column label="题目分数" align="center" prop="score"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPermi="['exam:eq:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPermi="['exam:eq:remove']">删除
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

        <!-- 添加或修改试题对话框 -->
        <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
            <el-form ref="eqRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="考试ID" prop="examId">
                    <el-input v-model="form.examId" placeholder="请输入考试ID"/>
                </el-form-item>
                <el-form-item label="题目ID" prop="questionId">
                    <el-input v-model="form.questionId" placeholder="请输入题目ID"/>
                </el-form-item>
                <el-form-item label="题目分数" prop="score">
                    <el-input v-model="form.score" placeholder="请输入题目分数"/>
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

<script setup name="Eq">
import {listEq, getEq, delEq, addEq, updateEq} from "@/api/exam/eq"
import {getToken} from "@/utils/auth.js";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()

const eqList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const selectedRow = ref(null)

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        examId: null,
        questionId: null,
        examTitle: null
    },
    rules: {
        examId: [
            {required: true, message: "考试ID不能为空", trigger: "blur"}
        ],
        questionId: [
            {required: true, message: "题目ID不能为空", trigger: "blur"}
        ],
        score: [
            {required: true, message: "题目分数不能为空", trigger: "blur"}
        ],
    }
})

const {queryParams, form, rules} = toRefs(data)

//格式化答案显示
const formatAnswer = (answer, type) => {
    if (!answer) return ''
    if (type === '单选题') {
        return String.fromCharCode(65 + parseInt(answer))
    } else if (type === '多选题') {
        return answer.split(',').map(a => String.fromCharCode(65 + parseInt(a))).join(',')
    }
}

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

/** 查询试题列表 */
const getList = () => {
    loading.value = true
    listEq(queryParams.value).then(response => {
        eqList.value = response.rows
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
        eqId: null,
        examId: null,
        questionId: null,
        score: null,
        createTime: null
    }
    proxy.resetForm("eqRef")
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
    ids.value = selection.map(item => item.eqId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加试题"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _eqId = row.eqId || ids.value
    getEq(_eqId).then(response => {
        form.value = response.data
        open.value = true
        title.value = "修改试题"
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["eqRef"].validate(valid => {
        if (valid) {
            if (form.value.eqId != null) {
                updateEq(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addEq(form.value).then(response => {
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
    const _eqIds = row.eqId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delEq(_eqIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('exam/eq/export', {
        ...queryParams.value
    }, `eq_${new Date().getTime()}.xlsx`)
}

getList()
</script>
