<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input
            v-model="searchForm.cno"
            placeholder="课程名或课程号"
            clearable
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="getCourseList">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="确定批量删除吗？" @confirm="delHandle(null)">
          <el-button type="danger" slot="reference" :disabled="delBtlStatu">批量删除</el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>

    <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        stripe
        @selection-change="handleSelectionChange">

      <el-table-column
          type="selection"
          width="55">
      </el-table-column>


      <el-table-column
          prop="id"
          label="ID"
          width="120">
      </el-table-column>

      <el-table-column
          prop="cno"
          label="课程号">
      </el-table-column>
      <el-table-column
          prop="cname"
          label="课程名">
      </el-table-column>
      <el-table-column
          prop="tno"
          label="教师编号">
      </el-table-column>


      <el-table-column
          prop="icon"
          width="260px"
          label="操作">

        <template slot-scope="scope">
          <el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
          <el-button type="text" @click="liststudentHandle(scope.row.cno)">查看学生</el-button>
          <el-button type="text" @click="addStudentHandle(scope.row.cno)">添加学生</el-button>
          <el-divider direction="vertical"></el-divider>

          <template>
            <el-popconfirm title="确定删除吗？" @confirm="delHandle(scope.row.id)">
              <el-button type="text" slot="reference">删除</el-button>
            </el-popconfirm>
          </template>

        </template>
      </el-table-column>

    </el-table>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50, 100]"
        :current-page="current"
        :page-size="size"
        :total="total">
    </el-pagination>


    <!--新增对话框-->
    <el-dialog
        title="课程"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="handleClose">

      <el-form :model="editForm" :rules="editFormRules" ref="editForm">
        <el-form-item label="ID" prop="id" label-width="100px">
          <el-input v-model="editForm.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="课程号" prop="cno" label-width="100px">
          <el-input v-model="editForm.cno" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="课程名" prop="sname" label-width="100px">
          <el-input v-model="editForm.cname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师编号" prop="tno" label-width="100px">
        <el-select v-model="editForm.tno" placeholder="请选择">
          <el-option
              v-for="item in tnoList"
              :value="item" :key="item">
          </el-option>
        </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('editForm')">取 消</el-button>
        <el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
      </div>
    </el-dialog>

    <!--查看课程学生对话框-->
    <el-dialog
        title="学生列表"
        :visible.sync="dialogVisible1"
        width="600px"
        :before-close="handleClose1">
      <el-row>
        <el-col :span="8"  v-for="item in editForm1" :value="item" :key="item.cno">
          <el-card :body-style="{ padding: '0px' }">
            <div style="padding: 14px;">
              <span>{{item.sno}}</span>
              <span>{{item.sname}}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

    </el-dialog>
    <!--添加学生对话框-->
    <el-dialog
        title="添加学生"
        :visible.sync="dialogVisible2"
        width="600px"
        :before-close="handleClose2">
      <el-form :model="editForm2"  ref="editForm2">
      <el-form-item label="课程号" prop="cno" label-width="100px">
        <el-input v-model="editForm2.cno" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item label="学生编号" prop="sno" label-width="100px">
        <el-select v-model="editForm2.sno" placeholder="请选择">
          <el-option
              v-for="item1 in snoList"
              :value="item1" :key="item1">
          </el-option>
        </el-select>
      </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('editForm2')">取 消</el-button>
        <el-button type="primary" @click="addStudent(editForm2.cno,editForm2.sno)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "CourseList",
  data() {
    return {
      searchForm: {},
      delBtlStatu: true,
      total: 0,
      size: 10,
      current: 1,
      dialogVisible: false,
      dialogVisible1: false,
      dialogVisible2: false,
      editForm: {
        id:"",
        cno:"",
        cname:"",
        tno:"",
      },
      editForm1: {
        id:"",
        sno:"",
        sname:"",
        sex:"",
        pwd:""
      },
      editForm2: {
        sno:"",
        cno:""
      },
      tableData: [],
      editFormRules: {
        cname: [
          {required: true, message: '请输入课程名', trigger: 'blur'}
        ],
        tno: [
          {required: true, message: '请绑定教师编号', trigger: 'blur'}
        ]
      },
      editFormRules1: {
        sno: [
          {required: true, message: '请绑定学生编号', trigger: 'blur'}
        ]
      },
      multipleSelection: [],
      roleDialogFormVisible: false,
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      roleForm: {},
      roleTreeData:  [],
      treeCheckedKeys: [],
      checkStrictly: true,
      tnoList:[],
      snoList:[]
    }
  },
  created() {
    this.getCourseList()
    this.$axios.get("/course/list").then(res => {
      this.roleTreeData = res.data.data.records
    })
    this.$axios.get("/course/tno/list").then(res => {
      this.tnoList = res.data.data
      console.log(res.data.data);
    })
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      console.log("勾选")
      console.log(val)
      this.multipleSelection = val;
      this.delBtlStatu = val.length == 0
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.size = val
      this.getCourseList()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.current = val
      this.getCourseList()
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false
      this.editForm = {}
    },
    resetForm1(formName) {
      this.dialogVisible1 = false
      this.editForm1 = {}
    },
    resetForm2(formName) {
      this.dialogVisible2 = false
      this.editForm2 = {}
    },
    handleClose() {
      this.resetForm('editForm')
    },
    handleClose1() {
      this.resetForm1('editForm1')
    },
    handleClose2() {
      this.resetForm2('editForm2')
    },
    getCourseList() {
      this.$axios.get("/course/list/", {
        params: {
          cno: this.searchForm.cno,
          current: this.current,
          size: this.size
        }
      }).then(res => {
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.current = res.data.data.current
        this.total = res.data.data.total
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.editForm)
          this.$axios.post('/course' + (this.editForm.id?'/update' : '/save'), this.editForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: '恭喜你，操作成功',
                  type: 'success',
                  onClose:() => {
                    this.getCourseList()
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
    editHandle(id) {
      console.log(id)
      this.$axios.get('/course/Info/' + id).then(res => {
        this.editForm = res.data.data
        this.dialogVisible = true
      })
    },
    delHandle(id) {
      var ids = []
      if (id) {
        ids.push(id)
      } else {
        this.multipleSelection.forEach(row => {
          ids.push(row.id)
        })
      }
      console.log(ids)
      this.$axios.post("/course/delete", ids).then(res => {
        this.$message({
          showClose: true,
          message: '恭喜你，操作成功',
          type: 'success',
          onClose:() => {
            this.getCourseList()
          }
        });
      })
    },
    liststudentHandle(cno){
      this.$axios.get("/course/listStudent?cno="+cno).then(res => {
        this.editForm1 = res.data.data
        this.dialogVisible1 = true
      })
    },
    addStudentHandle(cno){
      this.$axios.get("/student/getsno").then(res => {
        this.snoList = res.data.data
        console.log(this.snoList)
        this.editForm2.cno = cno
        this.dialogVisible2 = true
      })
    },
    addStudent(cno,sno){
      this.$axios.post("/scourse/addStudent",this.editForm2).then(res => {
        console.log(res.data.data)
        this.dialogVisible2 = false
      })
    }
  }
}
</script>
