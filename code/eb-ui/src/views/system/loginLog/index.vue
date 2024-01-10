<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24">
        <el-form :model="queryForm" ref="queryForm" size="small" :inline="true" label-width="80px">
          <el-form-item label="用户名">
            <el-input v-model="queryForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" type="primary" @click="getList">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-table v-loading="loading" :data="logList" border>
          <el-table-column label="编号" align="center" prop="id" min-width="50"/>
          <el-table-column label="用户名" align="center" prop="username" min-width="100"/>
          <el-table-column label="登录时间" align="center" prop="loginTime" min-width="100"/>
          <el-table-column label="登录状态" align="center" prop="loginStatus" min-width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.loginStatus === '成功' ? 'primary' : 'danger'">{{ scope.row.loginStatus }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
        <!--        分页-->
        <el-pagination
          style="float: right"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.pageNo"
          :page-size="queryForm.pageSize"
          :total="total"
          :page-sizes="[10, 20, 30, 40, 50]"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import {listLoginLog} from "@/api/system/loginLog";

export default {
  name: "LoginLog",
  data() {
    return {
      queryForm: {
        pageNo: 1,
        pageSize: 10,
        username: null,
      },
      loading: true,
      logList: [],
      total: 0
    };
  },
  created() {
    this.getList()
  },
  methods: {
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNo = val
      this.getList()
    },
    getList() {
      this.loading = true
      listLoginLog(this.queryForm).then(res => {
        this.logList = res.data.records
        this.total = res.data.total
        this.loading = false
      })
    },
    resetQuery() {
      this.queryForm = {
        pageNo: 1,
        pageSize: 10,
        username: null,
      }
      this.getList()
    }
  }
};
</script>
