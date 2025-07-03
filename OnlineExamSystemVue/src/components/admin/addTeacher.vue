<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
-->
<!-- 添加教师 -->
<template>
  <div class="add-container">
    <el-card class="form-card" shadow="hover">
      <div slot="header" class="card-header">
        <i class="el-icon-user-solid"></i>
        <span>添加新教师</span>
      </div>
      
      <el-form ref="form" :model="form" label-width="100px" :rules="rules" class="teacher-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="teacherName">
              <el-input v-model="form.teacherName" placeholder="请输入教师姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学院" prop="institute">
              <el-input v-model="form.institute" placeholder="请输入所属学院"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话号码" prop="tel">
              <el-input v-model="form.tel" placeholder="请输入电话号码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="密码" prop="pwd">
              <el-input v-model="form.pwd" type="password" placeholder="请输入密码" show-password></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职称" prop="type">
              <el-input v-model="form.type" placeholder="请输入职称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="身份证号" prop="cardId">
              <el-input v-model="form.cardId" placeholder="请输入身份证号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="电子邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入电子邮箱"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item class="form-buttons">
          <el-button type="primary" @click="onSubmit()" icon="el-icon-check"> 立即创建</el-button>
          <el-button type="danger" @click="cancel()" icon="el-icon-close"> 取消</el-button>
        </el-form-item>
      </el-form>
      
      <div class="form-tips">
        <el-alert
          title="提示：添加教师后，教师可以使用手机号码作为账号登录系统"
          type="info"
          :closable="false"
          show-icon>
        </el-alert>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: { //表单数据初始化
        teacherName: "",
        institute: "",
        tel: "",
        email: "",
        pwd: "",
        cardId: "",
        sex: "",
        type: "",
        role: 2
      },
      rules: {
        teacherName: [
          { required: true, message: '请输入教师姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        institute: [
          { required: true, message: '请输入学院', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        tel: [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的电话号码', trigger: 'blur' }
        ],
        pwd: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度至少为6个字符', trigger: 'blur' }
        ],
        cardId: [
          { required: true, message: '请输入身份证号码', trigger: 'blur' },
          { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请输入职称', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      },
      loading: false
    };
  },
  methods: {
    onSubmit() { //数据提交
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true;
          this.$axios({
            url: '/api/teacher',
            method: 'post',
            data: {
              ...this.form
            }
          }).then(res => {
            if(res.data.code == 200) {
              this.$message({
                message: '教师添加成功',
                type: 'success'
              });
              this.$router.push({path: '/teacherManage'});
            }
          }).catch(error => {
            this.$message.error('添加失败，请稍后重试');
          }).finally(() => {
            this.loading = false;
          });
        } else {
          this.$message.error('请正确填写所有必填项');
          return false;
        }
      });
    },
    cancel() { //取消按钮
      this.$confirm('确认取消添加教师？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$router.push({path: '/teacherManage'});
      }).catch(() => {
        // 取消操作
      });
    }
  }
};
</script>
<style lang="less" scoped>
.add-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
  
  .form-card {
    max-width: 900px;
    margin: 0 auto;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
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
    
    .teacher-form {
      margin-top: 20px;
      
      .el-form-item {
        margin-bottom: 22px;
        
        ::v-deep .el-form-item__label {
          font-weight: 500;
        }
      }
      
      .form-buttons {
        display: flex;
        justify-content: center;
        margin-top: 30px;
        
        .el-button {
          padding: 12px 25px;
          font-size: 14px;
          margin: 0 15px;
        }
      }
    }
    
    .form-tips {
      margin-top: 20px;
    }
  }
}

/* 移动端适配 */
@media screen and (max-width: 768px) {
  .add-container {
    padding: 10px;
    
    .form-card {
      .teacher-form {
        .form-buttons {
          .el-button {
            padding: 10px 15px;
            margin: 0 5px;
          }
        }
      }
    }
  }
}
</style>

