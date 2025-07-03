// 修改题库
<template>
  <div class="edit-answer-container">
    <div class="page-header">
      <i class="el-icon-edit-outline"></i>
      <h2>编辑试题</h2>
    </div>
    
    <el-card shadow="hover" class="main-card">
      <el-tabs v-model="activeName" type="border-card">
        <el-tab-pane name="first">
          <span slot="label"><i class="el-icon-circle-plus"></i> 编辑试题</span>
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
                  <div class="tip-text">在下面的输入框中输入题目,形如--DNS 服务器和DHCP服务器的作用是（）</div>
                  <el-input
                    type="textarea"
                    rows="3"
                    v-model="postChange.question"
                    placeholder="请输入题目内容"
                    resize="none"
                    class="question-input"
                  >
                  </el-input>
                </el-form-item>
                
                <el-form-item label="选项">
                  <el-row :gutter="15" class="options-row">
                    <el-col :span="12">
                      <div class="option-item">
                        <el-tag type="primary" size="small">A</el-tag>
                        <el-input
                          placeholder="请输入选项A的内容"
                          v-model="postChange.answerA"
                          clearable
                        >
                        </el-input>
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <div class="option-item">
                        <el-tag type="success" size="small">B</el-tag>
                        <el-input
                          placeholder="请输入选项B的内容"
                          v-model="postChange.answerB"
                          clearable
                        >
                        </el-input>
                      </div>
                    </el-col>
                  </el-row>
                  
                  <el-row :gutter="15" class="options-row">
                    <el-col :span="12">
                      <div class="option-item">
                        <el-tag type="warning" size="small">C</el-tag>
                        <el-input
                          placeholder="请输入选项C的内容"
                          v-model="postChange.answerC"
                          clearable
                        >
                        </el-input>
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <div class="option-item">
                        <el-tag type="danger" size="small">D</el-tag>
                        <el-input
                          placeholder="请输入选项D的内容"
                          v-model="postChange.answerD"
                          clearable
                        >
                        </el-input>
                      </div>
                    </el-col>
                  </el-row>
                </el-form-item>
                
                <el-form-item label="解析">
                  <div class="tip-text">在下面的输入框中输入题目解析</div>
                  <el-input
                    type="textarea"
                    rows="3"
                    v-model="postChange.analysis"
                    placeholder="请输入答案解析"
                    resize="none"
                    class="question-input"
                  >
                  </el-input>
                </el-form-item>
                
                <el-form-item>
                  <div class="submit-btn">
                    <el-button type="primary" icon="el-icon-check" @click="changeSubmit()">提交修改</el-button>
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
                  <div class="tip-text">输入题目,形如--计算机网络可以分为()和()。需要考生答题部分用括号括起来。</div>
                  <el-input
                    type="textarea"
                    rows="3"
                    v-model="postFill.question"
                    placeholder="请输入题目内容"
                    resize="none"
                    class="question-input"
                  >
                  </el-input>
                </el-form-item>
                
                <el-form-item label="正确答案">
                  <el-input v-model="postFill.answer" placeholder="输入答案，多个答案用英文逗号分隔"></el-input>
                </el-form-item>
                
                <el-form-item label="解析">
                  <div class="tip-text">在下面的输入框中输入题目解析</div>
                  <el-input
                    type="textarea"
                    rows="3"
                    v-model="postFill.analysis"
                    placeholder="请输入答案解析"
                    resize="none"
                    class="question-input"
                  >
                  </el-input>
                </el-form-item>
                
                <el-form-item>
                  <div class="submit-btn">
                    <el-button type="primary" icon="el-icon-check" @click="fillSubmit()">提交修改</el-button>
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
                  <div class="tip-text">在下面的输入框中输入题目</div>
                  <el-input
                    type="textarea"
                    rows="3"
                    v-model="postJudge.question"
                    placeholder="请输入题目内容"
                    resize="none"
                    class="question-input"
                  >
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
                  <div class="tip-text">在下面的输入框中输入题目解析</div>
                  <el-input
                    type="textarea"
                    rows="3"
                    v-model="postJudge.analysis"
                    placeholder="请输入答案解析"
                    resize="none"
                    class="question-input"
                  >
                  </el-input>
                </el-form-item>
                
                <el-form-item>
                  <div class="submit-btn">
                    <el-button type="primary" icon="el-icon-check" @click="judgeSubmit()">提交修改</el-button>
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
                  <div class="tip-text">在下面的输入框中输入题目</div>
                  <el-input 
                    type="textarea" 
                    rows="3" 
                    v-model="postShortAnswer.question" 
                    placeholder="请输入题目内容" 
                    resize="none"
                    class="question-input"
                  >
                  </el-input>
                </el-form-item>
                
                <el-form-item label="参考答案">
                  <div class="tip-text">在下面的输入框中输入参考答案</div>
                  <el-input 
                    type="textarea" 
                    rows="4" 
                    v-model="postShortAnswer.answer" 
                    placeholder="请输入参考答案" 
                    resize="none"
                    class="question-input"
                  >
                  </el-input>
                </el-form-item>
                
                <el-form-item label="解析">
                  <div class="tip-text">在下面的输入框中输入题目解析</div>
                  <el-input 
                    type="textarea" 
                    rows="3" 
                    v-model="postShortAnswer.analysis" 
                    placeholder="请输入答案解析" 
                    resize="none"
                    class="question-input"
                  >
                  </el-input>
                </el-form-item>
                
                <el-form-item>
                  <div class="submit-btn">
                    <el-button type="primary" icon="el-icon-check" @click="shortAnswerSubmit()">提交修改</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </div>
          </section>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
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
      optionValue: "选择题", //题型选中值
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
      type: "选择题",
      questionId: "",
    };
  },
  mounted() {
    this.getQuestion();
  },
  created() {
  },
  methods: {
    getQuestion() {
        // 获取题目
        this.type = this.$route.query.type;
        this.questionId = this.$route.query.questionId;

        this.optionValue = this.type;
        // 根据类型和id获取题目
        this.$axios(
            `/api/answers/${this.type}/${this.questionId}`
        )
        .then((res) => {
            switch(this.type) {
                case "选择题":
                    this.postChange = res.data.data.multiQuestion;
                    break;
                case "判断题":
                    this.postJudge = res.data.data.judgeQuestion;
                    break;
                case "填空题":
                    this.postFill = res.data.data.fillQuestion;
                    console.log(this.postFill)
                    break;
                case "简答题":
                    this.postShortAnswer = res.data.data.shortAnswerQuestion;
                    console.log(this.postShortAnswer);
                    break;
            }
        })
        .catch((error) => {});        
    },
    changeSubmit() {
      //选择题题库提交
      this.postChange.questionId = this.questionId;
      this.$axios({
        //提交数据到选择题题库表
        url: "/api/editMultiQuestion",
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
              message: "已修改题目",
              type: "success",
            });
            this.postChange = {};
          }
        })
        .then(() => {
            this.getQuestion();
        });
    },
    fillSubmit() {
      //填空题提交
      this.postFill.questionId = this.questionId;
      this.$axios({
        url: "/api/editFillQuestion",
        method: "post",
        data: {
          ...this.postFill,
        },
      })
        .then((res) => {
          let status = res.data.code;
          if (status == 200) {
            this.$message({
              message: "已修改题目",
              type: "success",
            });
            this.postFill = {};
          }
        })
        .then(() => {
            this.getQuestion();
        });
    },
    judgeSubmit() {
      //判断题提交
      this.postJudge.questionId = this.questionId;
      this.$axios({
        url: "/api/editJudgeQuestion",
        method: "post",
        data: {
          ...this.postJudge,
        },
      })
        .then((res) => {
          let status = res.data.code;
          if (status == 200) {
            this.$message({
              message: "已修改题目",
              type: "success",
            });
            this.postJudge = {};
          }
        })
        .then(() => {
            this.getQuestion();
        });
    },
    shortAnswerSubmit() {
      //简答题提交
      this.postShortAnswer.questionId = this.questionId;
      
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
        url: "/api/editShortAnswerQuestion",
        method: "post",
        data: {
          ...this.postShortAnswer,
        },
      })
        .then((res) => {
          let status = res.data.code;
          if (status == 200) {
            this.$message({
              message: "已修改题目",
              type: "success",
            });
          } else {
            this.$message({
              message: "修改失败",
              type: "error",
            });
          }
        })
        .then(() => {
            this.getQuestion();
        });
    },
  },
};
</script>

<style lang="less" scoped>
.edit-answer-container {
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
}

@media screen and (max-width: 768px) {
  .edit-answer-container {
    padding: 10px;
    
    .question-section {
      padding: 12px;
    }
    
    .option-form {
      .w150 {
        width: 100%;
      }
    }
  }
}
</style>


