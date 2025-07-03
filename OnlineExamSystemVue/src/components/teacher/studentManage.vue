// 学生管理页面
<template>
  <div class="all">
    <el-card class="search-card" shadow="hover">
      <div slot="header" class="card-header">
        <i class="el-icon-user"></i>
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
        <el-form-item label="所在校区">
          <el-input v-model="condition.campus" placeholder="所在校区" clearable></el-input>
        </el-form-item>
        <el-form-item label="学制">
          <el-input v-model="condition.educationSystem" placeholder="学制" clearable></el-input>
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

    <el-card class="table-card" shadow="hover" style="margin-top: 20px;">
      <div slot="header" class="card-header">
        <i class="el-icon-user-solid"></i>
        <span>学生信息列表</span>
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
          <el-table-column prop="studentId" label="学号" min-width="100">
            <template slot-scope="scope">
              <el-tag size="medium">{{ scope.row.studentId }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="studentName" label="姓名" min-width="90"></el-table-column>
          <el-table-column prop="institute" label="学院" min-width="110"></el-table-column>
          <el-table-column prop="major" label="专业" min-width="120"></el-table-column>
          <el-table-column prop="grade" label="年级" min-width="70">
            <template slot-scope="scope">
              <el-tag size="small" type="success">{{ scope.row.grade }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="clazz" label="班级" min-width="70">
            <template slot-scope="scope">
              <el-tag size="small" type="warning">{{ scope.row.clazz }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="campus" label="所在校区" min-width="100"></el-table-column>
          <el-table-column prop="educationSystem" label="学制" min-width="70"></el-table-column>
          <el-table-column prop="sex" label="性别" min-width="70">
            <template slot-scope="scope">
              <el-tag size="small" :type="scope.row.sex === '男' ? 'primary' : 'danger'">{{ scope.row.sex }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="tel" label="联系方式" min-width="110"></el-table-column>
          <el-table-column fixed="right" label="操作" min-width="220" align="center">
            <template slot-scope="scope">
              <div class="operation-buttons">
                <el-button @click="checkGrade(scope.row.studentId)" type="primary" size="small" icon="el-icon-edit" class="op-button">编辑</el-button>
                <el-button @click="deleteById(scope.row.studentId)" type="danger" size="small" icon="el-icon-delete" class="op-button">删除</el-button>
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
          :page-sizes="[4, 10, 20, 50]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          background>
        </el-pagination>
      </div>
    </el-card>

    <!-- 编辑对话框-->
    <el-dialog
      title="编辑学生信息"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <section class="update">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="学号">
            <el-input v-model="form.studentId" disabled></el-input>
            <div class="tip-text">学号不可修改</div>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="form.studentName"></el-input>
          </el-form-item>
          <el-form-item label="学院">
            <el-input v-model="form.institute"></el-input>
          </el-form-item>
          <el-form-item label="专业">
            <el-input v-model="form.major"></el-input>
          </el-form-item>
          <el-form-item label="年级">
            <el-input v-model="form.grade"></el-input>
          </el-form-item>
          <el-form-item label="班级">
            <el-input v-model="form.clazz"></el-input>
          </el-form-item>
          <el-form-item label="所在校区">
            <el-input v-model="form.campus"></el-input>
          </el-form-item>
          <el-form-item label="学制">
            <el-input v-model="form.educationSystem"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.sex" placeholder="请选择性别">
              <el-option label="男" value="男"></el-option>
              <el-option label="女" value="女"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="电话号码">
            <el-input v-model="form.tel"></el-input>
          </el-form-item>
        </el-form>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit()">确 定</el-button>
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
        size: 4, //每页条数
      },
      dialogVisible: false, //对话框
      form: {}, //保存点击以后当前试卷的信息,
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
      loading: false
    };
  },
  created() {
    this.getStudentGrade();
  },
  methods: {
    getStudentGrade() {
      this.loading = true;
      // 根据条件获取学生成绩
      var name = (this.condition.name.trim() == "" ? "@" : this.condition.name);
      var grade = (this.condition.grade.trim() == "" ? "@" : this.condition.grade);
      var tel = (this.condition.tel.trim() == "" ? "@" : this.condition.tel);
      var institute = (this.condition.institute.trim() == "" ? "@" : this.condition.institute);
      var major = (this.condition.major.trim() == "" ? "@" : this.condition.major);
      var clazz = (this.condition.clazz.trim() == "" ? "@" : this.condition.clazz);
      var campus = (this.condition.campus.trim() == "" ? "@" : this.condition.campus);
      var educationSystem = (this.condition.educationSystem.trim() == "" ? "@" : this.condition.educationSystem);

      this.$axios(`/api/students/${this.pagination.current}/${this.pagination.size}/${name}/${grade}/${tel}/${institute}/${major}/${clazz}/${campus}/${educationSystem}`)
        .then(res => {
          this.pagination = res.data.data;
          console.log(this.pagination)
        })
        .catch(error => {
          this.$message.error('获取学生信息失败，请稍后重试');
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 重置表单
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
    checkGrade(studentId) { //修改学生信息
      this.dialogVisible = true
      this.$axios(`/api/student/${studentId}`)
        .then(res => {
          this.form = res.data.data
        })
        .catch(error => {
          this.$message.error('获取学生详情失败，请稍后重试');
        });
    },
    deleteById(studentId) { //删除当前学生
      var _this = this;
      this.$confirm("确定删除当前学生吗？删除后无法恢复","Warning",{
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'danger'
      }).then(()=> { //确认删除
        this.$axios({
          url: `/api/student/${studentId}`,
          method: 'delete',
        })
        .then(res => {
          this.$message.success('删除成功');
          this.getStudentGrade()
        })
        .catch(error => {
          this.$message.error('删除失败，请稍后重试');
        });
      }).catch(() => {
        // 取消删除操作
      })
    },
    submit() { //提交更改
      this.dialogVisible = false
      this.$axios({
        url: '/api/student',
        method: 'put',
        data: {
          ...this.form
        }
      }).then(res => {
        if(res.data.code ==200) {
          this.$message({
            message: '更新成功',
            type: 'success'
          })
        }
        this.getStudentGrade()
      }).catch(error => {
        this.$message.error('更新失败，请稍后重试');
      });
    },
    handleClose(done) { //关闭提醒
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        }).catch(_ => {});
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
        margin-right: 10px;
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
          padding: 10px 0;
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

  .tip-text {
    font-size: 12px;
    color: #909399;
    line-height: 1;
    padding-top: 4px;
  }
}

.operation-buttons {
  display: flex;
  justify-content: center;
  flex-wrap: nowrap;
  white-space: nowrap;
  gap: 10px;

  .op-button {
    min-width: 80px;
    margin: 0;
  }
}

/* 覆盖element-ui的默认样式 */
::v-deep .el-table__fixed-right {
  height: 100% !important;
  z-index: 900; /* 提高层级确保按钮不被遮挡 */
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
  right: 0 !important;
}

::v-deep .el-table__fixed-right .el-table__fixed-body-wrapper {
  overflow: visible;
}

::v-deep .el-table .el-table__fixed-right .el-table__body td:last-child {
  padding: 10px 5px;
}

::v-deep .el-button+.el-button {
  margin-left: 0;
}

/* 移动设备适配 */
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
