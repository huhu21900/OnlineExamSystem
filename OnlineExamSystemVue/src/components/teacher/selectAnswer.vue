//查询所有题库
<template>
  <div class="answer-container">
    <div class="answer-header">
      <div class="header-title">
        <i class="el-icon-collection"></i>
        <h2>题目查询</h2>
      </div>
      <div class="search-form">
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item label="试卷名称">
            <el-input v-model="subject" placeholder="试卷名称" prefix-icon="el-icon-tickets"></el-input>
          </el-form-item>
          <el-form-item label="章节">
            <el-input v-model="section" placeholder="章节" prefix-icon="el-icon-reading"></el-input>
          </el-form-item>
          <el-form-item label="题目内容">
            <el-input v-model="question" placeholder="题目内容" prefix-icon="el-icon-search"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getAnswerInfo" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <el-card shadow="hover" class="answer-card">
      <el-table 
        :data="pagination.records" 
        border 
        stripe
        style="width: 100%"
        :header-cell-style="{background:'#f5f7fa', color:'#606266', fontWeight: 'bold'}"
        :row-class-name="tableRowClassName"
      >
        <el-table-column fixed="left" prop="subject" label="试卷名称" width="150"></el-table-column>
        <el-table-column prop="question" label="题目信息" width="400" show-overflow-tooltip></el-table-column>
        <el-table-column prop="section" label="所属章节" width="120"></el-table-column>
        <el-table-column prop="type" label="题目类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeTag(scope.row.type)" size="mini">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="试题分数" width="80">
          <template slot-scope="scope">
            <el-tag type="success" size="mini">{{ scope.row.score }}分</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="难度等级" width="80">
          <template slot-scope="scope">
            <el-tag :type="getDifficultyTag(scope.row.level)" size="mini">{{ scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="160">
          <template slot-scope="scope">
            <div class="operation-buttons">
              <el-button @click="toEdit(scope.row.type, scope.row.questionId)" type="primary" icon="el-icon-edit" size="mini">编辑</el-button>
              <el-button @click="handleDelete(scope.row.type, scope.row.questionId)" type="danger" icon="el-icon-delete" size="mini">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[6, 10, 20]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        class="page"
        background
        hide-on-single-page
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pagination: {
        //分页后的考试信息
        current: 1, //当前页
        total: null, //记录条数
        size: 6 //每页条数

      },
      section: "",
      subject: "",
      question: "",
    };
  },
  created() {
    this.getAnswerInfo();
  },
  methods: {
    getTypeTag(type) {
      switch(type) {
        case '选择题': return 'primary';
        case '填空题': return 'warning';
        case '判断题': return 'info';
        case '简答题': return '';
        default: return 'info';
      }
    },
    getDifficultyTag(level) {
      switch(level) {
        case 1: return 'info';
        case 2: return '';
        case 3: return 'warning';
        case 4:
        case 5: return 'danger';
        default: return '';
      }
    },
    toEdit(type, id) { 
      // 前往编辑题目
      this.$router.push({path:'/editAnswerChildren',query: {type: type, questionId: id}})
    },
    handleDelete(type, id) {
      // 删除题目
      this.$confirm('确认删除该题目吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 根据题目类型调用不同的删除API
        let url = '';
        switch(type) {
          case '选择题':
            url = `/api/multiQuestion/${id}`;
            break;
          case '填空题':
            url = `/api/fillQuestion/${id}`;
            break;
          case '判断题':
            url = `/api/judgeQuestion/${id}`;
            break;
          case '简答题':
            url = `/api/shortAnswerQuestion/${id}`;
            break;
          default:
            this.$message.error('未知题目类型');
            return;
        }
        
        this.$axios.delete(url)
          .then(res => {
            if (res.data.code === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              // 刷新题目列表
              this.getAnswerInfo();
            } else {
              this.$message.error('删除失败: ' + res.data.message);
            }
          })
          .catch(err => {
            this.$message.error('删除失败: ' + err.message);
          });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });          
      });
    },
    getAnswerInfo(size, current) {
      //分页查询所有试卷信息
      if(typeof size === 'number' && !isNaN(size)) {
        this.pagination.size = size;
      }
      if(typeof current === 'number' && !isNaN(current)) {
        this.pagination.current = current;
      } else {
        this.pagination.current = 1;
      }
      var subject = this.subject;
      if(this.subject.trim() == "") {
        subject = "@";
      }
      var section = this.section;
      if(this.section.trim() == "") {
        section = "@";
      }
      var question = this.question;
      if(this.question.trim() == "") {
        question = "@";
      }
      this.$axios(
        `/api/answers/${this.pagination.current}/${this.pagination.size}/${subject}/${section}/${question}`
      )
        .then(res => {
          this.pagination = res.data.data;
        })
        .catch(error => {});
    },
    //改变当前记录条数
    handleSizeChange(val) {
      this.getAnswerInfo(val);
    },
    //改变当前页码，重新发送请求
    handleCurrentChange(val) {
      this.getAnswerInfo(undefined, val);
    },
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "even-row";
      } else {
        return "odd-row";
      }
    }
  }
};
</script>
<style lang="less" scoped>
.answer-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
  
  .answer-header {
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
    
    .header-title {
      display: flex;
      align-items: center;
      margin-bottom: 20px;
      
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
    
    .search-form {
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      
      .el-form-item {
        margin-bottom: 15px;
        margin-right: 20px;
      }
      
      .el-button {
        padding: 9px 20px;
        font-size: 14px;
      }
    }
  }
  
  .answer-card {
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .page {
      margin-top: 25px;
      display: flex;
      justify-content: center;
      align-items: center;
      padding-bottom: 10px;
    }
    
    .operation-buttons {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 8px;
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
    }
    
    td, th {
      padding: 8px 0;
    }
    
    .cell {
      text-align: center;
    }
  }
}

// 响应式设计
@media screen and (max-width: 768px) {
  .answer-container {
    padding: 10px;
    
    .answer-header {
      .search-form {
        padding: 15px;
        
        .el-form-item {
          margin-right: 10px;
        }
      }
    }
  }
}
</style>
