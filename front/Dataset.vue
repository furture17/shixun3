<template>
  <div style="padding: 10px" >

    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>数据集列表</el-breadcrumb-item>
    </el-breadcrumb>

    <div style id="small-container">
      <!--      搜索区域-->
      <div style="padding: 10px">
        <el-button type="primary" @click="addFile">上传数据集</el-button>
      </div>
      <!--    表单显示区-->
      <el-table stripe
                :data="tableData"
                id = "user-table"
                max-height="250"
                style="width: 100%">
        <el-table-column
            fixed
            type="index"
            label="编号"
            width="70">
        </el-table-column>
        <el-table-column
            prop="name"
            label="名称"
            width="150">
        </el-table-column>
      </el-table>
    </div>

    <el-dialog title="上传新数据集" v-model="addVisible" width="300px">
      <el-form-item label="上传图片" ref="uploadElement" >
        <el-upload
            class="upload-demo"
            action="http://192.168.43.55:8080/file/fileUp"
            :data="uploadData"
            :limit="1"
            :on-success="uploadFileSuccess">
          <template #trigger>
            <el-button type="primary">添加数据集</el-button>
          </template>
          <template #tip>
            <div class="el-upload__tip">pdf, md, word格式均可</div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form label-width="70px">
        <el-button type="primary" @click="closeAddFile">结束上传</el-button>
      </el-form>

    </el-dialog>

  </div>
</template>


<script>
import axios from "axios";
import {ElMessage} from "element-plus";
import store from "@/store";


export default {
  name: "fileup",
  data(){
    return{
      file: {
        id:'',
        name:'',
        currentFile: [],
        userAccount: store.state.userId,//'gunp'
      },
      addVisible:false,
      tableData:[],
      dataset: {
        jdt:'',
        lucene:'',
        pde:'',
      },
      uploadData: {
        userAccount: store.state.userId//'gunp'
      },
    }
  },
  mounted(){
    this.load()
  },
  methods:{
    load(){
      axios.get("http://192.168.43.55:8080/file/getFiles",{
        params:{
          //headers: {userAccount: this.user.id}
          userAccount:this.file.userAccount
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
          this.dataset = res.data.data
          if(this.dataset.jdt=='1'){
            if(this.dataset.lucene=='1') {
              if (this.dataset.pde == '1') {this.tableData = [{name: 'jdt'}, {name: 'lucene'}, {name: 'pde'}]}
              else {this.tableData = [{name: 'jdt'}, {name: 'lucene'},]}
            }else{
              if(this.dataset.pde=='1') {this.tableData=[{name:'jdt'},{name:'pde'}]}
              else {this.tableData=[{name:'jdt'}]}
            }
          }else{
            if(this.dataset.lucene=='1') {
              if (this.dataset.pde == '1') {this.tableData = [ {name: 'lucene'}, {name: 'pde'}]}
              else {this.tableData = [ {name: 'lucene'},]}
            }else{
              if(this.dataset.pde=='1') {this.tableData=[{name:'pde'}]}
            }
          }
        }
      })
    },
    //新增数据集
    addFile(){
      this.file={}
      console.log('1')
      this.addVisible = true
    },
    closeAddFile(){
      this.addVisible = false
    },
    // 上传文件返回函数，为了得到后端返回的下载url，保存到数据库中
    uploadFileSuccess(response, file, fileList) {
      let filePath = response.data;
      // 上传文件成功后的response参数已经是返回报文中的data部分，不需要使用response.data.data
      alert(response.data);
      // 回调函数中的this指针就是当前页面vue对象
      this.attachForm.downloadUrl = filePath;
    }

  }
}
</script>



