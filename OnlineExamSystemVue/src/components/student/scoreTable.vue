//显示学生成绩
<template>
  <div class="score-container">
    <div class="page-header">
      <i class="el-icon-data-line"></i>
      <span>我的分数</span>
    </div>

    <div class="score-wrapper">
      <div class="score-header">
        <div class="header-title">
          <i class="el-icon-s-data"></i>
          <span>成绩列表</span>
        </div>
        <div class="header-actions">
          <el-tooltip content="刷新数据" placement="top">
            <el-button type="primary" icon="el-icon-refresh" circle size="small" @click="getScore"></el-button>
          </el-tooltip>
        </div>
      </div>

      <div v-if="errorMsg" class="error-container">
        <div class="error-msg">
          <i class="el-icon-warning-outline"></i>
          <span>{{ errorMsg }}</span>
        </div>
      </div>

      <div class="score-content" v-loading="loading">
        <el-table 
          ref="filterTable" 
          :data="score" 
          style="width: 100%"
          border
          :header-cell-style="{background:'#f5f7fa', color:'#606266', fontWeight:'bold'}"
          :cell-style="{verticalAlign: 'middle'}"
          :row-class-name="tableRowClassName">
          <el-table-column
            prop="answerDate"
            label="考试日期"
            sortable
            column-key="answerDate"
            :filters="filter"
            :filter-method="filterHandler">
            <template slot-scope="scope">
              <div class="date-cell">
                <i class="el-icon-date"></i>
                <span>{{ scope.row.answerDate }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="subject"
            label="课程名称"
            width="340"
            filter-placement="bottom-end">
            <template slot-scope="scope">
              <el-tag size="medium" effect="plain" color="#ecf5ff" class="subject-tag">
                <i class="el-icon-reading"></i>
                {{ scope.row.subject }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="etScore" label="考试分数" width="140">
            <template slot-scope="scope">
              <div class="score-value" :class="{'score-fail': scope.row.ptScore != 1, 'score-pass': scope.row.ptScore == 1}">
                {{ scope.row.etScore }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="是否及格" width="140">
            <template slot-scope="scope">
              <el-tag 
                :type="scope.row.ptScore == 1 ? 'success' : 'danger'" 
                effect="dark"
                class="status-tag">
                <i :class="scope.row.ptScore == 1 ? 'el-icon-check' : 'el-icon-close'"></i>
                {{scope.row.ptScore == 1 ? "及格" : "不及格"}}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <div class="empty-data" v-if="score.length === 0 && !loading && !errorMsg">
          <i class="el-icon-document"></i>
          <p>暂无成绩数据</p>
        </div>
      </div>

      <div class="score-pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[4, 6, 8, 10]"
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
  data() {
    return {
      pagination: { //分页后的留言列表
        current: 1, //当前页
        total: null, //记录条数
        size: 10 //每页条数
      },
      loading: false, //加载标识符
      score: [], //学生成绩
      filter: null, //过滤参数
      errorMsg: "" // 错误信息
    }
  },
  created() {
    this.getScore()
    this.loading = true //数据加载则遮罩表格
  },
  methods: {
    getScore() {
      this.loading = true;
      this.errorMsg = "";
      let studentId = this.$cookies.get("cid")
      
      // 记录studentId的类型和值，用于调试
      console.log("学生ID类型:", typeof studentId);
      console.log("学生ID值:", studentId);
      
      // 确保studentId是字符串类型
      if (typeof studentId === 'number') {
        studentId = studentId.toString()
        console.log("转换后的学生ID:", studentId);
      }
      
      if (!studentId) {
        this.errorMsg = "未获取到学生ID，请重新登录";
        this.loading = false;
        return;
      }
      
      this.$axios(`/api/score/${this.pagination.current}/${this.pagination.size}/${studentId}`)
        .then(res => {
          console.log("成绩查询结果:", res.data);
          this.loading = false //数据加载完成去掉遮罩
          
          if(res.data.code == 200) {
            if (!res.data.data || !res.data.data.records || res.data.data.records.length === 0) {
              this.score = [];
              this.pagination.total = 0;
              return;
            }
            
            this.score = res.data.data.records
            this.pagination = {...res.data.data}
            let mapVal = this.score.map((element,index) => { //通过map得到 filter:[{text,value}]形式的数组对象
              let newVal = {}
              newVal.text = element.answerDate
              newVal.value = element.answerDate
              return newVal
            })
            let hash = []
            const newArr = mapVal.reduce((item, next) => { //对新对象进行去重操作
              hash[next.text] ? '' : hash[next.text] = true && item.push(next);
              return item
            }, []);
            this.filter = newArr
          } else {
            this.errorMsg = res.data.message || "获取成绩失败";
            this.score = [];
          }
        })
        .catch(error => {
          console.error("获取成绩出错:", error);
          this.loading = false;
          this.errorMsg = "获取成绩时发生错误，请稍后重试";
          this.score = [];
        });
    },
    //改变当前记录条数
    handleSizeChange(val) {
      this.pagination.size = val
      this.getScore()
    },
    //改变当前页码，重新发送请求
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getScore()
    },
    formatter(row, column) {
      return row.address;
    },
    filterTag(value, row) {
      return row.tag === value;
    },
    filterHandler(value, row, column) {
      const property = column["property"];
      return row[property] === value;
    },
    tableRowClassName({row, rowIndex}) {
      if (row.ptScore != 1) {
        return 'fail-row';
      }
      return '';
    }
  }
};
</script>

<style lang="less" scoped>
.score-container {
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

  .score-wrapper {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
  }

  .score-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0 20px 0;
    border-bottom: 1px solid #EBEEF5;
    
    .header-title {
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
  }

  .error-container {
    padding: 20px 0;
    
    .error-msg {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 15px;
      background-color: #fff2f0;
      border-radius: 4px;
      color: #f56c6c;
      
      i {
        font-size: 20px;
        margin-right: 8px;
      }
    }
  }

  .score-content {
    padding: 20px 0;
    min-height: 400px;
    
    .date-cell {
      display: flex;
      align-items: center;
      height: 40px;
      
      i {
        margin-right: 6px;
        color: #409EFF;
      }
    }
    
    .subject-tag {
      display: flex;
      align-items: center;
      padding: 8px 12px;
      height: 24px;
      line-height: 24px;
      
      i {
        margin-right: 6px;
        color: #409EFF;
      }
    }
    
    .score-value {
      font-weight: bold;
      font-size: 16px;
      height: 40px;
      display: flex;
      align-items: center;
      
      &.score-pass {
        color: #67c23a;
      }
      
      &.score-fail {
        color: #f56c6c;
      }
    }
    
    .status-tag {
      padding: 6px 12px;
      display: inline-flex;
      align-items: center;
      height: 32px;
      line-height: 1;
      
      i {
        margin-right: 5px;
      }
    }
  }

  .empty-data {
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

  .score-pagination {
    padding: 20px 0;
    display: flex;
    justify-content: center;
  }
}

/deep/ .el-table {
  .fail-row {
    background-color: rgba(245, 108, 108, 0.1);
  }
}
</style>
