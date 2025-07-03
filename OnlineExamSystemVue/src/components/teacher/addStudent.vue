<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
-->
<!-- 添加学生 -->
<template>
  <div class="add-student-container">
    <div class="form-card">
      <div class="card-header">
        <h2>添加学生信息</h2>
        <div class="divider"></div>
      </div>
      <el-form ref="form" :model="form" label-width="100px" :rules="rules" class="student-form">
        <div class="form-section">
          <div class="section-title">基本信息</div>
          <div class="form-row">
            <el-form-item label="学号" prop="studentId" required>
              <el-input v-model="form.studentId" placeholder="请输入学号"></el-input>
              <div class="tip-text">请输入学号</div>
            </el-form-item>
            <el-form-item label="姓名" prop="studentName" required>
              <el-input v-model="form.studentName" placeholder="请输入姓名"></el-input>
            </el-form-item>
          </div>
        </div>

        <div class="form-section">
          <div class="section-title">学籍信息</div>
          <div class="form-row">
            <el-form-item label="学院" prop="institute" required>
              <el-input v-model="form.institute" placeholder="请输入学院"></el-input>
            </el-form-item>
            <el-form-item label="所属专业" prop="major" required>
              <el-input v-model="form.major" placeholder="请输入专业"></el-input>
            </el-form-item>
          </div>
          <div class="form-row">
            <el-form-item label="班级" prop="clazz" required>
              <el-input v-model="form.clazz" placeholder="请输入班级"></el-input>
            </el-form-item>
          </div>
          <div class="form-row">
            <el-form-item label="所在校区" prop="campus" required>
              <el-input v-model="form.campus" placeholder="请输入所在校区"></el-input>
            </el-form-item>
            <el-form-item label="学制" prop="educationSystem" required>
              <el-input v-model="form.educationSystem" placeholder="请输入学制"></el-input>
            </el-form-item>
          </div>
        </div>

        <div class="form-section">
          <div class="section-title">账号信息</div>
          <div class="form-row">
            <el-form-item label="密码" prop="pwd" required>
              <el-input v-model="form.pwd" type="password" placeholder="请输入密码" show-password></el-input>
            </el-form-item>
          </div>
        </div>

        <div class="form-actions">
          <el-button type="primary" @click="onSubmit()" size="medium" icon="el-icon-check">立即创建</el-button>
          <el-button type="danger" @click="cancel()" size="medium" icon="el-icon-close">取消</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: { //表单数据初始化
        studentId: null,
        studentName: null,
        major: null,
        clazz: null,
        institute: null,
        pwd: null,
        campus: null,
        educationSystem: null,
        role: 2,
        grade: "2023",
        tel: "",
        email: "",
        cardId: "",
        sex: "男"
      },
      rules: {
        studentId: [
          { required: true, message: '请输入学号', trigger: 'blur' }
        ],
        studentName: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        institute: [
          { required: true, message: '请输入学院', trigger: 'blur' }
        ],
        major: [
          { required: true, message: '请输入专业', trigger: 'blur' }
        ],
        clazz: [
          { required: true, message: '请输入班级', trigger: 'blur' }
        ],
        campus: [
          { required: true, message: '请输入所在校区', trigger: 'blur' }
        ],
        educationSystem: [
          { required: true, message: '请输入学制', trigger: 'blur' }
        ],
        pwd: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度至少为6个字符', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    onSubmit() { //数据提交
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 确保cardId字段有值，如果没有则生成一个基于学号的唯一值
          if (!this.form.cardId) {
            // 使用学号作为基础生成一个简单的身份证号模拟值
            this.form.cardId = '5002' + this.form.studentId + '53';
          }
          
          this.$axios({
            url: '/api/student',
            method: 'post',
            data: {
              ...this.form
            }
          }).then(res => {
            if (res.data.code == 200) {
              this.$message({
                message: '数据添加成功',
                type: 'success'
              })
              this.$router.push({ path: '/studentManage' })
            } else {
              this.$message({
                message: '添加失败，请检查学号是否已存在',
                type: 'error'
              })
            }
          }).catch(error => {
            this.$message({
              message: '添加失败，请检查输入信息',
              type: 'error'
            })
            console.error('添加失败：', error);
          })
        } else {
          this.$message({
            message: '请完善表单信息',
            type: 'warning'
          })
          return false;
        }
      });
    },
    cancel() { //取消按钮
      this.$confirm('确定取消添加学生信息吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs.form.resetFields();
        this.$router.push({ path: '/studentManage' });
      }).catch(() => {});
    }
  }
};
</script>
<style lang="less" scoped>
.add-student-container {
  padding: 20px;
  display: flex;
  justify-content: center;
  min-height: calc(100vh - 120px);
}

.form-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 800px;
  padding: 20px 30px 30px;
}

.card-header {
  margin-bottom: 20px;
  
  h2 {
    font-size: 22px;
    color: #303133;
    margin: 0 0 15px 0;
    font-weight: 500;
  }
  
  .divider {
    height: 2px;
    background: linear-gradient(to right, #409EFF, #a0cfff);
    margin-bottom: 10px;
  }
}

.form-section {
  margin-bottom: 25px;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 15px;
  
  &:last-child {
    border-bottom: none;
  }
  
  .section-title {
    font-size: 16px;
    color: #409EFF;
    font-weight: 500;
    margin-bottom: 15px;
    padding-left: 10px;
    border-left: 3px solid #409EFF;
  }
}

.form-row {
  display: flex;
  margin-bottom: 5px;
  
  .el-form-item {
    flex: 1;
    margin-right: 20px;
    
    &:last-child {
      margin-right: 0;
    }
  }
}

.tip-text {
  font-size: 12px;
  color: #909399;
  line-height: 1;
  padding-top: 4px;
}

.form-actions {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  
  .el-button {
    padding: 12px 25px;
    font-size: 14px;
    margin: 0 15px;
  }
}

/deep/ .el-form-item__label {
  font-weight: 500;
}

/deep/ .el-input__inner {
  border-radius: 4px;
}

/deep/ .el-form-item.is-required:not(.is-no-asterisk) .el-form-item__label-wrap>.el-form-item__label:before,
/deep/ .el-form-item.is-required:not(.is-no-asterisk)>.el-form-item__label:before {
  color: #f56c6c;
}
</style>

