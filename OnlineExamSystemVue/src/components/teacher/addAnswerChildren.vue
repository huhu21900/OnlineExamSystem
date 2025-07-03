// 添加题库
<template>
  <div class="add-answer-container">
    <div class="page-header">
      <i class="el-icon-plus"></i>
      <h2>添加试题</h2>
    </div>
    
    <el-card shadow="hover" class="main-card">
      <el-tabs v-model="activeName" type="border-card">
        <el-tab-pane name="first">
          <span slot="label"><i class="el-icon-circle-plus"></i>添加试题</span>
          <section class="append">
            <el-form :model="optionValue" label-width="90px" label-position="left" class="option-form" size="small">
              <el-row :gutter="15">
                <el-col :span="8">
                  <el-form-item label="题目类型:">
                    <el-select v-model="optionValue" placeholder="请选择题型" class="w150">
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                
                <el-col :span="8">
                  <el-form-item v-if="optionValue == '选择题'" label="所属章节：">
                    <el-input placeholder="请输入对应章节" v-model="postChange.section" class="w150" clearable>
                    </el-input>
                  </el-form-item>
                  <el-form-item v-if="optionValue == '填空题'" label="所属章节：">
                    <el-input placeholder="请输入对应章节" v-model="postFill.section" class="w150" clearable>
                    </el-input>
                  </el-form-item>
                  <el-form-item v-if="optionValue == '判断题'" label="所属章节：">
                    <el-input placeholder="请输入对应章节" v-model="postJudge.section" class="w150" clearable>
                    </el-input>
                  </el-form-item>
                  <el-form-item v-if="optionValue == '简答题'" label="所属章节：">
                    <el-input placeholder="请输入对应章节" v-model="postShortAnswer.section" class="w150" clearable>
                    </el-input>
                  </el-form-item>
                </el-col>
                
                <el-col :span="8">
                  <el-form-item v-if="optionValue == '选择题'" label="难度等级:">
                    <el-select v-model="postChange.level" placeholder="选择难度等级" class="w150">
                      <el-option v-for="item in levels" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item v-if="optionValue == '填空题'" label="难度等级:">
                    <el-select v-model="postFill.level" placeholder="选择难度等级" class="w150">
                      <el-option v-for="item in levels" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item v-if="optionValue == '判断题'" label="难度等级:">
                    <el-select v-model="postJudge.level" placeholder="选择难度等级" class="w150">
                      <el-option v-for="item in levels" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item v-if="optionValue == '简答题'" label="难度等级:">
                    <el-select v-model="postShortAnswer.level" placeholder="选择难度等级" class="w150">
                      <el-option v-for="item in levels" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-row v-if="optionValue == '选择题'">
                <el-col :span="8">
                  <el-form-item label="正确选项:">
                    <el-select v-model="postChange.rightAnswer" placeholder="选择正确答案" class="w150">
                      <el-option v-for="item in rights" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
            
            <!-- 选择题部分 -->
            <div class="question-section" v-if="optionValue == '选择题'">
              <div class="section-title">
                <el-divider content-position="left">
                  <i class="el-icon-s-opportunity"></i> 选择题内容
                </el-divider>
              </div>
              
              <el-form label-position="top" size="small" class="question-form">
                <el-form-item label="题目">
                  <div class="tip-text">请输入题目，例如"DNS 服务器和DHCP服务器的作用是（）"</div>
                  <el-input type="textarea" rows="3" v-model="postChange.question" placeholder="请输入题目内容" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item label="选项">
                  <el-row :gutter="15" class="options-row">
                    <el-col :span="12">
                      <div class="option-item">
                        <el-tag type="primary" size="small">A</el-tag>
                        <el-input placeholder="请输入选项A的内容" v-model="postChange.answerA" clearable="">
                        </el-input>
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <div class="option-item">
                        <el-tag type="success" size="small">B</el-tag>
                        <el-input placeholder="请输入选项B的内容" v-model="postChange.answerB" clearable="">
                        </el-input>
                      </div>
                    </el-col>
                  </el-row>
                  
                  <el-row :gutter="15" class="options-row">
                    <el-col :span="12">
                      <div class="option-item">
                        <el-tag type="warning" size="small">C</el-tag>
                        <el-input placeholder="请输入选项C的内容" v-model="postChange.answerC" clearable="">
                        </el-input>
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <div class="option-item">
                        <el-tag type="danger" size="small">D</el-tag>
                        <el-input placeholder="请输入选项D的内容" v-model="postChange.answerD" clearable="">
                        </el-input>
                      </div>
                    </el-col>
                  </el-row>
                </el-form-item>
                
                <el-form-item label="解析">
                  <div class="tip-text">请输入题目解析</div>
                  <el-input type="textarea" rows="3" v-model="postChange.analysis" placeholder="请输入答案解析" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item>
                  <div class="submit-btn">
                    <el-button type="primary" icon="el-icon-check" @click="changeSubmit()">立即添加</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 填空题部分 -->
            <div class="question-section fill" v-if="optionValue == '填空题'">
              <div class="section-title">
                <el-divider content-position="left">
                  <i class="el-icon-edit-outline"></i> 填空题内容
                </el-divider>
              </div>
              
              <el-form label-position="top" size="small" class="question-form">
                <el-form-item label="题目">
                  <div class="tip-text">输入题目，填空部分用括号()表示</div>
                  <el-input type="textarea" rows="3" v-model="postFill.question" placeholder="请输入题目内容" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item label="正确答案">
                  <el-input v-model="postFill.answer" placeholder="输入答案，多个答案用英文逗号分隔"></el-input>
                </el-form-item>
                
                <el-form-item label="解析">
                  <div class="tip-text">请输入答案解析</div>
                  <el-input type="textarea" rows="3" v-model="postFill.analysis" placeholder="请输入答案解析" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item>
                  <div class="submit-btn">
                    <el-button type="primary" icon="el-icon-check" @click="fillSubmit()">立即添加</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 判断题 -->
            <div class="question-section judge" v-if="optionValue == '判断题'">
              <div class="section-title">
                <el-divider content-position="left">
                  <i class="el-icon-help"></i> 判断题内容
                </el-divider>
              </div>
              
              <el-form label-position="top" size="small" class="question-form">
                <el-form-item label="题目">
                  <div class="tip-text">请输入判断题题目</div>
                  <el-input type="textarea" rows="3" v-model="postJudge.question" placeholder="请输入题目内容" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item label="正确答案">
                  <div class="judge-options">
                    <el-radio v-model="postJudge.answer" label="T">
                      <el-tag type="success" size="small">正确</el-tag>
                    </el-radio>
                    <el-radio v-model="postJudge.answer" label="F">
                      <el-tag type="danger" size="small">错误</el-tag>
                    </el-radio>
                  </div>
                </el-form-item>
                
                <el-form-item label="解析">
                  <div class="tip-text">请输入题目解析</div>
                  <el-input type="textarea" rows="3" v-model="postJudge.analysis" placeholder="请输入答案解析" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item>
                  <div class="submit-btn">
                    <el-button type="primary" icon="el-icon-check" @click="judgeSubmit()">立即添加</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 简答题 -->
            <div class="question-section short-answer" v-if="optionValue == '简答题'">
              <div class="section-title">
                <el-divider content-position="left">
                  <i class="el-icon-tickets"></i> 简答题内容
                </el-divider>
              </div>
              
              <el-form label-position="top" size="small" class="question-form">
                <el-form-item label="题目">
                  <div class="tip-text">请输入简答题题目</div>
                  <el-input type="textarea" rows="3" v-model="postShortAnswer.question" placeholder="请输入题目内容" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item label="参考答案">
                  <div class="tip-text">请输入参考答案</div>
                  <el-input type="textarea" rows="4" v-model="postShortAnswer.answer" placeholder="请输入参考答案" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item label="解析">
                  <div class="tip-text">请输入题目解析</div>
                  <el-input type="textarea" rows="3" v-model="postShortAnswer.analysis" placeholder="请输入答案解析" resize="none"
                    class="question-input">
                  </el-input>
                </el-form-item>
                
                <el-form-item>
                  <div class="submit-btn">
                    <el-button type="primary" icon="el-icon-check" @click="shortAnswerSubmit()">立即添加</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </div>
          </section>
        </el-tab-pane>
        
        <el-tab-pane name="second">
          <span slot="label"><i class="el-icon-magic-stick"></i> 在线组卷</span>
          <div class="random-section">
            <el-card shadow="hover" class="random-card">
              <div slot="header" class="card-header">
                <span><i class="el-icon-s-grid"></i> 为《{{ subject }}》试卷随机添加题目</span>
              </div>
              
              <el-form label-position="left" label-width="90px" class="random-form" size="small">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="选择题数量">
                      <el-input-number v-model="changeNumber" :min="0" :step="1" step-strictly controls-position="right" size="small"></el-input-number>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="填空题数量">
                      <el-input-number v-model="fillNumber" :min="0" :step="1" step-strictly controls-position="right" size="small"></el-input-number>
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="判断题数量">
                      <el-input-number v-model="judgeNumber" :min="0" :step="1" step-strictly controls-position="right" size="small"></el-input-number>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="简答题数量">
                      <el-input-number v-model="shortAnswerNumber" :min="0" :step="1" step-strictly controls-position="right" size="small"></el-input-number>
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-form-item>
                  <div class="random-btn">
                    <el-button type="success" icon="el-icon-magic-stick" @click="create()">立即组卷</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      changeNumber: 0, //选择题出题数量
      fillNumber: 0, //填空题出题数量
      judgeNumber: 0, //判断题出题数量
      shortAnswerNumber: 0, //简答题出题数量
      activeName: "first", //活动选项卡
      options: [
        //题库类型
        {
          value: "选择题",
          label: "选择题",
        },
        {
          value: "填空题",
          label: "填空题",
        },
        {
          value: "判断题",
          label: "判断题",
        },
        {
          value: "简答题",
          label: "简答题",
        },
      ],
      levels: [
        //难度等级
        {
          value: "1",
          label: "1",
        },
        {
          value: "2",
          label: "2",
        },
        {
          value: "3",
          label: "3",
        },
        {
          value: "4",
          label: "4",
        },
        {
          value: "5",
          label: "5",
        },
      ],
      rights: [
        //正确答案
        {
          value: "A",
          label: "A",
        },
        {
          value: "B",
          label: "B",
        },
        {
          value: "C",
          label: "C",
        },
        {
          value: "D",
          label: "D",
        },
      ],
      paperId: null,
      optionValue: "选择题", //题型选中值
      subject: "", //试卷名称用来接收路由参数
      postChange: {
        //选择题提交内容
        subject: "", //试卷名称
        level: "", //难度等级选中值
        rightAnswer: "", //正确答案选中值
        section: "", //对应章节
        question: "", //题目
        analysis: "", //解析
        answerA: "",
        answerB: "",
        answerC: "",
        answerD: "",
      },
      postFill: {
        //填空题提交内容
        subject: "", //试卷名称
        level: "", //难度等级选中值
        answer: "", //正确答案
        section: "", //对应章节
        question: "", //题目
        analysis: "", //解析
      },
      postJudge: {
        //判断题提交内容
        subject: "", //试卷名称
        level: "", //难度等级选中值
        answer: "", //正确答案
        section: "", //对应章节
        question: "", //题目
        analysis: "", //解析
      },
      postShortAnswer: {
        //简答题提交内容
        subject: "", //试卷名称
        level: "", //难度等级选中值
        answer: "", //正确答案
        section: "", //对应章节
        question: "", //题目
        analysis: "", //解析
      },
      postPaper: {
        //考试管理表对应字段
        paperId: null,
        questionType: null, // 试卷类型 1--选择题  2--填空题   3--判断题  4--简答题
        questionId: null,
      },
    };
  },
  created() {
    this.getParams();
  },
  methods: {
    create() {
      // 确保所有数量字段都是数字，且不为null
      const changeNum = parseInt(this.changeNumber) || 0;
      const fillNum = parseInt(this.fillNumber) || 0;
      const judgeNum = parseInt(this.judgeNumber) || 0;
      const shortAnswerNum = parseInt(this.shortAnswerNumber) || 0;
      
      // 检查是否至少有一种题型数量大于0
      if (changeNum === 0 && fillNum === 0 && judgeNum === 0 && shortAnswerNum === 0) {
        this.$message({
          message: "请至少设置一种题型的数量大于0",
          type: "warning"
        });
        return;
      }

      this.$axios({
        url: "/api/item",
        method: "post",
        data: {
          changeNumber: changeNum,
          fillNumber: fillNum,
          judgeNumber: judgeNum,
          shortAnswerNumber: shortAnswerNum,
          paperId: this.paperId,
          subject: this.subject,
        },
      }).then((res) => {
        let data = res.data;
        if (data.code == 200) {
          setTimeout(() => {
            this.$router.push({ path: "/selectAnswer" });
          }, 1000);
          this.$message({
            message: data.message,
            type: "success",
          });
        } else if (data.code == 400) {
          this.$message({
            message: data.message,
            type: "error",
          });
        }
      });
    },
    getParams() {
      let subject = this.$route.query.subject; //获取试卷名称
      let paperId = this.$route.query.paperId; //获取paperId
      this.paperId = paperId;
      this.subject = subject;
      this.postPaper.paperId = paperId;
      
      // 初始化简答题的科目
      this.postShortAnswer.subject = subject;
    },
    changeSubmit() {
      //选择题题库提交
      this.postChange.subject = this.subject;
      this.$axios({
        //提交数据到选择题题库表
        url: "/api/MultiQuestion",
        method: "post",
        data: {
          ...this.postChange,
        },
      })
        .then((res) => {
          //添加成功显示提示
          let status = res.data.code;
          if (status == 200) {
            this.$message({
              message: "已添加到题库",
              type: "success",
            });
            this.postChange = {
              //选择题提交内容
              subject: "", //试卷名称
              level: "", //难度等级选中值
              rightAnswer: "", //正确答案选中值
              section: "", //对应章节
              question: "", //题目
              analysis: "", //解析
              answerA: "",
              answerB: "",
              answerC: "",
              answerD: "",
            };
          }
        })
        .then(() => {
          this.$axios(`/api/multiQuestionId`).then((res) => {
            //获取当前题目的questionId
            let questionId = res.data.data.questionId;
            this.postPaper.questionId = questionId;
            this.postPaper.questionType = 1;
            this.$axios({
              url: "/api/paperManage",
              method: "Post",
              data: {
                ...this.postPaper,
              },
            });
          });
        });
    },
    fillSubmit() {
      //填空题提交
      this.postFill.subject = this.subject;
      this.$axios({
        url: "/api/fillQuestion",
        method: "post",
        data: {
          ...this.postFill,
        },
      })
        .then((res) => {
          let status = res.data.code;
          if (status == 200) {
            this.$message({
              message: "已添加到题库",
              type: "success",
            });
            this.postFill = {
              //填空题提交内容
              subject: "", //试卷名称
              level: "", //难度等级选中值
              answer: "", //正确答案
              section: "", //对应章节
              question: "", //题目
              analysis: "", //解析
            };
          }
        })
        .then(() => {
          this.$axios(`/api/fillQuestionId`).then((res) => {
            //获取当前题目的questionId
            let questionId = res.data.data.questionId;
            this.postPaper.questionId = questionId;
            this.postPaper.questionType = 2;
            this.$axios({
              url: "/api/paperManage",
              method: "Post",
              data: {
                ...this.postPaper,
              },
            });
          });
        });
    },
    judgeSubmit() {
      //判断题提交
      this.postJudge.subject = this.subject;
      this.$axios({
        url: "/api/judgeQuestion",
        method: "post",
        data: {
          ...this.postJudge,
        },
      })
        .then((res) => {
          let status = res.data.code;
          if (status == 200) {
            this.$message({
              message: "已添加到题库",
              type: "success",
            });
            this.postJudge = {
              //判断题提交内容
              subject: "", //试卷名称
              level: "", //难度等级选中值
              answer: "", //正确答案
              section: "", //对应章节
              question: "", //题目
              analysis: "", //解析
            };
          }
        })
        .then(() => {
          this.$axios(`/api/judgeQuestionId`).then((res) => {
            //获取当前题目的questionId
            let questionId = res.data.data.questionId;
            this.postPaper.questionId = questionId;
            this.postPaper.questionType = 3;
            this.$axios({
              url: "/api/paperManage",
              method: "Post",
              data: {
                ...this.postPaper,
              },
            });
          });
        });
    },
    shortAnswerSubmit() {
      //简答题提交
      this.postShortAnswer.subject = this.subject;
      
      // 确保章节和难度等级不为空
      if (!this.postShortAnswer.section || this.postShortAnswer.section.trim() === '') {
        this.$message({
          message: "请填写所属章节",
          type: "warning"
        });
        return;
      }
      
      if (!this.postShortAnswer.level || this.postShortAnswer.level.trim() === '') {
        this.$message({
          message: "请选择难度等级",
          type: "warning"
        });
        return;
      }
      
      this.$axios({
        url: "/api/shortAnswerQuestion",
        method: "post",
        data: {
          ...this.postShortAnswer,
        },
      })
        .then((res) => {
          let status = res.data.code;
          if (status == 200) {
            this.$message({
              message: "已添加到题库",
              type: "success",
            });
            this.postShortAnswer = {
              //简答题提交内容
              subject: this.subject, //试卷名称
              level: "", //难度等级选中值
              answer: "", //正确答案
              section: "", //对应章节
              question: "", //题目
              analysis: "", //解析
            };
          }
        })
        .then(() => {
          this.$axios(`/api/shortAnswerQuestionId`).then((res) => {
            //获取当前题目的questionId
            let questionId = res.data.data.questionId;
            this.postPaper.questionId = questionId;
            this.postPaper.questionType = 4;
            this.$axios({
              url: "/api/paperManage",
              method: "Post",
              data: {
                ...this.postPaper,
              },
            });
          });
        });
    },
  },
};
</script>

