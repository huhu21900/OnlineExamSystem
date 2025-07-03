<!-- 添加考试 -->
<template>
    <section class="add-exam-container">
        <el-card class="exam-form-card">
            <div slot="header" class="card-header">
                <h2>添加新考试</h2>
                <p class="subtitle">请填写以下考试信息</p>
            </div>
            <el-form ref="form" :model="form" label-width="120px" :rules="rules" size="medium">
                <el-form-item label="试卷名称" prop="source">
                    <el-input v-model="form.source" placeholder="请输入试卷名称"></el-input>
                </el-form-item>
                <el-form-item label="介绍" prop="description">
                    <el-input v-model="form.description" placeholder="请输入考试介绍"></el-input>
                </el-form-item>
                
                <el-divider content-position="left">院系与专业信息</el-divider>
                
                <el-form-item label="所属学院" prop="institute">
                    <el-input v-model="form.institute" placeholder="请输入所属学院"></el-input>
                </el-form-item>
                <el-form-item label="所属专业" prop="major">
                    <el-input v-model="form.major" placeholder="请输入所属专业"></el-input>
                </el-form-item>
                <el-form-item label="年级" prop="grade">
                    <el-input v-model="form.grade" placeholder="请输入年级"></el-input>
                </el-form-item>
                
                <el-divider content-position="left">考试时间设置</el-divider>
                
                <el-form-item label="考试日期" prop="examDate">
                    <el-date-picker
                        placeholder="选择日期"
                        v-model="form.examDate"
                        style="width: 100%"
                        value-format="yyyy-MM-dd"
                        type="date"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item label="持续时间" prop="totalTime">
                    <el-input-number 
                        v-model="form.totalTime" 
                        :min="1" 
                        :step="5"
                        controls-position="right"
                        style="width: 100%">
                    </el-input-number>
                    <span class="time-unit">分钟</span>
                </el-form-item>
                
                <el-divider content-position="left">其他信息</el-divider>
                
                <el-form-item label="考试类型" prop="type">
                    <el-input v-model="form.type" placeholder="请输入考试类型，如：期中考试、期末考试等"></el-input>
                </el-form-item>
                <el-form-item label="考生提示" prop="tips">
                    <el-input 
                        type="textarea" 
                        v-model="form.tips" 
                        placeholder="请输入考生提示信息"
                        :rows="4">
                    </el-input>
                </el-form-item>
                <el-form-item class="form-buttons">
                    <el-button type="primary" @click="onSubmit()" icon="el-icon-check" :loading="submitting">立即创建</el-button>
                    <el-button type="danger" @click="cancel()" icon="el-icon-close">取消</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </section>
</template>

<script>
export default {
    data() {
        return {
            submitting: false, // 提交状态
            form: {
                //表单数据初始化
                source: null,
                description: null,
                institute: null,
                major: null,
                grade: null,
                examDate: null,
                totalTime: 90,
                totalScore: null,
                type: null,
                tips: null,
                paperId: null,
            },
            rules: {
                source: [{ required: true, message: '请输入试卷名称', trigger: 'blur' }],
                description: [{ required: true, message: '请输入考试介绍', trigger: 'blur' }],
                institute: [{ required: true, message: '请输入所属学院', trigger: 'blur' }],
                major: [{ required: true, message: '请输入所属专业', trigger: 'blur' }],
                grade: [{ required: true, message: '请输入年级', trigger: 'blur' }],
                examDate: [{ required: true, message: '请选择考试日期', trigger: 'change' }],
                totalTime: [{ required: true, message: '请输入考试持续时间', trigger: 'blur' }],
                type: [{ required: true, message: '请输入考试类型', trigger: 'blur' }],
                tips: [{ required: true, message: '请输入考生提示', trigger: 'blur' }],
            }
        };
    },
    methods: {
        formatTime(date) {
            //日期格式化
            let year = date.getFullYear();
            let month =
                date.getMonth() + 1 < 10
                    ? "0" + (date.getMonth() + 1)
                    : date.getMonth() + 1;
            let day =
                date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            let hours =
                date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            let minutes =
                date.getMinutes() < 10
                    ? "0" + date.getMinutes()
                    : date.getMinutes();
            let seconds =
                date.getSeconds() < 10
                    ? "0" + date.getSeconds()
                    : date.getSeconds();
            // 拼接
            return (
                year +
                "-" +
                month +
                "-" +
                day +
                " " +
                hours +
                ":" +
                minutes +
                ":" +
                seconds
            );
        },
        onSubmit() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    this.submitting = true;
                    this.$axios(`/api/examManagePaperId`).then((res) => {
                        console.log(res)
                        if(res.data.data == null || res.data.data.paperId == null) {
                            res.data.data = {
                                paperId : 1000
                            }
                        }
                        this.form.paperId = res.data.data.paperId + 1; //实现paperId自增1
                        this.$axios({
                            url: "/api/exam",
                            method: "post",
                            data: {
                                ...this.form,
                            },
                        }).then((res) => {
                            this.submitting = false;
                            if (res.data.code == 200) {
                                this.$message({
                                    message: "考试创建成功",
                                    type: "success",
                                });
                                this.$router.push({ path: "/selectExam" });
                            } else {
                                this.$message.error(res.data.message || "创建失败，请重试");
                            }
                        }).catch(error => {
                            this.submitting = false;
                            this.$message.error("创建失败：" + (error.message || "未知错误"));
                        });
                    }).catch(error => {
                        this.submitting = false;
                        this.$message.error("获取试卷ID失败：" + (error.message || "未知错误"));
                    });
                } else {
                    this.$message({
                        message: "请完善表单信息",
                        type: "warning"
                    });
                    return false;
                }
            });
        },
        cancel() {
            //取消按钮
            this.$confirm('确定要取消创建考试吗？已填写的内容将不会保存', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '继续编辑',
                type: 'warning'
            }).then(() => {
                this.form = {
                    source: null,
                    description: null,
                    institute: null,
                    major: null,
                    grade: null,
                    examDate: null,
                    totalTime: 90,
                    totalScore: null,
                    type: null,
                    tips: null,
                    paperId: null
                };
                this.$router.push({ path: "/selectExam" });
            }).catch(() => {});
        },
    },
};
</script>
<style lang="less" scoped>
.add-exam-container {
    padding: 20px;
    
    .exam-form-card {
        max-width: 800px;
        margin: 0 auto;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        
        .card-header {
            text-align: center;
            
            h2 {
                margin: 0;
                font-size: 24px;
                color: #409EFF;
            }
            
            .subtitle {
                margin: 10px 0 0;
                font-size: 14px;
                color: #909399;
            }
        }
        
        .el-form {
            padding: 10px 20px;
            
            .el-form-item {
                margin-bottom: 22px;
            }
            
            .el-divider {
                margin: 24px 0;
                
                .el-divider__text {
                    font-size: 16px;
                    font-weight: bold;
                    color: #606266;
                }
            }
            
            .time-unit {
                margin-left: 10px;
                color: #606266;
            }
            
            .form-buttons {
                margin-top: 30px;
                text-align: center;
                
                .el-button {
                    padding: 12px 25px;
                    font-size: 16px;
                }
            }
        }
    }
}
</style>

