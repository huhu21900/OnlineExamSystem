// 教师管理页面
<template>
  <div class="all">
    <el-card class="search-card" shadow="hover">
      <div slot="header" class="card-header">
        <i class="el-icon-user"></i>
        <span>教师信息查询</span>
      </div>
      <el-form :inline="true" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.teacherName" placeholder="教师姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="searchForm.institute" placeholder="所属学院" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchTeachers" icon="el-icon-search">查询</el-button>
          <el-button @click="resetSearch" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="hover" style="margin-top: 20px;">
      <div slot="header" class="card-header">
        <i class="el-icon-user-solid"></i>
        <span>教师信息列表</span>
      </div>
      
      <div class="table-container">
        <el-table 
          :data="pagination.records" 
          border 
          stripe
          highlight-current-row
          v-loading="loading"
          element-loading-text="加载中..."
          element-loading-spinner="el-icon-loading"
          :style="{width: '100%'}"
          class="responsive-table">
          <el-table-column prop="teacherId" label="ID" min-width="80">
            <template slot-scope="scope">
              <el-tag size="medium">{{ scope.row.teacherId }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="teacherName" label="姓名" min-width="80"></el-table-column>
          <el-table-column prop="institute" label="学院" min-width="120"></el-table-column>
          <el-table-column prop="sex" label="性别" min-width="60">
            <template slot-scope="scope">
              <el-tag size="small" :type="scope.row.sex === '男' ? 'primary' : 'danger'">{{ scope.row.sex }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="tel" label="联系方式" min-width="120"></el-table-column>
          <el-table-column prop="email" label="邮箱" min-width="150"></el-table-column>
          <el-table-column prop="cardId" label="身份证号" min-width="180"></el-table-column>
          <el-table-column prop="type" label="职称" min-width="80"></el-table-column>
          <el-table-column fixed="right" label="操作" min-width="180" align="center">
            <template slot-scope="scope">
              <div class="operation-buttons">
                <el-button @click="checkGrade(scope.row.teacherId)" type="primary" size="small" icon="el-icon-edit" class="op-button">编辑</el-button>
                <el-button @click="deleteById(scope.row.teacherId)" type="danger" size="small" icon="el-icon-delete" class="op-button">删除</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[6, 10, 20, 50]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          background>
        </el-pagination>
      </div>
    </el-card>
    
    <!-- 编辑对话框-->
    <el-dialog title="编辑教师信息" :visible.sync="dialogVisible" width="40%" :before-close="handleClose">
      <section class="update">
        <el-form ref="form" :model="form" label-width="100px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="姓名">
                <el-input v-model="form.teacherName" placeholder="请输入教师姓名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="学院">
                <el-input v-model="form.institute" placeholder="请输入所属学院"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="性别">
                <el-select v-model="form.sex" placeholder="请选择性别" style="width: 100%">
                  <el-option label="男" value="男"></el-option>
                  <el-option label="女" value="女"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="电话号码">
                <el-input v-model="form.tel" placeholder="请输入电话号码"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="密码">
                <el-input v-model="form.pwd" type="password" placeholder="请输入密码" show-password></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职称">
                <el-input v-model="form.type" placeholder="请输入职称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="身份证号">
                <el-input v-model="form.cardId" placeholder="请输入身份证号"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="邮箱">
                <el-input v-model="form.email" placeholder="请输入邮箱地址"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" icon="el-icon-close"> 取 消</el-button>
        <el-button type="primary" @click="submit()" icon="el-icon-check"> 确 定</el-button>
      </span>
    </el-dialog>
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
        size: 6, //每页条数
      },
      dialogVisible: false, //对话框
      form: {}, //保存点击以后当前试卷的信息
      searchForm: {
        teacherName: "",
        institute: ""
      },
      loading: false,
    };
  },
  created() {
    this.getTeacherInfo();
  },
  methods: {
    getTeacherInfo() {
      //分页查询所有试卷信息
      this.loading = true;
      this.$axios(`/api/teachers/${this.pagination.current}/${this.pagination.size}`)
        .then(res => {
          this.pagination = res.data.data;
        })
        .catch(error => {
          this.$message.error("获取教师信息失败，请稍后重试");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    //搜索教师
    searchTeachers() {
      this.pagination.current = 1;
      this.getTeacherInfo();
    },
    //重置搜索
    resetSearch() {
      this.searchForm = {
        teacherName: "",
        institute: ""
      };
      this.pagination.current = 1;
      this.getTeacherInfo();
    },
    //改变当前记录条数
    handleSizeChange(val) {
      this.pagination.size = val;
      this.getTeacherInfo();
    },
    //改变当前页码，重新发送请求
    handleCurrentChange(val) {
      this.pagination.current = val;
      this.getTeacherInfo();
    },
    checkGrade(teacherId) { //修改教师信息
      this.dialogVisible = true
      this.loading = true;
      this.$axios(`/api/teacher/${teacherId}`)
        .then(res => {
          this.form = res.data.data
        })
        .catch(error => {
          this.$message.error("获取教师信息失败，请稍后重试");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    deleteById(teacherId) { //删除当前教师
      this.$confirm("确定删除当前教师吗？删除后无法恢复", "Warning", {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'danger'
      }).then(() => { //确认删除
        this.loading = true;
        this.$axios({
          url: `/api/teacher/${teacherId}`,
          method: 'delete',
        }).then(res => {
          this.$message.success("删除成功");
          this.getTeacherInfo();
        }).catch(error => {
          this.$message.error("删除失败，请稍后重试");
        }).finally(() => {
          this.loading = false;
        });
      }).catch(() => {
        // 取消删除
      })
    },
    submit() { //提交更改
      // 校验
      if (this.form.teacherName == "") {
        this.$message({
          message: '请输入教师姓名',
          type: 'error'
        })
        return
      }
      if (this.form.institute == "") {
        this.$message({
          message: '请输入学院',
          type: 'error'
        })
        return
      }
      if (this.form.sex == "") {
        this.$message({
          message: '请输入性别',
          type: 'error'
        })
        return
      }
      if (this.form.tel == "") {
        this.$message({
          message: '请输入电话号码',
          type: 'error'
        })
        return
      }
      if(this.form.tel.length > 11) {
        this.$message({
          message: '请输入正确的电话号码',
          type: 'error'
        })
        return
      }
      if (this.form.pwd == "") {
        this.$message({
          message: '请输入密码',
          type: 'error'
        })
        return
      }
      if (this.form.cardId == "") {
        this.$message({
          message: '请输入身份证号码',
          type: 'error'
        })
        return
      }
      if(this.form.cardId.length > 18) {
        this.$message({
          message: '请输入正确的身份证号码',
          type: 'error'
        })
        return
      }
      if (this.form.type == "") {
        this.$message({
          message: '请输入职称',
          type: 'error'
        })
        return
      }
      this.dialogVisible = false
      this.loading = true;
      this.$axios({
        url: '/api/teacher',
        method: 'put',
        data: {
          ...this.form
        }
      }).then(res => {
        console.log(res)
        if (res.data.code == 200) {
          this.$message({
            message: '更新成功',
            type: 'success'
          })
        }
        this.getTeacherInfo()
      }).catch(error => {
        this.$message.error("更新失败，请稍后重试");
      }).finally(() => {
        this.loading = false;
      });
    },
    handleClose(done) { //关闭提醒
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        }).catch(_ => { });
    },
  }
};
</script>
<style lang="less" scoped>
.all {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);

  .card-header {
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

  .search-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .search-form {
      display: flex;
      flex-wrap: wrap;
      margin-top: 10px;
      
      .el-form-item {
        margin-right: 15px;
        margin-bottom: 15px;
      }
    }
  }

  .table-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .table-container {
      margin-bottom: 20px;
      overflow-x: auto;
      
      .el-table {
        border-radius: 4px;
        
        ::v-deep th {
          background-color: #f5f7fa;
          color: #303133;
          font-weight: 600;
          padding: 12px 0;
        }

        ::v-deep .el-table__row td {
          padding: 12px 0;
        }
      }

      .responsive-table {
        min-width: 900px;
      }
    }
    
    .pagination-container {
      padding: 15px 0 5px;
      display: flex;
      justify-content: center;
    }
  }
}

.operation-buttons {
  display: flex;
  justify-content: center;
  flex-wrap: nowrap;
  white-space: nowrap;
  gap: 10px;

  .op-button {
    min-width: 70px;
    margin: 0;
  }
}

/* 覆盖element-ui的默认样式 */
::v-deep .el-table__fixed-right {
  height: 100% !important;
  z-index: 900;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
  right: 0 !important;
}

::v-deep .el-table__fixed-right .el-table__fixed-body-wrapper {
  overflow: visible;
}

::v-deep .el-table .el-table__fixed-right .el-table__body td:last-child {
  padding: 10px 5px;
}

/* 编辑对话框样式 */
::v-deep .el-dialog {
  border-radius: 8px;
}

::v-deep .el-dialog__header {
  padding: 20px;
  border-bottom: 1px solid #EBEEF5;
}

::v-deep .el-dialog__title {
  font-weight: 600;
  color: #303133;
}

::v-deep .el-dialog__body {
  padding: 30px 20px;
}

::v-deep .el-dialog__footer {
  padding: 20px;
  border-top: 1px solid #EBEEF5;
}

/* 移动端适配 */
@media screen and (max-width: 768px) {
  .all {
    padding: 10px;
    
    .search-card {
      .search-form {
        .el-form-item {
          margin-right: 5px;
        }
      }
    }
    
    .operation-buttons {
      flex-direction: column;
      gap: 5px;
      
      .el-button {
        margin: 0;
      }
    }
  }
}
</style>
