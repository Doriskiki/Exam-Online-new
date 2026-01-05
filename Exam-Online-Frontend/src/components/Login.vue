<template>
  <el-container>
    <el-main>
      <el-card class="box-card" shadow="always">
        <div slot="header" class="card-header">
          <p>在线考试系统登录</p>
        </div>

        <div>
          <el-form
            :model="loginForm"
            :rules="loginFormRules"
            ref="loginForm"
            :status-icon="true"
            label-width="100px"
          >
            <el-form-item prop="username">
              <el-input
                prefix-icon="el-icon-user"
                v-model="loginForm.username"
                placeholder="账号"
              ></el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                prefix-icon="el-icon-lock"
                v-model="loginForm.password"
                placeholder="密码"
                show-password
              ></el-input>
            </el-form-item>

            <el-form-item prop="code">
              <el-input
                class="code"
                prefix-icon="el-icon-chat-line-round"
                v-model="loginForm.code"
                placeholder="验证码"
              ></el-input>
              <img
                src="http://localhost:8888/util/getCodeImg"
                @click="changeCode"
                id="code"
                style="float: right;margin-top: 4px;cursor: pointer"
                title="看不清,点击刷新"
                alt="验证码"
              />
            </el-form-item>

            <el-form-item>
              <el-button round type="primary" @click="submitForm('loginForm')"
                >登录</el-button
              >
              <el-button round type="warning" @click="toRegisterPage"
                >学员注册</el-button
              >
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "Login",
  data() {
    //自定义验证码校验规则
    var validateCode = (rule, value, callback) => {
      //验证码不区分大小写
      if (
        value.toString().toLocaleLowerCase() !==
        this.code.toString().toLocaleLowerCase()
      ) {
        callback(new Error("验证码输入错误"));
      } else {
        callback();
      }
    };
    return {
      //登录表单数据信息
      loginForm: {
        username: "",
        password: "",
        //验证码
        code: ""
      },
      //登录表单的校验规则
      loginFormRules: {
        username: [
          {
            required: true,
            message: "请输入账号",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur"
          },
          {
            min: 5,
            message: "密码不能小于5个字符",
            trigger: "blur"
          }
        ],
        code: [
          {
            required: true,
            message: "请输入验证码",
            trigger: "blur"
          },
          {
            validator: validateCode,
            trigger: "blur"
          }
        ]
      },
      //后台的验证码
      code: (window.onload = () => this.getCode())
    };
  },
  mounted() {
    this.changeCode();
    //检验用户是否存在token,存在直接跳转主页
    this.checkToken();
  },
  methods: {
    //表单信息提交
    submitForm() {
      this.$refs["loginForm"].validate(valid => {
        if (valid) {
          //验证通过
          console.log('准备登录，账号:', this.loginForm.username);
          const data = new FormData();
          data.append("username", this.loginForm.username);
          data.append("password", this.loginForm.password);
          //发送登录请求
          this.$http.post(this.API.login, data).then(resp => {
            console.log('登录响应:', resp.data);
            if (resp.data.code === 200) {
              localStorage.setItem("authorization", resp.data.data);
              
              // 获取并保存用户ID和角色信息
              this.$http.get(this.API.checkToken).then(res => {
                console.log('checkToken响应:', res.data);
                if (res.data.code === 200) {
                  const userData = res.data.data;
                  console.log('用户数据:', userData);
                  
                  // 支持多种字段名
                  const userId = userData.userId || userData.id;
                  const roleId = userData.roleId || userData.role;
                  const username = userData.username;
                  
                  if (userId) {
                    localStorage.setItem("userId", userId);
                    console.log('已保存用户ID:', userId);
                  }
                  if (roleId !== undefined) {
                    localStorage.setItem("roleId", roleId);
                    console.log('已保存角色ID:', roleId);
                    
                    // 显示角色信息
                    let roleName = '未知';
                    if (roleId === 1) roleName = '管理员';
                    else if (roleId === 2) roleName = '教师';
                    else if (roleId === 3) roleName = '学生';
                    
                    console.log('%c用户角色: ' + roleName, 'color: red; font-size: 16px; font-weight: bold;');
                    
                    // 如果不是管理员，给出提示
                    if (roleId !== 1) {
                      console.warn('当前登录的不是管理员账号！roleId =', roleId);
                    }
                  } else {
                    console.error('无法获取角色ID！用户数据:', userData);
                  }
                  if (username) {
                    localStorage.setItem("username", username);
                    console.log('已保存用户名:', username);
                  }
                }
              }).catch(error => {
                console.error('获取用户信息失败:', error);
              });
              
              this.$notify({
                title: "提示",
                message: "登陆成功^_^",
                type: "success",
                duration: 2000
              });
              this.$router.push("/index");
            } else {
              //请求出错
              this.changeCode();
              this.getCode();
              this.$notify({
                title: "提示",
                message: resp.data.message,
                type: "error",
                duration: 2000
              });
            }
          });
        } else {
          //验证不通过
          if (this.code !== this.loginForm.code) {
            this.$notify({
              title: "提示",
              message: "验证码输入有误",
              type: "error",
              duration: 2000
            });
          }
          return false;
        }
      });
    },
    //注册页面跳转
    toRegisterPage() {
      this.$router.push("/register");
    },
    //点击图片刷新验证码
    changeCode() {
      const code = document.querySelector("#code");
      code.src = "http://localhost:8888/util/getCodeImg?id=" + Math.random();
      code.onload = () => this.getCode();
    },
    //获取后台验证码
    getCode() {
      this.$http.get(this.API.getCode).then(resp => {
        this.code = resp.data.message;
      });
    },
    //检验token
    async checkToken() {
      if (window.localStorage.getItem("authorization") !== null) {
        const resp = await this.$http.get(this.API.checkToken);
        if (resp.data.code === 200) {
          //如果token合法自动跳转主页
          await this.$router.push("/index");
        }
      }
    }
  }
};
</script>

