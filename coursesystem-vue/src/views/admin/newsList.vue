<template>
  <div class="m-container">
    <div class="block">
      <el-timeline>
        <el-timeline-item v-bind:timestamp="nnew.created" placement="top" v-for="nnew in news">
          <el-card>
            <h4><router-link :to="{name: 'newsEdit', params: {newsId: nnew.id}}">{{nnew.title}}</router-link></h4>
            <p>{{nnew.description}}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>

    </div>
    <el-pagination class="mpage"
                   background
                   layout="prev, pager, next"
                   :current-page=currentPage
                   :page-size=pageSize
                   @current-change=page
                   :total="total">
    </el-pagination>
  </div>
</template>
<script>
export default {
  name: "newsList",
  data() {
    return {
      news: {},
      currentPage: 1,
      total: 0,
      pageSize: 5
    }
  },
  methods: {
    page(currentPage) {
      const _this = this
      this.$axios.get('/admin/news?currentPage=' + currentPage).then((res) => {
        _this.news = res.data.data.records
        _this.currentPage = res.data.data.current
        _this.total = res.data.data.total
        _this.pageSize = res.data.data.size
      })
    }
  },
  mounted () {
    this.page(1);
  }
}
</script>
<style>
.m-container{
  margin-top: 50px;
}
.mpage{
  margin: 0 auto;
  text-align: center;
}
</style>