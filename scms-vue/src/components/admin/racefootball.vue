<template>
  <div>
    <!--导航-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>足球赛程管理</el-breadcrumb-item>
      <el-breadcrumb-item>足球赛程信息</el-breadcrumb-item>
    </el-breadcrumb>
    
    <!--足球赛程列表主体-->
    <el-card>
      <!--搜索区域-->
      <el-row :gutter="25">
        <el-col :span="5">
          <!--搜索输入框-->
          <el-input
              v-model="queryInfo.query"
              clearable
              placeholder="请输入搜索内容"
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

        <!--添加按钮-->
        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible = true"
          >添加赛程
          </el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="success" @click="addraceDialogVisible1 = true"
          >生成八分之一决赛
          </el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="success" @click="addraceDialogVisible2 = true"
          >生成四分之一决赛
          </el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="success" @click="addraceDialogVisible3 = true"
          >生成半决赛
          </el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="success" @click="addraceDialogVisible4 = true"
          >生成决赛
          </el-button>
        </el-col>
      </el-row>
      
      <!--足球赛程列表-->
      <el-table :data="racefootball" border stripe>
        <!--索引列-->
        <el-table-column type="index"></el-table-column>
        <el-table-column label="赛程编号" prop="schedule_id"></el-table-column>
        <el-table-column label="赛程名称" prop="name"></el-table-column>
        <el-table-column label="比赛时间" prop="time"></el-table-column>
        <el-table-column label="地点" prop="location"></el-table-column>
        <el-table-column label="裁判" prop="referee"></el-table-column>
        <el-table-column label="A队" prop="team_a"></el-table-column>
        <el-table-column label="A队得分" prop="a_score"></el-table-column>
        <el-table-column label="B队" prop="team_b"></el-table-column>
        <el-table-column label="B队得分" prop="b_score"></el-table-column>
        <el-table-column label="胜队" prop="winner"></el-table-column>
        <el-table-column label="赛程性质" prop="match_class"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <!--修改按钮-->
            <el-button
                icon="el-icon-edit"
                size="mini"
                type="primary"
                @click="showEditDialog(scope.row.schedule_id)"
            ></el-button>
            <!--删除按钮-->
            <el-button
                icon="el-icon-delete"
                size="mini"
                type="danger"
                @click="deleterace(scope.row.schedule_id)"
            ></el-button>
            <!-- 判断胜负按钮 -->
            <!-- 判断甲队获胜按钮 -->
            
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
    
    <!--添加赛程对话框-->
    <el-dialog
        :visible.sync="addDialogVisible"
        title="添加赛程"
        width="40%"
        @close="addDialogClosed"
    >
      <el-form
          ref="addFormRef"
          :model="addForm"
          class="demo-ruleForm"
          label-width="80px"
      >
        <el-form-item label="赛程名称">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="比赛时间">
          <el-date-picker
              v-model="addForm.time"
              placeholder="选择时间"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item label="地点">
          <el-input v-model="addForm.location"></el-input>
        </el-form-item>
        <el-form-item label="裁判">
          <el-input v-model="addForm.referee"></el-input>
        </el-form-item>
        <el-form-item label="A队">
          <el-input v-model="addForm.team_a"></el-input>
        </el-form-item>
        <el-form-item label="B队">
          <el-input v-model="addForm.team_b"></el-input>
        </el-form-item>
        <el-form-item label="赛程性质">
          <el-input v-model="addForm.match_class"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addrace">确定</el-button>
        <el-button @click="addDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>

     <!--生成八分之一决赛对话框-->
    <el-dialog
        :visible.sync="addraceDialogVisible1"
        title="生成八分之一决赛"
        width="40%"
        @close="addraceDialogClosed1"
    >
      <el-form
          ref="addFormRef"
          :model="addForm"
          class="demo-ruleForm"
          label-width="80px"
      >
      <el-form-item label="赛程名称">
        <el-input v-model="addForm.name"></el-input>
      </el-form-item>
        <el-form-item label="比赛时间">
          <el-date-picker
              v-model="addForm.time"
              placeholder="选择时间"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addracetable">确定</el-button>
        <el-button @click="addraceDialogVisible1 = false">取消</el-button>
      </span>
    </el-dialog>

    <!--生成四分之一决赛对话框-->
    <el-dialog
        :visible.sync="addraceDialogVisible2"
        title="生成四分之一决赛"
        width="40%"
        @close="addraceDialogClosed2"
    >
      <el-form
          ref="addFormRef"
          :model="addForm"
          class="demo-ruleForm"
          label-width="80px"
      >
        <el-form-item label="赛程名称">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="比赛时间">
          <el-date-picker
              v-model="addForm.time"
              placeholder="选择时间"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addracetable">确定</el-button>
        <el-button @click="addraceDialogVisible2 = false">取消</el-button>
      </span>
    </el-dialog>

    <!--生成半决赛对话框-->
    <el-dialog
        :visible.sync="addraceDialogVisible3"
        title="生成半决赛"
        width="40%"
        @close="addraceDialogClosed3"
    >
      <el-form
          ref="addFormRef"
          :model="addForm"
          class="demo-ruleForm"
          label-width="80px"
      >
        <el-form-item label="赛程名称">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="比赛时间">
          <el-date-picker
              v-model="addForm.time"
              placeholder="选择时间"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addracetable">确定</el-button>
        <el-button @click="addraceDialogVisible3 = false">取消</el-button>
      </span>
    </el-dialog>

    <!--生成决赛对话框-->
    <el-dialog
        :visible.sync="addraceDialogVisible4"
        title="生成决赛"
        width="40%"
        @close="addraceDialogClosed4"
    >
      <el-form
          ref="addFormRef"
          :model="addForm"
          class="demo-ruleForm"
          label-width="80px"
      >
        <el-form-item label="赛程名称">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="比赛时间">
          <el-date-picker
              v-model="addForm.time"
              placeholder="选择时间"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addracetable">确定</el-button>
        <el-button @click="addraceDialogVisible4 = false">取消</el-button>
      </span>
    </el-dialog>

    <!--修改赛程对话框-->
    <el-dialog
        :visible.sync="editDialogVisible"
        title="修改赛程"
        width="40%"
        @close="editDialogClosed"
    >
      <el-form
          ref="editFormRef"
          :model="editForm"
          class="demo-ruleForm"
          label-width="80px"
      >
      
        <el-form-item label="赛程名称">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="比赛时间">
          <el-date-picker
              v-model="addForm.time"
              placeholder="选择时间"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
          >
        </el-date-picker>
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-input v-model="editForm.location"></el-input>
        </el-form-item>
        <el-form-item label="裁判" prop="referee">
          <el-input v-model="editForm.referee"></el-input>
        </el-form-item>
        <el-form-item label="A队" prop="team_a">
          <el-input v-model="editForm.team_a"></el-input>
        </el-form-item>
        <el-form-item label="A队得分" prop="a_score">
          <el-input v-model="editForm.a_score"></el-input>
        </el-form-item>
        <el-form-item label="B队" prop="team_b">
          <el-input v-model="editForm.team_b"></el-input>
        </el-form-item>
        <el-form-item label="B队得分" prop="b_score">
          <el-input v-model="editForm.b_score"></el-input>
        </el-form-item>
        <el-form-item label="赛程性质" prop="match_class">
          <el-input v-model="editForm.match_class"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editSchedule">确定</el-button>
        <el-button @click="editDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "racefootball",
  data() {
    return {
      racefootball: [],
      queryInfo: {
        currentPage: 1,
        pageSize: 10,
        query: "",
      },
      total: 0,
      addDialogVisible: false,
      addraceDialogVisible1:false,
      addraceDialogVisible2:false,
      addraceDialogVisible3:false,
      addraceDialogVisible4:false,
      editDialogVisible: false,
      addForm: {
        name: "",
        time: "",
        location: "",
        referee: "",
        team_a: "",
        team_b: "",
        match_class: "",
      },
      editForm: {
        name: "",
        time: "",
        location: "",
        referee: "",
        team_a: "",
        a_score:"",
        team_b: "",
        b_score:"",
        match_class: "",
      },
    };
  },
  created() {
    this.page();
  },
  methods: {
    async page() {
      const _this = this;
      axios
          .get("/racefootball/queryrace?queryInfo=", {params: _this.queryInfo})
          .then((res) => {
            let data = res.data.data;
            _this.racefootball = data.records;
             console.log(_this.racefootball);
            _this.queryInfo.currentPage = data.current;
            _this.total = data.total;
            _this.queryInfo.pageSize = data.size;
          });
    },
    addrace() {
      const _this = this;
      axios.post("/racefootball/addrace", _this.addForm).then((res) => {
        if (res.data.status != 200) {
          return _this.$message.error(res.data.msg);
        }
        _this.$message.success("操作成功");
        _this.addDialogVisible = false;
        _this.page();
      });
    },
    addracetable() {
      const _this = this;
      axios.post("/racefootball/addracetable", _this.addForm).then((res) => {
        if (res.data.status != 200) {
          return _this.$message.error(res.data.msg);
        }
        _this.$message.success("操作成功");
        _this.addDialogVisible = false;
        _this.page();
      });
    },
    async deleterace(id) {
      const _this = this;
      const confirmResult = await _this
          .$confirm("此操作将永久删除赛程，是否继续？", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
          .catch((err) => err);
      if (confirmResult !== "confirm") {
        return _this.$message.info("已取消删除");
      }
      axios.delete("/racefootball/deleterace?schedule_id=" + id).then((res) => {
        if (res.status == 200) {
          _this.$message.success("删除成功");
          _this.addDialogVisible = false;
          _this.page();
        } else {
          _this.$message.error("删除失败");
        }
      });
    },
    showEditDialog(schedule_id) {
      const _this = this;
      axios
          .get("/racefootball/getSchedule?schedule_id=" + schedule_id)
          .then((res) => {
            if (res.status == 200) {
              _this.editDialogVisible = true;
              _this.editForm = res.data.data;
            }
          });
    },
    editSchedule() {
      const _this = this;
      console.log(_this.editForm);
      axios.post("/racefootball/updateSchedule", _this.editForm).then((res) => {
        console.log(_this.editForm);
        if (res.data.status != 200) {
          return _this.$message.error(res.data.msg);
        }
        _this.$message.success("操作成功");
        _this.editDialogVisible = false;
        _this.page();
      });
    },
    handleSizeChange(size) {
      this.queryInfo.pageSize = size;
      this.page();
    },
    handleCurrentChange(page) {
      this.queryInfo.currentPage = page;
      this.page();
    },
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },
  },
};
</script>

<style scoped>
.el-breadcrumb {
  margin-bottom: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>
