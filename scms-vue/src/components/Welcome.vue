<template>
  <el-container>
    <el-header >欢迎使用赛事领航</el-header>
    <el-main>
      <div class="el-main">
        <br>
        <br>
        <div class="container">
          <div class="score" style="height:400px;width:250px;overflow:auto;background:#EEEEEE;">
            <div class="block">
              <el-timeline>
                <el-timeline-item
                    v-for="(activity, index) in activities"
                    :key="index"
                    :color="'#00a6ff'"
                    :size="'large'"
                    :timestamp="activity.startTime">
                  {{activity.itemName}}
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
          <!--     图片展示区域 -->
          <div class="image-slider">
            <img :src="currentImage" alt="Slider Image">
          </div>

          <div class="item" style="height:400px;width:250px;overflow:auto;background:#EEEEEE;">
            <ul class="passage">
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【评论】谁的<span class="keywordfontcolor1052884">运动会</span>？</el-link><small>2014-10-25</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】<span class="keywordfontcolor1052884">运动会背后的那些人</span></el-link><small>2023-10-21</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】极限飞盘，趣味<span class="keywordfontcolor1052884">运动 </span></el-link><small>2023-10-21</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】第62届<span class="keywordfontcolor1052884">运动会</span>“首金”诞生</el-link><small>2023-10-20</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】文法健儿：精神不灭 薪火相传</el-link>><small>2023-10-20</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【言论】体育<span class="keywordfontcolor1052884">运动会</span>的育人启示</el-link><small>2021-10-27</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">“奔跑吧！华农”<span class="keywordfontcolor1052884">运动会</span>抢先看</el-link><small>2015-10-19</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">经管土管学院举办红色<span class="keywordfontcolor1052884">运动会</span></el-link><small>2011-04-30</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">我校举行教职工趣味<span class="keywordfontcolor1052884">运动会</span></el-link><small>2011-03-04</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】乐享器械<span class="keywordfontcolor1052884">运动</span>，锻炼酣畅淋漓</el-link><small>2023-10-20</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】<span class="keywordfontcolor1052884">运动</span>场上的食科人，青春与奋斗的交响曲</el-link><small>2023-10-21</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】周忆美、韦玮：<span class="keywordfontcolor1052884">运动</span>构成了我们的生活</el-link><small>2023-10-24</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】华中农业大学第62届<span class="keywordfontcolor1052884">运动会</span>闭幕：一人打破校纪录</el-link><small>2023-10-21</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">【<span class="keywordfontcolor1052884">运动会</span>】幕后工作者：场外的“竞赛”同样精彩</el-link><small>2023-10-22</small></li>
              <li><el-link type="primary" href="http://news.hzau.edu.cn/info/1013/16096.htm" target="_blank">我校在省大学生<span class="keywordfontcolor1052884">运动会</span>羽毛球比赛斩获佳绩</el-link><small>2018-05-24</small></li>
            </ul>
          </div>
        </div>
        <br>
        <br>
      </div>
    </el-main>
    <el-footer height="100px">
      <el-popover class="popover1"
                  placement="right"
                  width="400"
                  trigger="click">
        <el-table :data="gridData">
          <el-table-column width="150" property="name" label="姓名"></el-table-column>
          <el-table-column width="100" property="domain" label="区域"></el-table-column>
          <el-table-column width="300" property="phone" label="电话"></el-table-column>
        </el-table>
        <el-button slot="reference">志愿者分布</el-button>
      </el-popover>
      <el-popover
          placement="bottom"
          width="200"
          trigger="click"
      >
        <el-image src="../assets/hzu_gate.jpg" alt="观众座位分布图"></el-image>
        <el-button slot="reference">观众座位分布</el-button>
      </el-popover>
    </el-footer>
  </el-container>
</template>

<script>
import axios from "axios";

