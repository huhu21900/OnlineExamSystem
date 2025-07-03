// 所有学生
<template>
  <div class="all">
    <el-card class="search-card" shadow="hover">
      <div class="card-title">
        <i class="el-icon-search"></i>
        <span>学生信息查询</span>
      </div>
      <el-form :inline="true" class="search-form">
      <el-form-item label="姓名">
          <el-input v-model="condition.name" placeholder="姓名" clearable></el-input>
      </el-form-item>
      <el-form-item label="学院">
          <el-input v-model="condition.institute" placeholder="学院" clearable></el-input>
      </el-form-item>
      <el-form-item label="专业">
          <el-input v-model="condition.major" placeholder="专业" clearable></el-input>
      </el-form-item>
      <el-form-item label="年级">
          <el-input v-model="condition.grade" placeholder="年级" clearable></el-input>
      </el-form-item>
      <el-form-item label="班级">
          <el-input v-model="condition.clazz" placeholder="班级" clearable></el-input>
      </el-form-item>
      <el-form-item label="联系方式">
          <el-input v-model="condition.tel" placeholder="联系方式" clearable></el-input>
      </el-form-item>
      <el-form-item>
          <el-button type="primary" @click="getStudentGrade" icon="el-icon-search">查询</el-button>
          <el-button @click="resetForm" icon="el-icon-refresh">重置</el-button>
      </el-form-item>
    </el-form>
    </el-card>

    <el-card class="table-card" shadow="hover">
      <div class="card-title">
        <i class="el-icon-user"></i>
        <span>学生成绩列表</span>
      </div>

      <el-table 
        :data="pagination.records" 
        border 
        stripe
        highlight-current-row
        class="student-table"
        v-loading="loading"
        element-loading-text="加载中..."
        element-loading-spinner="el-icon-loading">
        <el-table-column fixed="left" prop="studentName" label="姓名" min-width="100">
          <template slot-scope="scope">
            <el-tag size="medium" type="info">{{ scope.row.studentName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="institute" label="学院" min-width="180"></el-table-column>
        <el-table-column prop="major" label="专业" min-width="180"></el-table-column>
        <el-table-column prop="grade" label="年级" min-width="100">
          <template slot-scope="scope">
            <el-tag size="medium" type="success">{{ scope.row.grade }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="clazz" label="班级" min-width="100">
          <template slot-scope="scope">
            <el-tag size="medium" type="warning">{{ scope.row.clazz }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sex" label="性别" min-width="80">
          <template slot-scope="scope">
            <el-tag size="medium" :type="scope.row.sex === '男' ? 'primary' : 'danger'">{{ scope.row.sex }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="tel" label="联系方式" min-width="130"></el-table-column>
        <el-table-column fixed="right" label="操作" min-width="120" align="center">
        <template slot-scope="scope">
            <el-button 
              @click="checkGrade(scope.row.studentId)" 
              type="primary" 
              size="small" 
              icon="el-icon-view">
              查看成绩
            </el-button>
        </template>
      </el-table-column>
    </el-table>

      <div class="pagination-container">
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.current"
          :page-sizes="[6, 10, 20, 30]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
          background
    ></el-pagination>
      </div>
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
      condition: {
        name: "",
        tel: "",
        grade: "",
        clazz: "",
        major: "",
        institute: "",
        campus: "",
        educationSystem: ""
      },
      loading: false // 添加loading状态
    };
  },
  created() {
    this.getStudentGrade();
  },
  methods: {
    getStudentGrade() {
      // 显示加载状态
      this.loading = true;
      
      // 根据条件获取学生成绩
      var name = (this.condition.name.trim() == "" ? "@" : this.condition.name);
      var grade = (this.condition.grade.trim() == "" ? "@" : this.condition.grade);
      var tel = (this.condition.tel.trim() == "" ? "@" : this.condition.tel);
      var institute = (this.condition.institute.trim() == "" ? "@" : this.condition.institute);
      var major = (this.condition.major.trim() == "" ? "@" : this.condition.major);
      var clazz = (this.condition.clazz.trim() == "" ? "@" : this.condition.clazz);
      var campus = (this.condition.campus && this.condition.campus.trim() != "" ? this.condition.campus : "@");
      var educationSystem = (this.condition.educationSystem && this.condition.educationSystem.trim() != "" ? this.condition.educationSystem : "@");
      
      this.$axios(`/api/students/${this.pagination.current}/${this.pagination.size}/${name}/${grade}/${tel}/${institute}/${major}/${clazz}/${campus}/${educationSystem}`)
        .then(res => {
        this.pagination = res.data.data;
        })
        .catch(error => {
        console.error("获取学生成绩失败:", error);
          this.$message.error("获取学生成绩失败，请稍后重试");
        })
        .finally(() => {
          // 无论成功失败都关闭loading
          this.loading = false;
      });
    },
    //重置表单
    resetForm() {
      this.condition = {
        name: "",
        tel: "",
        grade: "",
        clazz: "",
        major: "",
        institute: "",
        campus: "",
        educationSystem: ""
      };
      this.pagination.current = 1;
      this.getStudentGrade();
    },
    //改变当前记录条数
    handleSizeChange(val) {
      this.pagination.size = val;
      this.getStudentGrade();
    },
    //改变当前页码，重新发送请求
    handleCurrentChange(val) {
      this.pagination.current = val;
      this.getStudentGrade();
    },
    checkGrade(studentId) {
      // 确保studentId是字符串类型
      if (typeof studentId === 'number') {
        studentId = studentId.toString();
      }
      this.$router.push({ path: "/grade", query: { studentId: studentId } });
    }
  }
};
</script>
<style lang="less" scoped>
.all {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);

  .search-card, .table-card {
    margin-bottom: 20px;
    border-radius: 8px;
    
    .card-title {
      margin-bottom: 15px;
      display: flex;
      align-items: center;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      
      i {
        margin-right: 8px;
        font-size: 18px;
        color: #409EFF;
      }
    }
  }
  
  .search-form {
    display: flex;
    flex-wrap: wrap;
    
    .el-form-item {
      margin-right: 15px;
      margin-bottom: 15px;
      
      ::v-deep .el-form-item__label {
        font-weight: 500;
      }
    }
  }

  .student-table {
    margin-bottom: 15px;
    border-radius: 4px;
    overflow: hidden;
    
    ::v-deep .el-table__header-wrapper th {
      background-color: #f5f7fa;
      color: #303133;
      font-weight: 600;
      padding: 12px 0;
    }
    
    ::v-deep .el-table__row td {
      padding: 10px 0;
    }
    
    ::v-deep .el-button {
      padding: 7px 15px;
    }
  }
  
  .pagination-container {
    padding: 15px 0 5px;
    display: flex;
    justify-content: center;
  }
}

@media screen and (max-width: 768px) {
  .all {
    padding: 10px;
    
    .search-form {
      .el-form-item {
        margin-right: 8px;
  }
  }
}
}
</style>
