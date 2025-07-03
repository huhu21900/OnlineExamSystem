<!--学生考试首页-->

<template>
    <div id="student">
        <div class="header-container">
            <div class="header-content">
                <div class="left-section">
                    <div class="logo-container">
                        <img src="../../assets/img/exam-logo.svg" class="logo-img" />
                        <div class="logo-text">在线考试系统</div>
                    </div>
                    
                    <div class="nav-links">
                        <div class="nav-item" :class="{ active: activeMenu === 'exam' }" @click="exam()">
                            <i class="el-icon-reading"></i>
                            <span>考试中心</span>
                        </div>
                        <div class="nav-item" :class="{ active: activeMenu === 'practice' }" @click="practice()">
                            <i class="el-icon-edit-outline"></i>
                            <span>试卷练习</span>
                        </div>
                        <div class="nav-item" :class="{ active: activeMenu === 'score' }">
                            <router-link to="/scoreTable">
                                <i class="el-icon-data-line"></i>
                                <span>我的分数</span>
                            </router-link>
                        </div>
                        <div class="nav-item" :class="{ active: activeMenu === 'message' }">
                            <router-link to="/message">
                                <i class="el-icon-chat-dot-round"></i>
                                <span>交流区</span>
                            </router-link>
                        </div>
                    </div>
                </div>

                <div class="user-info" @mouseenter="flag = true" @mouseleave="flag = false">
                    <el-avatar shape="circle" :size="40" :src="require('@/assets/img/avatar.jpg')"></el-avatar>
                    <span class="username">{{ user.userName }}</span>
                    <i class="el-icon-caret-bottom"></i>
                    <div class="dropdown-menu" v-show="flag">
                        <div class="dropdown-item" @click="manage()">
                            <i class="el-icon-lock"></i>
                            <span>修改密码</span>
                        </div>
                        <div class="dropdown-item exit" @click="exit()">
                            <i class="el-icon-switch-button"></i>
                            <span>退出登录</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--路由区域-->
        <div class="main-container">
            <router-view></router-view>
        </div>
        
        <v-footer></v-footer>
    </div>
</template>

<script>
import myFooter from "@/components/student/myFooter";
import { mapState } from "vuex";
export default {
    components: {
        "v-footer": myFooter,
    },
    data() {
        return {
            flag: false,
            user: {},
            activeMenu: 'exam'
        };
    },
    created() {
        this.userInfo();
        // 根据当前路由路径设置激活菜单项
        const path = this.$route.path;
        if (path.includes('scoreTable')) {
            this.activeMenu = 'score';
        } else if (path.includes('message')) {
            this.activeMenu = 'message';
        } else if (this.$store.state.isPractice) {
            this.activeMenu = 'practice';
        } else {
            this.activeMenu = 'exam';
        }
    },
    methods: {
        exit() {
            //退出登录
            this.$router.push({ path: "/" }); //跳转到登录页面
            this.$cookies.remove("cname"); //清除cookie
            this.$cookies.remove("cid");
            this.$cookies.remove("rb_token"); //清除cookie
            this.$cookies.remove("rb_role");
        },
        manage() {
            //跳转到修改密码页面
            this.$router.push({ path: "/manager" });
        },
        userInfo() {
            let studentName = this.$cookies.get("cname");
            let studentId = this.$cookies.get("cid");
            console.log(`studentId${studentId}`);
            console.log(`studentName ${studentName}`);
            this.user.userName = studentName;
            this.user.studentId = studentId;
        },
        practice() {
            //跳转练习模式
            this.activeMenu = 'practice';
            let isPractice = true;
            this.$store.commit("practice", isPractice);
            this.$router.push({ path: "/startExam" });
        },
        exam() {
            //跳转考试模式
            this.activeMenu = 'exam';
            let isPractice = false;
            this.$store.commit("practice", isPractice);
            this.$router.push({ path: "/student" });
        },
    },
    computed: mapState(["isPractice"]),
    watch: {
        $route(to) {
            const path = to.path;
            if (path.includes('scoreTable')) {
                this.activeMenu = 'score';
            } else if (path.includes('message')) {
                this.activeMenu = 'message';
            } else if (this.$store.state.isPractice) {
                this.activeMenu = 'practice';
            } else {
                this.activeMenu = 'exam';
            }
        }
    }
};
</script>

<style scoped>
#student {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    background-color: #f5f7fa;
}

.header-container {
    background-color: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 0;
}

.header-content {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 20px;
    height: 64px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.left-section {
    display: flex;
    align-items: center;
}

.logo-container {
    display: flex;
    align-items: center;
    margin-right: 40px;
}

.logo-img {
    width: 36px;
    height: 36px;
    margin-right: 10px;
}

.logo-text {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
}

.nav-links {
    display: flex;
    height: 64px;
}

.nav-item {
    position: relative;
    padding: 0 20px;
    height: 64px;
    line-height: 64px;
    cursor: pointer;
    transition: all 0.3s;
    color: #606266;
}

.nav-item a {
    display: flex;
    height: 100%;
    align-items: center;
    color: inherit;
    text-decoration: none;
}

.nav-item i {
    font-size: 18px;
    margin-right: 5px;
}

.nav-item:hover {
    color: #409EFF;
    background-color: rgba(64, 158, 255, 0.1);
}

.nav-item.active {
    color: #409EFF;
    font-weight: 500;
}

.nav-item.active::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 20px;
    right: 20px;
    height: 2px;
    background-color: #409EFF;
}

.user-info {
    position: relative;
    display: flex;
    align-items: center;
    cursor: pointer;
    padding: 0 15px;
    height: 64px;
}

.username {
    margin: 0 8px;
    font-size: 15px;
    color: #303133;
}

.dropdown-menu {
    position: absolute;
    top: 64px;
    right: 0;
    width: 130px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    z-index: 100;
}

.dropdown-item {
    display: flex;
    align-items: center;
    padding: 12px 15px;
    font-size: 14px;
    color: #606266;
    cursor: pointer;
    transition: all 0.3s;
}

.dropdown-item:hover {
    background-color: #f5f7fa;
    color: #409EFF;
}

.dropdown-item i {
    font-size: 16px;
    margin-right: 8px;
}

.dropdown-item.exit {
    border-top: 1px solid #EBEEF5;
}

.dropdown-item.exit:hover {
    color: #F56C6C;
}

.main-container {
    flex: 1;
    padding: 20px 0;
}
</style>
