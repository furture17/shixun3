<template>
  <div>
    <el-form>
      <el-form-item label="账号">
        <el-input v-model="user.id" placeholder="ID"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="user.password" @input="securityLevel = calcSecurityLevel()" placeholder="Password"
                  type="password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-steps :active="securityLevel" style="width: 100%">
          <el-step title="弱" description="密码强度：1"></el-step>
          <el-step title="较弱" description="密码强度：2"></el-step>
          <el-step title="中" description="密码强度：3"></el-step>
          <el-step title="较强" description="密码强度：4"></el-step>
          <el-step title="强" description="密码强度：5"></el-step>
        </el-steps>
      </el-form-item>
      <el-form-item label="确认">
        <el-input v-model="user.repeat" placeholder="Repeat Password" type="password"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="user.email" placeholder="Email"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="user.phone" placeholder="Phone"></el-input>
      </el-form-item>
    </el-form>
    <el-button type="success" @click="register">注册</el-button>
  </div>
</template>

<script lang="ts">

import {defineComponent} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";

const minSecurityLevel = 3;
const minPasswordLength = 6;

export default defineComponent({
  data() {
    return {
      user: {
        id: '',
        password: '',
        repeat: '',
        email: '',
        phone: '',
      },
      securityLevel: 0,
      registerPermission: {}
    }
  },
  methods: {
    // 计算密码复杂的
    calcSecurityLevel() {
      let strength = 0;
      // 密码长度
      if (this.user.password.length >= minPasswordLength) {
        strength += 1;
      }
      // 小写字母
      if (this.user.password.match(/([a-z])+/)) {
        strength += 1;
      }
      // 数字
      if (this.user.password.match(/([0-9])+/)) {
        strength += 1;
      }
      // 大写字母
      if (this.user.password.match(/([A-Z])+/)) {
        strength += 1;
      }
      // 特殊字符
      if (this.user.password.match(/([*_@?.;"':+=()!`%/\\])+/)) {
        strength += 1;
      }
      return strength;
    },
    register() {
      axios.post('http://192.168.43.55:8080/user/register', {
        userAccount: this.user.id,
        userPassword: this.user.password,
        checkPassword: this.user.repeat,
        //email: this.user.email,
        //phone: this.user.phone,
      }).then(res => {
        const {data, message} = res.data
        ElMessage({
          type: data,
          message
        })
      }).catch(err => ElMessage.error(err.message))
    }
  }
})
</script>