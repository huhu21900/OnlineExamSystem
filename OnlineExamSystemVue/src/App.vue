<!--
 * @Description:
 * @Author:
 * @Date: 2024-03-08 20:38:49
-->
<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  created() {
    var token = this.$cookies.get("rb_token");
    var role = this.$cookies.get("rb_role");
    if(token == null || token == "" || role == null || role == null) {
      this.$router.push({path: '/' }) //跳转到首页
    }
  }
}
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow-x: hidden;
}

.icon-r-yes {
  display: none;
}

ul {
  list-style: none;
}

a {
  text-decoration: none;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: "Microsoft YaHei", "Helvetica", "Tahoma", "Geneva", "Arial", sans-serif;
  background-color: #eee;
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
}
</style>
