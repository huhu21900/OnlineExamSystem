<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
-->
// 展示组件页面
<template>
  <div id="index">
    <header1 class="topbar"></header1>
    <section class="container">
      <div class="left_side">
        <mainLeft></mainLeft>
      </div>
      <div class="main_wrapper">
        <navigator class="nav"></navigator>
        <router-view></router-view>
      </div>
    </section>
  </div>
</template>

<script>
import header from '@/components/common/header'
import mainLeft from '@/components/common/mainLeft'
import navigator from '@/components/common/navigator'
export default {
  components:{
    header1: header,
    mainLeft: mainLeft,
    navigator:navigator
  },
  data() {
    return {
      username: '许如梦'
    }
  },
  methods: {
    
  }
}
</script>

<style lang="less" scoped>
#index {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

#index .nav {
  box-shadow: 1px 0 5px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.container {
  display: flex;
  background-color: #fff;
  flex: 1;
  overflow: hidden;
}

.left_side {
  height: auto;
  min-height: calc(100vh - 80px);
  overflow: hidden;
}

.main_wrapper {
  overflow: hidden;
  flex: 1;
  background-color: #fff;
}
</style>

