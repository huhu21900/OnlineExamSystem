// 我的试卷页面
<template>
    <div id="myExam">
        <div class="title">
            <i class="iconfont icon-r-paper"></i>
            考试中心
        </div>
        <div class="wrapper">
            <div class="top-container">
                <div class="top-title">
                    <i class="iconfont icon-r-list"></i>
                    <span>试卷列表</span>
                </div>
                <div class="search-container">
                    <div class="search-box">
                        <input type="text" placeholder="输入试卷名称搜索" class="search" v-model="key" />
                        <i class="el-icon-search"></i>
                    </div>
                    <el-button type="primary" @click="search()" icon="el-icon-search" class="search-btn">搜索试卷</el-button>
                </div>
            </div>
            <div class="exam-content" v-loading="loading">
                <div class="paper">
                    <div @click="toExamMsg(item)" class="item" v-for="(item, index) in pagination.records" :key="index">
                        <div class="item-header">
                            <i class="iconfont icon-r-paper"></i>
                            <span class="item-title">{{ item.source }}</span>
                        </div>
                        <p class="name">{{ item.source }}-{{ item.description }}</p>
                        <div class="info">
                            <div class="info-item">
                                <i class="el-icon-date"></i>
                                <span>考试时间：{{ item.examDate.substr(0, 10) }}</span>
                            </div>
                            <div class="info-item" v-if="item.totalTime != null">
                                <i class="el-icon-time"></i>
                                <span>限时{{ item.totalTime }}分钟</span>
                            </div>
                            <div class="info-item">
                                <i class="el-icon-trophy"></i>
                                <span>满分{{ item.totalScore }}分</span>
                            </div>
                        </div>
                        <div class="item-footer">
                            <el-button size="small" type="primary" class="exam-btn" @click.stop="toExamMsg(item)">
                                <i class="el-icon-edit-outline"></i>
                                开始考试
                            </el-button>
                        </div>
                    </div>
                </div>
                <div class="no-data" v-if="pagination.records && pagination.records.length === 0">
                    <i class="el-icon-document"></i>
                    <p>暂无试卷数据</p>
                </div>
            </div>
            <div class="pagination">
                <el-pagination 
                    @size-change="handleSizeChange" 
                    @current-change="handleCurrentChange"
                    :current-page="pagination.current" 
                    :page-sizes="[6, 10, 20, 40]" 
                    :page-size="pagination.size"
                    layout="total, sizes, prev, pager, next, jumper" 
                    :total="pagination.total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    // name: 'myExam'
    data() {
        return {
            loading: false,
            key: null, //搜索关键字
            allExam: null, //所有考试信息
            pagination: {
                //分页后的考试信息
                current: 1, //当前页
                total: null, //记录条数
                size: 6, //每页条数
            },
        };
    },
    created() {
        this.getExamInfo();
        this.loading = true;
    },
    // watch: {

    // },
    methods: {
        //获取当前所有考试信息
        getExamInfo() {
            this.$axios(
                `/api/exams/${this.pagination.current}/${this.pagination.size}`
            )
                .then((res) => {
                    if(res.data && res.data.data) {
                        this.pagination = res.data.data;
                    } else {
                        // 如果没有返回有效数据，初始化一个空的记录数组
                        this.pagination.records = [];
                        this.pagination.total = 0;
                    }
                    this.loading = false;
                    console.log(this.pagination);
                })
                .catch((error) => {
                    console.log(error);
                    this.loading = false;
                    // 发生错误时初始化空记录
                    this.pagination.records = [];
                    this.pagination.total = 0;
                });
        },
        //改变当前记录条数
        handleSizeChange(val) {
            this.pagination.size = val;
            this.getExamInfo();
        },
        //改变当前页码，重新发送请求
        handleCurrentChange(val) {
            this.pagination.current = val;
            this.getExamInfo();
        },
        //搜索试卷
        search() {
            this.$axios("/api/exams").then((res) => {
                if (res.data.code == 200) {
                    let allExam = res.data.data;
                    let newPage = allExam.filter((item) => {
                        return item.source.includes(this.key);
                    });
                    this.pagination.records = newPage;
                }
            });
        },
        //跳转到试卷详情页
        toExamMsg(exam) {
            if (exam.examDate > this.formatDateNow()) {
                this.$message({
                    message: "考试时间未到",
                    type: "error",
                });
                return;
            }
            this.$router.push({
                path: "/examMsg",
                query: { examCode: exam.examCode },
            });
        },
        formatDateNow() {
            var date = new Date(); // 当前日期时间
            var year = date.getFullYear(); // 年份
            var month = ("0" + (date.getMonth() + 1)).slice(-2); // 月份+1
            var day = ("0" + date.getDate()).slice(-2); // 日
            return year + "-" + month + "-" + day;
        },
    },
};
</script>

