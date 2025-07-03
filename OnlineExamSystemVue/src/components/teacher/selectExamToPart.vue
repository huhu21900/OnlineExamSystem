<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
-->
//查询所有考试跳转到分段页面
<template>
  <div class="exam">
    <el-card class="exam-card" shadow="hover">
      <div slot="header" class="card-header">
        <div class="header-left">
          <i class="el-icon-s-data"></i>
          <span>考试成绩分段分析</span>
        </div>
        <div class="header-right">
          <el-input
            placeholder="搜索试卷名称或描述"
            prefix-icon="el-icon-search"
            v-model="searchText"
            clearable
            @clear="getExamInfo"
            @keyup.enter.native="handleSearch"
            style="width: 250px;"
          ></el-input>
          <el-button type="primary" size="small" @click="handleSearch" icon="el-icon-search">搜索</el-button>
        </div>
      </div>

      <div class="table-container">
        <el-table 
          :data="filteredExams" 
          border 
          stripe
          highlight-current-row
          style="width: 100%"
          v-loading="loading"
          element-loading-text="加载中..."
          element-loading-spinner="el-icon-loading">
          <el-table-column fixed="left" prop="examCode" label="试卷ID" min-width="100">
            <template slot-scope="scope">
              <el-tag size="medium">{{ scope.row.examCode }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="source" label="试卷名称" min-width="160">
            <template slot-scope="scope">
              <span class="highlight-text">{{ scope.row.source }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="介绍" min-width="160" show-overflow-tooltip></el-table-column>
          <el-table-column prop="institute" label="所属学院" min-width="120"></el-table-column>
          <el-table-column prop="major" label="所属专业" min-width="140"></el-table-column>
          <el-table-column prop="grade" label="年级" min-width="80">
            <template slot-scope="scope">
              <el-tag size="small" type="success">{{ scope.row.grade }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="examDate" label="考试日期" min-width="120">
            <template slot-scope="scope">
              <i class="el-icon-date"></i>
              {{ scope.row.examDate }}
            </template>
          </el-table-column>
          <el-table-column prop="totalTime" label="持续时间" min-width="100">
            <template slot-scope="scope">
              <i class="el-icon-time"></i>
              {{ scope.row.totalTime }}分钟
            </template>
          </el-table-column>
          <el-table-column prop="totalScore" label="总分" min-width="80">
            <template slot-scope="scope">
              <el-tag size="small" type="warning">{{ scope.row.totalScore }}分</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="试卷类型" min-width="110"></el-table-column>
          <el-table-column prop="tips" label="考生提示" min-width="180" show-overflow-tooltip></el-table-column>
          <el-table-column fixed="right" label="操作" width="140" align="center">
        <template slot-scope="scope">
              <el-button 
                @click="toPart(scope.row.examCode, scope.row.source)" 
                type="primary" 
                size="small" 
                icon="el-icon-pie-chart">
                查看分段
              </el-button>
        </template>
      </el-table-column>
    </el-table>
      </div>

      <div class="pagination-container">
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.current"
          :page-sizes="[4, 8, 10, 20]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          background>
    </el-pagination>
      </div>
    </el-card>
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
      dialogVisible: false,
      loading: false,
      searchText: '',
      examList: []  // 保存原始数据
    }
  },
  computed: {
    filteredExams() {
      if (!this.searchText) {
        return this.pagination.records || [];
      }
      
      const searchTerm = this.searchText.toLowerCase();
      return this.pagination.records.filter(exam => 
        exam.source.toLowerCase().includes(searchTerm) || 
        (exam.description && exam.description.toLowerCase().includes(searchTerm))
      );
    }
  },
  created() {
    this.getExamInfo()
  },
  methods: {
    getExamInfo() { //分页查询所有试卷信息
      this.loading = true;
      this.$axios(`/api/exams/${this.pagination.current}/${this.pagination.size}`)
        .then(res => {
          this.pagination = res.data.data;
          this.examList = res.data.data.records || [];
        })
        .catch(error => {
          console.error('获取考试信息失败:', error);
          this.$message.error('获取考试信息失败，请稍后重试');
        })
        .finally(() => {
          this.loading = false;
        })
    },
    // 搜索处理
    handleSearch() {
      this.getExamInfo();
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
    toPart(examCode, source) { //跳转到分段charts页面
      this.$router.push({path: '/scorePart', query:{examCode: examCode, source: source}})
    }
  },
};
</script>
<style lang="less" scoped>
.exam {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
  
  .exam-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .header-left {
        display: flex;
        align-items: center;
        
        i {
          margin-right: 8px;
          font-size: 18px;
          color: #409EFF;
        }
        
        span {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }
      
      .header-right {
        display: flex;
        gap: 10px;
      }
    }
    
    .table-container {
      margin-bottom: 20px;
      overflow-x: auto; /* 添加水平滚动 */
      
      ::v-deep .el-table {
        border-radius: 4px;
        
        th {
          background-color: #f5f7fa;
          padding: 12px 0;
          
          .cell {
            font-weight: 600;
            color: #303133;
          }
        }
        
        td {
          padding: 10px 0;
        }
        
        /* 修复操作列被覆盖的问题 */
        .el-table__fixed-right {
          height: 100% !important;
          z-index: 3; /* 增加z-index确保不被覆盖 */
        }
        
        .el-button {
          padding: 8px 15px;
          font-size: 12px;
        }
      }
      
      .highlight-text {
        color: #409EFF;
        font-weight: 500;
      }
    }
    
    .pagination-container {
      padding: 15px 0 5px;
    display: flex;
    justify-content: center;
    }
  }
}

@media screen and (max-width: 768px) {
  .exam {
    padding: 10px;
    
    .exam-card {
      .card-header {
        flex-direction: column;
        align-items: flex-start;
        
        .header-right {
          margin-top: 10px;
          width: 100%;
        }
      }
      
      .table-container {
        overflow-x: auto;
        
        ::v-deep .el-table__fixed-right {
          box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
        }
      }
    }
  }
}
</style>
