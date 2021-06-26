<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>个人信息</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="editHandle(dataForm.id)">修改信息</el-button>
      </div>
      <div v class="text item">
        {{'工号号: ' + dataForm.tno }}
      </div>
      <div  class="text item">
        {{'姓名: ' + dataForm.tname }}
      </div>
      <div  class="text item">
        {{'性别: ' + dataForm.sex }}
      </div>
    </el-card>
    <!--新增对话框-->
    <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="handleClose">

      <el-form :model="editForm" :rules="editFormRules" ref="editForm">
        <el-form-item label="工号号" prop="tno" label-width="100px" >
          <el-input v-model="editForm.tno" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="tname" label-width="100px">
          <el-input v-model="editForm.tname" autocomplete="off"></el-input>
          <!--          <el-alert-->
          <!--              title="初始密码为888888"-->
          <!--              :closable="false"-->
          <!--              type="info"-->
          <!--              style="line-height: 12px;"-->
          <!--          ></el-alert>-->
        </el-form-item>

        <el-form-item label="性别"  prop="sex" label-width="100px">
          <el-radio-group v-model="editForm.sex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="密码"  prop="pwd" label-width="100px">
          <el-input v-model="editForm.pwd" autocomplete="off"></el-input>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('editForm')">取 消</el-button>
        <el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>


<script>
export default {
  name: "TeachertInfo",
  data(){
    return{
      dataForm:{
        id:'',
        tno: '',
        tname: '',
        sex: '',
        pwd:''
      },
      editForm: {
        id:"",
        tno:"",
        tname:"",
        sex:0,
        pwd:""
      },
      dialogVisible: false,
      editFormRules: {
        tname: [
          {required: true, message: '请输入姓名', trigger: 'blur'}
        ],
        sex: [
          {required: true, message: '请输入性别', trigger: 'blur'}
        ],
        pwd: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ]
      },
    }
  },
  created() {
    const id = this.$store.getters.getUser.id;
    this.getInfo(id)
  },

  methods: {
    getInfo(id) {
      console.log(id)
      this.$axios.get("/teacher/Info/"+id).then(res => {
        console.log(res.data.data)
        this.dataForm = res.data.data
      })
    },
    editHandle(id) {
      console.log(id)
      this.$axios.get('/teacher/Info/' + id).then(res => {
        this.editForm = res.data.data
        if (res.data.data.sex=="男"){
          this.editForm.sex = 0
        }
        else{
          this.editForm.sex = 1
        }
        this.dialogVisible = true
      })
    },
    submitForm(formName) {
      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.editForm)
          this.$axios.post('/teacher' + (this.editForm.id?'/update' : '/save'), this.editForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: '恭喜你，操作成功',
                  type: 'success',
                  onClose:() => {
                    this.getInfo(this.dataForm.id)
                    _this.$store.commit('REMOVE_INFO')
                    _this.$router.push('/login')
                  }
                });
                this.dialogVisible = false
              })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handleClose() {
      this.resetForm('editForm')
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false
      this.editForm = {}
    },
  }

}
</script>

<style>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 480px;
}
</style>
