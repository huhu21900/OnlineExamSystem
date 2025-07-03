<template>
  <div id="nav">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item class="title">
        <i :class="getTitleIcon(currentTitle)" class="nav-icon"></i>
        {{currentTitle}}
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="currentSubItem">{{currentSubItem}}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  data() {
    return {
      active: [],
      index1: null,
      currentTitle: '首页',
      currentSubItem: null,
      // 导航图标映射
      navIcons: {
        '首页': 'el-icon-s-home',
        '考试管理': 'el-icon-s-management',
        '题库管理': 'el-icon-s-order',
        '成绩查询': 'el-icon-data-analysis',
        '学生管理': 'el-icon-user',
        '教师管理': 'el-icon-s-custom'
      }
    }
  },
  computed: mapState(["menu"]),
  watch: {
    // 监听路由变化，更新当前标题
    '$route': {
      immediate: true,
      handler(to) {
        this.updateCurrentTitle(to.path);
      }
    }
  },
  methods: {
    getTitleIcon(title) {
      return this.navIcons[title] || 'el-icon-s-home';
    },
    updateCurrentTitle(path) {
      // 根据路径判断当前页面
      if (path === '/index/' || path === '/index') {
        this.currentTitle = '首页';
        this.currentSubItem = null;
        return;
      }
      
      // 遍历菜单寻找匹配的路径
      for (let i = 0; i < this.menu.length; i++) {
        const item = this.menu[i];
        if (item.content) {
          for (let j = 0; j < item.content.length; j++) {
            const subItem = item.content[j];
            if (subItem.path === path) {
              this.currentTitle = item.title;
              this.currentSubItem = subItem.name;
              return;
            }
          }
        }
      }
      
      // 如果没有匹配到，使用默认标题
      this.currentTitle = '首页';
      this.currentSubItem = null;
    },
    
    getIndex() {
      this.bus.$on('sendIndex',(data)=>{
        this.index1 = data;
        
        // 处理首页菜单的特殊情况
        if (data === '0') {
          this.currentTitle = '首页';
          this.currentSubItem = null;
          return;
        }
        
        // 找到对应索引的菜单项
        const menuItem = this.menu.find(item => item.index === data);
        if (menuItem) {
          this.currentTitle = menuItem.title;
        }
      })
    }
  },
  created() {
    this.getIndex();
  },
  beforeDestroy() {
    this.bus.$off('sendIndex'); // 销毁事件监听，避免内存泄漏
  },
}
</script>

<style scoped>
#nav {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

#nav .el-breadcrumb {
  height: 60px;
  line-height: 60px;
  padding: 0 20px;
  font-size: 14px;
}

#nav .el-breadcrumb .title {
  font-weight: bold;
  font-size: 16px;
  color: #333;
  display: flex;
  align-items: center;
}

.nav-icon {
  margin-right: 8px;
  font-size: 18px;
  color: #409EFF;
}

/* 覆盖element-ui的默认面包屑样式 */
::v-deep .el-breadcrumb__inner a,
::v-deep .el-breadcrumb__inner.is-link {
  color: #606266;
  font-weight: normal;
  transition: color 0.2s;
}

::v-deep .el-breadcrumb__inner a:hover,
::v-deep .el-breadcrumb__inner.is-link:hover {
  color: #409EFF;
  text-decoration: none;
}

::v-deep .el-breadcrumb__separator {
  color: #909399;
  margin: 0 9px;
}
</style>
