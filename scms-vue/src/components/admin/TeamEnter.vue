<template>
  <div>
    <!--导航-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>报名管理</el-breadcrumb-item>
      <el-breadcrumb-item>团队赛事报名</el-breadcrumb-item>
    </el-breadcrumb>

    <!--项目列表主体-->
    <el-card>
      <!--搜索区域-->
      <el-row :gutter="25">
        <el-col :span="5">
          <!--搜索添加-->
        <el-col :span="20">
          <el-select
              v-model="selectItemId"
              filterable
              placeholder="请选择项目"
              @change="page(true)"
          >
            <el-option
                v-for="item in allItemOptions"
                :key="item.itemId"
                :label="item.itemName"
                :value="item.itemName"
            >
            </el-option>
          </el-select>
        </el-col>

          <el-col :span="4">
            <el-button type="primary" @click="signTeam"
            >报名
            </el-button
            >
          </el-col>
        </el-col>
        <div style="float: left">
          <el-col>
            <el-select
                v-model="selectSeasonId"
                filterable
                placeholder="请选择运动会"
                @change="page(true)"
            >
              <el-option
                  v-for="item in allSeasonOptions"
                  :key="item.seasonId"
                  :label="item.seasonName"
                  :value="item.seasonId"
              >
              </el-option>
            </el-select>
          </el-col>
        </div>
        <div style="float: left">
          <el-col>
            <el-select
                v-model="selectTeamId"
                filterable
                placeholder="请选择团队"
                @change="page(true)"
            >
              <el-option
                  v-for="item in allTeamOptions"
                  :key="item.teamId"
                  :label="item.teamName"
                  :value="item.teamName"
              >
              </el-option>
            </el-select>
          </el-col>
        </div>
      </el-row>
      <!--项目列表 stripe隔行变色-->
      <el-table :data="itemList" border stripe>
        <!--索引列-->

        <el-table-column type="index"></el-table-column>
        <el-table-column label="赛事名称" prop="eventName"></el-table-column>
        <el-table-column label="参赛队名" prop="teamName"></el-table-column>
<!--        <el-table-column label="队伍组号" prop="groupName"></el-table-column>-->
<!--        <el-table-column label="操作" prop="state">-->
<!--          <template slot-scope="scope">-->
<!--            &lt;!&ndash;详情&ndash;&gt;-->

