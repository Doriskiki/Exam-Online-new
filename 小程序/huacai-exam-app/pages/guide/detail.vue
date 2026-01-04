<template>
    <view class="guide-detail-container">
        <view class="detail-content">
            <view class="guide-item">
                <view class="guide-header">
                    <view class="guide-type">{{ guide.type }}</view>
                </view>

                <view class="guide-title">
                    {{ guide.title }}
                </view>

                <rich-text class="guide-content" :nodes="guide.content"></rich-text>

                <view class="guide-summary">
                    <view class="summary-label">要点总结：</view>
                    <view class="summary-content">{{ guide.summary }}</view>
                </view>

                <view class="guide-tips" v-if="guide.tips">
                    <view class="tips-label">实用技巧：</view>
                    <view class="tips-content">{{ guide.tips }}</view>
                </view>
            </view>
        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onLoad} from "@dcloudio/uni-app";
import {getGuide} from "@/pages/api/exam/guide";

const guide = ref({})

const load = (options) => {
    getGuide(options.id).then(res => {
        guide.value = res.data
    })
}

onLoad((options) => {
    load(options)
})

</script>

<style lang="scss">
page {
  background-color: #f8f9fa;
}

.guide-detail-container {
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

  .detail-content {
    padding: 20px;
    margin-top: -30px;

    .guide-item {
      background: white;
      border-radius: 15px;
      padding: 20px;
      margin-bottom: 20px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

      .guide-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .guide-type {
          font-size: 12px;
          padding: 3px 8px;
          border-radius: 10px;
          background-color: #4facfe;
          color: white;
        }
      }

      .guide-title {
        font-size: 18px;
        font-weight: 600;
        color: #333;
        margin-bottom: 20px;
        line-height: 1.5;
      }

      .guide-content {
        font-size: 15px;
        color: #333;
        margin-bottom: 20px;
        line-height: 1.6;
      }

      .guide-summary {
        padding: 15px;
        background: #f1f8ff;
        border-radius: 10px;
        margin-bottom: 15px;

        .summary-label {
          font-weight: 500;
          color: #333;
          margin-bottom: 5px;
          display: block;
        }

        .summary-content {
          color: #333;
          line-height: 1.5;
        }
      }

      .guide-tips {
        padding: 15px;
        background: #fff8e1;
        border-radius: 10px;
        margin-bottom: 15px;

        .tips-label {
          font-weight: 500;
          color: #333;
          margin-bottom: 5px;
          display: block;
        }

        .tips-content {
          color: #333;
          line-height: 1.5;
        }
      }
    }

    .action-buttons {
      display: flex;
      justify-content: center;

      .btn {
        flex: 1;
        height: 45px;
        border-radius: 25px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 16px;
        font-weight: 500;
        border: none;
        margin: 0 10px;

        &.like-btn {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          color: white;
        }
      }
    }
  }
}
</style>
