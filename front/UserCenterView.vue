<template>
  <div class="hello">
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户中心</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="flex">
      <el-descriptions title="用户信息" :column="3" border>
        <el-descriptions-item
            label="用户名"
            label-align="right"
            align="center"
            label-class-name="my-label"
            class-name="my-content"
            width="80px"
        >{{ user.name }}
        </el-descriptions-item
        >
        <el-descriptions-item label="权限" label-align="right" align="center"
        >{{ user.role }}
        </el-descriptions-item
        >
        <el-descriptions-item
            label="电话"
            label-align="right"
            width="180px"
        >{{ user.phone }}
        </el-descriptions-item
        >
        <el-descriptions-item label="邮箱" label-align="right" align="center"
        >{{ user.email }}
        </el-descriptions-item
        >
      </el-descriptions>
    </div>
  </div>
</template>
<!-- input 的accept属性可以用来解决格式筛选问题 -->


<script>
import axios from "axios";
import {ElMessage} from "element-plus";
import store from "@/store";
import router from "@/router";

export default {
  name: 'HelloWorld',
  created() {
    this.user.name = store.state.userId
    this.user.role = store.state.userRole
    this.load()
  },
  data() {
    return {
      user: {
        role: '',
        name: '',
        email: '',
        phone: '',
      },
      imgStr: require('@/assets/lg.png'),
      errorStr: '',
      changeVisible: false,
      passwordVisible: false,
      inputPassword: '',
      key: ''
    }
  },
  methods: {
    //加载用户信息
    load() {//填了3个相同url，后端原url文件没有user/updateInfo
      axios.get("http://192.168.43.55:8080/user/info", {
        headers: {id: this.user.id}
      })
          .then(res => {
            const {success, message, data} = res.data
            ElMessage({
              type: success ? 'success' : 'error',
              message
            })
            if (success) {
              this.user.name = data.name
              this.user.email = data.email
              this.user.phone = data.phone

            }
          })
          .catch(err => ElMessage.error(err.message))
    },

    onchangeImgFun(e) {
      var file = e.target.files[0]
      console.log(file)
      // 获取图片的大小，做大小限制有用
      let imgSize = file.size
      console.log(imgSize)
      var _this = this // this指向问题，所以在外面先定义
      // 比如上传头像限制5M大小，这里获取的大小单位是b
      if (imgSize <= 50 * 1024) {
        // 合格
        _this.errorStr = ''
        console.log('大小合适')
        // 开始渲染选择的图片
        // 本地路径方法 1
        // this.imgStr = window.URL.createObjectURL(e.target.files[0])
        // console.log(window.URL.createObjectURL(e.target.files[0]))                  // 获取当前文件的信息

//base64方法 2
        var reader = new FileReader()
        reader.readAsDataURL(file) // 读出 base64
        reader.onloadend = function () {
          // 图片的 base64 格式, 可以直接当成 img 的 src 属性值
          var dataURL = reader.result
          console.log(dataURL)
          _this.imgStr = dataURL
          // 下面逻辑处理
        }
      } else {
        console.log('大小不合适')
        _this.errorStr = '图片大小超出范围'
      }
    }
  }
}


</script>

<style scoped>

.user-header {
  position: relative;
  display: inline-block;
}

.user-header-com {
  width: 144px;
  height: 144px;
  display: inline-block;
}

.header-upload-btn {
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  /* 通过定位把input放在img标签上面，通过不透明度隐藏 */
}

.tip {
  font-size: 14px;
  color: #666;
}

/* error是用于错误提示 */
.error {
  font-size: 12px;
  color: tomato;
  margin-left: 10px;
}

.hello {
  background-color: white;
  padding: 40px;
  border-radius: 20px;
  width: 60%;
  height: 70%;
}

.mb {
  position: center;
}
</style>
