<template>
  <div class="m-container">
  <el-row>
    <el-col :span="8"  v-for="item in cname" :value="item" :key="item.cno">
      <el-card :body-style="{ padding: '0px' }">
        <img src="https://www.gxun.edu.cn/images/gx_03.png" class="image">
        <div style="padding: 14px;">
          <span>{{item.cname}}</span>
          <div class="bottom clearfix">
            <el-button type="text" class="button"  >
              <router-link :to="{name: 'CourseDetail', params: {cno: item.cno}}">详细信息
            </router-link>
            </el-button>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
  </div>
</template>

<script>
export default {
  name: "TeacherCourse",
  data() {
    return {
      cname:[]
    }
  },
  created() {
    const _this = this
    const sno = this.$store.getters.getUser.sno
    if(sno) {
      this.$axios.get('/course/getCourse/' + sno).then((res) => {
        console.log(res)
        this.cname = res.data.data
      });
    }
  },
  methods: {
  },
}

</script>

<style scoped>
.m-container{
  margin-top: 50px;
}
</style>