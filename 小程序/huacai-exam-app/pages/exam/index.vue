<template>
    <view class="exam-container">
        <view class="exam-content">
            <view class="exam-list">
                <view class="exam-item" v-for="(exam, index) in examList" :key="index" @click="startExam(exam)">
                    <view class="exam-header">
                        <text class="exam-title">{{ exam.title }}</text>
                        <view class="exam-status" :style="getStatusStyle(exam.status)">
                            <div style="color: white">{{ exam.status }}</div>
                        </view>
                    </view>
                    <view class="exam-details">
                        <text class="exam-time">考试时长: {{ exam.time }}小时</text>
                    </view>
                    <view class="exam-info">
                        <text class="info-text">及格分: {{ exam.passScore }}</text>
                        <text class="info-text">总分: {{ exam.totalScore }}</text>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {selectMyExam} from "@/pages/api/exam/eu";

const examList = ref([])

const getStatusStyle = (status) => {
    const styleMap = {
        '进行中': 'background-color: #e3aa23; color: #333;',
        '考试通过': 'background-color: #25982f; color: #333;',
        '考试未通过': 'background-color: #b20e45; color: #333;'
    }
    return styleMap[status] || ''
}

//查询列表数据
const getList = () => {
    selectMyExam().then(res => {
        examList.value = res.data
    })
}

//跳转到考试详情页面
const startExam = (exam) => {
    //检查考试状态, 确保每个考试只能考一次
    if (exam.status === '考试通过' || exam.status === '考试未通过') {
        uni.showToast({
            title: '您已经完成此考试, 不可重复作答',
            icon: 'none'
        })
        return
    }

    uni.navigateTo({
        url: '/pages/exam/detail?id=' + exam.examId
    })
}

onMounted(() => {
    getList()
})

</script>

<style lang="scss">
page {
  background-color: #f8f9fa;
}

.exam-container {
  padding-top: 20px;

  .header-section {
    position: relative;
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    padding: 60px 20px 20px;
    color: white;
    text-align: center;

    .back-btn {
      position: absolute;
      left: 20px;
      top: 60px;
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.2);

      .iconfont {
        font-size: 20px;
      }
    }

    .header-title {
      font-size: 22px;
      font-weight: 600;
    }
  }

  .exam-content {
    padding: 20px;
    margin-top: -30px;

    .exam-list {
      display: flex;
      flex-direction: column;
      gap: 15px;

      .exam-item {
        background: white;
        border-radius: 15px;
        padding: 20px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        transition: all 0.3s ease;

        &:active {
          background-color: #f8f9fa;
        }

        .exam-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 15px;

          .exam-title {
            font-size: 18px;
            font-weight: 600;
            color: #333;
            flex: 1;
          }

          .exam-status {
            font-size: 12px;
            padding: 3px 8px;
            border-radius: 10px;
            margin-left: 10px;
            color: white;
          }
        }

        .exam-details {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 10px;

          .exam-meta {
            display: flex;

            .meta-item {
              font-size: 12px;
              color: #999;
              margin-right: 15px;
              display: flex;
              align-items: center;

              .iconfont {
                margin-right: 3px;
                font-size: 12px;
              }
            }
          }

          .exam-time {
            font-size: 12px;
            color: #999;
          }
        }

        .exam-info {
          display: flex;
          justify-content: space-between;
          padding-top: 10px;
          border-top: 1px dashed #eee;

          .info-text {
            font-size: 12px;
            color: #666;
          }
        }
      }
    }
  }
}
</style>