<!--            <el-button-->
<!--                icon="el-icon-tickets"-->
<!--                size="mini"-->
<!--                type="primary"-->
<!--                :isabled="scope.row.process==='semifianls'||(scope.row.process==='finals'&&scope.row.catalog.length!==1)"-->
<!--                @click="-->
<!--                signItem(scope.row);-->
<!--              "-->
<!--            >报名-->
<!--            </el-button-->
<!--            >-->
<!--          </template>-->
<!--        </el-table-column>-->
      </el-table>
      <!--分页组件-->
      <div>
        <el-pagination
            :current-page="queryInfo.currentPage"
            :page-size="queryInfo.pageSize"
            :page-sizes="[5, 10, 20, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>
    </el-card>


  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "TeamEnter",
  data() {
    return {

      userList: [],
      // selectUids:[],

      itemList: [],
      scorers: [],
      itemDetail: [],
//所有运动会届时列表
      allSeasonOptions: [],
      allItemOptions: [],
     allTeamOptions: [],
      //选择的届时
      selectSeasonId: "",
      selectItemId: "",
      selectTeamId: "",

      signItemInfo: {
        user: {
          userId: "",
        },
        item: {itemId: ""},
      },

      addAthletesItemForm: {
        item: {itemId: ""},
        user: {userId: "",},
        userIds: "",
      },

      addCaipanForm: {
        userId:"",
        name:"",
        signTime:"",
        phone:""

      },
      //select远程搜索加载中
      loading: false,
      addTeamEnter: {
        eventName: "",
        teamName: "",
      },
      queryInfo: {
        currentPage: 1,
        pageSize: 10,
        query: "",
      },
      total: 0,
      // 对话框状态
      dialogTableVisible: false,
      dialogFormVisible: false,
    };
  },
  created() {
    this.getSeasons();
    this.getItem();
    this.getTeam();
  },
  methods: {
    async page(isSelect) {
      if (isSelect === true) {
        this.queryInfo.currentPage = 1;
        this.queryInfo.pageSize = 10;
      }
      const _this = this;
      axios
          .get("/teamEnter/queryTeamEnter?eventName="+_this.selectItemId+"&queryInfo=", {params: _this.queryInfo})
          .then((res) => {
            let data = res.data.data;
            _this.itemList = data.records;
            _this.queryInfo.currentPage = data.current;
            _this.total = data.total;
            _this.queryInfo.pageSize = data.size;
          });
    },


//获取运动会届时
    async getSeasons() {
      const _this = this;
      axios
          .get("/season/querySeason?query=&currentPage=1&pageSize=999999999")
          .then((res) => {
            let data = res.data.data.records;
            data.forEach((item, index) => {
              if (item.seasonStatus != 0) {
                _this.selectSeasonId = item.seasonId
              }
            })
            _this.allSeasonOptions = data;
            _this.page();

          });
    },

    async getItem() {
      const _this = this;
      axios
          .get("/item/queryItem3?query=&currentPage=1&pageSize=999999999")
          .then((res) => {
            let data = res.data.data.records;
            data.push({
              itemId: 0,
              itemName: "所有项目",
            });
            _this.allItemOptions = data;
            _this.page()
          });
    },
    async getTeam() {
      const _this = this;
      axios
          .get("/team/queryTeam?query=&currentPage=1&pageSize=999999999")
          .then((res) => {
            let data = res.data.data.records;

            _this.allTeamOptions = data;
            _this.page()
          });
    },
    async getItemDetail(id) {
      const _this = this;
      axios.get("/item/getItem?itemId=" + id).then((res) => {
        let data = res.data.data;
        _this.itemDetail = [];
        _this.itemDetail.push(data);
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

    async signTeam() {
      const _this = this;

      const confirmResult = await _this
          .$confirm("确定报名吗", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .catch((err) => err);
      if (confirmResult !== "confirm") {
        return _this.$message.info("已取消报名");
      }
     axios.post("/teamEnter/addTeamEnter", _this.addTeamEnter).then((res) => {
        if (res.data.status != 200) {
          return _this.$message.error(res.data.msg);
        }
        _this.$message.success("报名成功");
        _this.page();
      });
    },

    //多人项目报名
    async signItemAthletes() {
      const _this = this;
      const confirmResult = await _this
          .$confirm("确定报名参赛该项目吗", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .catch((err) => err);
      if (confirmResult !== "confirm") {
        return _this.$message.info("已取消报名");
      }
      _this.addAthletesItemForm.userIds = _this.addAthletesItemForm.userIds.toString();
      _this.addAthletesItemForm.user.userId = JSON.parse(localStorage.getItem("user")).userId;
      axios.post("/athlete/addAthlete", _this.addAthletesItemForm).then((res) => {
        if (res.data.status != 200) {
          _this.addAthletesItemForm.userIds = [];
          return _this.$message.error(res.data.msg);
        }
        _this.$message.success("报名成功");
        _this.dialogTableVisible = false;
        _this.page();
      });
    },
    addDialogClosed() {
      const _this = this;
      _this.$refs.addFormRef.resetFields();
    },

    async searchUser(queryName) {
      const _this = this;
      _this.loading = true;
      axios
          .get("/user/queryUser?currentPage=1&pageSize=999999999&query=" + queryName)
          .then((res) => {
            let data = res.data.data.records;
            data.forEach((item, index) => {
              item.nickname = item.nickname + "      " + item.team.teamName;
            });
            _this.userList = data;
            this.loading = false;
          });
    },


  },
};
</script>

<style lang="less" scoped>
.el-breadcrumb {
  margin-bottom: 15px;
  font-size: 17px;
}

.myTable {
  border-collapse: collapse;
  margin: 0 auto;
  text-align: center;
}

.myTable td,
.myTable th {
  border: 1px solid #cad9ea;
  color: #666;
  height: 40px;
}
</style>