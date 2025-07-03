<!-- 顶部信息栏 -->
<template>
    <header id="topbar">
        <el-dialog
            :append-to-body="true"
            style="z-index: 2028px"
            title="修改密码"
            :visible.sync="dialogVisible"
            width="30%"
        >
            <el-form
                status-icon
                ref="ruleForm2"
                label-width="100px"
                class="demo-ruleForm"
            >
                <el-form-item label="旧密码" prop="pass">
                    <el-input
                        type="password"
                        v-model="oldPsw"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="pass">
                    <el-input
                        type="password"
                        v-model="newPsw"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="确认新密码" prop="checkPass">
                    <el-input
                        type="password"
                        v-model="confirmNewPsw"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false"> 取 消</el-button>
                <el-button type="primary" @click="resetPsw"> 确 定</el-button>
            </span>
        </el-dialog>

        <el-row>
            <el-col :span="4" class="topbar-left">
                <div class="logo-container">
                    <img src="@/assets/img/exam-logo.svg" alt="系统Logo" class="system-logo">
                <span class="system-title">在线考试系统</span>
                </div>
            </el-col>
            <el-col :span="20" class="topbar-right">
                <div class="toggle-container">
                    <i @click="toggle()" class="el-icon-s-fold toggle-icon" :title="flag ? '展开菜单' : '收起菜单'"></i>
                </div>
                
                <div class="user-container">
                    <el-dropdown trigger="click" @command="handleCommand">
                        <span class="user-wrapper">
                            <span class="user-role">{{ getRoleName() }}</span>
                            <span class="user-name">{{ user.userName }}</span>
                            <el-avatar :size="40" class="user-avatar">
                                <img src="@/assets/img/R-C.jpg" alt="用户头像">
                            </el-avatar>
                            <i class="el-icon-caret-bottom dropdown-icon"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown" class="user-dropdown">
                            <el-dropdown-item command="password">
                                <i class="el-icon-key"></i> 修改密码
                            </el-dropdown-item>
                            <el-dropdown-item command="logout" divided>
                                <i class="el-icon-switch-button"></i> 退出登录
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </el-col>
        </el-row>
    </header>
</template>

<script>
import { mapState, mapMutations } from "vuex";
export default {
    data() {
        return {
            login_flag: false,
            user: {
                //用户信息
                userName: null,
                userId: null,
            },
            dialogVisible: false,
            oldPsw: "",
            newPsw: "",
            confirmNewPsw: "",
            role: 0,
        };
    },
    created() {
        this.getUserInfo();
        this.role = this.$cookies.get("role");
    },
    computed: mapState(["flag", "menu"]),
    methods: {
        // 获取角色名称
        getRoleName() {
            let role = this.$cookies.get("role");
            if (role == 0) {
                return "超级管理员";
            } else if (role == 1) {
                return "教师";
            } else if (role == 2) {
                return "学生";
            }
            return "";
        },
        // 处理下拉菜单命令
        handleCommand(command) {
            if (command === 'password') {
                this.dialogVisible = true;
            } else if (command === 'logout') {
                this.exit();
            }
        },
        // 管理员重置密码
        resetPsw() {
            if (this.oldPsw == "") {
                this.$message.error("请输入旧密码");
                return;
            }
            if (this.newPsw == "") {
                this.$message.error("请输入新密码");
                return;
            }
            if (this.confirmNewPsw != this.newPsw) {
                this.$message.error("两次新密码不一致");
                return;
            }
            this.$axios(
                `/api/admin/resetPsw/${this.user.userId}/${this.oldPsw}/${this.newPsw}`
            ).then((res) => {
                let status = res.data.code;
                if (status == 200) {
                    if (res.data.data != true) {
                        this.$message.error(res.data.data);
                    } else {
                        // 修改成功
                        this.$message.success("密码修改成功");
                        this.dialogVisible = false;
                        this.oldPsw = "";
                        this.newPsw = "";
                        this.confirmNewPsw = "";
                    }
                }
            }).catch(error => {
                this.$message.error("密码修改失败，请稍后重试");
            });
        },
        //左侧栏放大缩小
        ...mapMutations(["toggle"]),
        getUserInfo() {
            //获取用户信息
            let userName = this.$cookies.get("cname");
            let userId = this.$cookies.get("cid");
            this.user.userName = userName;
            this.user.userId = userId;
        },
        index() {
            this.$router.push({ path: "/index" });
        },
        exit() {
            let role = this.$cookies.get("role");
            this.$router.push({ path: "/" }); //跳转到登录页面
            this.$cookies.remove("cname"); //清除cookie
            this.$cookies.remove("cid");
            this.$cookies.remove("role");
            this.$cookies.remove("rb_token"); //清除cookie
            this.$cookies.remove("rb_role");
            if (role == 0) {
                this.menu.pop();
            }
        },
    },
};
</script>

