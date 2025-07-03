<!--左边下拉导航栏-->
<template>
  <div id="left">
    <el-menu
      active-text-color="#ffffff"
      text-color="#e0f2ff"
      :default-active="this.$route.path"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      :collapse="flag"
      background-color="#1e62b3"
      menu-trigger="click" 
      router
      :collapse-transition="true">
      
      <!-- 首页作为一级菜单直接点击跳转 -->
      <el-menu-item index="/index/" @click="handleTitle('0')">
        <i class="el-icon-s-home menu-icon"></i>
        <span slot="title" class="title">首页</span>
      </el-menu-item>
      
      <!-- 考试管理菜单 -->
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-s-management menu-icon"></i>
          <span class="title">考试管理</span>
        </template>
        <el-menu-item index="/selectExam" @click="handleTitle('1')">
          <i class="el-icon-search"></i>
          <span>考试查询</span>
        </el-menu-item>
        <el-menu-item index="/addExam" @click="handleTitle('1')">
          <i class="el-icon-plus"></i>
          <span>添加考试</span>
        </el-menu-item>
      </el-submenu>
      
      <!-- 题库管理菜单 -->
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-s-order menu-icon"></i>
          <span class="title">题库管理</span>
        </template>
        <el-menu-item index="/selectAnswer" @click="handleTitle('2')">
          <i class="el-icon-document"></i>
          <span>题目列表</span>
        </el-menu-item>
        <el-menu-item index="/addAnswer" @click="handleTitle('2')">
          <i class="el-icon-edit"></i>
          <span>新增题目</span>
        </el-menu-item>
      </el-submenu>
      
      <!-- 成绩查询菜单 -->
      <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-data-analysis menu-icon"></i>
          <span class="title">成绩查询</span>
        </template>
        <el-menu-item index="/allStudentsGrade" @click="handleTitle('3')">
          <i class="el-icon-s-data"></i>
          <span>学生成绩查询</span>
        </el-menu-item>
        <el-menu-item index="/selectExamToPart" @click="handleTitle('3')">
          <i class="el-icon-s-marketing"></i>
          <span>成绩分段查询</span>
        </el-menu-item>
      </el-submenu>
      
      <!-- 学生管理菜单 -->
      <el-submenu index="4">
        <template slot="title">
          <i class="el-icon-user menu-icon"></i>
          <span class="title">学生管理</span>
        </template>
        <el-menu-item index="/studentManage" @click="handleTitle('4')">
          <i class="el-icon-user"></i>
          <span>学生管理</span>
        </el-menu-item>
        <el-menu-item index="/addStudent" @click="handleTitle('4')">
          <i class="el-icon-plus"></i>
          <span>添加学生</span>
        </el-menu-item>
      </el-submenu>
      
      <!-- 教师管理菜单 (仅管理员可见) -->
      <el-submenu v-if="isAdmin" index="5">
        <template slot="title">
          <i class="el-icon-s-custom menu-icon"></i>
          <span class="title">教师管理</span>
        </template>
        <el-menu-item index="/teacherManage" @click="handleTitle('5')">
          <i class="el-icon-s-custom"></i>
          <span>教师管理</span>
        </el-menu-item>
        <el-menu-item index="/addTeacher" @click="handleTitle('5')">
          <i class="el-icon-plus"></i>
          <span>添加教师</span>
        </el-menu-item>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: "mainLeft",
  data() {
    return {
      isAdmin: false
    }
  },
  computed: {
    ...mapState(["flag","menu"])
  },
  created() {
    this.checkRole()
  },
  methods: {
    handleOpen(key, keyPath) {
      // console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      // console.log(key, keyPath);
    },
    //点击标题传递参数给navigator组件
    handleTitle(index) {
      this.bus.$emit('sendIndex', index)
    },
    checkRole() {
      let role = this.$cookies.get("role")
      if(role == 0) {
        this.isAdmin = true
      }
    }
  },
}
</script>

<style>
/* 主菜单样式 */
.el-menu-vertical-demo .el-submenu__title {
  overflow: hidden;
  height: 56px;
  line-height: 56px;
}

.menu-icon {
  font-size: 18px;
  margin-right: 10px;
  color: #e0f2ff !important;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  min-height: 100%;
  height: 100%;
  width: 220px;
}

#left {
  height: 100%;
  min-height: 100vh;
  background-color: #1e62b3;
  z-index: 0;
  overflow: hidden;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.12);
}

#left .el-menu-vertical-demo .title {
  color: #e0f2ff;
  font-size: 16px;
  font-weight: 500;
  margin-left: 5px;
}

/* 子菜单样式 */
.el-submenu .el-menu-item {
  padding-left: 40px !important;
  height: 50px;
  line-height: 50px !important;
  background-color: #164785 !important;
  transition: background-color 0.3s;
  border-left: 4px solid transparent;
}

/* 子菜单图标特殊样式 */
.el-submenu .el-menu-item i {
  font-size: 18px;
  margin-right: 10px;
  width: 24px;
  text-align: center;
  display: inline-block;
  color: #e0f2ff !important;
  transition: color 0.2s, transform 0.2s;
}

.el-submenu .el-menu-item span {
  color: #e0f2ff !important;
  font-size: 14px;
}

.el-submenu .el-menu-item:hover {
  background-color: #1b5092 !important;
}

.el-submenu .el-menu-item:hover i {
  color: #ffd04b !important;
  transform: scale(1.15);
}

.el-submenu .el-menu-item.is-active {
  background-color: #2978CF !important;
  color: #ffffff !important;
  border-left: 4px solid #52B5FF;
}

.el-submenu .el-menu-item.is-active i {
  color: #ffd04b !important;
}

/* 菜单项样式 */
.el-menu-item {
  height: 56px;
  line-height: 56px;
  border-left: 4px solid transparent;
  transition: all 0.3s;
}

.el-menu-item.is-active {
  background-color: #2978CF !important;
  color: #ffffff !important;
  border-left: 4px solid #52B5FF;
}

.el-menu-item:hover {
  background-color: #23518a !important;
}

.el-menu-item:hover i {
  color: #ffd04b !important;
}

.el-submenu {
  border-left: 4px solid transparent;
}

.el-submenu.is-active {
  border-left: 4px solid #52B5FF;
}

.el-submenu__title {
  height: 56px;
  line-height: 56px;
  transition: all 0.3s;
}

.el-submenu__title:hover {
  background-color: #23518a !important;
}

/* 修复图标颜色 */
.el-submenu__title i,
.el-menu-item i {
  color: #e0f2ff !important;
}

/* 折叠菜单样式 */
.el-menu--collapse {
  width: 64px;
}

.el-menu--collapse .menu-icon {
  font-size: 20px;
  margin-right: 0;
}

/* 确保折叠菜单时图标居中 */
.el-menu--collapse .el-menu-item i,
.el-menu--collapse .el-submenu__title i {
  margin: 0;
  margin-right: 0;
  padding: 0;
  text-align: center;
}

/* 添加过渡效果 */
.el-menu--collapse .el-submenu__title span,
.el-menu--collapse .el-menu-item span {
  height: 0;
  width: 0;
  overflow: hidden;
  visibility: hidden;
  display: inline-block;
}

.el-submenu .el-menu-item:hover i {
  color: #ffd04b !important;
  transform: scale(1.15);
}

.el-submenu .el-menu-item.is-active i {
  color: #ffd04b !important;
}
</style>
