<template>
  <div class="app-container">
    <el-card class="box-card">
      <div class="text item">
        <el-row :gutter="10">
          <el-col :span="8">
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
                    <el-upload
                        class="avatar-uploader"
                        :action="baseUrl + '/common/upload/avatar'"
                        :headers="{'Authorization': 'Bearer ' + getToken()}"
                        :show-file-list="false"
                        :limit="1"
                        :multiple="false"
                        accept=".jpg,.png"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload">
                      <img v-if="userForm.avatar" :src="baseUrl + '/common/file' + userForm.avatar" class="avatar">
                      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      <i class="el-icon-user"></i>
                      用户名
                    </template>
                    {{ userForm.username }}
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      <i class="el-icon-mobile"></i>
                      手机号码
                    </template>
                    {{ userForm.phone }}
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      <i class="el-icon-message"></i>
                      邮箱
                    </template>
                    {{ userForm.email }}
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      <i class="el-icon-date"></i>
                      创建时间
                    </template>
                    {{ userForm.createTime }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-card>
          </el-col>

          <el-col :span="16">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>基本资料</span>
              </div>
              <div class="text item">
                <el-tabs v-model="activeName" @tab-click="handleClick">
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
                      <el-form-item>
                        <el-button type="primary">保存</el-button>
                        <el-button type="danger">关闭</el-button>
                      </el-form-item>
                    </el-form>
                  </el-tab-pane>
                  <el-tab-pane label="修改密码" name="updatePassword">
                    <el-form :model="resetPwdForm" :rules="resetPwdRules" ref="resetPwdForm" label-width="100px">
                      <el-form-item label="原密码" prop="oldPwd">
                        <el-input v-model="resetPwdForm.oldPwd"></el-input>
                      </el-form-item>
                      <el-form-item label="新密码" prop="newPwd">
                        <el-input v-model="resetPwdForm.newPwd"></el-input>
                      </el-form-item>
                      <el-form-item label="确认密码" prop="confirmPwd">
                        <el-input v-model="resetPwdForm.confirmPwd"></el-input>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary">保存</el-button>
                        <el-button type="danger">关闭</el-button>
                      </el-form-item>
                    </el-form>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {getUserInfo, updateUser} from "@/api/system/user";
import {getToken} from "@/utils/auth";

export default {
  name: 'Profile',
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
      userForm: {},
      resetPwdForm: {},
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
      resetPwdRules: {
        oldPwd: [
          {required: true, message: '原密码不能为空', trigger: 'blur'}
        ],
        newPwd: [
          {required: true, message: '新密码不能为空', trigger: 'blur'}
        ],
        confirmPwd: [
          {required: true, message: '确认密码不能为空', trigger: 'blur'}
        ],
      }
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
    this.getUser()
  },
  methods: {
    getToken,
    getUser() {
      getUserInfo(this.userId).then(response => {
        this.userForm = response.data
      })
    },
    handleClick(tab, event) {

    },
    handleAvatarSuccess(res, file) {
      if (res.code === 200) {
        this.userForm.avatar = res.data
        updateUser(this.userForm).then(response => {
          if (response.code === 200) {
            this.$message.success(res.msg)
            // 更新用户信息
            this.getUser()
          }
        })
      } else {
        this.$message.error(res.msg)
      }
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
<style>
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
