<template>
  <div class="dailySchdule">
    <!-- 选择运动会组件 -->
    <div style="float: left">
<!--      <el-col :span="16">-->
<!--        <el-select-->
<!--            v-model="selectSeasonId"-->
<!--            filterable-->
<!--            placeholder="请选择运动会"-->
<!--            @change="page(true)"-->
<!--        >-->
<!--          <el-option-->
<!--              v-for="item in allSeasonOptions"-->
<!--              :key="item.seasonId"-->
<!--              :label="item.seasonName"-->
<!--              :value="item.seasonId"-->
<!--          >-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-col>-->
      <el-col :span="16">
      <el-input
          v-model="football.name"
          clearable
          placeholder="请输入赛事名称"
          @clear="page"
          @keyup.enter.native="page"
      >
        <!--搜索按钮-->
        <el-button
            slot="append"
            icon="el-icon-search"
            @click="page"
        ></el-button>
      </el-input>
      </el-col>
    </div>
    <!-- 其他元素 -->
    <div id="game1">
      <p>决<br>赛</p>
    </div>
    <div id="game2">
      <p>半<br>决<br>赛</p>
    </div>
    <div id="game3">
      <p>四<br>分<br>之<br>一<br>决<br>赛</p>
    </div>
    <div id="game4">
      <p>第<br>四<br>轮</p>
    </div>
    <div style="font-size:15px;margin-top:-20px;">
      <vue2-org-tree
          id="matches"
          :data="treeData.data"
          :horizontal=false
          :collapsable=false
          :label-class-name="labelClassName"
          :render-content="renderContent"
          @on-node-mouseover="onMouseover"
      />
    </div>
  </div>
</template>


<style>
.org-tree-node-label-inner:hover {
  background-color: #60b7df !important;
}
</style>