<style scoped>
/* 顶部导航栏样式 */
#topbar {
    position: relative;
    z-index: 10;
    background: linear-gradient(90deg, #1976d2, #42a5f5);
    height: 80px;
    color: #fff;
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
}

/* 左侧区域 */
#topbar .topbar-left {
    height: 80px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    overflow: hidden;
    padding-left: 20px;
}

.logo-container {
    display: flex;
    align-items: center;
    cursor: pointer;
    padding: 0 15px 0 0;
    height: 100%;
}

.system-logo {
    width: 48px;
    height: 48px;
    margin-right: 10px;
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
    transition: transform 0.3s;
}

.logo-container:hover .system-logo {
    transform: scale(1.1);
}

.system-title {
    font-size: 22px;
    font-weight: 600;
    color: white;
    letter-spacing: 1px;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* 右侧区域 */
.topbar-right {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding: 0 20px;
    height: 80px;
}

/* 折叠图标区域 */
.toggle-container {
    display: flex;
    align-items: center;
    position: absolute;
    left: 240px;
    top: 50%;
    transform: translateY(-50%);
}

.toggle-icon {
    font-size: 24px;
    cursor: pointer;
    transition: all 0.3s;
    color: #fff;
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    padding: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toggle-icon:hover {
    background-color: rgba(255, 255, 255, 0.2);
    transform: scale(1.1);
}

/* 用户信息区域 */
.user-container {
    display: flex;
    align-items: center;
    margin-left: auto;
}

.user-wrapper {
    display: flex;
    align-items: center;
    padding: 5px 10px;
    border-radius: 24px;
    background-color: rgba(255, 255, 255, 0.1);
    transition: all 0.3s;
    cursor: pointer;
}

.user-wrapper:hover {
    background-color: rgba(255, 255, 255, 0.2);
}

.user-role {
    background-color: rgba(255, 255, 255, 0.2);
    color: white;
    padding: 2px 8px;
    border-radius: 12px;
    font-size: 12px;
    margin-right: 8px;
    letter-spacing: 1px;
}

.user-name {
    margin-right: 10px;
    font-size: 14px;
    font-weight: 500;
    color: #ffffff;
}

.user-avatar {
    margin: 0 5px;
    border: 2px solid rgba(255, 255, 255, 0.8);
    background-color: #fff;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
    transition: all 0.3s;
}

.user-wrapper:hover .user-avatar {
    transform: scale(1.05);
    border-color: #ffffff;
}

.dropdown-icon {
    color: rgba(255, 255, 255, 0.8);
    font-size: 12px;
    margin-left: 5px;
    transition: transform 0.3s;
}

.user-wrapper:hover .dropdown-icon {
    transform: rotate(180deg);
}

/* 下拉菜单样式 */
.user-dropdown {
    min-width: 150px;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

::v-deep .el-dropdown-menu__item {
    display: flex;
    align-items: center;
    padding: 10px 20px;
    font-size: 14px;
}

::v-deep .el-dropdown-menu__item i {
    margin-right: 10px;
    font-size: 16px;
    color: #409EFF;
}

::v-deep .el-dropdown-menu__item:hover {
    background-color: #f5f7fa;
}

/* 动画效果 */
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.5s;
}

.fade-enter, 
.fade-leave-to {
    opacity: 0;
}
</style>
