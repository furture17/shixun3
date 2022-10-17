<template>
  <div :style="{backgroundImage:``}" id="bg" >
    <div id="container">
      <h1>{{ activeName.toUpperCase() }}</h1>
      <div id="login-box">
        <el-tabs v-model="activeName" type="border-card" stretch tab-position="bottom">
          <el-tab-pane label="登录" name="signIn">
            <sign-in-box></sign-in-box>
          </el-tab-pane>
          <el-tab-pane label="找回密码" name="findBack">
            <find-back-box></find-back-box>
          </el-tab-pane>
          <el-tab-pane label="注册" name="register">
            <register-box></register-box>
          </el-tab-pane>
          <el-tab-pane label="管理员登录" name="adminSign">
            <adminbox></adminbox>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import SignInBox from "@/components/authority/SignInBox";
import FindBackBox from "@/components/authority/FindBackBox";
import RegisterBox from "@/components/authority/RegisterBox";
import Adminbox from "@/components/authority/Adminbox";
import {defineComponent} from "vue";

export default defineComponent({
  components: {
    SignInBox,
    FindBackBox,
    RegisterBox,
    Adminbox
  },
  data() {
    return {
      user: {
        name: '',
        password: ''
      },
      activeName: 'signIn'
    }
  }
})
</script>

<style scoped>
#container {
 position: center;
  width: 40%;
}

#login-box {
  border-radius: 10px;
  background-color: #ffffff;
  margin: 5px;
  padding: 5px;

}

#bg {
  width: 100vw;
  height: 100vh;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;


  background-repeat: no-repeat;
  background-size: 100% 100%;
  opacity: 0.90;
  text-align: center;

}
</style>