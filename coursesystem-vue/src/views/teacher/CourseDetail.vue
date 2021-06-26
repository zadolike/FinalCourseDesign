<template>
<div>
  <el-upload class="upload-demo"
             :action="uploadUrl"
             :before-upload="handleBeforeUpload"
             :on-error="handleUploadError"
             :before-remove="beforeRemove"
             multiple
             :limit="5"
             :on-exceed="handleExceed"
             :file-list="fileList">
    <el-button size="small" type="primary">点击上传</el-button>
  </el-upload>

<!--  下载-->
  <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      border
      stripe
      >
    <el-table-column
        prop="filename"
        label="文件名">
    </el-table-column>
    <el-table-column
        prop="icon"
        width="260px"
        label="操作">

      <template slot-scope="scope">
<!--        <el-button  type="button" @click="download(scope.row.filename)">  下载附件</el-button>-->
        <a :href="'http://localhost:8081/teacher/file/download?fileName='+scope.row.filename">下载附件</a>
        <el-divider direction="vertical"></el-divider>

<!--        <template>-->
<!--          <el-popconfirm title="确定删除吗？" @confirm="delHandle(scope.row.id)">-->
<!--            <el-button type="text" slot="reference">删除</el-button>-->
<!--          </el-popconfirm>-->
<!--        </template>-->

      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script>
export default {
  name: "CourseDeatil",
  data () {
    return {
      uploadUrl: 'http://localhost:8081/teacher/file/upload',
      fileList: [],
      downloadlist:[],
      tableData: [],
    }
  },
  created() {
    this.getFileList()
  },
  methods: {

    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 5 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    handleUploadError(error, file) {
      console.log("文件上传出错："+error)
      // this.$notify.error({
      //          title: 'error',
      //          message: '上传出错:' +  error,
      //          type: 'error',
      //          position: 'bottom-right'
      //        })
    },
    //测试上传文件(注意web的上下文)
    handleBeforeUpload(file){
      this.uploadUrl = 'http://localhost:8081/teacher/file/upload'
      console.log("开始上传，上传的文件为："+file)
      let formData = new FormData();
      formData.append("multipartFiles", file);

      this.$axios.post('/teacher/file/upload',formData,{
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        params:{
          'cno':this.$route.params,
        }
      }).then((res) => {
        console.log("文件上传返回："+res)
        this.$router.go(0);
      }).catch(error => {
        console.log("文件上传异常:"+error)
      })
    },
    //获取所有该课程文件
    getFileList(){
      this.$axios.get('/teacher/file/getFileList',{
        params:{
          'cno':this.$route.params,
        }
      }).then((res) => {
        console.log(res.data.data.records)
        this.tableData = res.data.data.records
    })
    },
    //文件下载
    download(fileName){
      this.$axios.get('/teacher/file/download?fileName='+fileName,{timeout: 1000})
    }
  }
}
</script>

<style scoped>

</style>