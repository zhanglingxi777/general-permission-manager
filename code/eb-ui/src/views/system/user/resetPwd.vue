<template>
  <el-row>
    <el-form ref="resetPwdForm" :model="resetPwdForm" :rules="resetPwdRules" label-width="100px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="原密码" prop="oldPwd">
            <el-input type="password" show-password v-model="resetPwdForm.oldPwd" maxlength="20" show-word-limit
                      placeholder="请输入原密码"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="新密码" prop="newPwd">
            <el-input type="password" show-password v-model="resetPwdForm.newPwd" maxlength="20" show-word-limit
                      placeholder="请输入新密码"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="确认新密码" prop="confirmPwd">
            <el-input type="password" show-password v-model="resetPwdForm.confirmPwd" maxlength="20" show-word-limit
                      placeholder="请再次输入新密码"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <span style="float: right">
      <el-button @click="closeResetPwd">取 消</el-button>
      <el-button type="primary" @click="submitResetPwd">确 定</el-button>
    </span>
  </el-row>
</template>
<script>
import {resetPwd} from "@/api/system/user";
import {encrypt} from '@/utils/aes'
import {removeToken} from "@/utils/auth";
export default {
  name: 'ResetPwd',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    let validate2Pwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('确认密码不能为空'))
      } else if (value !== this.resetPwdForm.newPwd) {
        callback(new Error('输入的两次密码不一致，请重新输入'))
      } else {
        callback()
      }
    }
    return {
      // 修改密码
      resetPwdForm: {
        oldPwd: '',
        newPwd: '',
        confirmPwd: ''
      },
      resetPwdRules: {
        oldPwd: [
          {required: true, message: "原密码不能为空", trigger: "blur"}
        ],
        newPwd: [
          {required: true, message: "新密码不能为空", trigger: "blur"}
        ],
        confirmPwd: [
          {required: true, message: "确认密码不能为空", trigger: "blur"},
          {validator: validate2Pwd, required: true, trigger: 'blur'}
        ]
      },
    }
  },
  methods: {
    submitResetPwd() {
      this.$refs.resetPwdForm.validate(valid => {
        if (valid) {
          let data = {
            userId: this.$store.getters.userId,
            oldPwd: encrypt(this.resetPwdForm.oldPwd),
            newPwd: encrypt(this.resetPwdForm.newPwd),
            confirmPwd: encrypt(this.resetPwdForm.confirmPwd)
          }
          resetPwd(data).then(response => {
            if (response.code === 200) {
              this.$message.success(response.msg)
              this.closeResetPwd()
              // 修改完密码 需要重新登录
              removeToken()
              setTimeout(() => {
                this.$router.push('/login')
              }, 500)
            } else {
              this.$message.error(response.msg)
            }
          })
        }
      })
    },
    // 关闭修改密码页面
    closeResetPwd() {
      this.$emit('closeDialog', false)
    }
  }
}
</script>

<style scoped lang="scss">

</style>
