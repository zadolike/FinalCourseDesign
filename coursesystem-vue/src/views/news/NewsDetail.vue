<template>
  <div class="m-container">
    <div class="mblog">
      <h2>{{ news.title }}</h2>
      <el-link icon="el-icon-edit" v-if="ownnews"><router-link :to="{name: 'newsEdit', params: {newsId: news.id}}">编辑</router-link></el-link>
      <el-divider></el-divider>
      <div class="content markdown-body" v-html="news.content"></div>
    </div>
  </div>
</template>
<script>
import 'github-markdown-css/github-markdown.css' // 然后添加样式markdown-body
export default {
  name: "NewsDetail",
  data() {
    return {
      news: {
        title: "",
        description: "",
        content: ""
      },
      ownnews: false
    }
  },
  methods: {
    getnews() {
      const newsId = this.$route.params.newsId
      const _this = this
      this.$axios.get('/admin/news/' + newsId).then((res) => {
        console.log(res)
        console.log(res.data.data)
        _this.news = res.data.data
        var MarkdownIt = require('markdown-it'),
            md = new MarkdownIt();
        var result = md.render(_this.news.content);
        _this.news.content = result
        // 判断是否是自己的文章，能否编辑
        _this.ownnews =  ("Admin" === this.$store.getters.getUser.role)
      });
    }
  },
  created() {
    this.getnews()
  }
}
</script>
<style>
.m-container{
  margin-top: 50px;
}
.mblog{
  margin: 0 auto;
  text-align: center;
}
</style>