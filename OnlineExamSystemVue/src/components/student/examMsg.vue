// 点击试卷后的缩略信息
<template>
  <div id="msg">
    <div class="page-header">
      <div class="breadcrumb">
        <i class="el-icon-arrow-left" @click="$router.go(-1)"></i>
        <span>试卷列表</span>
        <i class="el-icon-arrow-right"></i>
        <span class="current">{{ examData.source }}</span>
      </div>
    </div>

    <div class="exam-container">
      <div class="exam-header">
        <div class="exam-title">
          <i class="iconfont icon-r-paper"></i>
          <h2>{{ examData.source }}</h2>
        </div>
        <div class="exam-score">
          <div class="score-card">
            <div class="score-label">总分</div>
            <div class="score-value">{{ score[0] + score[1] + score[2] + score[3] }}</div>
          </div>
        </div>
      </div>

      <div class="exam-info">
        <div class="info-items">
          <div class="info-item">
            <i class="el-icon-date"></i>
            <span>更新于 {{ examData.examDate }}</span>
          </div>
          <div class="info-item">
            <i class="el-icon-school"></i>
            <span>{{ examData.institute }}</span>
          </div>
          <div class="info-item">
            <i class="el-icon-collection-tag"></i>
            <span>{{ examData.type }}</span>
          </div>
        </div>
        <div class="exam-start">
          <el-button type="primary" @click="toAnswer(examData.examCode)">
            <i class="el-icon-edit"></i>
            {{ $store.state.isPractice == true ? '开始练习' : '开始考试' }}
          </el-button>
        </div>
      </div>

      <div class="exam-notice" @click="dialogVisible = true">
        <i class="el-icon-info"></i>
        <span>考生须知</span>
      </div>
    </div>

    <div class="exam-content">
      <el-collapse v-model="activeName" class="custom-collapse">
        <el-collapse-item name="0" class="exam-overview">
          <template slot="title">
            <div class="overview-header">
              <div class="overview-title">
                <i class="el-icon-document"></i>
                <span>{{ examData.source }}</span>
              </div>
              <div class="overview-info">
                <div class="info-tag">
                  <i class="el-icon-trophy"></i>
                  <span>{{ score[0] + score[1] + score[2] + score[3] }}分</span>
                </div>
                <div class="info-tag">
                  <i class="el-icon-time"></i>
                  <span>{{ examData.totalTime }}分钟</span>
                </div>
                <el-button size="small" type="primary" class="questions-btn">查看试题</el-button>
              </div>
            </div>
          </template>

          <el-collapse class="question-sections">
            <el-collapse-item>
              <template slot="title">
                <div class="section-title">
                  <i class="el-icon-circle-check"></i>
                  <span>选择题</span>
                  <div class="section-info">
                    <span class="question-count">{{ topicCount[0] }}题</span>
                    <span class="question-score">{{ score[0] }}分</span>
                  </div>
                </div>
              </template>
              <div class="question-list">
                <div class="question-item" v-for="(list, index) in topic[1]" :key="index">
                  <span class="question-number">{{ index + 1 }}.</span>
                  <span class="question-text">{{ list.question }}</span>
                  <span class="question-score">{{ list.score }}分</span>
                </div>
              </div>
            </el-collapse-item>

            <el-collapse-item>
              <template slot="title">
                <div class="section-title">
                  <i class="el-icon-edit"></i>
                  <span>填空题</span>
                  <div class="section-info">
                    <span class="question-count">{{ topicCount[1] }}题</span>
                    <span class="question-score">{{ score[1] }}分</span>
                  </div>
                </div>
              </template>
              <div class="question-list">
                <div class="question-item" v-for="(list, index) in topic[2]" :key="index">
                  <span class="question-number">{{ topicCount[0] + index + 1 }}.</span>
                  <span class="question-text">{{ list.question }}</span>
                  <span class="question-score">{{ list.score }}分</span>
                </div>
              </div>
            </el-collapse-item>

            <el-collapse-item>
              <template slot="title">
                <div class="section-title">
                  <i class="el-icon-finished"></i>
                  <span>判断题</span>
                  <div class="section-info">
                    <span class="question-count">{{ topicCount[2] }}题</span>
                    <span class="question-score">{{ score[2] }}分</span>
                  </div>
                </div>
              </template>
              <div class="question-list">
                <div class="question-item" v-for="(list, index) in topic[3]" :key="index">
                  <span class="question-number">{{ topicCount[0] + topicCount[1] + index + 1 }}.</span>
                  <span class="question-text">{{ list.question }}</span>
                  <span class="question-score">{{ list.score }}分</span>
                </div>
              </div>
            </el-collapse-item>

            <el-collapse-item>
              <template slot="title">
                <div class="section-title">
                  <i class="el-icon-notebook-2"></i>
                  <span>简答题</span>
                  <div class="section-info">
                    <span class="question-count">{{ topicCount[3] || 0 }}题</span>
                    <span class="question-score">{{ score[3] || 0 }}分</span>
                  </div>
                </div>
              </template>
              <div class="question-list">
                <div class="question-item" v-for="(list, index) in topic[4]" :key="index">
                  <span class="question-number">{{ topicCount[0] + topicCount[1] + topicCount[2] + index + 1 }}.</span>
                  <span class="question-text">{{ list.question }}</span>
                  <span class="question-score">{{ list.score }}分</span>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-collapse-item>
      </el-collapse>
    </div>

    <!--考生须知对话框-->
    <el-dialog title="考生须知" :visible.sync="dialogVisible" width="30%" center>
      <div class="notice-content">
        <i class="el-icon-warning-outline notice-icon"></i>
        <p>{{ examData.tips || '请仔细阅读考试须知，遵守考试规则。' }}</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">我已了解</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false, //对话框属性
      activeName: '0',  //默认打开序号
      topicCount: [],//每种类型题目的总数
      score: [],  //每种类型分数的总数
      examData: { //考试信息
        // source: null,
        // totalScore: null,
      },
      topic: {  //试卷信息

      },
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    //初始化页面数据
    init() {
      let examCode = this.$route.query.examCode //获取路由传递过来的试卷编号
      this.$axios(`/api/exam/${examCode}`).then(res => {  //通过examCode请求试卷详细信息
        res.data.data.examDate = res.data.data.examDate.substr(0, 10)
        this.examData = { ...res.data.data }
        let paperId = this.examData.paperId
        this.$axios(`/api/paper/${paperId}`).then(res => {  //通过paperId获取试题题目信息
          this.topic = { ...res.data }
          let keys = Object.keys(this.topic) //对象转数组
          keys.forEach(e => {
            let data = this.topic[e]
            this.topicCount.push(data.length)
            let currentScore = 0
            for (let i = 0; i < data.length; i++) { //循环每种题型,计算出总分
              currentScore += data[i].score
            }
            this.score.push(currentScore) //把每种题型总分存入score
          })
          
          // 确保score数组有足够的元素，防止未定义错误
          while (this.score.length < 4) {
            this.score.push(0);
          }
          
          // 确保topicCount数组有足够的元素
          while (this.topicCount.length < 4) {
            this.topicCount.push(0);
          }
        })
      })
    },
    toAnswer(id) {
      if (this.$store.state.isPractice) {
        // 练习，根据科目名称从题库里面搜索所有题目
        this.$confirm(`将开始练习题库中所有科目为【${this.examData.source}】的题目`, "友情提示", {
          confirmButtonText: " 确认开始",
          cancelButtonText: " 取消",
          type: "warning",
        })
          .then(() => {
            this.$router.push({ path: "/answer", query: { examCode: id } })
          })
          .catch(() => { });
      } else {

        this.$router.push({ path: "/answer", query: { examCode: id } })
      }
    }
  }
}
</script>

