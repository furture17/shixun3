<template>


  <div class="common-layout">
    <el-container>
      <el-header class="common-header flex-float">
        <div class="flex">
          <!--          <img class="logo" src="../../assets/lg.png" alt="">-->
          <h1 class="title">软件缺陷预测 管理系统</h1>
        </div>
        <el-button type=danger @click="loginOut">退出</el-button>
      </el-header>
      <el-container>
        <el-aside class="common-aside" width="200px">
          <el-menu
              background-color="none"
              text-color="#fff"
              :router="true"
          >
            <el-sub-menu index="1">
              <template #title>
                <el-icon>
                  <Avatar/>
                </el-icon>
                <span>用户管理</span>
              </template>
              <el-menu-item-group>
                <div v-if="role=='user'">
                  <el-menu-item index="/userCenter">用户中心</el-menu-item>
                </div>
                <div v-if="role=='admin'">
                  <el-menu-item index="/user">用户列表</el-menu-item>
                </div>
<!--                <el-menu-item index="/user">用户列表</el-menu-item>-->
              </el-menu-item-group>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>
                <el-icon>
                  <Compass/>
                </el-icon>
                <span>工作台</span>
              </template>
<!--              <el-menu-item-group>-->
<!--                <el-menu-item index="/usercenter">数据集1</el-menu-item>-->
<!--              </el-menu-item-group>-->
              <el-menu-item-group>
                <el-menu-item index="/dataset">数据集</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group>
                <el-menu-item index="/model">模型预测</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group>
                <el-menu-item index="/addModel">新增模型</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group>
                <el-menu-item index="/predict">预测结果可视化</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group>
                <el-menu-item index="/history">损失函数</el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>
            <el-sub-menu index="3">
              <template #title>
                <el-icon>
                  <Coin/>
                </el-icon>
                <span>关于</span>
              </template>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main>

          <!-- router-view -->
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>

</template>


<script>

import router from "@/router";
import store from "@/store";

export default {
  name: "layout",
  setup() {

    const role = store.state.userRole;


    const loginOut = () => {

      router.push({
        path: "/authority"
      })

    }
    return {
      loginOut,
      role

    }
  }
}
</script>

<style>
.el-container {
  height: 100vh;
  overflow: hidden;
}

.common-header {

  background: rgb(39, 45, 53);
}

.common-aside {
  background: rgb(48, 55, 65);
}

.logo {
  width: 80px;
  height: 60px;
}

.title {
  color: #fff;
  position: absolute;
}

.el-main {
  background: #efefef;
}
</style>