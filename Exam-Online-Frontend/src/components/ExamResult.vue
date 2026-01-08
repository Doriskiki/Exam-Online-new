<template>
  <el-container>
    <el-header height="150">
      <el-button
        round
        type="primary"
        icon="el-icon-back"
        @click="$router.push('/examOnline')"
        size="medium"
        >返回列表</el-button
      >
      <el-card
        style="height: 100px; text-align: center"
        v-if="examInfo && examRecord"
      >
        <span class="examName">{{ examInfo.examName || "" }}</span>
        <span class="examTime" v-if="examRecord.examTime"
          >({{ examRecord.examTime }})</span
        >

        <el-row
          style="
            margin-top: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            column-gap: 20px;
          "
        >
          <el-tooltip
            class="item"
            effect="dark"
            content="包括(单选、多选、判断题)"
            placement="top-start"
          >
            <span style="font-weight: 800; font-size: 17px; color: red">
              本次客观题得分: {{ examRecord.logicScore || 0 }}分</span
            >
          </el-tooltip>

          <el-tooltip
            class="item"
            effect="dark"
            content="简答题与客观题"
            placement="top-start"
          >
            <span style="font-weight: 800; font-size: 17px; color: #1f90ff">
              试卷总分: {{ examInfo.totalScore || 0 }}分</span
            >
          </el-tooltip>

          <el-tooltip
            class="item"
            effect="dark"
            content="简答题与客观题"
            placement="top-start"
          >
            <span style="font-weight: 800; font-size: 14px">
              考生姓名: {{ examUserName }}</span
            >
          </el-tooltip>
        </el-row>
      </el-card>
    </el-header>

    <el-main style="display: flex; flex-wrap: wrap; column-gap: 20px">
      <!--      <el-card>-->
      <!--题目信息-->
      <div
        v-for="(item, index) in questionInfo"
        :key="index"
        style="margin-top: 15px; width: 450px"
      >
        <div>
          <span class="num">{{ index + 1 }}</span>
          <span v-if="item.questionType === 1">【单选题】</span>
          <span v-else-if="item.questionType === 2">【多选题】</span>
          <span v-else-if="item.questionType === 3">【判断题】</span>
          <span v-else>【简答题】</span>
          <span>{{ item.questionContent }}:</span>
          <span style="color: red; font-style: italic; font-weight: 400"
            >&nbsp;({{ questionScore.get(String(item.questionId)) }}分)</span
          >
        </div>
        <!--题目中的配图-->
        <div
          v-if="item.images && item.images.length > 0"
          style="margin-top: 15px"
        >
          <img
            v-for="(url, imgIndex) in item.images"
            :key="imgIndex"
            :src="url"
            title="点击查看大图"
            alt="题目图片"
            style="
              width: 100px;
              height: 100px;
              cursor: pointer;
              margin-right: 10px;
            "
            @click="showBigImg(url)"
          />
        </div>

        <!--单选 和 判断 的答案列表-->
        <div
          style="margin-top: 25px"
          v-show="item.questionType === 1 || item.questionType === 3"
        >
          <div class="el-radio-group">
            <label
              v-for="(i2, index2) in item.answer"
              :class="
                String(index2) === userAnswer[index] && i2.isTrue === 'true'
                  ? 'activeAndTrue'
                  : String(index2) === userAnswer[index]
                  ? 'active'
                  : i2.isTrue === 'true'
                  ? 'true'
                  : ''
              "
            >
              <span>{{ optionName[index2] + "、" + i2.answer }}</span>
              <img
                style="
                  position: absolute;
                  left: 100%;
                  top: 50%;
                  transform: translateY(-50%);
                  width: 40px;
                  height: 40px;
                  float: right;
                  cursor: pointer;
                "
                title="点击查看大图"
                v-if="i2.images !== null"
                v-for="i3 in i2.images"
                :src="i3"
                alt=""
                @mouseover="showBigImg(i3)"
              />
            </label>
          </div>
        </div>

        <!--多选的答案列表-->
        <div style="margin-top: 25px" v-show="item.questionType === 2">
          <div class="el-radio-group">
            <label
              v-for="(i2, index2) in item.answer"
              :class="
                (userAnswer[index] + '').indexOf(index2 + '') !== -1 &&
                i2.isTrue === 'true'
                  ? 'activeAndTrue'
                  : (userAnswer[index] + '').indexOf(index2 + '') !== -1
                  ? 'active'
                  : i2.isTrue === 'true'
                  ? 'true'
                  : ''
              "
            >
              <span>{{ optionName[index] + "、" + i2.answer }}</span>
              <template v-if="i2.images">
                <img
                  style="
                    position: absolute;
                    left: 100%;
                    top: 50%;
                    transform: translateY(-50%);
                    width: 40px;
                    height: 40px;
                    float: right;
                    cursor: pointer;
                  "
                  title="点击查看大图"
                  v-for="i3 in i2.images"
                  :src="i3"
                  alt=""
                  @mouseover="showBigImg(i3)"
                />
              </template>
            </label>
          </div>
        </div>

        <!--简答题的答案-->
        <div style="margin-top: 25px" v-show="item.questionType === 4">
          <div class="ques-analysis">
            <h3 style="font-weight: 400">我的回答：</h3>
            <p style="font-weight: 400; color: orange">
              {{ userAnswer[index] }}
            </p>
          </div>
        </div>
      </div>

      <!--      </el-card>-->
    </el-main>

    <!--图片回显-->
    <el-dialog :visible.sync="bigImgDialog" @close="bigImgDialog = false">
      <img style="width: 100%" :src="bigImgUrl" />
    </el-dialog>
  </el-container>
