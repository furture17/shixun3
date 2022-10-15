<template>
  <div>
    <el-form>
      <el-form-item label="账号">
        <el-input v-model.number="user.id" placeholder="ID"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="user.password" placeholder="Password" type="password"></el-input>
      </el-form-item>
    </el-form>
    <el-button type="primary" @click="signIn">登录</el-button>
  </div>
</template>

<script lang="ts">
import axios from "axios";
import {defineComponent} from "vue";
import {ElMessage} from "element-plus";

import store from "@/store";
import router from "@/router";

export default defineComponent({
  data() {
    return {
      user: {
        id: '',
        password: '',
      },
    }
  },
  methods: {
    signIn() {//h5556095v9.zicp.fun
      axios.post('http://192.168.43.55:8080/user/login', {//push,get
        userAccount: this.user.id,
        userPassword: this.user.password
      }, {})
          .then(res => {
            console.log('then')
            const {data, message} = res.data
            ElMessage({
              type: data,
              message
            })
              store.commit('setUserId', this.user.id)
              if(res.data.data.userRole=='0'){
                store.commit("setRole",'user')
              }else if(res.data.data.userRole=='1'){
                store.commit("setRole",'admin')
              }
              router.push('/index')
          })
          .catch(err => {
            console.log('catch')
            ElMessage.error(err.message)
          })
    }
  }
})
</script>