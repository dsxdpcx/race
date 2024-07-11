<template>
  <div>
    <h3>欢迎使用赛事领航！！！</h3>


  <div class="textBox">
      <transition name="slide">
        <p class="text" :key="text.id">{{text.val.content}}</p>
      </transition>
  </div>

  <div class="block">
    <span class="demonstration"></span>
     <el-carousel height="300px">
      <el-carousel-item v-for="notice in noticeList" :key="notice.noticeId">
        <img :src="notice.pic">
      </el-carousel-item>
    </el-carousel>
  </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Welcome",
  data () {
    return {
      noticeList: [],
      number: 0
    }
  },
  computed: {
    text () {
      return {
        id: this.number,
        val: this.noticeList[this.number]
      }
    }
  },
  mounted () {
    this.startMove()
  },
  created() {
    this.page();
  },
  methods: {
    async page() {
      const _this = this;
      axios
          .get(
              "/notice/queryNotice"
          )
          .then((res) => {
            let data = res.data.data;
            _this.noticeList = data;
          });
    },
    startMove () {
      // eslint-disable-next-line
      let timer = setTimeout(() => {
        if (this.number === this.noticeList.length - 1) {
          this.number = 0;
        } else {
          this.number += 1;
        }
        this.startMove();
      }, 2000); // 滚动不需要停顿则将2000改成动画持续时间
    }

  }
};
</script>

<style lang="less" scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
h2 {
  padding: 20px 0
}
.textBox {
  width: 100%;
  height: 40px;
  margin: 0 auto;
  overflow: hidden;
  position: relative;
  text-align: center;
}
.text {
  width: 100%;
  position: absolute;
  bottom: 0;
}
.slide-enter-active, .slide-leave-active {
  transition: all 0.5s linear;
}
.slide-enter{
  transform: translateY(20px) scale(1);
  opacity: 1;
}
.slide-leave-to {
  transform: translateY(-20px) scale(0.8);
  opacity: 0;
}
</style>