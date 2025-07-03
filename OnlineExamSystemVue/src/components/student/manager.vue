<!--管理中心-->
<template>
  <div class="manager-bg">
    <div class="manager-card">
      <div class="manager-title">
        <i class="el-icon-lock"></i>
        修改你的密码
      </div>
    <el-form :model="ruleForm2" status-icon :rules="rules2" ref="ruleForm2" label-width="100px" class="demo-ruleForm">
        <el-form-item label="密码" prop="pass" class="pass">
          <el-input type="password" v-model="ruleForm2.pass" autocomplete="off" prefix-icon="el-icon-lock"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm2.checkPass" autocomplete="off" prefix-icon="el-icon-lock"></el-input>
      </el-form-item>
      <el-form-item>
          <div class="btn-group">
            <el-button type="primary" @click="submitForm('ruleForm2')" class="submit-btn">提交</el-button>
            <el-button @click="resetForm('ruleForm2')" class="reset-btn">重置</el-button>
          </div>
      </el-form-item>
    </el-form>
      <div class="tips">
        <i class="el-icon-info"></i> 请牢记新密码，密码应包含字母和数字，长度不少于6位。
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm2.checkPass !== '') {
            this.$refs.ruleForm2.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm2.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        ispass: true,
        ruleForm2: {
          pass: '',
          checkPass: ''
        },
        rules2: {
          pass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let studentId = this.$cookies.get("cid")
            this.$axios({ //修改密码
              url: '/api/studentPWD',
              method: 'put',
              data: {
                pwd: this.ruleForm2.pass,
                studentId
              }
            }).then(res => {
              if(res.data != null ) { //修改成功提示
                this.$message({
                  message: '密码修改成功...',
                  type: 'success'
                })
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<style scoped>
.manager-bg {
  min-height: 80vh;
  background: linear-gradient(135deg, #f5f7fa 60%, #e3f0ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.manager-card {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 32px 0 rgba(64, 158, 255, 0.10);
  padding: 48px 40px 32px 40px;
  width: 400px;
  margin: 60px 0 120px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.manager-title {
  font-size: 26px;
  font-weight: 700;
  color: #409EFF;
  margin-bottom: 32px;
  display: flex;
  align-items: center;
  gap: 10px;
}
.demo-ruleForm {
  width: 100%;
  margin-bottom: 10px;
}
.pass label {
  color: #f56c6c;
  font-size: 18px;
}
.el-form-item {
  margin-bottom: 28px;
}
.el-input__inner {
  border-radius: 8px;
  font-size: 16px;
  padding-left: 36px !important;
}
.submit-btn {
  width: 120px;
  font-size: 18px;
  border-radius: 24px;
}
.reset-btn {
  width: 100px;
  font-size: 18px;
  border-radius: 24px;
  margin-left: 16px;
}
.tips {
  margin-top: 18px;
  color: #909399;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
}
.btn-group {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 18px;
}
@media (max-width: 500px) {
  .manager-card {
    width: 98vw;
    padding: 24px 4vw 18px 4vw;
  }
  .manager-title {
  font-size: 20px;
}
  .btn-group {
    flex-direction: column;
    gap: 12px;
    width: 100%;
  }
  .submit-btn, .reset-btn {
    width: 100%;
    margin-left: 0 !important;
}
}
</style>