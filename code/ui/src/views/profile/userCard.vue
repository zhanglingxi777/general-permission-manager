<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>用户信息</span>
    </div>
    <div class="text item">
      <el-descriptions class="margin-top" :column="1" border>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-picture-outline"></i>
            头像
          </template>
          <avatar-upload ref="avatarUpload" :avatar="userInfo.avatar" :username="userInfo.username" @avatarUploadRes="getAvatarUploadRes"/>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          {{ userInfo.username }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile"></i>
            手机号码
          </template>
          {{ userInfo.phone }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
          {{ userInfo.email }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-date"></i>
            创建时间
          </template>
          {{ userInfo.createTime }}
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>
</template>

<script>
import AvatarUpload from "@/components/AvatarUpload/index.vue";
import {updateUser} from "@/api/system/user";

export default {
  name: 'UserCard',
  components: {AvatarUpload},
  props: {
    // 用户信息
    userInfo: {
      default: {},
      type: Object
    }
  },
  methods: {
    getAvatarUploadRes(val) {
      // 更新用户头像数据
      let data = {
        id: this.userForm.id,
        avatar: val.data
      }
      updateUser(data).then(response => {
        this.$message.success("上传成功!")
        // 强制更新头像上传组件 刷新图片
        this.$refs.avatarUpload.$forceUpdate()
      })
    },
  }
}
</script>

<style scoped lang="scss">
.box-card {
  height: 500px !important;
}
</style>
