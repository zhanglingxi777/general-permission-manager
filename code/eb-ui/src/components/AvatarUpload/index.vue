<template>
  <el-upload
    class="avatar-uploader"
    :action="baseUrl + '/common/upload/avatar' + (username ? '/' + username : '')"
    :headers="{'Authorization': 'Bearer ' + getToken(), 'Cache-Control': 'no-cache'}"
    :show-file-list="false"
    :limit="1"
    :multiple="false"
    accept=".jpg,.png"
    :on-success="handleAvatarSuccess"
    :before-upload="beforeAvatarUpload">
    <img v-if="avatar" :src="baseUrl + '/common/file' + avatar + '?date=' + Date.now()" class="avatar">
    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
  </el-upload>
</template>
<script>
import {getToken} from "@/utils/auth";

export default {
  name: 'AvatarUpload',
  props: {
    avatar: {
      type: String,
      default: null
    },
    username: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
    }
  },
  methods: {
    getToken,
    handleAvatarSuccess(res, file) {
      this.$emit('avatarUploadRes', res)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isPng = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG && !isPng) {
        this.$message.error('上传头像图片只能是 JPG,PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return (isJPG || isPng) && isLt2M
    }
  }
}
</script>
<style lang="scss">
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