<style scoped>
#matches{
  /*position: absolute;*/
  left: 350px;
  width: calc(100% - 350px);
  margin-top: 0px;
  background-color: transparent;
}
#game1, #game2, #game3, #game4 {
  position: absolute;
  font: italic 1em Georgia, serif;
  text-shadow: 1px;
  font-size: 20px;
  background-color: rgb(227, 223, 223);
  writing-mode: lr-tb;
  text-align: center;
  width: 70px;
  border-radius: 25px;
  font-weight: bold;
}
#game1 { top: 70px; left: 500px; height: 100px; }
#game2 { top: 310px; left: 350px; height: 120px; }
#game3 { top: 490px; left: 350px; height: 190px; }
#game4 { top: 750px; left: 350px; height: 120px; }
</style>
<script>
import axios from "axios";
import Vue2OrgTree from 'vue2-org-tree';
export default {
  game: 1,
  name: 'Ranking',
  data () {
    return {
      components: {
        Vue2OrgTree
      },
      addDialogVisible: false,
      reverse: false,
      actnum: 0,
      scorers: [],
      cheerlist:[],
      football:{
        name:"",
      },
      itemDetail: [],
      //所有运动会届时列表
      allSeasonOptions: [],
      //选择的届时
      selectSeasonId: "",
      userId: JSON.parse(localStorage.getItem("user")).userId,
      queryInfo: {
        currentPage: 1,
        pageSize: 10,
        query: "",
      },
      total: 0,
      lineType: 'solid',
      span1: {
        color: 'red'
      },
      'treeData': {
        basicInfo: { id: null, label: '---null' },
        basicSwitch: false,
        data: {
          id: 1,
          player1: {
            name: 'N. Djokovic',
            score: '6 7 7'
          },
          player2: {
            name: 'S. Tsitsipas',
            score: '3 6 6'
          },
          children: [
            {
              id: 2,
              player1: {
                name: 'S. Tsitsipas',
                score: '7 6 6 6'
              },
              player2: {
                name: 'K. Khachanov',
                score: '6 4 7 3'
              },
              children: [
                {
                  id: 4,
                  player1: {
                    name: 'K. Khachanov',
                    score: '7 6 3'
                  },
                  player2: {
                    name: 'S. Korda',
                    score: '6 3 0'
                  },
                  children: [
                    {
                      id: 8,
                      player1: {
                        name: 'K. Khachanov',
                        score: '6 6 7'
                      },
                      player2: {
                        name: 'Y. Nishioka',
                        score: '0 0 6'
                      }
                    },
                    {
                      id: 9,
                      player1: {
                        name: 'S. Korda',
                        score: '3 6 6 1 7'
                      },
                      player2: {
                        name: 'H. Hurkacz',
                        score: '6 3 2 6 6'
                      }
                    }
                  ]
                },
                {
                  id: 5,
                  player1: {
                    name: 'S. Tsitsipas',
                    score: '6 7 6'
                  },
                  player2: {
                    name: 'J. Lehecka',
                    score: '3 6 4'
                  },
                  children: [
                    {
                      id: 10,
                      player1: {
                        name: 'S. Tsitsipas',
                        score: '6 6 3 4 6'
                      },
                      player2: {
                        name: 'J. Sinner',
                        score: '4 4 6 6 3'
                      }
                    },
                    {
                      id: 11,
                      player1: {
                        name: 'J. Lehecka',
                        score: '4 6 7 7'
                      },
                      player2: {
                        name: 'F. Auger-Aliassime',
                        score: '6 3 6 6'
                      }
                    }
                  ]
                }
              ]
            },
            {
              id: 3,
              player1: {
                name: 'N. Djokovic',
                score: '7 6 6'
              },
              player2: {
                name: 'T. Paul',
                score: '5 1 2'
              },
              children: [
                {
                  id: 6,
                  player1: {
                    name: 'N. Djokovic',
                    score: '6 6 6'
                  },
                  player2: {
                    name: 'A. Rublev',
                    score: '1 2 4'
                  },
                  children: [
                    {
                      id: 12,
                      player1: {
                        name: 'A. Rublev',
                        score: '6 3 6 4 7'
                      },
                      player2: {
                        name: 'H. Rune',
                        score: '3 6 3 6 6'
                      }
                    },
                    {
                      id: 13,
                      player1: {
                        name: 'N. Djokovic',
                        score: '6 6 6'
                      },
                      player2: {
                        name: 'A. de Minaur',
                        score: '2 1 2'
                      }
                    }
                  ]
                },
                {
                  id: 7,
                  player1: {
                    name: 'T. Paul',
                    score: '7 6 5 6'
                  },
                  player2: {
                    name: 'B. Shelton',
                    score: '6 3 7 4'
                  },
                  children: [
                    {
                      id: 14,
                      player1: {
                        name: 'B. Shelton',
                        score: '6 6 6 7 6'
                      },
                      player2: {
                        name: 'J. Wolf',
                        score: '7 2 7 6 2'
                      }
                    },
                    {
                      id: 15,
                      player1: {
                        name: 'T. Paul',
                        score: '6 4 6 7'
                      },
                      player2: {
                        name: 'R. Bautista Agut',
                        score: '2 6 2 5'
                      }
                    }
                  ]
                }
              ]
            }
          ]
        }
      },
      // labelClassName: '白色', // 默认颜色
      scrollTreeStyle: 'width:100%;'
    }
  },
  created () {

    this.page();
  },

  methods: {
    async page() {
      const _this = this;
      axios
          .post("/racefootball/showRaceTable",_this.football)
          .then((res) => {
          if (res.data.status != 200) {
              return _this.$message.error(res.data.msg);
            }
            _this.$message.success("操作成功");
            let data = res.data.data;
            console.log(data)
            _this.treeData.data=data;
          });
    },
    async getSeasons() {
      const _this = this;
      axios
          .get("/season/querySeason?query=&currentPage=1&pageSize=999999999")
          .then((res) => {
            let data = res.data.data.records;
            data.push({
              seasonId: 0,
              seasonStatus: 0,
              seasonName: "所有运动会",
            })
            data.forEach((item, index) => {
              if (item.seasonStatus != 0) {
                _this.selectSeasonId = item.seasonId
              }
            })
            _this.allSeasonOptions = data;
            _this.page();
          });
    },
    async getSchedule() {
      const _this = this;
      axios
          .post("/racefootballshow/RaceTable"+this.football)
          .then((res) => {
             if (res.data.status != 200) {
              return _this.$message.error(res.data.msg);
            }
            let data = res.data.data;
            _this.treeData.data=data;
            _this.page();
          });
    },
    handleSizeChange(newSize) {
      const _this = this;
      _this.queryInfo.pageSize = newSize;
      _this.page();
    },
    handleCurrentChange(newPage) {
      const _this = this;
      _this.queryInfo.currentPage = newPage;
      _this.page();
    },
    // 渲染节点
    renderContent (h, data) {
      return (
         // <div className="tree1" style="width: 100px; height: 100px;">

            <div className="tree1" >
            <div>
              <div class="playerRow1" >
                <div class="playerRowNameWrapper">
                  <p class="playerRowTeam" style="color:#0091d2 !important;">{data.player1.name}</p>
                </div>
                <div class="playerRowStatusWrapper">
                  <span class="playerRowStatus">√</span>
                  <span class="playerRowScore">{data.player1.score}</span>
                </div>
              </div>
              <hr/>
              <div class="playerRow2">
                <div class="playerRowNameWrapper">
                  <p class="playerRowTeam">{data.player2.name}</p>
                </div>
                <div class="playerRowStatusWrapper">
                  <span class="playerRowStatus"></span>
                  <span class="playerRowScore">{data.player2.score}</span>
                </div>
              </div>
            </div>
          </div>
      )
    },

    onMouseout (e, data) {
      this.basicSwitch = false
    },
    // 鼠标移入
    onMouseover (e, data) {
      // alert('选中当前比赛')
    }
  }
}
</script>
