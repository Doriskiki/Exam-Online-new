<template>
  <el-container>
    <el-header height="220">
      <!-- 人脸注册提示 -->
      <el-alert
        title="温馨提示：参加考试前请先完成人脸注册"
        type="info"
        :closable="true"
        show-icon
        style="margin-bottom: 15px;">
        <template slot="default">
          <span>本系统采用人脸识别验证身份，请先注册人脸信息。</span>
          <el-button 
            type="text" 
            size="small"
            @click="$router.push('/faceRegister')"
            style="margin-left: 10px;">
            立即注册 →
          </el-button>
        </template>
      </el-alert>

      <div style="display: flex; align-items: center;">
        <el-select
          @change="typeChange"
          clearable
          v-model="queryInfo.examType"
          placeholder="请选择考试类型"
        >
          <el-option
            v-for="item in examType"
            :key="item.type"
            :label="item.info"
            :value="item.type"
          >
          </el-option>
        </el-select>

        <el-input
          v-model="queryInfo.examName"
          placeholder="考试名称"
          @blur="getExamInfo"
          style="margin-left: 5px;width: 220px"
          prefix-icon="el-icon-search"
        ></el-input>
      </div>
    </el-header>

    <el-main>
      <el-table
        height="100%"
        ref="questionTable"
        highlight-current-row
        v-loading="loading"
        :data="examInfo"
        tooltip-effect="dark"
        style="width: 100%;"
      >
        <el-table-column align="center" label="试卷名称">
          <template slot-scope="scope">
            {{ scope.row.examName }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="试卷类型">
          <template slot-scope="scope">
            {{ scope.row.type === 1 ? "公开" : "密码" }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="考试时间范围" width="200">
          <template slot-scope="scope">
            {{
              scope.row.startTime !== "null" && scope.row.endTime !== "null"
                ? scope.row.startTime + " ~" + scope.row.endTime
                : "不限制"
            }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="试卷总时长">
          <template slot-scope="scope">
            {{ scope.row.duration + "分钟" }}
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          prop="totalScore"
          label="试卷总分"
        ></el-table-column>

        <el-table-column
          align="center"
          prop="passScore"
          label="及格分数"
        ></el-table-column>

        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button
              round
              size="small"
              :disabled="!checkExam(scope.row)"
              @click="toStartExam(scope.row)"
              :icon="checkExam(scope.row) ? 'el-icon-edit' : 'el-icon-close'"
              :type="checkExam(scope.row) ? 'primary' : 'info'"
            >
              {{ checkExam(scope.row) ? "考试" : "暂未开始" }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--分页-->
      <el-pagination
        style="margin-top: 25px"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNo"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-main>
    <el-dialog
      title="考试提示"
      :visible.sync="startExamDialog"
      center
      width="60%"
    >
      <el-card style="margin-top: 25px">
        <span>本次考试名称：</span>{{ currentSelectedExam.examName }}
        <br />
        <span>本次考试具体内容：</span>{{ currentSelectedExam.examDesc }}
        <br />
        <span>考试总时长：</span>{{ currentSelectedExam.duration + "分钟" }}
        <br />
        <span>试卷总分：</span>{{ currentSelectedExam.totalScore }}分
        <br />
        <span>及格分数：</span>{{ currentSelectedExam.passScore }}分
        <!--        <br>-->
        <!--        <span>开放类型：</span> {{ currentSelectedExam.type === 2 ? '需要密码' : '完全公开' }}-->
        <!--        <br>-->
      </el-card>

      <span slot="footer" class="dialog-footer">
        <el-button round type="warning" @click="startExamDialog = false"
          >取消</el-button
        >
        <el-button
          round
          type="primary"
          @click="showFaceVerify"
          >开始考试</el-button
        >
      </span>
    </el-dialog>

    <!-- 人脸验证对话框 -->
    <face-verify
      v-if="currentSelectedExam.examId"
      :visible.sync="faceVerifyVisible"
      :exam-id="currentSelectedExam.examId"
      :user-id="currentUserId"
      @verify-success="onVerifySuccess"
      @verify-cancel="onVerifyCancel"
    ></face-verify>
  </el-container>
</template>

<script>
import FaceVerify from './FaceVerify.vue'

export default {
  name: "ExamOnline",
  components: {
    FaceVerify
  },
  data() {
    return {
      queryInfo: {
        examType: '',
        startTime: '',
        endTime: '',
        examName: '',
        pageNo: 1,
        pageSize: 10
      },
      //表格是否在加载
      loading: true,
      //考试类型信息
      examType: [
        {
          info: "公开考试",
          type: 1
        },
        {
          info: "需要密码",
          type: 2
        }
      ],
      //考试信息
      examInfo: [],
      //查询到的考试总数
      total: 0,
      //开始考试的提示框
      startExamDialog: false,
      //当前选中的考试的信息
      currentSelectedExam: {},
      //人脸验证对话框
      faceVerifyVisible: false,
      //当前用户ID
      currentUserId: null
    };
  },
  created() {
    this.getExamInfo();
    this.getCurrentUser();
  },
  methods: {
    //考试类型搜索
    typeChange(val) {
      this.queryInfo.examType = val;
      this.getExamInfo();
    },
    //查询考试信息
    getExamInfo() {
      // 过滤掉 null、undefined 和空字符串，只发送有效参数
      const params = {};
      Object.keys(this.queryInfo).forEach(key => {
        const value = this.queryInfo[key];
        if (value !== null && value !== undefined && value !== '') {
          params[key] = value;
        }
      });
      
      // 确保必要的分页参数存在
      if (!params.pageNo) params.pageNo = 1;
      if (!params.pageSize) params.pageSize = 10;
      
      console.log('发送考试查询请求，参数:', params);
      
      this.$http.post(this.API.getExamInfo, params).then(resp => {
        if (resp.data.code === 200) {
          resp.data.data.forEach(item => {
            item.startTime = String(item.startTime).substring(0, 10);
            item.endTime = String(item.endTime).substring(0, 10);
          });
          this.examInfo = resp.data.data;
          this.getExamTotal();
          this.loading = false;
        }
      }).catch(error => {
        console.error('获取考试信息失败:', error);
        this.$message.error('获取考试信息失败，请稍后重试');
        this.loading = false;
      });
    },
    //查询考试总数
    getExamTotal() {
      // 过滤掉 null、undefined 和空字符串，只发送有效参数
      const params = {};
      Object.keys(this.queryInfo).forEach(key => {
        const value = this.queryInfo[key];
        if (value !== null && value !== undefined && value !== '') {
          params[key] = value;
        }
      });
      
      // 设置大分页获取总数
      params.pageNo = 1;
      params.pageSize = 9999;
      
      this.$http.post(this.API.getExamInfo, params).then(resp => {
        if (resp.data.code === 200) {
          this.total = resp.data.data.length;
        }
      }).catch(error => {
        console.error('获取考试总数失败:', error);
      });
    },
    //获取当前用户信息
    getCurrentUser() {
      this.$http.get(this.API.getCurrentUser).then(resp => {
        if (resp.data.code === 200) {
          // 支持多种字段名
          this.currentUserId = resp.data.data.userId || resp.data.data.id || localStorage.getItem('userId');
          console.log('当前用户ID:', this.currentUserId);
          
          // 如果还是没有，尝试从token解析
          if (!this.currentUserId) {
            this.$message.warning('无法获取用户ID，请重新登录');
          }
        }
      }).catch(error => {
        console.error('获取用户信息失败:', error);
        // 尝试从localStorage获取
        this.currentUserId = localStorage.getItem('userId');
      });
    },
    //分页页面大小改变
    handleSizeChange(val) {
      this.queryInfo.pageSize = val;
      this.getExamInfo();
    },
    //分页插件的页码
    handleCurrentChange(val) {
      this.queryInfo.pageNo = val;
      this.getExamInfo();
    },
    //去考试准备页面
    toStartExam(row) {
      if (row.type === 2) {
        this.$prompt("请输入考试密码", "考试提醒", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(({ value }) => {
            if (value === row.password) {
              console.log(value)
              this.startExamDialog = true;
              this.currentSelectedExam = row;
              console.log("1",this.currentSelectedExam);
              console.log("33333111",this.faceVerifyVisible);

            } else {
              this.$message.error("密码错误o(╥﹏╥)o");
            }
          })
          .catch(() => {});
      } else {
        this.startExamDialog = true;
        this.currentSelectedExam = row;
      }
    },
    //显示人脸验证对话框
    showFaceVerify() {
      console.log("22222",this.currentSelectedExam);
      console.log("33333222",this.faceVerifyVisible);

      // 确保有考试ID和用户ID
      if (!this.currentSelectedExam.examId) {
        this.$message.error('考试信息不完整，请重新选择');
        return;
      }
      if (!this.currentUserId) {
        this.$message.error('用户信息不完整，请重新登录');
        return;
      }
      console.log("222",this.currentSelectedExam)
      this.startExamDialog = false;
      this.faceVerifyVisible = true;
    },
    //人脸验证成功
    onVerifySuccess() {
      console.log('人脸验证成功，准备跳转');
      console.log('当前选中的考试:', this.currentSelectedExam);
      console.log('考试ID:', this.currentSelectedExam.examId);
      
      const examId = this.currentSelectedExam.examId;
      if (!examId) {
        console.error('examId 无效');
        this.$message.error('考试信息错误，无法进入考试');
        return;
      }
      
      const targetPath = '/exam/' + examId;
      console.log('跳转路径:', targetPath);
      
      this.$message.success('人脸验证通过，正在进入考试...');
      
      // 关闭人脸验证对话框
      this.faceVerifyVisible = false;
      
      setTimeout(() => {
        console.log('开始执行路由跳转');
        this.$router.push(targetPath).then(() => {
          console.log('路由跳转成功');
        }).catch(err => {
          console.error('路由跳转失败:', err);
          // 如果是导航重复错误，忽略它
          if (err.name !== 'NavigationDuplicated') {
            this.$message.error('跳转失败，请重试');
          }
        });
      }, 500);
    },
    //人脸验证取消
    onVerifyCancel() {
      this.$message.info('已取消人脸验证');
      this.startExamDialog = true;
    }
  },
  computed: {
    //检查考试的合法性
    checkExam(row) {
      return row => {
        let date = new Date();
        if (row.status === 2) return false;
        if (row.startTime === "null" && row.endTime === "null") {
          return true;
        } else if (row.startTime === "null") {
          return date < new Date(row.endTime);
        } else if (row.endTime === "null") {
          return date > new Date(row.startTime);
        } else if (
          date > new Date(row.startTime) &&
          date < new Date(row.endTime)
        )
          return true;
      };
    }
  }
};
</script>

<style scoped lang="scss">
.el-container {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #F0FDFF 0%, #E0F9FF 100%);
  padding: 20px;

  :deep(.el-table) {
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 8px 24px rgba(0, 217, 255, 0.15);
    
    thead {
      color: rgb(85, 85, 85) !important;
    }
  }

  :deep(.has-gutter tr th) {
    background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
    color: #ffffff;
    font-weight: bold;
    line-height: 32px;
  }

  :deep(.el-alert) {
    border-radius: 16px;
    background: linear-gradient(135deg, #F0FDFF 0%, #ffffff 100%);
    border: 2px solid #00D9FF;
    box-shadow: 0 4px 12px rgba(0, 217, 255, 0.1);
  }

  :deep(.el-select) {
    .el-input__inner {
      border-radius: 20px;
      border: 2px solid #00D9FF;
      transition: all 0.3s ease;
      
      &:focus {
        border-color: #4FD1C5;
        box-shadow: 0 0 0 3px rgba(0, 217, 255, 0.1);
      }
    }
  }

  :deep(.el-input__inner) {
    border-radius: 20px;
    border: 2px solid #00D9FF;
    transition: all 0.3s ease;
    
    &:focus {
      border-color: #4FD1C5;
      box-shadow: 0 0 0 3px rgba(0, 217, 255, 0.1);
    }
  }

  :deep(.el-button) {
    border-radius: 20px;
    transition: all 0.3s ease;
    
    &.el-button--primary {
      background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
      border: none;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(0, 217, 255, 0.4);
      }
    }
  }

  :deep(.el-dialog) {
    border-radius: 24px;
    overflow: hidden;
    
    .el-dialog__header {
      background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
      padding: 20px;
      
      .el-dialog__title {
        color: #ffffff;
        font-weight: bold;
      }
    }
    
    .el-card {
      border-radius: 16px;
      border: none;
      box-shadow: 0 4px 12px rgba(0, 217, 255, 0.1);
    }
  }

  :deep(.el-pagination) {
    .el-pager li {
      border-radius: 8px;
      transition: all 0.3s ease;
      
      &.active {
        background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
      }
      
      &:hover {
        color: #00D9FF;
      }
    }
    
    button {
      border-radius: 8px;
    }
  }
}

.el-header {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  background: transparent;
  padding-bottom: 20px;
}

@keyframes leftMoveIn {
  0% {
    transform: translateX(-100%);
    opacity: 0;
  }
  100% {
    transform: translateX(0%);
    opacity: 1;
  }
}

.el-table {
  box-shadow: 0 8px 24px rgba(0, 217, 255, 0.15);
  height: calc(100% - 60px) !important;
  overflow: auto !important;
  border-radius: 16px;
}

span {
  font-weight: 500;
  display: inline-block;
  font-size: 16px;
  padding: 10px !important;
}

.el-form-item {
  margin-bottom: 10px;
}

.el-form {
  :deep(.el-input) {
    width: 100% !important;
  }
  :deep(.el-select) {
    width: 100% !important;
  }
  :deep(.el-form-item__label) {
    padding-bottom: 0 !important;
  }
}
</style>
