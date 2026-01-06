<template>

  <el-container v-if="show">
    <el-header style="margin-top: 60px">
      <el-row>
        <el-col :span="18" :offset="1">
          <span class="startExam">正在考试</span>
          <span class="examTitle">距离考试结束还有：</span>
          <span style="color: red;font-size: 18px;">{{ duration | timeFormat }}</span>
          <el-button type="primary" round
                     style="float: right;color: #ffffff;font-weight: 800"
                     @click="uploadExamToAdmin">提交试卷
          </el-button>
        </el-col>
      </el-row>
    </el-header>

    <el-main>
      <el-row>
        <el-col :span="18" :offset="1">
          <el-col :span="16">
            <el-card style="min-height: 500px">
              <!--题目信息-->
              <div>
                <span class="num">{{ curIndex+1 }}</span>
                <span v-if="questionInfo[curIndex].questionType === 1">【单选题】</span>
                <span v-else-if="questionInfo[curIndex].questionType === 2">【多选题】</span>
                <span v-else-if="questionInfo[curIndex].questionType === 3">【判断题】</span>
                <span v-else>【简答题】</span>
                <span>{{ questionInfo[curIndex].questionContent}}:</span>
              </div>
              
              <!--题目中的配图-->
              <div v-if="questionInfo[curIndex].images && questionInfo[curIndex].images.length > 0" style="margin-top: 15px;">
                <div v-for="(url, imgIndex) in questionInfo[curIndex].images" 
                     :key="imgIndex"
                     style="display: inline-block; margin-right: 10px; position: relative;">
                  <img :src="url" 
                       :data-key="curIndex + '_' + imgIndex"
                       title="点击查看大图" 
                       alt="题目图片"
                       style="width: 100px;height: 100px;cursor: pointer;object-fit: cover;border: 2px solid #F0FDFF;border-radius: 8px;" 
                       @click="showBigImg(url)"
                       @error="handleImageError">
                  <div v-if="imageLoadError[curIndex + '_' + imgIndex]" 
                       style="width: 100px;height: 100px;border: 2px solid #ff6b6b;border-radius: 8px;display: flex;flex-direction: column;align-items: center;justify-content: center;background: #ffe0e0;font-size: 12px;text-align: center;padding: 5px;position: absolute;top: 0;left: 0;">
                    <div>图片加载失败</div>
                    <a :href="url" target="_blank" style="color: #00D9FF;text-decoration: underline;font-size: 10px;margin-top: 5px;">在新窗口打开</a>
                  </div>
                </div>
              </div>

              <!--单选、判断 的答案列表-->
              <div style="margin-top: 25px"
                   v-show="questionInfo[curIndex].questionType === 1 || questionInfo[curIndex].questionType === 3">
                <div class="el-radio-group">
                  <div v-for="(item,index) in questionInfo[curIndex].answer"
                       :key="index"
                       class="answer-option"
                       @click="checkSingleAnswer(index)"
                       :class="{'active': index === userAnswer[curIndex]}">
                    <span>{{ optionName[index] + '.' + item.answer}}</span>
                    <div v-if="item.images !== null && item.images.length > 0" class="answer-images">
                      <img v-for="(i2, i2Index) in item.images" 
                           :key="i2Index"
                           :src="i2" 
                           alt="" 
                           title="点击查看大图"
                           @click.stop="showBigImg(i2)"
                           @error="handleImageError"
                           style="width: 40px;height: 40px;cursor: pointer;margin-left: 10px;object-fit: cover;border: 2px solid #F0FDFF;border-radius: 4px;">
                    </div>
                  </div>
                </div>
              </div>

              <!--多选的答案列表-->
              <div style="margin-top: 25px" v-show="questionInfo[curIndex].questionType === 2">
                <div class="el-radio-group">
                  <div v-for="(item,index) in questionInfo[curIndex].answer"
                       :key="index"
                       class="answer-option"
                       @click="selectedMultipleAnswer(index)"
                       :class="{'active': (userAnswer[curIndex]+'').indexOf(index+'') !== -1}">
                    <span>{{ optionName[index] + '.' + item.answer}}</span>
                    <div v-if="item.images !== null && item.images.length > 0" class="answer-images">
                      <img v-for="(i2, i2Index) in item.images" 
                           :key="i2Index"
                           :src="i2" 
                           alt="" 
                           title="点击查看大图"
                           @click.stop="showBigImg(i2)"
                           @error="handleImageError"
                           style="width: 40px;height: 40px;cursor: pointer;margin-left: 10px;object-fit: cover;border: 2px solid #F0FDFF;border-radius: 4px;">
                    </div>
                  </div>
                </div>
              </div>

              <!--简答题的答案-->
              <div style="margin-top: 25px" v-show="questionInfo[curIndex].questionType === 4">
                <el-input
                  type="textarea"
                  :rows="8"
                  placeholder="请输入答案"
                  v-model="userAnswer[curIndex]">
                </el-input>
              </div>

              <!--上一题 下一题-->
              <div style="margin-top: 25px">
                <el-button round type="primary" icon="el-icon-back" :disabled="curIndex<1" @click="curIndex--">上一题</el-button>
                <el-button round type="primary" icon="el-icon-right" :disabled="curIndex>=questionInfo.length-1"
                           @click="curIndex++">下一题</el-button>
              </div>

            </el-card>
          </el-col>

          <el-col :span="7" :offset="1">
            <!--答题卡卡片-->
            <el-card>
              <div>
                <p style="font-size: 18px;">答题卡</p>
                <div style="margin-top: 25px">
                  <span
                    style="background-color: rgb(238,238,238);padding: 5px 10px 5px 10px;margin-left: 15px">未作答</span>
                  <span style="background-color: rgb(87,148,247);color: white;
                padding: 5px 10px 5px 10px;margin-left: 15px">已作答</span>
                </div>
              </div>

              <!--单选的答题卡-->
              <div style="margin-top: 25px">
                <p style="font-size: 18px;">单选题</p>
                <el-button style="margin-top: 10px;margin-left: 15px" size="mini"
                           v-show="questionInfo[item-1].questionType === 1"
                           :class="questionInfo[item-1].questionType === 1 && userAnswer[item-1] !== undefined ?
                            'done' : userAnswer[item-1] === undefined ? curIndex === (item-1) ? 'orange' : 'noAnswer' : 'noAnswer'"
                           v-for="item in questionInfo.length" :key="item" @click="curIndex = item-1">{{ item }}
                </el-button>
              </div>

              <!--多选的答题卡-->
              <div style="margin-top: 25px">
                <p style="font-size: 18px;">多选题</p>
                <el-button style="margin-top: 10px;margin-left: 15px" size="mini"
                           v-show="questionInfo[item-1].questionType === 2"
                           :class="questionInfo[item-1].questionType === 2 && userAnswer[item-1] !== undefined ?
                            'done' : userAnswer[item-1] === undefined ? curIndex === (item-1) ? 'orange' : 'noAnswer' : 'noAnswer'"
                           v-for="item in questionInfo.length" :key="item" @click="curIndex = item-1">{{ item }}
                </el-button>
              </div>

              <!--判断的答题卡-->
              <div style="margin-top: 25px">
                <p style="font-size: 18px;">判断题</p>
                <el-button style="margin-top: 10px;margin-left: 15px" size="mini"
                           v-show="questionInfo[item-1].questionType === 3"
                           :class="questionInfo[item-1].questionType === 3 && userAnswer[item-1] !== undefined ?
                            'done' : userAnswer[item-1] === undefined ? curIndex === (item-1) ? 'orange' : 'noAnswer' : 'noAnswer'"
                           v-for="item in questionInfo.length" :key="item" @click="curIndex = item-1">{{ item }}
                </el-button>
              </div>

              <!--简答的答题卡-->
              <div style="margin-top: 25px">
                <p style="font-size: 18px;">简答题</p>
                <el-button style="margin-top: 10px;margin-left: 15px" size="mini"
                           v-show="questionInfo[item-1].questionType === 4"
                           :class="questionInfo[item-1].questionType === 4 && userAnswer[item-1] !== undefined ?
                            'done' : userAnswer[item-1] === undefined ? curIndex === (item-1) ? 'orange' : 'noAnswer' : 'noAnswer'"
                           v-for="item in questionInfo.length" :key="item" @click="curIndex = item-1">{{ item }}
                </el-button>
              </div>
            </el-card>
          </el-col>

        </el-col>
      </el-row>
      <video id="video" muted="muted" class="camera-video"
             height="250px" width="300" autoplay="autoplay"></video>
      <canvas id="canvas" hidden width="200px" height="200px"></canvas>
    </el-main>
    <!--图片回显-->
    <el-dialog :visible.sync="bigImgDialog" @close="bigImgDialog = false">
      <img style="width: 100%" :src="bigImgUrl">
    </el-dialog>
  </el-container>