<style lang="less" scoped>
.add-answer-container {
  padding: 15px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
  
  .page-header {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    
    i {
      font-size: 22px;
      color: #409EFF;
      margin-right: 8px;
    }
    
    h2 {
      margin: 0;
      font-size: 20px;
      color: #303133;
      font-weight: 600;
    }
  }
  
  .main-card {
    margin-bottom: 15px;
    border-radius: 6px;
    box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.08);
    
    ::v-deep .el-tabs__header {
      margin-bottom: 15px;
    }
    
    ::v-deep .el-tabs__item {
      height: 40px;
      line-height: 40px;
      font-size: 14px;
      
      i {
        margin-right: 4px;
      }
    }
  }
  
  .option-form {
    padding: 10px 0;
    
    .el-form-item {
      margin-bottom: 10px;
    }
    
    .w150 {
      width: 100%;
    }
  }
  
  .question-section {
    background-color: #f8f9fa;
    padding: 15px;
    border-radius: 6px;
    margin-bottom: 15px;
    
    .section-title {
      margin-bottom: 15px;
      
      ::v-deep .el-divider__text {
        background-color: #f8f9fa;
        font-size: 14px;
        font-weight: 600;
        color: #409EFF;
        
        i {
          margin-right: 4px;
          font-size: 16px;
        }
      }
    }
    
    .tip-text {
      color: #606266;
      font-size: 12px;
      margin-bottom: 5px;
    }
    
    .question-input {
      width: 100%;
      margin-bottom: 10px;
    }
    
    .options-row {
      margin-bottom: 10px;
      
      .option-item {
        display: flex;
        align-items: center;
        
        .el-tag {
          margin-right: 8px;
          width: 24px;
          text-align: center;
        }
      }
    }
    
    .judge-options {
      display: flex;
      gap: 25px;
      
      .el-radio {
        margin-right: 0;
      }
    }
    
    .submit-btn {
      text-align: center;
      margin-top: 15px;
      
      .el-button {
        padding: 8px 20px;
        font-size: 14px;
      }
    }
  }

  .question-form {
    .el-form-item {
      margin-bottom: 12px;
    }
    
    .el-form-item__label {
      padding-bottom: 4px;
      font-weight: 500;
    }
  }
  
  .random-section {
    padding: 0 15px 15px;
    
    .random-card {
      border-radius: 6px;
      
      .card-header {
        display: flex;
        align-items: center;
        
        i {
          margin-right: 4px;
          color: #409EFF;
        }
        
        span {
          font-size: 14px;
          font-weight: 600;
        }
      }
      
      .random-form {
        padding: 10px 0;
        
        .el-form-item {
          margin-bottom: 15px;
        }
        
        ::v-deep .el-input-number {
          width: 100%;
        }
        
        .random-btn {
          text-align: center;
          
          .el-button {
            padding: 8px 20px;
            font-size: 14px;
          }
        }
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .add-answer-container {
    padding: 10px;
    
    .question-section {
      padding: 12px;
    }
    
    .option-form {
      .w150 {
        width: 100%;
      }
    }
    
    .random-section {
      padding: 0 10px 10px;
    }
  }
}
</style>