</template>

<script>
export default {
  name: "ExamResult",
  data() {
    return {
      //考试记录信息
      examRecord: null,
      //考试的信息
      examInfo: null,
      //当前考试的题目
      questionInfo: [],
      //页面加载
      loading: {},
      //答案的选项名abcd数据
      optionName: [
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z",
      ],
      //图片回显的url
      bigImgUrl: "",
      //图片回显的对话框是否显示
      bigImgDialog: false,
      //用户回答的答案
      userAnswer: [],
      //单题的分值
      questionScore: new Map(),
    };
  },
  props: ["tagInfo"],
  computed: {
    examUserName() {
      return window.localStorage.getItem("userTrueName");
    },
  },
  created() {
    //一创建就改变头部的面包屑
    this.$emit("giveChildChangeBreakInfo", "考试结果", "考试结果");
    this.createTagsInParent();
    this.getExamRecord();

    //页面数据加载的等待状态栏
    this.loading = this.$Loading.service({
      body: true,
      lock: true,
      text: "数据拼命加载中,(*╹▽╹*)",
      spinner: "el-icon-loading",
    });
  },
  methods: {
    //向父组件中添加头部的tags标签
    createTagsInParent() {
      let flag = false;
      this.tagInfo.map((item) => {
        //如果tags全部符合
        if (item.name === "考试结果" && item.url === this.$route.path) {
          flag = true;
        } else if (item.name === "考试结果" && item.url !== this.$route.path) {
          this.$emit("updateTagInfo", "考试结果", this.$route.path);
          flag = true;
        }
      });
      if (!flag) this.$emit("giveChildAddTag", "考试结果", this.$route.path);
    },
    //查询用户当时考试的信息
    getExamRecord() {
      this.$http
        .get(this.API.getExamRecordById + "/" + this.$route.params.recordId)
        .then((resp) => {
          if (resp.data.code === 200 && resp.data.data) {
            this.examRecord = resp.data.data;
            this.getExamInfoById(resp.data.data.examId);
            this.userAnswer = resp.data.data.userAnswers.split("-");
            //获取单题的分值
            this.getQuestionScore(resp.data.data.examId);
            //获取所有题目信息
            resp.data.data.questionIds.split(",").forEach((item) => {
              this.getQuestionInfoById(item);
            });
            //数据加载完毕
            this.loading.close();
          } else {
            this.loading.close();
            this.$message.error("获取考试记录失败，请稍后重试");
            this.$router.push("/examOnline");
          }
        })
        .catch((err) => {
          this.loading.close();
          this.$message.error(
            "获取考试记录失败：" + (err.message || "网络错误")
          );
          this.$router.push("/examOnline");
        });
    },
    //根据考试id查询考试信息
    getExamInfoById(examId) {
      this.$http
        .get(this.API.getExamInfoById, { params: { examId: examId } })
        .then((resp) => {
          if (resp.data.code === 200) this.examInfo = resp.data.data;
        });
    },
    //根据id查询题目信息
    async getQuestionInfoById(questionId) {
      await this.$http
        .get(this.API.getQuestionById + "/" + questionId)
        .then((resp) => {
          if (resp.data.code === 200) {
            // 修复图片链接
            if (resp.data.data.images) {
              resp.data.data.images = resp.data.data.images
                .filter((url) => url && url.trim() !== "")
                .map((url) => this.normalizeImageUrl(url));
            }
            if (resp.data.data.answer) {
              resp.data.data.answer.forEach((ans) => {
                if (ans.images) {
                  ans.images = ans.images
                    .filter((url) => url && url.trim() !== "")
                    .map((url) => this.normalizeImageUrl(url));
                }
              });
            }

            this.questionInfo.push(resp.data.data);
            //重置问题的顺序 单选 多选 判断 简答
            this.questionInfo = this.questionInfo.sort(function (a, b) {
              return a.questionType - b.questionType;
            });
          } else {
            console.error(`获取题目 ${questionId} 失败:`, resp.data.message);
          }
        })
        .catch((error) => {
          console.error(`获取题目 ${questionId} 出错:`, error);
          this.$message.error(`题目 ${questionId} 加载失败，可能已被删除`);
        });
    },
    //标准化图片URL，处理不同端口和协议
    normalizeImageUrl(url) {
      if (!url) return "";
      if (typeof url === "string" && url.includes("124.222.121.87")) {
        try {
          const urlObj = new URL(url);
          const baseUrl = this.$http.defaults.baseURL.replace(/\/$/, "");
          return `${baseUrl}/images${urlObj.pathname}`;
        } catch (e) {
          console.error("URL解析失败:", url);
        }
      }
      if (url.startsWith("http://") || url.startsWith("https://")) {
        return url;
      }
      const baseUrl = this.$http.defaults.baseURL.replace(/\/$/, "");
      if (url.startsWith("/images/")) {
        return baseUrl + url;
      }
      if (url.startsWith("/")) {
        return baseUrl + "/images" + url;
      }
      return baseUrl + "/images/" + url;
    },
    //点击展示高清大图
    showBigImg(url) {
      this.bigImgUrl = this.normalizeImageUrl(url);
      this.bigImgDialog = true;
    },
    //根据考试id查询考试中每一题的分数
    async getQuestionScore(examId) {
      await this.$http
        .get(this.API.getExamQuestionByExamId + "/" + examId)
        .then((resp) => {
          if (resp.data.code === 200) {
            //设置单题分值给map
            const scores = resp.data.data.scores.split(",");
            resp.data.data.questionIds.split(",").forEach((item, index) => {
              // this.$set(this.questionScore, item, scores[index])
              this.questionScore.set(item, scores[index]);
            });
          }
        });
    },
  },
};
</script>

