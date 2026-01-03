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
                        v-hasPermi="['exam:exam:add']"
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
                        v-hasPermi="['exam:exam:edit']"
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
                        v-hasPermi="['exam:exam:remove']"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['exam:exam:export']"
                >导出
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="examList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="考试标题" align="center" prop="title"/>
            <el-table-column label="题目分类" align="center" prop="categoryName"/>
            <el-table-column label="考试时长(小时)" align="center" prop="time"/>
            <el-table-column label="总分" align="center" prop="totalScore"/>
            <el-table-column label="及格分" align="center" prop="passScore"/>
            <el-table-column label="组卷状态" align="center">
                <template #default="scope">
                    <el-tag type="danger" v-if="scope.row.eqCount === 0">未组卷</el-tag>
                    <el-tag type="success" v-else>已组卷</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="用户分配" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="User" @click="handleAssignUser(scope.row)"
                    >用户分配
                    </el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
                <template #default="scope">
                    <el-button link type="primary" icon="Collection" @click="handleManual(scope.row)"
                    >手动组卷
                    </el-button>
                    <el-button link type="primary" icon="MagicStick" @click="handleAutoCompose(scope.row)"
                    >自动组卷
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

        <!-- 添加或修改考试对话框 -->
        <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
            <el-form ref="examRef" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="考试标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入考试标题"/>
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
                <el-form-item label="考试时长(小时)" prop="time">
                    <el-input-number style="width: 100%;" v-model="form.time" placeholder="请输入考试时长(小时)"/>
                </el-form-item>
                <el-form-item label="总分" prop="totalScore">
                    <el-input-number style="width: 100%;" v-model="form.totalScore" placeholder="请输入总分"/>
                </el-form-item>
                <el-form-item label="及格分" prop="passScore">
                    <el-input-number style="width: 100%;" v-model="form.passScore" placeholder="请输入及格分"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </vxe-modal>

        <!-- 手动组卷组件  -->
        <ManualCompose :category-list="categoryList"
                       :exam-id="currentExamId"
                       v-model:visible="composeOpen"
                       @success="getList"
        ></ManualCompose>

        <vxe-modal title="分配考试用户" v-model="assignOpen" width="40%" show-maximize showFooter resize>
            <el-form :model="assignForm" ref="assignUserRef" label-width="100px" label-position="right">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="考试标题">
                            {{ assignForm.examTitle }}
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="考试ID">
                            {{ assignForm.examId }}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="选择用户">
                    <el-button type="primary" icon="Plus" @click="handleSelectUser">选择用户</el-button>
                </el-form-item>
                <div style="border: 1px solid #c9c6c6; border-radius: 4px; min-height: 150px; margin-top: 10px">
                    <div style="padding: 10px; border-bottom: 1px solid #c9c6c6; background-color: #ebeef5;font-weight: bold">
                        <span>已选择{{ selectedUsers.length }}个用户</span>
                    </div>
                    <div style="padding: 10px; overflow-y: auto">
                        <el-tag v-for="(user, index) in selectedUsers" :key="user.userId"
                                closable @close="removeUser(index)">
                            {{ user.userName }}
                        </el-tag>
                    </div>
                </div>
            </el-form>
            <template #footer>
                <el-button type="primary" @click="submitAssignUser" :disabled="selectedUsers.length === 0">确 定
                </el-button>
                <el-button @click="assignOpen = false">取 消</el-button>
            </template>
        </vxe-modal>

        <vxe-modal title="选择用户" v-model="userSelectOpen" width="40%" height="450px" show-maximize showFooter resize>
            <el-table :data="userList" ref="userTableRef" @selection-change="handleUserSelectionChange"
                      height="350" border style="width: 100%;">
                <el-table-column type="selection" width="55" align="center"/>
                <el-table-column label="用户名称" prop="userName"/>
            </el-table>

            <template #footer>
                <el-button type="primary" @click="submitSelectUser" :disabled="userSelected.length === 0">
                    确认选择
                </el-button>
                <el-button @click="userSelectOpen = false">
                    取 消
                </el-button>
            </template>
        </vxe-modal>
    </div>