<style lang="less" scoped>
#msg {
  width: 1100px;
  margin: 0 auto;
  background-color: #f5f7fa;
}

.page-header {
  margin: 20px 0;
  
  .breadcrumb {
    display: flex;
    align-items: center;
    font-size: 16px;
    color: #606266;
    
    i {
      margin: 0 8px;
      font-size: 14px;
      cursor: pointer;
    }
    
    .current {
      color: #303133;
      font-weight: 500;
    }
  }
}

.exam-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 20px;
  
  .exam-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    .exam-title {
      display: flex;
      align-items: center;
      
      i {
        font-size: 28px;
        margin-right: 12px;
        color: #409EFF;
      }
      
      h2 {
        font-size: 22px;
        font-weight: 600;
        margin: 0;
        color: #303133;
      }
    }
    
    .exam-score {
      .score-card {
        background: #fff;
        border-radius: 6px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        overflow: hidden;
        
        .score-label {
          background-color: #409EFF;
          color: #fff;
          padding: 4px 10px;
          font-size: 14px;
          text-align: center;
        }
        
        .score-value {
          padding: 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #409EFF;
          text-align: center;
          min-width: 60px;
        }
      }
    }
  }
  
  .exam-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    .info-items {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      
      .info-item {
        display: flex;
        align-items: center;
        margin-right: 24px;
        margin-bottom: 10px;
        
        i {
          font-size: 16px;
          color: #409EFF;
          margin-right: 6px;
        }
        
        span {
          font-size: 14px;
          color: #606266;
        }
      }
    }
    
    .exam-start {
      .el-button {
        padding: 12px 24px;
        font-size: 16px;
        
        i {
          margin-right: 5px;
        }
      }
    }
  }
  
  .exam-notice {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-top: 1px solid #EBEEF5;
    cursor: pointer;
    transition: all 0.3s;
    
    i {
      font-size: 16px;
      color: #E6A23C;
      margin-right: 8px;
    }
    
    span {
      font-size: 14px;
      color: #606266;
    }
    
    &:hover {
      color: #409EFF;
      
      i, span {
        color: #409EFF;
      }
    }
  }
}

