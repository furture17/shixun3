<template>
  <div>
    <el-form>
      <el-form-item label="账号">
        <el-input v-model="user.id" placeholder="ID"></el-input>
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
    signIn() {
      axios.post('http://localhost:8000/admin/signIn', {
        id: this.user.id,
        password: this.user.password
      }, {})
          .then(res => {
            const {success, message} = res.data
            ElMessage({
              type: success ? 'success' : 'error',
              message
            })
            if (success) {
              store.commit('setUserId', this.user.id)
              store.commit("setRole",'admin')
              router.push('/index')
            }
          })
          .catch(err => {
            ElMessage.error(err.message)
          })
    }
  }
})
</script>