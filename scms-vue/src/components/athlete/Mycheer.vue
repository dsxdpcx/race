<template>
  <div>
    <!--导航-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>广播管理</el-breadcrumb-item>
      <el-breadcrumb-item>我的加油稿</el-breadcrumb-item>
    </el-breadcrumb>

    <!--项目列表主体-->
    <el-card>
      <el-row :gutter="25">
        <div style="float: left">
          <el-col :span="16">
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
          <!--添加按钮-->
          <el-col :span="4">
            <el-button type="primary" @click="addDialogVisible = true"
            >提交
            </el-button
            >
          </el-col>
          <!-- 添加对话框 -->
          <el-dialog :visible.sync="addDialogVisible" title="提交加油稿">
            <el-form ref="addCheerForm" :model="addCheerForm">
              <el-form-item label="加油稿内容">
                <el-input type="textarea" v-model="addCheerForm.content"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="addCheer">提交</el-button>
              </el-form-item>
            </el-form>
          </el-dialog>
        </div>
      </el-row>

      <!--项目列表 stripe隔行变色-->
      <!--参数运动员列表 stripe隔行变色-->
      <el-table :data="cheerlist" border stripe>
        <!--索引列-->
        <el-table-column label="用户Id" prop="userId"></el-table-column>
        <el-table-column label="加油稿Id" prop="cheerId"></el-table-column>
        <el-table-column label="加油稿内容" prop="content">
          <template slot-scope="scope">
            <span v-if="scope.row.content.length > 35">{{ scope.row.content.substring(0, 35) }}...</span>
            <span v-else>{{ scope.row.content }}</span>
            <el-button type="text" @click="showContent(scope.row)">详情</el-button>
          </template>
        </el-table-column>

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
<!--    <el-steps :active=actnum >-->
<!--      <el-step v-for="(item,index) in activities"-->
<!--               :key="index"-->
<!--               :title="item.content"-->
<!--               :description="item.timestamp"></el-step>-->

<!--    </el-steps>-->
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Mycheer",
  data() {
    return {
      addDialogVisible: false,
      addCheerForm: {
        content: "",
      },
      /*activities: [{
        content: '报名成功',
        timestamp: '',
        color: ''
      }, {
        content: '通过审核',
        timestamp: '',
        color: ''
      },],*/
      reverse: false,
      actnum: 0,
      cheerlist: [],
      scorers: [],
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
      // 对话框状态
      dialogTableVisible: false,
      dialogFormVisible: false,
    };
  },
  created() {
    this.page();
    this.getSeasons();
  },
  methods: {
    async page(isSelect) {
      if (isSelect === true) {
        this.queryInfo.currentPage = 1;
        this.queryInfo.pageSize = 10;
      }
      const _this = this;
      axios
          .get(
              "/cheer/querycheer?userId=" +this.userId
          )
          .then((res) => {
            let data = res.data.data;
            _this.cheerlist = data;
            /*_this.activities[0].timestamp = _this.cheerlist[0].cheersubmitTime;
            if(_this.activities[0].timestamp!='') {
              _this.actnum = 1;
            }
            _this.activities[1].timestamp = _this.cheerlist[0].cheershenheTime;
            if(_this.activities[1].timestamp!='') {
              _this.actnum = 2;
            }
            _this.queryInfo.currentPage = data.current;
            _this.total = data.total;
            _this.queryInfo.pageSize = data.size;

            _this.cheerlist.forEach((cheer, index) => {
              if (cheer.shenhe == 1) {
                cheer.shenhe = "已通过";
              } else if (cheer.shenhe == 0){
                cheer.shenhe = "未通过";
              }else {
                cheer.shenhe = "未审核";
              }
            });*/
          });
    },

    async addCheer() {
      // 使用Axios提交加油稿
      try {
        const response = await axios.post("/cheer/addcheer", {
          content: this.addCheerForm.content,
          userId: this.userId, // 从后端服务获取的当前用户ID
        });
        this.page(); // 更新表格数据
        this.$message.success("提交成功");
      } catch (error) {
        this.$message.error("提交失败");
      }
    },

//获取运动会届时
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
    showContent(row) {
      // 弹出一个对话框，显示完整的加油稿内容
      this.$alert(row.content, '详情', {
        confirmButtonText: '确定',
        callback: action => {
          if (action === 'confirm') {
            this.$message({
              type: 'success',
              message: '已显示详情'
            });
          } else {
            this.$message({
              type: 'info',
              message: '已取消'
            });
          }
        }
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