<style scoped lang="scss">
.el-container {
  min-width: 417px;
  height: 100%;
  background: linear-gradient(135deg, #E0F9FF 0%, #B8F0FF 50%, #A0E8FF 100%);
  background-size: 100% 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

a {
  text-decoration: none;
  color: #00D9FF;
}

/*  card样式  */
.box-card {
  width: 450px;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 12px 40px rgba(0, 217, 255, 0.2);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.el-card {
  position: absolute;
  top: 45%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
  border-radius: 24px;
  border: 2px solid rgba(0, 217, 255, 0.1);
  
  :deep(.el-card__header) {
    background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
    border-bottom: none;
    padding: 30px 20px;
  }
  
  :deep(.el-card__body) {
    padding: 30px 40px;
  }
}

.card-header {
  text-align: center;
  font-size: 28px;
  font-weight: 700;
  color: #ffffff;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  p {
    font-size: 28px;
    margin: 0;
  }
}

/*  表单的左侧margin清楚 */
:deep(.el-form-item__content) {
  margin: 0 !important;
}

/*  按钮样式 */
:deep(.el-button) {
  width: 48%;
  border-radius: 20px;
  transition: all 0.3s ease;
  border: none;
  
  span {
    font-size: 16px;
    font-weight: bold !important;
    color: #ffffff;
  }
  
  &.el-button--primary {
    background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(0, 217, 255, 0.4);
    }
  }
  
  &.el-button--warning {
    background: linear-gradient(135deg, #FFB84D 0%, #FF9A4D 100%);
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(255, 184, 77, 0.4);
    }
  }
}

/*  按钮前的小图标样式更改*/
:deep(.el-icon) {
  margin-right: 3px;
}
:deep(.el-input__icon) {
  font-size: 18px;
  color: #00D9FF;
}

/*  验证码的输入框*/
.code {
  width: 72%;
}

:deep(.el-input__inner) {
  border-radius: 20px !important;
  padding-left: 40px;
  font-size: 16px;
  border: 2px solid #E0F9FF;
  transition: all 0.3s ease;
  
  &:focus {
    border-color: #00D9FF;
    box-shadow: 0 0 0 3px rgba(0, 217, 255, 0.1);
  }
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>