<style scoped lang="scss">
* {
  font-weight: 800;
}

.el-container {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #f0fdff 0%, #e0f9ff 100%);
  padding: 20px;
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

.examName {
  color: #00d9ff;
  font-size: 20px;
  font-weight: 800;
  padding-bottom: 10px;
}

.examTime {
  font-size: 16px;
  color: #4fd1c5;
  margin-left: 20px;
  font-weight: 700;
}

.el-radio-group label {
  display: block;
  width: 400px;
  padding: 15px 20px;
  border-radius: 16px;
  border: 2px solid #f0fdff;
  margin-bottom: 10px;
  position: relative;
  transition: all 0.3s ease;
  background: #ffffff;
  min-height: 60px;

  span {
    font-size: 16px;
    line-height: 1.5;
    display: block;
    word-wrap: break-word;
  }
}

.num {
  display: inline-block;
  background: linear-gradient(135deg, #00d9ff 0%, #4fd1c5 100%);
  border-radius: 50%;
  height: 37px;
  width: 37px;
  line-height: 37px;
  color: #fff;
  font-size: 20px;
  text-align: center;
  margin-right: 15px;
  box-shadow: 0 4px 12px rgba(0, 217, 255, 0.3);
}

/*选中的答案*/
.active {
  border: 2px solid #ff6b6b !important;
  background: rgba(255, 107, 107, 0.1);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
}

/*选中的答案且是正确答案*/
.activeAndTrue {
  border: 2px solid #4caf50 !important;
  background: rgba(76, 175, 80, 0.1);
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);

  &::after {
    content: "✓";
    position: absolute;
    top: 10px;
    right: 10px;
    color: #4caf50;
    font-size: 24px;
    font-weight: bold;
  }
}

.true {
  border: 2px solid #4caf50 !important;
  background: rgba(76, 175, 80, 0.05);

  &::after {
    content: "✓";
    position: absolute;
    top: 10px;
    right: 10px;
    color: #4caf50;
    font-size: 24px;
    font-weight: bold;
  }
}

.ques-analysis {
  padding: 20px;
  background: #ffffff;
  border-radius: 16px;
  border: 2px solid #f0fdff;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 217, 255, 0.1);

  h3 {
    color: #00d9ff;
    margin-bottom: 10px;
  }

  p {
    color: #666;
    line-height: 1.6;
  }
}

:deep(.el-card) {
  border-radius: 16px;
  border: none;
  box-shadow: 0 8px 24px rgba(0, 217, 255, 0.15);
  background: #ffffff;
}

:deep(.el-button--text) {
  color: #00d9ff;
  font-weight: 600;

  &:hover {
    color: #4fd1c5;
  }
}

:deep(.el-tooltip) {
  span {
    font-weight: 700;
  }
}
</style>
