<template>
    <div class="app-container">
        <!-- 顶部搜索 -->
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="考试标题" prop="title">
                <el-input
                        v-model="queryParams.title"
                        placeholder="请输入考试标题"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select style="width: 200px;" v-model="queryParams.status" placeholder="请选择状态" clearable>
                    <el-option
                            v-for="dict in exam_status"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="用户名" prop="userName">
                <el-input
                        v-model="queryParams.userName"
                        placeholder="请输入用户名"
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
                        v-hasPermi="['exam:eu:add']"
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
                        v-hasPermi="['exam:eu:edit']"
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
                        v-hasPermi="['exam:eu:remove']"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['exam:eu:export']"
                >导出
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="euList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="考试标题" align="center" prop="title"/>
            <el-table-column label="考试时长" align="center" prop="time">
                <template #default="scope">
                    {{ scope.row.time }}小时
                </template>
            </el-table-column>
            <el-table-column label="及格分" align="center" prop="passScore"/>
            <el-table-column label="总分" align="center" prop="totalScore"/>
            <el-table-column label="题目总数" align="center" prop="questionCount"/>
            <el-table-column label="正确题数" align="center" prop="correctCount"/>
            <el-table-column label="得分" align="center" prop="score"/>
            <el-table-column label="状态" align="center" prop="status">
                <template #default="scope">
                    <dict-tag :options="exam_status" :value="scope.row.status"/>
                </template>
            </el-table-column>
            <el-table-column label="用户名" align="center" prop="userName"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPermi="['exam:eu:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPermi="['exam:eu:remove']">删除
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

        <!-- 添加或修改考试用户分配对话框 -->
        <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
            <el-form ref="euRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="考试ID" prop="examId">
                    <el-input v-model="form.examId" placeholder="请输入考试ID"/>
                </el-form-item>
                <el-form-item label="题目总数" prop="questionCount">
                    <el-input v-model="form.questionCount" placeholder="请输入题目总数"/>
                </el-form-item>
                <el-form-item label="正确题数" prop="correctCount">
                    <el-input v-model="form.correctCount" placeholder="请输入正确题数"/>
                </el-form-item>
                <el-form-item label="得分" prop="score">
                    <el-input v-model="form.score" placeholder="请输入得分"/>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select v-model="form.status" placeholder="请选择状态">
                        <el-option
                                v-for="dict in exam_status"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="用户ID" prop="userId">
                    <el-input v-model="form.userId" placeholder="请输入用户ID"/>
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

<script setup name="Eu">
import {listEu, getEu, delEu, addEu, updateEu} from "@/api/exam/eu"
import {getToken} from "@/utils/auth.js";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()
const {exam_status} = proxy.useDict('exam_status')

const euList = ref([])
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
        status: null,
        userId: null,
        title: null,
        userName: null
    },
    rules: {
        examId: [
            {required: true, message: "考试ID不能为空", trigger: "blur"}
        ],
        questionCount: [
            {required: true, message: "题目总数不能为空", trigger: "blur"}
        ],
        status: [
            {required: true, message: "状态不能为空", trigger: "change"}
        ],
        userId: [
            {required: true, message: "用户ID不能为空", trigger: "blur"}
        ],
    }
})

const {queryParams, form, rules, upload} = toRefs(data)

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

/** 查询考试用户分配列表 */
const getList = () => {
    loading.value = true
    listEu(queryParams.value).then(response => {
        euList.value = response.rows
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
        euId: null,
        examId: null,
        questionCount: null,
        correctCount: null,
        score: null,
        status: null,
        userId: null,
        createTime: null
    }
    proxy.resetForm("euRef")
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
    ids.value = selection.map(item => item.euId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加考试用户分配"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _euId = row.euId || ids.value
    getEu(_euId).then(response => {
        form.value = response.data
        open.value = true
        title.value = "修改考试用户分配"
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["euRef"].validate(valid => {
        if (valid) {
            if (form.value.euId != null) {
                updateEu(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addEu(form.value).then(response => {
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
    const _euIds = row.euId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delEu(_euIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('exam/eu/export', {
        ...queryParams.value
    }, `eu_${new Date().getTime()}.xlsx`)
}

getList()
</script>
