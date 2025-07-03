<template>
    <div class="result-container">
        <div class="page-header">
            <i class="el-icon-data-analysis"></i>
            <span>考试结果</span>
        </div>

        <div class="result-wrapper">
            <div class="result-header">
                <i class="el-icon-trophy"></i>
                <span>本次考试成绩</span>
            </div>
            
            <div class="result-content">
                <div class="score-section">
                    <div class="score-circle" :class="{ 'score-animate': isTransition }">
                        <div class="score-number">{{ score }}</div>
                        <div class="score-label">分数</div>
                    </div>
                    
                    <div class="score-status">
                        <template v-if="score >= 60">
                            <i class="el-icon-success status-icon pass"></i>
                            <span class="status-text pass">恭喜您，通过考试！</span>
                        </template>
                        <template v-else>
                            <i class="el-icon-error status-icon fail"></i>
                            <span class="status-text fail">很遗憾，未通过考试</span>
                        </template>
                    </div>
                </div>

                <div class="exam-info">
                    <div class="info-card start-time">
                        <div class="info-icon">
                            <i class="el-icon-time"></i>
                        </div>
                        <div class="info-content">
                            <div class="info-label">开始时间</div>
                            <div class="info-value">{{ startTime }}</div>
                        </div>
                    </div>

                    <div class="info-card end-time">
                        <div class="info-icon">
                            <i class="el-icon-time"></i>
                        </div>
                        <div class="info-content">
                            <div class="info-label">结束时间</div>
                            <div class="info-value">{{ endTime }}</div>
                        </div>
                    </div>
                    
                    <div class="info-card duration" v-if="duration">
                        <div class="info-icon">
                            <i class="el-icon-stopwatch"></i>
                        </div>
                        <div class="info-content">
                            <div class="info-label">考试用时</div>
                            <div class="info-value">{{ duration }}</div>
                        </div>
                    </div>
                </div>

                <div class="action-buttons">
                    <el-button type="primary" @click="goToScoreTable">
                        <i class="el-icon-s-data"></i> 查看所有成绩
                    </el-button>
                    <el-button @click="goToExamCenter">
                        <i class="el-icon-document"></i> 返回考试中心
                    </el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            isTransition: false, //是否渲染完成
            score: 0, //总分
            startTime: null, //考试开始时间
            endTime: null, //考试结束时间,
            duration: null, // 考试用时
        };
    },
    created() {
        this.getScore();
        this.transiton();
    },
    methods: {
        transiton() {
            //一秒后过渡
            setTimeout(() => {
                this.isTransition = true;
            }, 500);
        },
        getScore() {
            let score = this.$route.query.score;
            let startTime = this.$route.query.startTime;
            let endTime = this.$route.query.endTime;
            this.score = score;
            this.startTime = startTime;
            this.endTime = endTime;
            
            // 计算考试用时
            if (startTime && endTime) {
                try {
                    const start = new Date(startTime);
                    const end = new Date(endTime);
                    const diff = Math.abs(end - start);
                    
                    // 计算分钟和秒
                    const minutes = Math.floor(diff / (1000 * 60));
                    const seconds = Math.floor((diff % (1000 * 60)) / 1000);
                    
                    this.duration = `${minutes}分钟${seconds}秒`;
                } catch (e) {
                    console.error("计算考试用时出错", e);
                    this.duration = null;
                }
            }
        },
        goToScoreTable() {
            this.$router.push('/scoreTable');
        },
        goToExamCenter() {
            this.$router.push('/student');
        }
    },
};
</script>

<style lang="less" scoped>
.result-container {
    width: 1100px;
    margin: 0 auto;

    .page-header {
        display: flex;
        align-items: center;
        margin: 30px 0;
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        
        i {
            font-size: 32px;
            margin-right: 12px;
            color: #409EFF;
        }
    }

    .result-wrapper {
        background-color: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        overflow: hidden;
    }
    
    .result-header {
        background-color: #409EFF;
        color: #fff;
        padding: 20px;
        display: flex;
        align-items: center;
        font-size: 22px;
        font-weight: 600;
        
        i {
            font-size: 28px;
            margin-right: 10px;
        }
    }
    
    .result-content {
        padding: 40px;
    }
    
    .score-section {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 40px;
        
        .score-circle {
            width: 200px;
            height: 200px;
            border-radius: 50%;
            background-color: #f5f7fa;
            border: 6px solid #e4e7ed;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            margin-bottom: 20px;
            transition: all 1.2s ease;
            transform: scale(0.8) rotate(0deg);
            opacity: 0.2;
            
            &.score-animate {
                border-color: #409EFF;
                background-color: #ecf5ff;
                transform: scale(1) rotate(360deg);
                opacity: 1;
                box-shadow: 0 0 20px rgba(64, 158, 255, 0.3);
            }
            
            .score-number {
                font-size: 64px;
                font-weight: 700;
                color: #409EFF;
                line-height: 1;
                margin-bottom: 10px;
            }
            
            .score-label {
                font-size: 16px;
                color: #909399;
            }
        }
        
        .score-status {
            display: flex;
            align-items: center;
            font-size: 20px;
            
            .status-icon {
                font-size: 24px;
                margin-right: 10px;
                
                &.pass {
                    color: #67c23a;
                }
                
                &.fail {
                    color: #f56c6c;
                }
            }
            
            .status-text {
                font-weight: 600;
                
                &.pass {
                    color: #67c23a;
                }
                
                &.fail {
                    color: #f56c6c;
                }
            }
        }
    }
    
    .exam-info {
        display: flex;
        justify-content: center;
        gap: 30px;
        margin-bottom: 40px;
        
        .info-card {
            background-color: #f5f7fa;
            border-radius: 8px;
            padding: 20px;
            width: 220px;
            display: flex;
            align-items: center;
            transition: all 0.3s;
            
            &:hover {
                transform: translateY(-5px);
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
            }
            
            &.start-time {
                border-left: 4px solid #e6a23c;
                
                .info-icon {
                    background-color: rgba(230, 162, 60, 0.1);
                    color: #e6a23c;
                }
            }
            
            &.end-time {
                border-left: 4px solid #409EFF;
                
                .info-icon {
                    background-color: rgba(64, 158, 255, 0.1);
                    color: #409EFF;
                }
            }
            
            &.duration {
                border-left: 4px solid #67c23a;
                
                .info-icon {
                    background-color: rgba(103, 194, 58, 0.1);
                    color: #67c23a;
                }
            }
            
            .info-icon {
                width: 50px;
                height: 50px;
                border-radius: 50%;
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 24px;
                margin-right: 15px;
            }
            
            .info-content {
                flex: 1;
                
                .info-label {
                    font-size: 14px;
                    color: #909399;
                    margin-bottom: 5px;
                }
                
                .info-value {
                    font-size: 16px;
                    font-weight: 600;
                    color: #303133;
                }
            }
        }
    }
    
    .action-buttons {
        display: flex;
        justify-content: center;
        gap: 20px;
        
        .el-button {
            padding: 12px 25px;
            font-size: 16px;
        }
    }
}
</style>

