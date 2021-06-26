<template>
  <el-menu
      class="el-menu-vertical-demo"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
  >
    <el-submenu index="1">
      <template slot="title">
        <i class="el-icon-s-operation"></i>
        <span>用户管理</span>
      </template>
      <router-link to="/admin/stuManage">
      <el-menu-item index="1-1">
        <template slot="title">
          <i class="el-icon-s-custom"></i>
          <span slot="title">学生管理</span>
        </template>
      </el-menu-item>
      </router-link>
      <router-link to="/admin/teacherManage">
      <el-menu-item index="1-2">
        <template slot="title">
          <i class="el-icon-s-custom"></i>
          <span slot="title">教师管理</span>
        </template>
      </el-menu-item>
      </router-link>
    </el-submenu>

    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-news"></i>
        <span>新闻管理</span>
      </template>
      <router-link to="/admin/newsList">
        <el-menu-item index="2-1">
          <template slot="title">
            <i class="el-icon-tickets"></i>
            <span slot="title">新闻列表</span>
          </template>
        </el-menu-item>
      </router-link>
      <router-link to="/admin/newsEdit">
        <el-menu-item index="2-2">
          <template slot="title">
            <i class="el-icon-add-location"></i>
            <span slot="title">新闻添加</span>
          </template>
        </el-menu-item>
      </router-link>
    </el-submenu>
    <router-link to="/admin/courseList">
      <el-menu-item index="3">
        <template slot="title">
            <i class="el-icon-reading"></i>
          <span slot="title">课程管理</span>
        </template>
      </el-menu-item>
    </router-link>
  </el-menu>
</template>
<script>
export default {
  name: "SideMenu",
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