export default {
  name: "Welcome",
  data () {
    return {
      gridData: [{
        name: '王小虎',
        domain: '一运正东',
        phone: '14687795787'
      }, {
        name: '张小强',
        domain: '一运东北',
        phone: '19836968963'
      }, {
        name: '李晓倩',
        domain: '一运西北',
        phone: '19836489992'
      }, {
        name: '赵晓红',
        domain: '一运正西',
        phone: '13782458978'
      }],
      images: [

      ],
      currentIndex: 0,
      activities: [
      //     {
      //   content: '女子短跑100m',
      //   timestamp: '2018-04-12 20:46',
      //   size: 'large',
      //   color: '#00a6ff'
      // }, {
      //   content: '男子自由泳100m',
      //   timestamp: '2018-04-03 20:46',
      //   color: '#00a6ff'
      // }, {
      //   content: '女子铅球',
      //   timestamp: '2018-04-03 20:46',
      //   size: 'large',
      //   color: '#00a6ff'
      // }, {
      //   content: '男子铅球',
      //   timestamp: '2018-04-03 20:46',
      //   color: '#00a6ff'
      // },{
      //   content: '男子短跑100m',
      //   timestamp: '2018-04-03 20:46',
      //   color: '#00a6ff'
      // },
      //   {
      //     content: '男子铅球',
      //     timestamp: '2018-04-03 20:46',
      //     color: '#00a6ff'
      //   },{
      //     content: '男子短跑100m',
      //     timestamp: '2018-04-03 20:46',
      //     color: '#00a6ff'
      //   },
      //   {
      //     content: '男子铅球',
      //     timestamp: '2018-04-03 20:46',
      //     color: '#00a6ff'
      //   },{
      //     content: '男子短跑100m',
      //     timestamp: '2018-04-03 20:46',
      //     color: '#00a6ff'
      //   },
      //   {
      //     content: '男子铅球',
      //     timestamp: '2018-04-03 20:46',
      //     color: '#00a6ff'
      //   },{
      //     content: '男子短跑100m',
      //     timestamp: '2018-04-03 20:46',
      //     color: '#00a6ff'
      //   }
      ],
      noticeList: [],
      number: 0
    }
  },
  computed: {
    currentImage() {
      return this.images[this.currentIndex].pic;
    }
  },
  mounted () {
    this.startImageSlider();
  },
  created() {
    this.page();
    this.item();
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
            _this.images = data;
          });
    },
    async item() {
      const _this = this;
      axios
          .get("/item/querytimeItem")
          .then((res) => {
            let data = res.data.data;
            _this.activities = data;
          });
      _this.getTemplateOptions();

    },
    // startMove () {
    //   // eslint-disable-next-line
    //   let timer = setTimeout(() => {
    //     if (this.number === this.noticeList.length - 1) {
    //       this.number = 0;
    //     } else {
    //       this.number += 1;
    //     }
    //     this.startMove();
    //   }, 2000); // 滚动不需要停顿则将2000改成动画持续时间
    // }
    startImageSlider() {
      setInterval(() => {
        this.currentIndex = (this.currentIndex + 1) % this.images.length;
      }, 1000); // 切换时间间隔，单位为毫秒
    },

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
.el-footer {
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中元素 */
}
.popover1{
  padding-right: 20px;
}
.el-header {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}
.el-footer{
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 100px;
}
.el-header{
  font-weight:bold;
}
h3{
  margin:0 auto;
}
.container {
  display: flex;
  flex-wrap: nowrap; /* 防止换行 */
  justify-content: space-around; /* 水平居中对齐 */
  align-items: flex-start; /* 垂直方向上对齐到顶部 */
}
.image-slider {
  width: 100%;
  max-width: 600px;
  margin: 20px auto;
}
.image-slider img {
  width: 100%;
  height: 400px;
  object-fit: cover; /* 保持图片比例并填充 */
  display: block;
}
.mesage{
  margin: 0 auto;
  border-style:outset;
  border-color: #1f4b14;
  border-width:2px;
}

</style>