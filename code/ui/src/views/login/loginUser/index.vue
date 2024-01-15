<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24">
        <el-table v-loading="loading" :data="loginUserList" border style="width: 100%">
          <el-table-column align="center" type="index" min-width="50"></el-table-column>
          <el-table-column align="center" prop="username" label="用户名" min-width="180"></el-table-column>
          <el-table-column align="center" prop="lastLoginTime" label="登录时间" min-width="180"></el-table-column>
          <el-table-column align="center" label="操作" min-width="180">
            <template slot-scope="scope">
              <el-button icon="el-icon-delete" type="danger" @click="logoutUser(scope.row)">注销登录</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import {forceLogout, listLoginUser} from "@/api/system/user";

export default {
  name: 'LoginUser',
  data() {
    return {
      loginUserList: [],
      loading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listLoginUser().then(response => {
        this.loginUserList = response.data
        this.loading = false
      })
    },
    logoutUser(row) {
      forceLogout(row.username).then(response => {
        if (response.code === 200) {
          this.$message.success(response.msg)
          this.getList()
        } else {
          this.$message.error(response.msg)
        }
      })
    }
  }
}
</script>
<style scoped lang="scss">

</style>
