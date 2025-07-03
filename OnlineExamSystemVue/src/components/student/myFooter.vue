<!--
 * @Description:
 * @Author:
 * @Date: 2024-03-08 20:38:49
-->
<!--页脚部分-->
<template>
<!--  <footer id="footer">-->
<!--&lt;!&ndash;    <div class="footer-content">&ndash;&gt;-->
<!--&lt;!&ndash;      <div class="footer-logo">&ndash;&gt;-->
<!--&lt;!&ndash;        <img src="../../assets/img/exam-logo.svg" alt="Logo" class="footer-logo-img">&ndash;&gt;-->
<!--&lt;!&ndash;        <div class="footer-logo-text">在线考试系统</div>&ndash;&gt;-->
<!--&lt;!&ndash;      </div>&ndash;&gt;-->
<!--&lt;!&ndash;      <div class="footer-links">&ndash;&gt;-->
<!--&lt;!&ndash;        <a href="javascript:;">关于我们</a>&ndash;&gt;-->
<!--&lt;!&ndash;        <a href="javascript:;">帮助中心</a>&ndash;&gt;-->
<!--&lt;!&ndash;        <a href="javascript:;">免责声明</a>&ndash;&gt;-->
<!--&lt;!&ndash;        <a href="javascript:;">使用协议</a>&ndash;&gt;-->
<!--&lt;!&ndash;        <a href="javascript:;">联系我们</a>&ndash;&gt;-->
<!--&lt;!&ndash;      </div>&ndash;&gt;-->
<!--&lt;!&ndash;      <div class="footer-copyright">&ndash;&gt;-->
<!--&lt;!&ndash;        © {{ new Date().getFullYear() }} 在线考试系统 版权所有&ndash;&gt;-->
<!--&lt;!&ndash;      </div>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->
<!--  </footer>-->
</template>

<script>
export default {
  name: "myFooter"
}
</script>

<style scoped>
#footer {
  background-color: #f5f7fa;
  border-top: 1px solid #e4e7ed;
  padding: 30px 0;
  margin-top: 30px;
}

.footer-content {
  max-width: 1100px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.footer-logo {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.footer-logo-img {
  width: 30px;
  height: 30px;
  margin-right: 8px;
}

.footer-logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.footer-links {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: 15px;
}

.footer-links a {
  color: #606266;
  font-size: 14px;
  text-decoration: none;
  margin: 0 15px;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #409EFF;
}

.footer-copyright {
  color: #909399;
  font-size: 12px;
}
</style>
