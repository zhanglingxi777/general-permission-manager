<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="8">
        <UserCard :user-info="userForm"/>
      </el-col>

      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <div class="text item">
            <el-tabs v-model="activeName">
              <el-tab-pane label="基本资料" name="baseInfo">
                <el-form :model="userForm" :rules="userRules" ref="userForm" label-width="100px">
                  <el-form-item label="用户姓名" prop="realName">
                    <el-input v-model="userForm.realName"></el-input>
                  </el-form-item>
                  <el-form-item label="手机号码" prop="phone">
                    <el-input v-model="userForm.phone"></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱" prop="email">
                    <el-input v-model="userForm.email"></el-input>
                  </el-form-item>
                  <el-form-item label="性别" prop="gender">
                    <el-radio-group v-model="userForm.gender">
                      <el-radio label="男">男</el-radio>
                      <el-radio label="女">女</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-form>
                <span style="float: right;">
                  <el-button type="primary" @click="handleUpdateUser">确 定</el-button>
                  <el-button @click="$router.push('/dashboard')">取 消</el-button>
                </span>
              </el-tab-pane>
              <el-tab-pane label="修改密码" name="updatePassword">
                <reset-pwd @closeDialog="$router.push('/dashboard')"/>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {getUserInfo, updateUser} from "@/api/system/user";
import {getToken} from "@/utils/auth";
import AvatarUpload from "@/components/AvatarUpload/index.vue";
import ResetPwd from "@/views/system/user/resetPwd.vue";
import UserCard from "@/views/profile/userCard.vue";

export default {
  name: 'Profile',
  components: {UserCard, ResetPwd, AvatarUpload},
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
      userForm: {},
      activeName: 'baseInfo',
      userRules: {
        realName: [
          {required: true, message: '用户姓名不能为空', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '手机号码不能为空', trigger: 'blur'}
        ],
        email: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'}
        ]
      },
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'name',
      'avatar',
      'roles'
    ])
  },
  created() {
    if (this.userId) {
      getUserInfo(this.userId).then(response => {
        this.userForm = response.data
      })
    }
  },
  methods: {
    getToken,
    handleUpdateUser() {
      let data = {
        id: this.userForm.id,
        realName: this.userForm.realName,
        phone: this.userForm.phone,
        email: this.userForm.email,
        gender: this.userForm.gender
      }
      updateUser(data).then(response => {
        if (response.code === 200) {
          this.$message.success(response.msg)
          this.getUser()
        } else {
          this.$message.error(response.msg)
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>

</style>
