<template>
  <!--引入布局-->
  <el-container class="home-container">
    <el-header>
      <div class="div">
        <img alt class="img" src="../assets/hzu_logo.jpg"/>
        <span class="span">赛事领航</span>
      </div>
      <div>
        <span style="font-size:16px; margin-right:30px; font-weight:700">{{currentUser.nickname}}</span>

        <el-button size="mini" type="info" @click="editDialogVisible = true"
        >修改密码
        </el-button
        >
        <el-button size="mini" type="info" @click="gopersonal">个人中心</el-button>
        <el-button size="mini" type="info" @click="logout">安全退出</el-button>


      </div>
      <div>
        <div class="button-container">
          <el-button class="icon-button" @click="gotoMessage">
            <i class="el-icon-message"></i>
          </el-button>
          <span v-if="unreadCount > 0" class="red-dot">{{ unreadCount }}</span>
        </div>
        <el-button size="mini" type="primary" @click="showForm">反馈</el-button>
        <div v-if="isFormVisible" class="form-container">
          <el-form label-position="top">
            <el-form-item label="请选择身份：">
              <el-button @click="navigateToOption('option1')">管理员</el-button>
              <el-button @click="navigateToOption('option2')">用户</el-button>
              <el-button @click="navigateToOption('cancel')">退出</el-button>
            </el-form-item>
          </el-form>
        </div>
    </div>

    </el-header>
    <el-container>
      <el-aside :width="iscollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapase">
          <i v-show="!this.iscollapse" class="el-icon-d-arrow-left"></i>
          <i v-show="this.iscollapse" class="el-icon-d-arrow-right"></i>
        </div>
        <el-menu
            :collapse="iscollapse"
            :collapse-transition="false"
            :default-active="activePath"
            :router="isrouter"
            active-text-color="#409eff"
            background-color="#545c64"
            text-color="#fff"
        >
          <!--一级菜单 -->
          <el-submenu
              v-for="item in menuList"
              v-show="
              item.mainmenu.type == 0 ||
              currentUser.userType == item.mainmenu.type
            "
              :key="item.mainmenu.mainmenuId"
              :index="item.mainmenu.path"
              :id="item.mainmenu.mainmenuId"
          >
            <template slot="title">
              <i :class="iconsObj[item.mainmenu.mainmenuId]"></i>
              <span style="margin-left:1em">{{ item.mainmenu.title }}</span>
            </template>
            <!--二级菜单-->
            <el-menu-item
                v-for="sub in item.mlist"
                v-show="sub.type == 0 || currentUser.userType == sub.type"
                :key="sub.id"
                :index="item.mainmenu.path + sub.path"
                :id="sub.id"
                @click="savePathState(item.mainmenu.path + sub.path)"
            >
              <template slot="title">
                <!-- <i :class="iconsObj[sub.id]"></i> -->
                <span>{{ sub.title }}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <transition mode="out-in" name="fade">
          <router-view></router-view>
        </transition>
      </el-main>
    </el-container>

    <!--修改用户区域-->
    <el-dialog
        :visible.sync="editDialogVisible"
        title="修改密码"
        width="40%"
        @close="editDialogClosed"
    >
      <el-form
          ref="editFormRef"
          :model="editForm"
          class="demo-ruleForm"
          label-width="180px"
      >
        <el-form-item label="请输入旧密码">
          <el-input v-model="editForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="请输入新密码">
          <el-input v-model="editForm.newPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item label="请再次确认密码">
          <el-input
              v-model="editForm.newPasswordConfirm"
              type="password"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="changePassword">确定</el-button>
        <el-button @click="editDialogVisible = false">取消</el-button>

      </span>
    </el-dialog>
  </el-container>
</template>

<script>
import axios from "axios";

