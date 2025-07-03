//查询所有考试
<template>
  <div class="exam-container">
    <div class="exam-header">
      <div class="header-title">
        <i class="el-icon-s-management"></i>
        <h2>考试查询</h2>
      </div>
      <div class="search-bar">
        <el-input
          placeholder="输入试卷名称搜索"
          prefix-icon="el-icon-search"
          v-model="searchKeyword"
          clearable
          @clear="getExamInfo"
          @keyup.enter.native="searchExam"
          size="small"
        ></el-input>
        <el-button type="primary" icon="el-icon-search" size="small" @click="searchExam">搜索</el-button>
      </div>
    </div>

    <el-card shadow="hover" class="exam-card">
      <el-table 
        :data="pagination.records" 
        border 
        stripe
        style="width: 100%"
        :header-cell-style="{background:'#f5f7fa', color:'#606266', fontWeight: 'bold'}"
        :row-class-name="tableRowClassName"
        :show-overflow-tooltip="true"
        :scrollbar-always-on="false"
      >
      <el-table-column
        fixed="left"
        prop="source"
        label="试卷名称"
          width="160"
      ></el-table-column>
      <el-table-column
        prop="description"
        label="介绍"
          width="180"
          show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="institute"
        label="所属学院"
          width="110"
      ></el-table-column>
      <el-table-column
        prop="major"
        label="所属专业"
          width="120"
          show-overflow-tooltip
      ></el-table-column>
        <el-table-column prop="grade" label="年级" width="80"></el-table-column>
      <el-table-column
        prop="examDate"
        label="考试日期"
          width="110"
      ></el-table-column>
      <el-table-column
        prop="totalTime"
        label="持续时间"
          width="90"
        >
          <template slot-scope="scope">
            <el-tag size="mini" type="info">{{ scope.row.totalTime }}分钟</el-tag>
          </template>
        </el-table-column>
      <el-table-column
        prop="totalScore"
        label="总分"
          width="70"
        >
          <template slot-scope="scope">
            <el-tag size="mini" type="success">{{ scope.row.totalScore }}分</el-tag>
          </template>
        </el-table-column>
      <el-table-column
        prop="type"
        label="试卷类型"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag size="mini" :type="getExamTypeTag(scope.row.type)">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
      <el-table-column
        prop="tips"
        label="考生提示"
          min-width="140"
          show-overflow-tooltip
      ></el-table-column>
        <el-table-column fixed="right" label="操作" width="280">
        <template slot-scope="scope">
            <div class="operation-buttons">
              <el-button type="success" size="mini" icon="el-icon-view" @click="getExamDetail(scope.row.examCode, scope.row.paperId)">详情</el-button>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="edit(scope.row.examCode)">编辑</el-button>
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteRecord(scope.row.examCode)">删除</el-button>
            </div>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.current"
        :page-sizes="[4, 8, 10, 20]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      class="page"
        background
        hide-on-single-page
    >
    </el-pagination>
    </el-card>
    
    <!-- 编辑对话框-->
    <el-dialog
      title="编辑试卷信息"
      :visible.sync="dialogVisible"
      width="40%"
      :before-close="handleClose"
      center
      custom-class="custom-dialog"
    >
      <section class="update-form">
        <el-form ref="form" :model="form" label-width="110px" :rules="rules">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="试卷名称" prop="source">
                <el-input v-model="form.source" placeholder="请输入试卷名称"></el-input>
          </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属学院" prop="institute">
                <el-input v-model="form.institute" placeholder="请输入所属学院"></el-input>
          </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="所属专业" prop="major">
                <el-input v-model="form.major" placeholder="请输入所属专业"></el-input>
          </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="年级" prop="grade">
                <el-input v-model="form.grade" placeholder="请输入年级"></el-input>
          </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="考试日期" prop="examDate">
              <el-date-picker
                type="date"
                placeholder="选择日期"
                v-model="form.examDate"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="持续时间" prop="totalTime">
                <el-input v-model="form.totalTime" type="number" oninput="if(value<=0)value=''" :min="1">
                  <template slot="append">分钟</template>
                </el-input>
          </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="试卷类型" prop="type">
                <el-select v-model="form.type" placeholder="请选择试卷类型" style="width: 100%">
                  <el-option 
                    v-for="item in examTypeOptions" 
                    :key="item.value" 
                    :label="item.label" 
                    :value="item.value">
                  </el-option>
                </el-select>
          </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="介绍" prop="description">
                <el-input v-model="form.description" placeholder="请输入介绍"></el-input>
          </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="考生提示" prop="tips">
            <el-input type="textarea" v-model="form.tips" placeholder="请输入考生提示" :rows="4"></el-input>
          </el-form-item>
        </el-form>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit()">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 查询试卷对话框 -->
    <el-dialog title="试卷内容" :visible.sync="showExamDetail" width="60%" custom-class="detail-dialog">
      <span>
        <div class="content">
          <div class="exam-info">
            <div class="exam-title">
              <h3>{{ examData.source }}</h3>
              <div class="exam-meta">
                <span><i class="el-icon-time"></i> {{ examData.totalTime }}分钟</span>
                <span><i class="el-icon-medal"></i> {{ score[0] + score[1] + score[2] + score[3] }}分</span>
                <span><i class="el-icon-date"></i> {{ examData.examDate }}</span>
              </div>
            </div>
          </div>
          <el-divider content-position="center">试题内容</el-divider>
          <el-collapse v-model="activeName" accordion>
            <el-collapse-item class="question-section" name="1">
              <template slot="title">
                <div class="section-title">
                  <i class="el-icon-s-opportunity"></i>
                  <span>选择题 (共{{ topicCount[0] }}题 共计{{ score[0] }}分)</span>
                </div>
              </template>
              <div class="question-list">
                <div class="question-item" v-for="(list, index) in topic[1]" :key="index">
                  <div class="question-header">
                    <span class="question-index">{{ index + 1 }}</span>
                    <span class="question-content">{{ list.question }}</span>
                    <span class="question-score">{{ list.score }}分</span>
                  </div>
                  <div class="question-options">
                    <div class="option">A. {{ list.answerA }}</div>
                    <div class="option">B. {{ list.answerB }}</div>
                    <div class="option">C. {{ list.answerC }}</div>
                    <div class="option">D. {{ list.answerD }}</div>
                    </div>
                  <div class="question-actions">
                    <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteQuestion(1, list.questionId)">删除</el-button>
                  </div>
                </div>
                  </div>
                </el-collapse-item>
            
            <el-collapse-item class="question-section" name="2">
                  <template slot="title">
                <div class="section-title">
                  <i class="el-icon-edit-outline"></i>
                  <span>填空题 (共{{ topicCount[1] }}题 共计{{ score[1] }}分)</span>
                    </div>
                  </template>
              <div class="question-list">
                <div class="question-item" v-for="(list, index) in topic[2]" :key="index">
                  <div class="question-header">
                    <span class="question-index">{{ topicCount[0] + index + 1 }}</span>
                    <span class="question-content">{{ list.question }}</span>
                    <span class="question-score">{{ list.score }}分</span>
                  </div>
                  <div class="question-actions">
                    <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteQuestion(2, list.questionId)">删除</el-button>
                  </div>
                </div>
                  </div>
                </el-collapse-item>
            
            <el-collapse-item class="question-section" name="3">
                  <template slot="title">
                <div class="section-title">
                  <i class="el-icon-help"></i>
                  <span>判断题 (共{{ topicCount[2] }}题 共计{{ score[2] }}分)</span>
                    </div>
                  </template>
              <div class="question-list">
                <div class="question-item" v-for="(list, index) in topic[3]" :key="index">
                  <div class="question-header">
                    <span class="question-index">{{ topicCount[0] + topicCount[1] + index + 1 }}</span>
                    <span class="question-content">{{ list.question }}</span>
                    <span class="question-score">{{ list.score }}分</span>
                  </div>
                  <div class="question-actions">
                    <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteQuestion(3, list.questionId)">删除</el-button>
                  </div>
                </div>
                  </div>
                </el-collapse-item>
            
            <el-collapse-item class="question-section" name="4">
                  <template slot="title">
                <div class="section-title">
                  <i class="el-icon-tickets"></i>
                  <span>简答题 (共{{ topicCount[3] || 0 }}题 共计{{ score[3] || 0 }}分)</span>
                    </div>
                  </template>
              <div class="question-list">
                <div class="question-item" v-for="(list, index) in topic[4]" :key="index">
                  <div class="question-header">
                    <span class="question-index">{{ topicCount[0] + topicCount[1] + topicCount[2] + index + 1 }}</span>
                    <span class="question-content">{{ list.question }}</span>
                    <span class="question-score">{{ list.score }}分</span>
                  </div>
                  <div class="question-actions">
                    <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteQuestion(4, list.questionId)">删除</el-button>
                  </div>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showExamDetail = false">关闭</el-button>
        <el-button type="primary" @click="showExamDetail = false">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: "",
      form: {}, //保存点击以后当前试卷的信息
      pagination: {
        //分页后的考试信息
        current: 1, //当前页
        total: null, //记录条数
        size: 4, //每页条数
      },
      dialogVisible: false,
      showExamDetail: false, //是否展示试卷
      activeName: '1',  //默认打开选择题
      topicCount: [],//每种类型题目的总数
      score: [],  //每种类型分数的总数
      examData: { //考试信息
        // source: null,
        // totalScore: null,
      },
      topic: {  //试卷信息

      },
      examTypeOptions: [
        { label: '期末考试', value: '期末考试', type: 'primary' },
        { label: '期中考试', value: '期中考试', type: 'success' },
        { label: '模拟考试', value: '模拟考试', type: 'info' },
        { label: '单元测试', value: '单元测试', type: 'warning' }
      ],
      examDetailCode: "",   // 当前查看试题详情中，考试编码code
      examDetailPaperId: "",    // 当前查看的试题详情中，试卷id
      rules: {
        source: [{ required: true, message: '请输入试卷名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入介绍', trigger: 'blur' }],
        institute: [{ required: true, message: '请输入所属学院', trigger: 'blur' }],
        major: [{ required: true, message: '请输入所属专业', trigger: 'blur' }],
        grade: [{ required: true, message: '请输入年级', trigger: 'blur' }],
        examDate: [{ required: true, message: '请选择考试日期', trigger: 'change' }],
        totalTime: [{ required: true, message: '请输入持续时间', trigger: 'blur' }],
        type: [{ required: true, message: '请选择试卷类型', trigger: 'change' }],
        tips: [{ required: true, message: '请输入考生提示', trigger: 'blur' }]
      }
    };
  },
  created() {
    this.getExamInfo();
  },
  methods: {
    tableRowClassName({row, rowIndex}) {
      if (rowIndex % 2 === 0) {
        return 'even-row';
      } else {
        return 'odd-row';
      }
    },
    getExamTypeTag(type) {
      const typeItem = this.examTypeOptions.find(item => item.value === type);
      return typeItem ? typeItem.type : 'info';
    },
    searchExam() {
      // 添加搜索功能
      this.pagination.current = 1;
      this.getExamInfo();
    },
    deleteQuestion(type, questionId) {
      // type: 1选择 2填空 3判断 4简答
      var paperId = this.examDetailPaperId;
      this.$confirm("确认从试卷中移除该试题？", "提示", {
        confirmButtonText: "确认删除",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then((_) => {
          this.$axios(`/api/paper/delete/${paperId}/${type}/${questionId}`).then(res => {  //通过examCode请求试卷详细信息
            this.getExamDetail(this.examDetailCode, paperId);
          })  
        })
        .catch((_) => {});
    },
    getExamDetail(examCode, paperId) {
      this.examDetailCode = examCode;
      this.examDetailPaperId = paperId;
      this.topicCount = [];
      this.score = [];
      this.examData = {},
      this.topic = {},

      this.showExamDetail = true;

      this.$axios(`/api/exam/${examCode}`).then(res => {  //通过examCode请求试卷详细信息
        res.data.data.examDate = res.data.data.examDate.substr(0,10)
        this.examData = { ...res.data.data}
        let paperId = this.examData.paperId
        this.$axios(`/api/paper/${paperId}`).then(res => {  //通过paperId获取试题题目信息
          this.topic = {...res.data}
          let keys = Object.keys(this.topic) //对象转数组
          keys.forEach(e => {
            let data = this.topic[e]
            this.topicCount.push(data.length)
            let currentScore = 0
            for(let i = 0; i< data.length; i++) { //循环每种题型,计算出总分
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
    edit(examCode) {
      //编辑试卷
      this.dialogVisible = true;
      this.$axios(`/api/exam/${examCode}`).then((res) => {
        //根据试卷id请求后台
        if (res.data.code == 200) {
          this.form = res.data.data;
        }
      });
    },
    handleClose(done) {
      //关闭提醒
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
    submit() {
      //提交修改后的试卷信息
      this.$refs.form.validate((valid) => {
        if (valid) {
      this.dialogVisible = false;
      
      this.$axios({
        url: "/api/exam",
        method: "put",
        data: {
          ...this.form,
        },
      }).then((res) => {
        if (res.data.code == 200) {
          this.$message({
            //成功修改提示
            message: "更新成功",
            type: "success",
          });
        }
        this.getExamInfo();
          });
        } else {
          return false;
        }
      });
    },
    deleteRecord(examCode) {
      this.$confirm("确定删除该记录吗,该操作不可逆！！！", "删除提示", {
        confirmButtonText: "确定删除",
        cancelButtonText: "取消",
        type: "danger",
      })
        .then(() => {
          //确认删除
          this.$axios({
            url: `/api/exam/${examCode}`,
            method: "delete",
          }).then((res) => {
            this.getExamInfo();
          });
        })
        .catch(() => {});
    },
    getExamInfo() {
      //分页查询所有试卷信息
      let url = `/api/exams/${this.pagination.current}/${this.pagination.size}`;
      if (this.searchKeyword) {
        url += `?keyword=${this.searchKeyword}`;
      }
      this.$axios(url)
        .then((res) => {
          this.pagination = res.data.data;
        })
        .catch((error) => {});
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
  },
};
</script>
<style lang="less" scoped>
.exam-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
  
  .exam-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    .header-title {
      display: flex;
      align-items: center;
      
      i {
        margin-right: 10px;
        font-size: 24px;
        color: #409EFF;
      }
      
      h2 {
        margin: 0;
        font-size: 22px;
        color: #303133;
        font-weight: 600;
      }
    }
    
    .search-bar {
      display: flex;
      width: 360px;
      
      .el-input {
        margin-right: 10px;
        
        ::v-deep .el-input__inner {
          border-radius: 18px;
          height: 36px;
        }
      }
      
      .el-button {
        border-radius: 18px;
        padding: 9px 20px;
      }
    }
  }
  
  .exam-card {
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
  .page {
      margin-top: 25px;
      display: flex;
      justify-content: center;
      align-items: center;
      padding-bottom: 10px;
      
      ::v-deep {
        .el-pagination {
          font-weight: normal;
          
          .btn-prev, .btn-next {
            background-color: #f4f4f5;
            border-radius: 2px;
            
            &:hover {
              color: #409EFF;
            }
          }
          
          .el-pager li {
            background-color: #f4f4f5;
            border-radius: 2px;
            
            &.active {
              background-color: #409EFF;
            }
            
            &:hover {
              color: #409EFF;
            }
          }
          
          .el-pagination__sizes {
            .el-select .el-input .el-input__inner {
              border-radius: 4px;
            }
          }
        }
      }
    }
  }
  
  .operation-buttons {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
    flex-wrap: nowrap;
    
    .el-button {
      padding: 5px 8px;
      margin: 0;
      font-size: 12px;
    }
  }
  
  .custom-dialog {
    .update-form {
      padding: 10px;
      
      .el-form-item {
        margin-bottom: 18px;
      }
    }
    
    .dialog-footer {
      text-align: right;
      
      .el-button {
        padding: 10px 20px;
        font-size: 14px;
      }
    }
  }
  
  .detail-dialog {
    .content {
      padding: 0 10px;
      
      .exam-info {
        margin-bottom: 20px;
        
        .exam-title {
          text-align: center;
          
          h3 {
            font-size: 22px;
            color: #303133;
            margin-bottom: 10px;
          }
          
          .exam-meta {
            display: flex;
            justify-content: center;
            gap: 30px;
            
            span {
              display: flex;
              align-items: center;
              font-size: 14px;
              color: #606266;
              
              i {
                margin-right: 5px;
                font-size: 16px;
              }
            }
          }
        }
      }
      
      .question-section {
        margin-bottom: 15px;
        
        .section-title {
          display: flex;
          align-items: center;
          font-size: 16px;
          font-weight: 500;
          
          i {
            margin-right: 8px;
            font-size: 18px;
          }
        }
        
        .question-list {
          padding: 0 20px;
          
          .question-item {
            padding: 15px;
            border-bottom: 1px solid #ebeef5;
            
            &:last-child {
              border-bottom: none;
            }
            
            .question-header {
              display: flex;
              align-items: flex-start;
              margin-bottom: 10px;
              
              .question-index {
                width: 25px;
                height: 25px;
                display: flex;
                justify-content: center;
                align-items: center;
                background-color: #409EFF;
                color: white;
                border-radius: 50%;
                margin-right: 10px;
                font-size: 14px;
                flex-shrink: 0;
              }
              
              .question-content {
                flex: 1;
                font-size: 15px;
                line-height: 1.5;
              }
              
              .question-score {
                margin-left: 15px;
                color: #F56C6C;
                font-weight: bold;
                flex-shrink: 0;
              }
            }
            
            .question-options {
              padding-left: 35px;
              margin-bottom: 10px;
              
              .option {
                margin-bottom: 8px;
                font-size: 14px;
                color: #606266;
              }
            }
            
            .question-actions {
              display: flex;
              justify-content: flex-end;
            }
          }
        }
      }
    }
  }
  
  // 表格样式
  ::v-deep .el-table {
    .even-row {
      background-color: #fafafa;
    }
    
    .odd-row {
      background-color: #ffffff;
    }
    
    th {
      background-color: #f5f7fa !important;
      font-size: 14px;
    }
    
    td, th {
      padding: 8px 0;
      height: 50px;
    }
    
    .el-button--mini {
      padding: 7px 10px;
      font-size: 12px;
    }
    
    .cell {
      text-align: center;
    }
    
    .el-tag {
      display: inline-block;
    }
    
    // 隐藏表格滚动条
    .el-scrollbar__wrap {
      overflow-x: hidden;
    }
    
    .el-scrollbar__bar.is-horizontal {
      display: none !important;
    }
  }
  
  // 折叠面板样式
  ::v-deep .el-collapse {
    border: none;
    
    .el-collapse-item__header {
      padding: 15px;
      background-color: #f5f7fa;
      border-radius: 4px;
      margin-bottom: 10px;
      border: none;
    }
    
    .el-collapse-item__wrap {
      border: none;
    }
    
    .el-collapse-item__content {
      padding: 10px 0;
    }
  }
}

// 响应式设计
@media screen and (max-width: 768px) {
  .exam-container {
    padding: 10px;
    
    .exam-header {
      flex-direction: column;
      align-items: flex-start;
      
      .header-title {
        margin-bottom: 15px;
      }
      
      .search-bar {
        width: 100%;
      }
    }
  }
}
</style>
