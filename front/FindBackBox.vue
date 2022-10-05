<template>
  <div>
    <el-form>
      <el-form-item label="账号">
        <el-input v-model="user.id" placeholder="Username"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="user.email" placeholder="Email"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="user.phone" placeholder="Phone"></el-input>
      </el-form-item>
    </el-form>
    <div style="color: rgba(44,62,80,0.63)">请尽可能多地填写正确的信息</div>
    <el-button type="danger" @click="findBack">找回密码</el-button>
  </div>
</template>

<script>
import axios from "axios";
import {ElMessage} from "element-plus";

export default {
  data() {
    return {
      user: {
        id: '',
        email: '',
        phone: ''
      }
    }
  },
  methods: {
    findBack() {
      axios.post('http://localhost:8000/user/retrieve_password', {
        id: this.user.id
      }).then(res => {
        const {success, message} = res.data
        ElMessage({
          type: success ? 'success' : 'error',
          message
        })
      })
    }
  }
}
</script>