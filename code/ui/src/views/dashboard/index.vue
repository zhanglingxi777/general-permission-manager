<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12">
        <div>
          <h2>灵溪通用权限管理系统</h2>
          <p class="introduce">
            本项目使用前后端分离技术，前端基于<el-link href="https://panjiachen.github.io/vue-element-admin-site/zh/" type="primary">vue-element-admin</el-link>框架，
            后端使用<el-tag type="primary">SpringBoot</el-tag>，<el-tag type="primary">SpringMVC</el-tag>开发，实现基础的RBAC，所有代码开源至
            <el-link href="https://gitee.com/zhanglingxi777/general-permission-manager" type="primary">Gitee</el-link>。该项目只是本人为完成JavaWeb作业而开发的基础框架，
            不保证所有代码都没有<el-tag type="danger">Bug</el-tag>。
            <br/>
            若该项目对大家有些参考价值，还请给作者点一个<el-tag type="warning">Star</el-tag>。
          </p>
        </div>
      </el-col>
      <el-col :span="12" style="height: 600px;">
        <span class="update-title">更新日志</span>

        <el-collapse accordion>
          <el-collapse-item title="v1.1" name="1.1">
            <div>改造权限认证流程，集成Spring Security安全框架框架</div>
          </el-collapse-item>
          <el-collapse-item title="v1.0" name="1.0">
            <div>灵溪通用权限管理系统开发完毕，实现基础的RBAC功能</div>
          </el-collapse-item>
        </el-collapse>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import UserCard from "@/views/profile/userCard.vue";
import {getUserInfo} from "@/api/system/user";

export default {
  name: 'Dashboard',
  components: {UserCard},
  data() {
    return {
      userInfo: {}
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'roles'
    ])
  },
  created() {
    if (this.userId) {
      getUserInfo(this.userId).then(response => {
        this.userInfo = response.data
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.introduce {
  font-size: 18px;
}

.update-title {
  display: inline-block;
  height: 20px;
  line-height: 20px;
  margin: 10px 0;
  font-weight: bold;
  font-size: 18px;
}

.password-icon {
  width: 50px;
  height: 50px;
  color: black;
}
</style>
