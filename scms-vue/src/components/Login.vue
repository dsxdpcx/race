<template>
  <div :style="bg" class="login_container">
    <!--登录块-->
    <div class="login_box">
      <div ref="vantaRef" style="width: 100%; height: 100vh"></div>
      <div class="title">赛 事 领 航</div>
      <!--logo-->
      <!-- <div class="avatar_box">
        <img src="../assets/logo.png" alt />
      </div> -->
      <!--表单-->
      <el-form
          ref="loginForm"
          :model="loginForm"
          :rules="loginRules"
          class="login_form"
          label-width="0"
      >
        <!--用户名-->
        <el-form-item>
          用户名：
          <el-input
              v-model="loginForm.username"
              prefix-icon="iconfont icon-denglu"
          ></el-input>
        </el-form-item>
        <!--密码-->
        <el-form-item>
          密码：
          <el-input
              v-model="loginForm.password"
              prefix-icon="iconfont icon-mima"
              type="password"
              @keyup.enter.native="submitForm('loginForm')"
          ></el-input>
        </el-form-item>
        <!--按钮-->
        <el-form-item class="btns">
          <el-button type="primary" @click="submitForm('loginForm')"
          >登录
          </el-button
          >
          <el-button type="primary" @click="resetForm('loginForm')"
          >重置
          </el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import * as THREE from "three";
import CLOUDS from "vanta/src/vanta.clouds";

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
      },
      loginRules: {
        username: [
          {required: true, message: "请输入用户名", trigger: "blur"},
          {
            min: 3,
            max: 30,
            message: "长度在 3 到 30 个字符",
            trigger: "blur",
          },
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"},
          {
            min: 1,
            max: 99,
            message: "长度在 1 到 99 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    this.vantaEffect = CLOUDS({
      el: this.$refs.vantaRef,
      THREE: THREE,
    });
    VANTA.CLOUDS({
      el: this.$refs.vantaRef,
      /*以下为样式配置*/
      mouseControls: true,
      touchControls: true,
      gyroControls: false,
      minHeight: 200.0,
      minWidth: 200.0,
      skyColor: 0x26b9f2,
      cloudColor: 0xb7c9e8,
      cloudShadowColor: 0x2f3a48,
      sunColor: 0x5c452f,
      sunGlareColor: 0xd4a798,
      sunlightColor: 0xdc9c59,
      speed: 1.2,
    });
  },
  beforeDestroy() {
    if (this.vantaEffect) {
      this.vantaEffect.destroy();
    }
  },
  methods: {
    /*处理登录*/
    submitForm(loginForm) {
      /*验证校验规则*/
      this.$refs["loginForm"].validate(async (valid) => {
        if (valid) {
          /*访问后台*/
          const _this = this;
          this.$http.post("/user/login", this.loginForm).then((res) => {
            if (res.data.status === 200) {
              // _this.$message.success("登录成功");
              const jwt = res.headers["authorization"];
              const user = res.data.data;
              _this.$store.commit("SET_TOKEN", jwt);
              _this.$store.commit("SET_USERINFO", user);
              /*路由跳转*/
              _this.$router.push("/home");
            } else {
              _this.$message.error(res.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    },
    resetForm(loginForm) {
      this.loginForm = {
        username: "",
        password: "",
      }
    },
  },
};
</script>

<style lang="less" scoped>
/*根节点样式*/
::v-deep .el-input__inner {
  background-color: transparent; // 设置为透明色
  outline: none; // 移除聚焦时的边框
  color: #fff; // 设置文字颜色，如果你背景是深色的话
}

::v-deep .el-input__inner:focus {
  border: 1px solid #dcdfe6; //
}
::v-deep .btns .el-button {
  background-color: transparent;
  border: none;
  color: #222222;
  @import url('https://fonts.googleapis.com/css2?family=Roboto&display=swap');
  font-family: 'SimSun', sans-serif;
  font-size: 16px;
}

.login_container {
  //background-color: #2b4b6b;
  height: 100%;
}

/*输入框样式*/
.login_box {
  width: 1650px;
  height: 600px;
  background-color: #999999;
  border-radius: 3px;
  position: absolute;

  .avatar_box {
    width: 130px;
    height: 130px;
    border: 1px solid #eee;
    border-radius: 50%;
    padding: 5px;
    box-shadow: 0 0 2px #ddd;
    position: absolute;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #eee;

    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      background-color: #eee;
    }
  }
}

.title {
  @import url('https://fonts.googleapis.com/css2?family=Roboto&display=swap');
  font-family: 'SimSun', sans-serif;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translateX(-50%);
  font-size: 50px;
  color: #fff;
}

.btns {
  display: flex;
  justify-content: flex-end;
  @import url('https://fonts.googleapis.com/css2?family=Roboto&display=swap');
  font-family: 'SimSun', sans-serif;
}

.login_form {
  position: absolute;
  bottom: 0%;
  width: 30%;
  padding: 0 10px;
  box-sizing: border-box;
  left: 50%;
  top: 80%;
  transform: translate(-50%, -50%);
  font-size: 16px;
  @import url('https://fonts.googleapis.com/css2?family=Roboto&display=swap');
  font-family: 'SimSun', sans-serif;
}
</style>