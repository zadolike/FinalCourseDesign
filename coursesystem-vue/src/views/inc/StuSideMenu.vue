<template>
  <el-menu
      class="el-menu-vertical-demo"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
  >
    <router-link to="/student/index">
      <el-menu-item index="1">
          <template slot="title">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页</span>
        </template>
      </el-menu-item>
    </router-link>
    <router-link to="/student/course">
      <el-menu-item index="2">
        <template slot="title">
          <i class="el-icon-reading"></i>
          <span slot="title">课程信息</span>
        </template>
      </el-menu-item>
    </router-link>
    <router-link to="/student/info">
      <el-menu-item index="3">
        <template slot="title">
          <i class="el-icon-s-custom"></i>
          <span slot="title">个人中心</span>
        </template>
      </el-menu-item>
    </router-link>
  </el-menu>
</template>
<script>
export default {
  name: "StuSideMenu",
  data() {
  }
}
</script>
<style scoped>
.el-menu-vertical-demo {
  height: 100%;
}
a{
  text-decoration:none;
}
</style>
