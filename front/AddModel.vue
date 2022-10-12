<template>
  <div style="padding: 10px" >
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>新增模型</el-breadcrumb-item>
    </el-breadcrumb>
    <div style id="small-container">
      <el-form label-width="100px">
        <el-form-item label="名称">
          <el-input v-model="model.name" class="m-2" size="default"></el-input>
        </el-form-item>
        <el-form-item label="选择模型" >
          <el-select v-model="model.alg" class="m-2" placeholder="模型" @change="changeValue()">
            <el-option label="逻辑回归" value="log"></el-option>
            <el-option label="KNN" value="knn"></el-option>
            <el-option label="贝叶斯" value="bys"></el-option>
            <el-option label="决策树" value="dt"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择模型">
          <div v-show="mp1">
            <el-form-item label="学习率">
              <el-input v-model="model.lr"></el-input>
            </el-form-item>
            <el-form-item label="正则化系数">
              <el-input v-model="model.lambda"></el-input>
            </el-form-item>
            <el-form-item label="训练轮数">
              <el-input v-model="model.epoch"></el-input>
            </el-form-item>
          </div>
          <div v-show="mp2">
            <el-form-item label="electNum">
              <el-input v-model="model.electNum"></el-input>
            </el-form-item>
            <el-form-item label="neighbor">
              <el-input v-model="model.neighbor"></el-input>
            </el-form-item>
          </div>
        </el-form-item>
        <el-form-item label="选择训练集">
          <el-select v-model="model.trainDataset" class="m-2" placeholder="数据集" >
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="选择测试集">
          <el-select v-model="model.testDataset" class="m-2" placeholder="数据集">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"/>
          </el-select>
        </el-form-item>
      </el-form>
      <span>确定添加该新模型？</span>
      <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="newmodel">确 定</el-button>
      </span>
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
      model: {
        name:'',
        userAccount:store.state.userId,
        alg:'',
        trainDataset:'',
        testDataset:'',
        accuracy:'',
        lr:'',
        lambda:'',
        epoch:'',
        electNum:'',
        neighbor:'',
      },
      dataset:[],
      options:[],
      mp1:'',
      mp2:'',
    }
  },
  mounted() {
    this.load()
  },
  methods:{
    load(){
      axios.get("http://192.168.43.55:8080/file/getFiles",{
        params: {
          //userAccount:store.state.userId
          userAccount:this.model.userAccount
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
              if (this.dataset.pde == '1') {this.options = [
                {value:'jdt',label:'jdt'},
                {value:'lucene',label:'lucene'},
                {value:'pde',label:'pde'}]}
              else {this.options = [
                {value:'jdt',label:'jdt'},
                {value:'lucene',label:'lucene'},]}
            }else{
              if(this.dataset.pde=='1') {this.options=[
                {value:'jdt',label:'jdt'},
                {value:'pde',label:'pde'}]}
              else {this.options=[{value:'jdt',label:'jdt'}]}
            }
          }else{
            if(this.dataset.lucene=='1') {
              if (this.dataset.pde == '1') {this.options = [
                {value:'lucene',label:'lucene'},
                {value:'pde',label:'pde'}]}
              else {this.options = [{value:'lucene',label:'lucene'},]}
            }else{
              if(this.dataset.pde=='1') {this.options=[{value:'pde',label:'pde'}]}
            }
          }
        }
      })
    },
    newmodel(){
      axios.post("http://192.168.43.55:8080/model/saveModel",{
        name:this.model.name,
        userAccount:this.model.userAccount,
        alg:this.model.alg,
        trainDataset:this.model.trainDataset,
        testDataset:this.model.testDataset,
        accuracy:this.model.accuracy,
        lr:this.model.lr,
        lambda:this.model.lambda,
        epoch:this.model.epoch,
        electNum:this.model.electNum,
        neighbor:this.model.neighbor,
      }).then(res =>{
        const {data, message} = res.data
        ElMessage({
          type: data,
          message
        })
        if (data) {
          console.log(res)
          console.log("request")
          this.tableData = res.data.records
        }
      })
      this.$router.push({
        name:'model',
      })
    },
    changeValue(){
      if(this.model.alg=='log'){
        this.mp2=false;
        this.mp1=true;
        this.load();
      }else if(this.model.alg=='knn'){
        this.mp1=false;
        this.mp2=true;
        this.load();
      }
    },
  }
}
</script>

<style scoped>
#small-container{
  /*background-color: rgba(124, 229, 214, 0.5);*/
  background-color: rgba(250, 250, 250, 0.9);
  border-radius: 10px;
  border: 1px solid rgba(44, 62, 80, 0.4);
  width:450px;
  /*align-items: center;*/
  margin: 30px;
  padding: 15px;
}

</style>-->


