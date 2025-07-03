<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
-->
//获取试卷并跳转到添加题库
<template>
  <div class="exam">
    <!-- 添加新增题库按钮 -->
    <div class="operation-bar">
      <div class="search-area">
        <el-input
          placeholder="请输入试卷名称关键词"
          v-model="searchKeyword"
          clearable
          class="search-input">
          <el-select v-model="searchType" slot="prepend" placeholder="请选择" style="width: 120px;">
            <el-option label="试卷名称" value="source"></el-option>
            <el-option label="所属学院" value="institute"></el-option>
            <el-option label="所属专业" value="major"></el-option>
            <el-option label="试卷类型" value="type"></el-option>
          </el-select>
          <el-button slot="append" icon="el-icon-search" @click="searchExams"></el-button>
        </el-input>
        <el-button type="info" plain size="small" @click="resetSearch" style="margin-left: 10px;">重置</el-button>
      </div>
    </div>
    
    <el-table :data="pagination.records" border>
      <el-table-column fixed="left" prop="source" label="试卷名称" width="150"></el-table-column>
      <el-table-column prop="description" label="介绍" width="150"></el-table-column>
      <el-table-column prop="institute" label="所属学院" width="120"></el-table-column>
      <el-table-column prop="major" label="所属专业" width="120"></el-table-column>
      <el-table-column prop="grade" label="年级" width="80"></el-table-column>
      <el-table-column prop="examDate" label="考试日期" width="100"></el-table-column>
      <el-table-column prop="totalTime" label="持续时间" width="80"></el-table-column>
      <!-- <el-table-column prop="totalScore" label="总分" width="120"></el-table-column> -->
      <el-table-column prop="type" label="试卷类型" width="100"></el-table-column>
      <el-table-column prop="tips" label="考生提示" min-width="200"></el-table-column>
      <el-table-column fixed="right" label="操作" width="180">
        <template slot-scope="scope">
          <div class="operation-buttons">
            <el-button @click="add(scope.row.paperId,scope.row.source)" type="primary" size="mini" icon="el-icon-edit-outline">录入</el-button>
            <el-button @click="deleteExam(scope.row.examCode)" type="danger" size="mini" icon="el-icon-delete">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.current"
      :page-sizes="[4, 8, 10]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total" class="page">
    </el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {}, //保存点击以后当前试卷的信息
      pagination: { //分页后的考试信息
        current: 1, //当前页
        total: null, //记录条数
        size: 4 //每页条数
      },
      searchKeyword: '', // 搜索关键词
      searchType: 'source', // 搜索类型，默认为试卷名称
    }
  },
  created() {
    this.getExamInfo()
  },
  methods: {
    // 搜索题库
    searchExams() {
      this.pagination.current = 1 // 重置为第一页
      this.getExamInfo()
    },
    
    // 重置搜索
    resetSearch() {
      this.searchKeyword = ''
      this.searchType = 'source'
      this.pagination.current = 1
      this.getExamInfo()
    },
    
    getExamInfo() { //分页查询所有试卷信息
      let url = `/api/exams/${this.pagination.current}/${this.pagination.size}`
      
      // 如果有搜索关键词，使用搜索API端点
      if (this.searchKeyword && this.searchKeyword.trim() !== '') {
        url = `/api/exams/search/${this.pagination.current}/${this.pagination.size}?keyword=${encodeURIComponent(this.searchKeyword)}&type=${this.searchType}`
      }
      
      this.$axios(url).then(res => {
        this.pagination = res.data.data
      }).catch(error => {
        this.$message.error('获取试卷信息失败')
      })
    },
    //改变当前记录条数
    handleSizeChange(val) {
      this.pagination.size = val
      this.getExamInfo()
    },
    //改变当前页码，重新发送请求
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getExamInfo()
    },
    add(paperId,source) { //增加题库
      this.$router.push({path:'/addAnswerChildren',query: {paperId: paperId,subject:source}})
    },
    // 删除题库
    deleteExam(examCode) {
      this.$confirm('确认删除该题库吗？删除后将无法恢复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios({
          url: `/api/exam/${examCode}`,
          method: 'delete'
        }).then(res => {
          if (res.data.code === 200) {
            this.$message.success('删除题库成功')
            this.getExamInfo() // 刷新列表
          } else {
            this.$message.error(res.data.message || '删除题库失败')
          }
        }).catch(error => {
          console.error('删除题库错误:', error)
          let errorMsg = '删除题库失败'
          
          if (error.response && error.response.data && error.response.data.message) {
            errorMsg = `删除题库失败: ${error.response.data.message}`
          }
          
          this.$message.error(errorMsg)
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }
  },
};
</script>
<style lang="less" scoped>
.exam {
  padding: 0px 40px;
  .operation-bar {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    
    .search-area {
      display: flex;
      align-items: center;
      width: 100%;
      
      .search-input {
        width: 100%;
        max-width: 500px;
      }
    }
  }
  .page {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .edit{
    margin-left: 20px;
  }
  .operation-buttons {
    display: flex;
    justify-content: center;
    align-items: center;
    white-space: nowrap;
    .el-button {
      margin: 0 3px;
      padding: 5px 8px;
      font-size: 12px;
      &:last-child {
        margin-right: 0;
      }
      &:first-child {
        margin-left: 0;
      }
    }
  }
}
</style>
