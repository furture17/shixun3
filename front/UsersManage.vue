<template>
  <div style="padding: 10px" >
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <div style id="small-container">
      <!--    表单显示区-->
      <el-table stripe
                :data="tableData"
                id = "user-table"
                max-height="250"
                style="width: 100%">
        <el-table-column
            fixed
            prop="id"
            label="账号"
            width="80">
        </el-table-column>
        <el-table-column
            prop="userAccount"
            label="用户名"
            width="150">
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱"
            width="300">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="电话"
            width="250">
        </el-table-column>
        <el-table-column
            prop="userRole"
            label="身份"
            width="150">
          <template #default="scope">
            <span v-if="scope.row.userRole == 0">普通用户</span>
            <span v-if="scope.row.userRole == 1">管理员</span>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="250">
          <template #default="scope">
            <el-button
                type="text"
                size="small"
                @click="upRole(scope.row)">
              提升为管理员
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>
<script>
import axios from "axios";
import {ElMessage} from "element-plus";
import store from "@/store";

export default {
  name: "about",
  data(){
    return{
      user: {
        id:'',
        password:'',
        name:'',
        email:'',
        phone:'',
      },
      total:0,
      pageNum:1,
      PageSize:10,
      search:'',
      addVisible:false,
      deleteVisible:false,
      changeVisible:false,
      tableData:[],
    }
  },
  mounted(){
    this.load()
  },
  methods:{
    //页面加载：参数是做模糊查询+分页显示的
    //具体为，一页显示Pagesize条查询结果，目前显示第pageNum页，按search做模糊查询
    load(){
      axios.get("http://192.168.43.55:8080/user/getUsers",{
        params:{
          userAccount:store.state.userId
        }
      }).then(res =>{
        const {data, message} = res.data
        ElMessage({
          type: data,
          message
        })
        if (data) {
          console.log(res)
          console.log("request")
          this.tableData = res.data.data
        }
      })
    },
    //显示弹窗：add：新增用户；delete；删除；change:修改
    upRole(row){
      axios.get("http://192.168.43.55:8080/user/upRole",{
        params:{
          userAccount:row.userAccount
        }
      }).then(res =>{
        const {data, message} = res.data
        ElMessage({
          type: data,
          message
        })
        if (data) {
          console.log(res)
          console.log("request")
        }
      })
      this.load()
    },

  }
}
</script>

