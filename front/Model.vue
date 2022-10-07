<template>
  <div style="padding: 10px" >
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>模型列表</el-breadcrumb-item>
    </el-breadcrumb>
    <div style id="small-container">
      <!--      搜索区域-->
      <div style="padding: 10px">
        <el-button type="primary" @click="addModel">新增模型</el-button>
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
            width="90">
        </el-table-column>
        <el-table-column
            prop="alg"
            label="采用模型"
            width="80">
        </el-table-column>
        <el-table-column
            prop="trainDataset"
            label="训练集"
            width="100">
        </el-table-column>
        <el-table-column
            prop="testDataset"
            label="测试集"
            width="100">
        </el-table-column>
        <el-table-column
            label="准确率"
            width="70">
          <template #default="scope">
            {{ fun(scope.row.accuracy) }}
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="250">
          <template #default="scope">
            <el-button
                type="text"
                size="small"
                @click="predict(scope.row)">
              训练
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
import router from "@/router";
import store from "@/store";

export default {
  name: "about",
  data(){
    return{
      addVisible:false,
      tableData:[],
      userAccount: store.state.userId
    }
  },
  mounted() {
    this.load()
  },
  methods:{
    //页面加载：参数是做模糊查询+分页显示的
    //具体为，一页显示Pagesize条查询结果，目前显示第pageNum页，按search做模糊查询
    load(){
      axios.get("http://192.168.43.55:8080/model/getModels",{
        params:{
          //userAccount:store.state.userAcount
          userAccount:this.userAccount
        }
      }).then(res =>{
        const {data} = res.data
        ElMessage({
          type: data,
        })
        if (data) {
          console.log(res)
          console.log("request")
          this.tableData = res.data.data
        }
      })
    },
    addModel(){
      router.push('/addModel')
    },
    fun(val){
      return Number(val).toFixed(2);
    },
    predict(row){
      if(row.alg == 'knn'){
        this.$router.push({
          name:'predict',
          params: {
            id:row.id,
            electNum:row.electNum,
            neighbor:row.neighbor,
            trainDataSetPath:row.trainDataset,//'PDE.csv',
            testDataSetPath:row.testDataset//'JDT.csv'
          }
        })
      }else if(row.alg == 'log'){
        this.$router.push({
          name:'history',
          params: {
            id:row.id,
            lr:row.lr,//'PDE.csv',
            lambda:row.lambda,
            epochNum:row.epoch,//'PDE.csv',
            trainDataSetPath:row.trainDataset,//'PDE.csv',
            testDataSetPath:row.testDataset//'JDT.csv'
          }
        })
      }else if(row.alg == 'bys'){
        axios.post("http://192.168.43.55:8080/alg/bysPredict", {
          id:row.id,
          trainDataSetPath: row.trainDataset,//'PDE.csv',
          testDataSetPath: row.testDataset,//'PDE.csv',
        }).then(res => {
          this.load()
        })
      }else if(row.alg == 'dt'){
        axios.post("http://192.168.43.55:8080/alg/dtPredict", {
          id:row.id,
          trainDataSetPath: row.trainDataset,//'PDE.csv',
          testDataSetPath: row.testDataset,//'PDE.csv',
        }).then(res => {
          this.load()
        })
      }
    },


  }
}
</script>