.exam-content {
  .custom-collapse {
    border-radius: 8px;
    overflow: hidden;
    
    /deep/ .el-collapse-item__header {
      background-color: #fff;
      padding: 18px 24px;
      
      &.is-active {
        background-color: #f5f7fa;
        border-bottom-color: #EBEEF5;
      }
    }
    
    /deep/ .el-collapse-item__content {
      padding: 24px;
    }
  }
  
  .overview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    
    .overview-title {
      display: flex;
      align-items: center;
      
      i {
        font-size: 20px;
        margin-right: 10px;
        color: #409EFF;
      }
      
      span {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }
    }
    
    .overview-info {
      display: flex;
      align-items: center;
      
      .info-tag {
        display: flex;
        align-items: center;
        margin-right: 16px;
        
        i {
          font-size: 16px;
          color: #409EFF;
          margin-right: 5px;
        }
        
        span {
          font-size: 14px;
          color: #606266;
        }
      }
      
      .questions-btn {
        margin-left: 16px;
      }
    }
  }
  
  .question-sections {
    margin-top: 20px;
    border-radius: 4px;
    
    /deep/ .el-collapse-item__header {
      padding: 12px 16px;
      
      &:hover {
        background-color: #f5f7fa;
      }
    }
    
    .section-title {
      display: flex;
      align-items: center;
      width: 100%;
      
      i {
        font-size: 16px;
        color: #409EFF;
        margin-right: 8px;
      }
      
      span {
        font-size: 16px;
        color: #303133;
        font-weight: 500;
      }
      
      .section-info {
        margin-left: auto;
        display: flex;
        align-items: center;
        
        .question-count {
          background-color: #f0f9eb;
          color: #67c23a;
          padding: 2px 8px;
          border-radius: 4px;
          font-size: 12px;
          margin-right: 10px;
        }
        
        .question-score {
          background-color: #ecf5ff;
          color: #409EFF;
          padding: 2px 8px;
          border-radius: 4px;
          font-size: 12px;
        }
      }
    }
    
    .question-list {
      padding: 10px 0;
      
      .question-item {
        display: flex;
        align-items: flex-start;
        padding: 10px 16px;
        transition: background-color 0.3s;
        border-radius: 4px;
        
        &:hover {
          background-color: #f5f7fa;
        }
        
        .question-number {
          font-weight: 500;
          color: #303133;
          margin-right: 8px;
          min-width: 24px;
        }
        
        .question-text {
          flex: 1;
          color: #606266;
          line-height: 1.5;
        }
        
        .question-score {
          color: #409EFF;
          font-weight: 500;
          margin-left: 12px;
        }
      }
    }
  }
}

.notice-content {
  display: flex;
  align-items: flex-start;
  padding: 20px 0;
  
  .notice-icon {
    font-size: 24px;
    color: #E6A23C;
    margin-right: 16px;
  }
  
  p {
    flex: 1;
    margin: 0;
    line-height: 1.6;
    color: #606266;
  }
}

/deep/ .el-dialog__title {
  font-weight: 600;
  color: #303133;
}

/deep/ .el-button--primary {
  font-weight: 500;
}
</style>