</template>

<script>

  export default {
    name: 'ExamPage',
    data () {
      return {
        //当前考试的信息
        examInfo: {},
        //当前的考试题目
        questionInfo: [
          {
            questionType: ''
          }
        ],
        //当前题目的索引值
        curIndex: 0,
        //控制大图的对话框
        bigImgDialog: false,
        //当前要展示的大图的url
        bigImgUrl: '',
        //用户选择的答案
        userAnswer: [],
        //页面数据加载
        loading: {},
        //页面绘制是否开启
        show: false,
        //答案的选项名abcd数据
        optionName: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
        //考试总时长
        duration: 0,
        //摄像头对象
        mediaStreamTrack: null,
        //摄像头是否开启
        cameraOn: false,
        //人脸识别相关
        faceVerifyTimer: null,
        faceVerifyInterval: 120000,
        faceVerifyCount: 0,
        faceVerifyFailCount: 0,
        maxFaceVerifyFail: 3,
        //图片加载错误标记
        imageLoadError: {}
      }
    },
    created () {
      this.getExamInfo()
      //页面数据加载的等待状态栏
      this.loading = this.$Loading.service({
        body: true,
        lock: true,
        text: '数据拼命加载中(*╹▽╹*)',
        spinner: 'el-icon-loading',
      })
      //开启摄像头
      window.onload = () => {
        setTimeout(() => {
          this.getCamera()
          // 启动定时人脸识别验证
          this.startFaceVerifyTimer()
        }, 1000)
      }
    },
    mounted () {
      //关闭浏览器窗口的时候移除localstorage的时间
      var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
      var isOpera = userAgent.indexOf('Opera') > -1; //判断是否Opera浏览器
      var isIE = userAgent.indexOf('compatible') > -1 && userAgent.indexOf('MSIE') > -1 && !isOpera; //判断是否IE浏览器
      var isIE11 = userAgent.indexOf('rv:11.0') > -1; //判断是否是IE11浏览器
      var isEdge = userAgent.indexOf('Edge') > -1 && !isIE; //判断是否IE的Edge浏览器
      if (!isIE && !isEdge && !isIE11) {//兼容chrome和firefox
        var _beforeUnload_time = 0, _gap_time = 0;
        var is_fireFox = navigator.userAgent.indexOf('Firefox') > -1;//是否是火狐浏览器
        window.onunload = function () {
          _gap_time = new Date().getTime() - _beforeUnload_time
          if (_gap_time <= 5) {
            localStorage.removeItem('examDuration' + this.examInfo.examId)
          } else {//谷歌浏览器刷新
          }
        };
        window.onbeforeunload = function () {
          _beforeUnload_time = new Date().getTime()
          if (is_fireFox) {//火狐关闭执行

          } else {//火狐浏览器刷新
          }
        };
      }
    },
    beforeDestroy () {
      // 清除人脸识别定时器
      if (this.faceVerifyTimer) {
        clearInterval(this.faceVerifyTimer);
      }
    },
    methods: {
      //查询当前考试的信息
      getExamInfo () {
        this.$http.get(this.API.getExamInfoById, { params: this.$route.params }).then((resp) => {
          if (resp.data.code === 200) {
            this.examInfo = resp.data.data
            //设置定时(秒)
            if (localStorage.getItem('examDuration' + this.examInfo.examId) === '0') localStorage.removeItem('examDuration' + this.examInfo.examId)
            this.duration = localStorage.getItem('examDuration' + this.examInfo.examId) || resp.data.data.examDuration * 60
            //考试剩余时间定时器
            this.timer = window.setInterval(() => {
              if (this.duration > 0) this.duration--
            }, 1000)
            this.getQuestionInfo(resp.data.data.questionIds.split(','))
          }
        })
      },
      //查询考试的题目信息
      async getQuestionInfo (ids) {
        const promises = ids.map((item, index) => {
          return this.$http.get(this.API.getQuestionById + '/' + item)
            .then((resp) => {
              if (resp.data.code === 200) {
                return resp.data.data
              } else {
                console.error(`获取题目 ${item} 失败:`, resp.data.message)
                return null
              }
            })
            .catch((error) => {
              console.error(`获取题目 ${item} 出错:`, error)
              this.$message.error(`题目 ${item} 加载失败，可能已被删除`)
              return null
            })
        })
        
        try {
          const results = await Promise.all(promises)
          this.questionInfo = results.filter(item => item !== null)
          
          if (this.questionInfo.length === 0) {
            this.$message.error('没有可用的题目，请联系管理员')
            this.$router.push('/examOnline')
            return
          }
          
          //重置问题的顺序：单选 多选 判断 简答
          this.questionInfo = this.questionInfo.sort(function (a, b) {
            return a.questionType - b.questionType
          })
          
          if (this.questionInfo.length < ids.length) {
            this.$message.warning(`部分题目加载失败，当前可用题目: ${this.questionInfo.length}/${ids.length}`)
          }
        } catch (error) {
          console.error('获取题目信息失败:', error)
          this.$message.error('题目加载失败，请稍后重试')
          this.$router.push('/examOnline')
        }
        
        this.loading.close()
        this.show = true
      },
      //点击展示高清大图
      showBigImg (url) {
        this.bigImgUrl = url
        this.bigImgDialog = true
      },
      //图片加载失败处理
      handleImageError(e) {
        const url = e.target.src
        console.error('图片加载失败:', url)
        // 标记图片加载失败
        const imgKey = e.target.getAttribute('data-key')
        if (imgKey) {
          this.$set(this.imageLoadError, imgKey, true)
        }
        e.target.style.display = 'none'
      },
      //检验单选题的用户选择的答案
      checkSingleAnswer (index) {
        console.log('选择答案:', index, '当前题目:', this.curIndex)
        console.log('当前题目信息:', this.questionInfo[this.curIndex])
        console.log('题目images字段:', this.questionInfo[this.curIndex].images)
        console.log('images类型:', typeof this.questionInfo[this.curIndex].images)
        this.$set(this.userAnswer, this.curIndex, index)
        console.log('userAnswer:', this.userAnswer)
        this.$forceUpdate()
      },
      //多选题用户的答案选中
      selectedMultipleAnswer (index) {
        if (this.userAnswer[this.curIndex] === undefined) {//当前是多选的第一个答案
          this.$set(this.userAnswer, this.curIndex, index)
        } else if (String(this.userAnswer[this.curIndex]).split(',').includes(index + '')) {//取消选中
          let newArr = []
          String(this.userAnswer[this.curIndex]).split(',').forEach(item => {
            if (item !== '' + index) newArr.push(item)
          })
          if (newArr.length === 0) {
            this.$set(this.userAnswer, this.curIndex, undefined)
          } else {
            this.$set(this.userAnswer, this.curIndex, newArr.join(','))
            //答案格式化顺序DBAC -> ABCD
            this.userAnswer[this.curIndex] = String(this.userAnswer[this.curIndex]).split(',').sort(function (a, b) {
              return a - b
            }).join(',')
          }
        } else if (!((this.userAnswer[this.curIndex] + '').split(',').includes(index + ''))) {//第n个答案
          this.$set(this.userAnswer, this.curIndex, this.userAnswer[this.curIndex] += ',' + index)
          //答案格式化顺序DBAC -> ABCD
          this.userAnswer[this.curIndex] = String(this.userAnswer[this.curIndex]).split(',').sort(function (a, b) {
            return a - b
          }).join(',')
        }
      },
      //调用摄像头
      getCamera () {
        let constraints = {
          video: {
            width: 200,
            height: 200
          },
          audio: false
        }
        //获得video摄像头
        let video = document.getElementById('video')
        let promise = navigator.mediaDevices.getUserMedia(constraints)
        promise.then((mediaStream) => {
          this.mediaStreamTrack = typeof mediaStream.stop === 'function' ? mediaStream : mediaStream.getTracks()[1]
          video.srcObject = mediaStream
          video.play()
          this.cameraOn = true
        }).catch((back) => {
          this.$message({
            duration: 1500,
            message: '请开启摄像头权限o(╥﹏╥)o!',
            type: 'error'
          })
        })
      },
      //关闭摄像头
      closeCamera () {
        try {
          const video = document.getElementById('video')
          if (video && video.srcObject) {
            let stream = video.srcObject
            let tracks = stream.getTracks()
            tracks.forEach(function (track) {
              track.stop()
            })
            video.srcObject = null
          }
        } catch (error) {
          console.error('关闭摄像头失败:', error)
        }
      },
      //启动定时人脸识别验证
      startFaceVerifyTimer() {
        // 每2分钟进行一次人脸识别验证
        this.faceVerifyTimer = setInterval(() => {
          this.verifyFaceDuringExam()
        }, this.faceVerifyInterval)
        
        // 第一次验证在30秒后进行
        setTimeout(() => {
          this.verifyFaceDuringExam()
        }, 30000)
      },
      //考试中人脸识别验证
      async verifyFaceDuringExam() {
        if (!this.cameraOn) {
          this.$message.warning('摄像头未开启，请开启摄像头继续考试')
          return
        }

        try {
          // 获取当前视频帧
          let video = document.getElementById('video')
          let canvas = document.getElementById('canvas')
          
          if (!video || !canvas) {
            console.warn('视频或画布元素不存在，跳过人脸验证')
            return
          }
          
          let ctx = canvas.getContext('2d')
          ctx.drawImage(video, 0, 0, 200, 200)
          let imageBase64 = canvas.toDataURL('image/jpeg', 0.8)

          // 调用人脸识别接口
          const res = await this.$http.post(
            `${this.API.faceVerifyExam}?userId=${this.examInfo.userId || localStorage.getItem('userId')}&examId=${this.examInfo.examId}`,
            { image: imageBase64 }
          )

          this.faceVerifyCount++

          if (res.data.code === 200 && res.data.data.matched) {
            // 验证通过
            console.log(`第${this.faceVerifyCount}次人脸验证通过，相似度：${res.data.data.similarity}`)
            
            // 如果相似度较低，给出警告
            if (res.data.data.similarity < 0.7) {
              this.$message.warning('人脸识别相似度较低，请保持正面面对摄像头')
            }
          } else {
            // 验证失败
            this.faceVerifyFailCount++
            this.$message.error(`人脸验证失败（${this.faceVerifyFailCount}/${this.maxFaceVerifyFail}），请确保本人在考试`)
            
            // 如果失败次数超过限制，强制提交试卷
            if (this.faceVerifyFailCount >= this.maxFaceVerifyFail) {
              this.$alert('人脸验证多次失败，系统将自动提交试卷', '警告', {
                confirmButtonText: '确定',
                type: 'error',
                callback: () => {
                  this.forceSubmitExam('人脸验证失败')
                }
              })
            }
          }
        } catch (error) {
          console.error('人脸识别验证失败:', error)
          this.$message.error('人脸识别服务异常，请联系管理员')
        }
      },
      //强制提交试卷
      async forceSubmitExam(reason) {
        if (this.cameraOn) {
          this.closeCamera()
        }
        
        // 清除定时器
        if (this.faceVerifyTimer) {
          clearInterval(this.faceVerifyTimer)
        }

        let data = {}
        data.questionIds = []
        data.userAnswers = this.userAnswer.join('-')
        data.examId = parseInt(this.$route.params.examId)
        data.remark = reason // 添加备注说明强制提交原因
        
        this.questionInfo.forEach((item, index) => {
          data.questionIds.push(item.questionId)
          if (index >= this.userAnswer.length) {
            data.userAnswers += '- '
          }
        })
        
        if (this.userAnswer.length === 0) {
          this.questionInfo.forEach(item => {
            data.userAnswers += ' -'
          })
        }
        
        data.questionIds = data.questionIds.join(',')
        
        this.$http.post(this.API.addExamRecord, data).then((resp) => {
          if (resp.data.code === 200) {
            this.$notify({
              title: '提示',
              message: `考试已结束：${reason}`,
              type: 'warning',
              duration: 3000
            })
            this.$router.push('/examResult/' + resp.data.data)
          }
        })
      },
      //上传用户考试信息进入后台
      async uploadExamToAdmin () {
        // 正则
        var reg = new RegExp('-', 'g')
        // 去掉用户输入的非法分割符（-),保证后端接受数据处理不报错
        this.userAnswer.forEach((item, index) => {
          if (this.questionInfo[index].questionType === 4) {//简答题答案处理
            this.userAnswer[index] = item.replace(reg, ' ')
          }
        })

        // 标记题目是否全部做完
        let flag = true
        for (let i = 0; i < this.userAnswer.length; i++) {// 检测用户是否题目全部做完
          if (this.userAnswer[i] === undefined) {
            flag = false
          }
        }
        // 如果用户所有答案的数组长度小于题目长度,这个时候也要将标志位置为false
        if (this.userAnswer.length < this.questionInfo.length) {
          flag = false
        }
        //题目未做完
        if (!flag) {
          // if (this.userAnswer.some((item) => item === undefined)) {
          this.$confirm('当前试题暂未做完, 是否继续提交o(╥﹏╥)o ?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let data = {}
            data.questionIds = []
            data.userAnswers = this.userAnswer.join('-')
            this.questionInfo.forEach((item, index) => {
              data.questionIds.push(item.questionId)
              //当前数据不完整 用户回答不完整 我们自动补充空答案(防止业务出错)
              if (index > (this.userAnswer.length - 1)) {
                data.userAnswers += '- '
              }
            })
            //如果所有题目全部未�?
            if (this.userAnswer.length === 0) {
              this.questionInfo.forEach(item => {
                data.userAnswers += ' -'
              })
              data.userAnswers.split(0, data.userAnswers.length - 1)
            }
            data.examId = parseInt(this.$route.params.examId)
            data.questionIds = data.questionIds.join(',')
            this.$http.post(this.API.addExamRecord, data).then((resp) => {
              if (resp.data.code === 200) {
                this.$notify({
                  title: '提示',
                  message: '考试结束 *^▽^*',
                  type: 'success',
                  duration: 2000
                })
                if (this.cameraOn) {
                  this.closeCamera()
                }
                this.$router.push('/examResult/' + resp.data.data)
              }
            })
          }).catch(() => {
            this.$notify({
              title: '提示',
              message: '继续加油! *^▽^*',
              type: 'success',
              duration: 2000
            })
          })
        } else {//当前题目做完�?
          if (this.cameraOn) {
            this.closeCamera()
          }
          let data = {}
          data.questionIds = []
          data.userAnswers = this.userAnswer.join('-')
          data.examId = parseInt(this.$route.params.examId)
          this.questionInfo.forEach((item, index) => {
            data.questionIds.push(item.questionId)
          })
          data.questionIds = data.questionIds.join(',')
          this.$http.post(this.API.addExamRecord, data).then((resp) => {
            if (resp.data.code === 200) {
              this.$notify({
                title: '提示',
                message: '考试结束 *^▽^*',
                type: 'success',
                duration: 2000
              })
              this.$router.push('/examResult/' + resp.data.data)
            }
          })
        }
      }
    },
    watch: {
      //监控考试的剩余时�?
      duration (newVal) {
        localStorage.setItem('examDuration' + this.examInfo.examId, newVal)
        //摄像头数�?
        let constraints = {
          video: {
            width: 200,
            height: 200
          },
          audio: false
        }
        //通过调用摄像头判断用户是否中途关闭摄像头
        let promise = navigator.mediaDevices.getUserMedia(constraints)
        promise.catch((back) => {
          this.cameraOn = false
        })
        if (!this.cameraOn) {//如果摄像头未开�?就再次调用开�?
          this.getCamera()
        }
        //考试时间结束了提交试�?
        if (newVal < 1) {
          if (this.cameraOn) {
            this.closeCamera()
          }
          let data = {}
          data.questionIds = []
          data.userAnswers = this.userAnswer.join('-')
          this.questionInfo.forEach((item, index) => {
            data.questionIds.push(item.questionId)
            //当前数据不完�?用户回答不完�?我们自动补充空答�?防止业务出错)
            if (index > this.userAnswer.length) {
              data.userAnswers += ' -'
            }
          })
          //如果所有题目全部未�?
          if (data.userAnswers === '') {
            this.questionInfo.forEach(item => {
              data.userAnswers += ' -'
            })
            data.userAnswers.split(0, data.userAnswers.length - 1)
          }
          data.examId = parseInt(this.$route.params.examId)

          data.questionIds = data.questionIds.join(',')
          this.$http.post(this.API.addExamRecord, data).then((resp) => {
            if (resp.data.code === 200) {
              this.$notify({
                title: '提示',
                message: '考试时间结束,已为您自动提�?*^▽^*',
                type: 'success',
                duration: 2000
              })
              this.$router.push('/examResult/' + resp.data.data)
            }
          })
        }
      },
    }
  }