</template>

<script setup name="Exam">
import {listExam, getExam, delExam, addExam, updateExam, autoCompose} from "@/api/exam/exam"
import {getToken} from "@/utils/auth.js";
import {selectAllCategoryList} from "@/api/exam/category.js";
import ManualCompose from "@/views/exam/exam/ManualCompose.vue";
import {ElLoading, ElMessage, ElMessageBox} from "element-plus";
import {selectNoAdminUserLit} from "@/api/system/user.js";
import {addEu} from "@/api/exam/eu.js";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()

const examList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const selectedRow = ref(null)

const userSelectOpen = ref(false)

const assignForm = ref({
    examId: '',
    examTitle: ''
})

const selectedUsers = ref([])
const assignOpen = ref(false)
const userSelected = ref([])

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        categoryId: null,
    },
    rules: {
        title: [
            {required: true, message: "考试标题不能为空", trigger: "blur"}
        ],
        categoryId: [
            {required: true, message: "分类ID不能为空", trigger: "blur"}
        ],
    },
})

const {queryParams, form, rules} = toRefs(data)

const handleAssignUser = (row) => {
    assignForm.value.examId = row.examId
    assignForm.value.examTitle = row.title
    selectedUsers.value = []
    assignOpen.value = true
}

//选择用户按钮操作
const handleSelectUser = () => {
    userSelectOpen.value = true
    getUserList()
}

const userList = ref(null)

//获取用户列表
const getUserList = () => {
    selectNoAdminUserLit().then(res => {
        userList.value = res.data
    })
}

//选择用户后的数据
const handleUserSelectionChange = (selection) => {
    userSelected.value = selection
}

//确定选择用户
const submitSelectUser = () => {
    //添加选中的用户到已选择列表中
    userSelected.value.forEach(user => {
        const exists = selectedUsers.value.some(item => item.userId === user.userId)
        if (!exists) {
            selectedUsers.value.push(user)
        }
    })
    userSelectOpen.value = false
}

//移除已选择的用户
const removeUser = (index) => {
    selectedUsers.value.splice(index, 1)
}

//提交分配用户
const submitAssignUser = () => {
    const promises = selectedUsers.value.map(user => {
        const form = {
            examId: assignForm.value.examId,
            userId: user.userId
        }
        return addEu(form)
    });

    Promise.all(promises).then(res => {
        ElMessage.success('分配成功')
        assignOpen.value = false
        getList()
    }).catch(error => {
        ElMessage.error(error.msg || '分配失败')
    })
}

//自动组卷操作
const handleAutoCompose = (row) => {
    ElMessageBox.confirm(
        '确认要为该考试进行自动组卷操作吗?系统将根据考试总分和分类自动选取题目',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            const loading = ElLoading.service({
                lock: true,
                text: '正在组卷中...',
                background: 'rgba(0, 0, 0, 0.7)',
            })
            autoCompose(row.examId).then(res => {
                ElMessage.success('自动组卷成功')
                loading.close()
                getList()
            })
        })
}

//当前选中的考试ID
const currentExamId = ref('')
//手动组卷组件弹窗是否打开
const composeOpen = ref(false)

//手动组卷
const handleManual = (row) => {
    currentExamId.value = row.examId
    composeOpen.value = true
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

/** 查询考试列表 */
const getList = () => {
    loading.value = true
    listExam(queryParams.value).then(response => {
        examList.value = response.rows
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
        examId: null,
        title: null,
        categoryId: null,
        time: null,
        totalScore: null,
        passScore: null,
        createTime: null
    }
    proxy.resetForm("examRef")
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
    ids.value = selection.map(item => item.examId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加考试"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _examId = row.examId || ids.value
    getExam(_examId).then(response => {
        form.value = response.data
        open.value = true
        title.value = "修改考试"
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["examRef"].validate(valid => {
        if (valid) {
            if (form.value.examId != null) {
                updateExam(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addExam(form.value).then(response => {
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
    const _examIds = row.examId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delExam(_examIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('exam/exam/export', {
        ...queryParams.value
    }, `exam_${new Date().getTime()}.xlsx`)
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
