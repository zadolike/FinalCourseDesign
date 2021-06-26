<template>
  <el-container>
    <el-aside width="200px">

      <SideMenu></SideMenu>

    </el-aside>
    <el-container>
      <el-header>
        <strong>课程信息资料系统</strong>

        <div class="header-avatar">



          <el-dropdown>
						<span class="el-dropdown-link">
						{{userInfo.role+":"+userInfo.sno}}<i class="el-icon-arrow-down el-icon--right"></i>
						</span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="logout">退出</el-dropdown-item>

            </el-dropdown-menu>
          </el-dropdown>
        </div>

      </el-header>
      <el-main>
        <Tabs></Tabs>
        <div style="margin: 0 15px;">
          <router-view/>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import SideMenu from "./inc/SideMenu";
export default {
  name: "Home",
  components: {
    SideMenu
  },
  data() {
    return {
      userInfo: {
        id: "",
        role: "",
        sno: ""
      }
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {

    getUserInfo() {
      const _this = this
      this.userInfo = _this.$store.getters.getUser;
    },
    logout() {
      const _this = this
      this.$axios.get('http://localhost:8081/logout', {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then((res) => {
        _this.$store.commit('REMOVE_INFO')
        _this.$router.push('/login')
      });
    }
  }
}
</script>

<style scoped>
.el-container {
  padding: 0;
  margin: 0;
  height: 100%;
}
.header-avatar {
  float: right;
  width: 210px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.el-dropdown-link {
  cursor: pointer;
}
.el-header {
  background-color: #17B3A3;
  color: #333;
  text-align: center;
  line-height: 60px;
}
.el-aside {
  background-color: #D3DCE6;
  color: #333;
  line-height: 200px;
}
.el-main {
  color: #333;
  padding: 0;
}
a {
  text-decoration: none;
}
</style>