<style lang="less" scoped>
#myExam {
    width: 1100px;
    margin: 0 auto;
    
    .title {
        font-size: 28px;
        font-weight: 600;
        margin: 30px 0;
        color: #303133;
        display: flex;
        align-items: center;
        
        i {
            font-size: 32px;
            margin-right: 12px;
            color: #409EFF;
        }
    }

    .wrapper {
        background-color: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        padding: 20px;
    }
}

.top-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0 20px 0;
    border-bottom: 1px solid #EBEEF5;
    
    .top-title {
        display: flex;
        align-items: center;
        font-size: 22px;
        font-weight: 500;
        
        i {
            font-size: 24px;
            margin-right: 10px;
            color: #409EFF;
        }
    }
    
    .search-container {
        display: flex;
        align-items: center;
        
        .search-box {
            position: relative;
            margin-right: 15px;
            
            .search {
                width: 250px;
                padding: 10px 15px;
                border-radius: 4px;
                border: 1px solid #DCDFE6;
                transition: all 0.3s;
                font-size: 14px;
                
                &:focus {
                    border-color: #409EFF;
                    outline: none;
                    box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
                }
                
                &::placeholder {
                    color: #C0C4CC;
                }
            }
            
            .el-icon-search {
                position: absolute;
                right: 10px;
                top: 50%;
                transform: translateY(-50%);
                font-size: 16px;
                color: #909399;
            }
        }
        
        .search-btn {
            font-weight: 500;
        }
    }
}

.exam-content {
    min-height: 400px;
    padding: 20px 0;
    
    .paper {
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
        gap: 20px;
        
        .item {
            width: 320px;
            padding: 20px;
            border-radius: 8px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            
            &:hover {
                transform: translateY(-5px);
                box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
                
                &:before {
                    transform: translateX(0);
                }
            }
            
            &:before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                width: 4px;
                height: 100%;
                background-color: #409EFF;
                transform: translateX(-4px);
                transition: transform 0.3s ease;
            }
            
            .item-header {
                display: flex;
                align-items: center;
                margin-bottom: 15px;
                
                i {
                    font-size: 24px;
                    margin-right: 10px;
                    color: #409EFF;
                }
                
                .item-title {
                    font-size: 18px;
                    font-weight: 600;
                    color: #303133;
                }
            }
            
            .name {
                font-size: 14px;
                color: #606266;
                margin: 10px 0 20px;
                line-height: 1.4;
                height: 40px;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
            }
            
            .info {
                display: flex;
                flex-direction: column;
                gap: 10px;
                
                .info-item {
                    display: flex;
                    align-items: center;
                    font-size: 14px;
                    color: #909399;
                    
                    i {
                        margin-right: 5px;
                        color: #409EFF;
                    }
                }
            }
            
            .item-footer {
                margin-top: 20px;
                display: flex;
                justify-content: flex-end;
                
                .exam-btn {
                    font-size: 14px;
                    
                    i {
                        margin-right: 5px;
                    }
                }
            }
        }
    }
    
    .no-data {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 300px;
        color: #909399;
        
        i {
            font-size: 60px;
            margin-bottom: 20px;
        }
        
        p {
            font-size: 16px;
        }
    }
}

.pagination {
    padding: 30px 0;
    display: flex;
    justify-content: center;
}
</style>
