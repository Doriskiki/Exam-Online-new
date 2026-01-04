<template>
    <view class="record-container">
        <view class="record-content">
            <view class="record-list" v-if="recordList.length > 0">
                <view class="record-item" v-for="(record, index) in recordList" :key="index">
                    <view class="record-header">
                        <text class="record-title">{{ record.title }}</text>
                        <view class="record-status" :style="getStatusStyle(record.status)">
                            {{ record.status }}
                        </view>
                    </view>
                    <view class="record-details">
                        <text class="label">得分:</text>
                        <text class="value"
                              :class="{ 'pass': record.score >= record.passScore, 'fail': record.score < record.passScore }">
                            {{ record.score }}分
                        </text>
                    </view>
                    <view class="record-actions">
                        <button class="action-btn detail-btn" @click="handleResult(record)">查看考试结果</button>
                    </view>

                </view>
            </view>

            <view class="empty-record" v-else>
                <text class="empty-text">暂无考试记录</text>
            </view>
        </view>

    </view>
</template>

<script setup >
import {onMounted, ref} from 'vue'
import {selectMyExamEndRecord} from "@/pages/api/exam/eu";

const recordList = ref([])

onMounted(() => {
    selectMyExamEndRecord().then(res => {
        recordList.value = res.data
    })
})

//查看考试结果
const handleResult = (record) => {
    //准备跳转参数
    const result = {
        examId: record.examId,
        euId: record.euId
    }
    //跳转到考试结果页面
    uni.redirectTo({
        url: '/pages/exam/result?result=' + encodeURIComponent(JSON.stringify(result))
    })
}

//获取状态对应的样式
const getStatusStyle = (status) => {
    const styleMap = {
        '考试通过': 'background-color: #92fe9d; color: #333;',
        '考试未通过': 'background-color: #fc0b0b; color: #ffffff;'
    }
    return styleMap[status] || ''
}

</script>

<style lang="scss">
page {
    background-color: #f8f9fa;
}

.record-container {
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

    .record-content {
        padding: 20px;
        margin-top: -30px;

        .record-list {
            display: flex;
            flex-direction: column;
            gap: 15px;

            .record-item {
                background: white;
                border-radius: 15px;
                padding: 20px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

                .record-header {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    margin-bottom: 15px;
                    padding-bottom: 15px;
                    border-bottom: 1px solid #eee;

                    .record-title {
                        font-size: 18px;
                        font-weight: 600;
                        color: #333;
                        flex: 1;
                    }

                    .record-status {
                        font-size: 12px;
                        padding: 3px 8px;
                        border-radius: 10px;
                        color: white;
                    }
                }

                .record-details {
                    margin-bottom: 20px;

                    .detail-row {
                        display: flex;
                        justify-content: space-between;
                        margin-bottom: 10px;

                        .label {
                            font-size: 14px;
                            color: #999;
                        }

                        .value {
                            font-size: 14px;
                            color: #333;
                            font-weight: 500;

                            &.pass {
                                color: #4caf50;
                            }

                            &.fail {
                                color: #f44336;
                            }
                        }
                    }
                }

                .record-actions {
                    display: flex;
                    justify-content: flex-end;

                    .action-btn {
                        height: 36px;
                        border-radius: 8px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-size: 14px;
                        padding: 0 20px;
                    }

                    .detail-btn {
                        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                        color: white;
                        border: none;
                    }
                }
            }
        }

        .empty-record {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 100px 0;

            .empty-icon {
                width: 100px;
                height: 100px;
                margin-bottom: 20px;
                opacity: 0.5;
            }

            .empty-text {
                font-size: 16px;
                color: #999;
            }
        }
    }
}
</style>
