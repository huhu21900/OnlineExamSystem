/*
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
 */
import VUE from 'vue'
import VUEX from 'vuex'

VUE.use(VUEX)

const state = {
  isPractice: false, //练习模式标志
  flag: false, //菜单栏左右滑动标志
  userInfo: null,
  menu: [
    {
      index: '0',
      title: '首页',
      icon: 'el-icon-s-home',
      content: [{ name: '首页', path: '/index/', icon: "el-icon-s-home" }],
    },
    {
      index: '1',
      title: '考试管理',
      icon: 'el-icon-s-management',
      content: [
        { name: '考试查询', path: '/selectExam', icon: "el-icon-search", showText: true },
        { name: '添加考试', path: '/addExam', icon: "el-icon-circle-plus", showText: true }
      ],
    },
    {
      index: '2',
      title: '题库管理',
      icon: 'el-icon-s-order',
      content: [
        { name: '题目列表', path: '/selectAnswer', icon: "el-icon-notebook-2", showText: true },
        { name: '新增题目', path: '/addAnswer', icon: "el-icon-circle-plus", showText: true },
        { name: '题目详情', path: '/addAnswerChildren', hide: true }
      ],
    },
    {
      index: '3',
      title: '成绩查询',
      icon: 'el-icon-data-analysis',
      content: [
        { name: '学生成绩查询', path: '/allStudentsGrade', icon: "el-icon-document-checked", showText: true },
        { name: '成绩详情', path: '/grade', hide: true },
        { name: '成绩分段查询', path: '/selectExamToPart', icon: "el-icon-pie-chart", showText: true },
        { name: '分段详情', path: '/scorePart', hide: true }
      ],
    },
    {
      index: '4',
      title: '学生管理',
      icon: 'el-icon-user',
      content: [
        { name: '学生管理', path: '/studentManage', icon: "el-icon-user", showText: true },
        { name: '添加学生', path: '/addStudent', icon: "el-icon-user-solid", showText: true }
      ],
    },
    // {
    //   index: '5',
    //   title: '教师管理',
    //   icon: 'el-icon-s-custom',
    //   content:[
    //     { name: '教师管理', path: '/teacherManage', icon: "el-icon-s-custom" },
    //     { name: '添加教师', path: '/addTeacher', icon: "el-icon-user-solid" }
    //   ],
    // },
    // {
    //   index: '7',
    //   title: '模块管理',
    //   icon: 'el-icon-s-grid',
    //   content:[{ name: '模块操作', path: '/module', icon: "el-icon-s-operation" }],
    // }
  ],
}
const mutations = {
  practice(state, status) {
    state.isPractice = status
  },
  toggle(state) {
    state.flag = !state.flag
  },
  changeUserInfo(state, info) {
    state.userInfo = info
  }
}
const getters = {

}

import '@/../config/initialize'
const actions = {
  getUserInfo(context, info) {
    context.commit('changeUserInfo', info)
  },
  getPractice(context, status) {
    context.commit('practice', status)
  }
}
export default new VUEX.Store({
  state,
  mutations,
  getters,
  actions,
  // store
})