</script>

<style scoped lang="scss">
  * {
    font-weight: 800;
  }

  .el-container {
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #F0FDFF 0%, #E0F9FF 100%);
    padding: 20px;
  }

  .startExam {
    color: #00D9FF;
    border-bottom: 4px solid #4FD1C5;
    font-size: 18px;
    font-weight: 700;
    padding-bottom: 10px;
  }

  .examTitle {
    font-size: 18px;
    color: #00D9FF;
    margin-left: 20px;
    font-weight: 700;
  }

  .el-radio-group .answer-option {
    display: block;
    width: 400px;
    padding: 15px 20px;
    border-radius: 16px;
    border: 2px solid #F0FDFF;
    margin-bottom: 10px;
    cursor: pointer;
    position: relative;
    transition: all 0.3s ease;
    background: #ffffff;
    min-height: 60px;

    &:hover {
      border-color: #00D9FF;
      box-shadow: 0 4px 12px rgba(0, 217, 255, 0.2);
    }

    &.active {
      border: 2px solid #00D9FF !important;
      background: rgba(0, 217, 255, 0.1) !important;
      box-shadow: 0 4px 12px rgba(0, 217, 255, 0.3);
    }

    span {
      font-size: 16px;
      line-height: 1.5;
      display: block;
      word-wrap: break-word;
    }
    
    .answer-images {
      margin-top: 10px;
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }
  }

  /*做过的题目的高亮颜色*/
  .done {
    background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
    color: #ffffff;
    border-radius: 8px;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 217, 255, 0.3);
    }
  }

  /*未做题目的高亮颜�?/
  .noAnswer {
    background-color: #ffffff;
    border: 2px solid #F0FDFF;
    border-radius: 8px;
    transition: all 0.3s ease;
    
    &:hover {
      border-color: #00D9FF;
    }
  }

  /*当前在做的题目高亮的颜色*/
  .orange {
    background: linear-gradient(135deg, #ffd54f 0%, #ffb300 100%);
    color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(255, 179, 0, 0.3);
  }

  .num {
    display: inline-block;
    background: linear-gradient(135deg, #00D9FF 0%, #4FD1C5 100%);
    height: 37px;
    border-radius: 50%;
    width: 37px;
    line-height: 37px;
    color: #fff;
    font-size: 20px;
    text-align: center;
    margin-right: 15px;
    box-shadow: 0 4px 12px rgba(0, 217, 255, 0.3);
  }

  .camera-video {
    position: absolute;
    top: 5%;
    right: 0;
    border-radius: 16px;
    box-shadow: 0 8px 24px rgba(0, 217, 255, 0.2);
  }

  :deep(.el-card) {
    border-radius: 16px;
    border: none;
    box-shadow: 0 8px 24px rgba(0, 217, 255, 0.15);
    background: #ffffff;
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
    
    &.el-button--warning {
      background: linear-gradient(135deg, #ffd54f 0%, #ffb300 100%);
      border: none;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(255, 179, 0, 0.4);
      }
    }
  }

  :deep(.el-textarea__inner) {
    border-radius: 16px;
    border: 2px solid #F0FDFF;
    transition: all 0.3s ease;
    
    &:focus {
      border-color: #00D9FF;
      box-shadow: 0 0 0 3px rgba(0, 217, 255, 0.1);
    }
  }
</style>