export default {
  name: "Home",
  // 页面加载
  data() {
    return {
      // 菜单列表
      menuList: [],
      unreadCount: 0,
      editForm: {
        username: JSON.parse(localStorage.getItem("user")).username,
        password: "",
        newPassword: "",
        newPasswordConfirm: "",
      },
      userId: JSON.parse(localStorage.getItem("user")).userId,

      editDialogVisible: false,

      //当前用户
      currentUser: "",

      iscollapse: false,
      isrouter: true,
      activePath: "/welcome",
      iconsObj: {
        100: "iconfont icon-tiyuketeshuchuli",
        200: "iconfont icon-bumen",
        300: "iconfont icon-peixunhuodongdongyuandahui",
        400: "iconfont icon-shebeiyanshoudan",
        500: "iconfont icon-yuangong",
        600: "iconfont icon-zhishijingsai",
        700: "iconfont icon-chengjishouji",
        800: "iconfont icon-canjiaxiaowaijingsai",
        900: "iconfont icon-dangtuanzhishijuesai",
        1000: "iconfont icon-xinxibeian",
        1100:"iconfont icon-jiaoshijieyong",
        1200:"iconfont icon-xinxibeian",
        1300:"iconfont icon-shiwuzhaoling",
        1400:"iconfont icon-baoleiji",
      },
    };
  },
  created() {
    this.getMenuList();
    this.currentUser = JSON.parse(localStorage.getItem("user"));
    this.activePath = window.sessionStorage.getItem("path");
  },
  methods: {
    showForm() {
      this.isFormVisible = true;
    },
    navigateToOption(option) {
      // 根据选项做不同的页面导航处理
      switch (option) {
        case 'option1':
          // 执行页面导航，例如使用路由或直接跳转链接
          window.open('https://mail.qq.com/cgi-bin/frame_html?sid=D_rxfIz13NhXVQNl&r=47fdaad6138e15a76130566887d642bc', '_blank');
          break;
        case 'option2':
          window.open('mailto:1443164389@qq.com', '_blank');
          break;
        case 'cancel':
          // 执行退出逻辑，例如清除token等
          window.open('/Home','self');
          break;
        default:
          break;
      }

    },

    logout() {
      const _this = this;
      _this.$http
          .get("/user/logout", {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          })
          .then((res) => {
            _this.$store.commit("REMOVE_INFO");
            _this.$router.push("/login");
            _this.savePathState("/")
          });
    },
    gotoMessage() {
      this.$router.push("/message/mymessage");
    },fetchUnreadCount() {
      // 模拟获取未读消息数量
      const _this = this;
      axios
          .get(
              "/message/queryMessage?userId=" + this.userId
          )
          .then((res) => {
            let data = res.data.data;
            _this.unreadCount = data;
          });
    },

    gopersonal(){
      this.$router.push('/personal');
    },

    //修改密码
    changePassword() {
      const _this = this;
      if (
          _this.editForm.password == "" ||
          _this.editForm.newPassword == "" ||
          _this.editForm.newPassword == ""
      ) {
        return _this.$message.info("请输入完整");
      }
      if (_this.editForm.newPassword != _this.editForm.newPasswordConfirm) {
        _this.editForm.newPassword = "";
        _this.editForm.newPasswordConfirm = "";
        return _this.$message.info("两次密码输入不一致");
      }

      _this.$http.put("/user/changePwd", _this.editForm).then((res) => {
        if (res.data.status != 200) {
          _this.editForm.newPassword = "";
          _this.editForm.newPasswordConfirm = "";
          return _this.$message.info("修改失败,请检查密码是否正确");
        }
        _this.$message.info("修改密码成功，请重新登录");
        _this.$store.commit("REMOVE_INFO");
        _this.$router.push("/login");
      });
    },

    //获取导航菜单方法
    async getMenuList() {
      const _this = this;
      _this.$http.get("menu").then((res) => {
        //如果路径为401
        if (window.location.pathname != "/401") {
          if (res.status === 200) {
            _this.menuList = res.data.data;
            _this.$message.success(
                " 欢迎您! " + JSON.parse(localStorage.getItem("user")).nickname
            );
          } else {
            _this.$message.error("获取列表失败");
          }
        }
      });
    },
    // 控制伸缩
    toggleCollapase() {
      const _this = this;
      _this.iscollapse = !_this.iscollapse;
    },
    // 保存路径
    savePathState(activePath) {
      // 放入session
      window.sessionStorage.setItem("path", activePath);
      this.activePath = activePath;
    },

    editDialogClosed() {
      const _this = this;
      _this.$refs.editFormRef.resetFields();
    },
  },
  mounted() {
    // 组件挂载时启动定时任务
    this.IntervalID = setInterval(this.fetchUnreadCount, 1000);
  }
};
</script>

<style lang="less" scoped>
.home-container {
  height: 100%;
}

.el-header {
  background-color: #2b4b6b;
  display: flex;
  justify-content: space-between;
  padding-left: 0%;
  align-items: center;
  color: #ffffff;
  font-size: 20px;
}

.div {
  display: flex;
  align-items: center;
}

.span {
  margin-left: 15px;
  //@import url('https://fonts.googleapis.com/css2?family=Roboto&display=swap');
  //font-family: 'SimSun', sans-serif;
  font: 1em Georgia, serif;
}

.el-aside {
  background-color: #444444;
}

.el-menu {
  border-right: none;
}

.el-main {
  background-color: #eaedf1;
}

.el-submenu .el-menu-item {
  padding-left: 70px!important; /* 根据需要调整这个值来改变缩进 */
}

.img {
  width: 55px;
  height: 55px;
}

/*|||按钮样式*/
.toggle-button {
  background-color: #4a5064;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer; /*显示小手*/
}

.fade-enter {
  opacity: 0;
}

.fade-leave {
  opacity: 1;
}

.fade-enter-active {
  transition: opacity 0.3s;
}

.fade-leave-active {
  opacity: 0;
  transition: opacity 0.3s;
}
.button-container {
  position: relative;
  display: inline-block;
}


.icon-button {
  padding: 0;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent; /* 移除按钮的背景色 */
  border: none; /* 移除按钮的边框 */
}
.icon-button i {
  font-size: 24px; /* 放大图标 */
  color: #409EFF; /* 设置图标颜色 */
}
.red-dot {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 5px 8px;
  font-size: 12px;
  line-height: 1;
  text-align: center;
}
</style>