<template>
  <div class="app-container">
    <el-row>
      <!-- 用户信息     -->
      <el-col :span="24">
        <el-row>
          <el-form ref="queryForm" :model="queryForm" size="small" :inline="true" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="queryForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="姓名">
              <el-input v-model="queryForm.realName" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="电话">
              <el-input v-model="queryForm.phone" placeholder="请输入电话"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="getList">查询</el-button>
              <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-button type="success" size="small" icon="el-icon-plus" @click="handlerAdd">新增</el-button>
            <el-button type="primary" size="small" icon="el-icon-upload2" @click="openImport = true">导入</el-button>
            <el-button type="warning" size="small" icon="el-icon-download" @click="handleOutput">导出</el-button>
          </el-col>
        </el-row>

        <el-row>
          <el-table v-loading="loading" :data="userList" border style="width: 100%">
            <el-table-column label="头像" align="center" prop="avatar" show-overflow-tooltip min-width="120">
              <template slot-scope="scope">
                <el-avatar v-if="scope.row.avatar" shape="square"
                           :src="baseUrl + '/common/file' + scope.row.avatar + '?date=' + Date.now()"></el-avatar>
              </template>
            </el-table-column>
            <el-table-column label="用户名" align="center" prop="username" show-overflow-tooltip min-width="120"/>
            <el-table-column label="姓名" align="center" prop="realName" show-overflow-tooltip min-width="120"/>
            <el-table-column label="电话" align="center" prop="phone" show-overflow-tooltip min-width="120"/>
            <el-table-column label="邮箱" align="center" prop="email" show-overflow-tooltip min-width="120"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                             min-width="350">
              <template slot-scope="scope">
                <el-button type="primary" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
                <el-button type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
                <el-button type="success" icon="el-icon-help" @click="handleAssign(scope.row)">分配角色</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            style="float: right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryForm.pageNo"
            :page-size="queryForm.pageSize"
            :page-sizes="[10, 20, 30, 40, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </el-row>
      </el-col>
    </el-row>

    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="title" :visible="open" width="800px" @close="cancel" append-to-body>
      <el-tabs type="border-card" v-model="activeName">
        <el-tab-pane name="baseInfo" label="基本信息">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="用户名" prop="username">
                  <el-input maxlength="20" show-word-limit :disabled="Boolean(form.id)" v-model="form.username"
                            placeholder="请输入用户名"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="姓名" prop="realName">
                  <el-input maxlength="50" show-word-limit v-model="form.realName" placeholder="请输入姓名"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="电话" prop="phone">
                  <el-input maxlength="50" show-word-limit v-model="form.phone" placeholder="请输入电话"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                  <el-input maxlength="50" show-word-limit v-model="form.email" placeholder="请输入邮箱"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="form.gender">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12" v-if="form.id">
                <el-form-item label="头像" prop="avatar">
                  <avatar-upload ref="avatarUpload" :username="form.username" :avatar="form.avatar" @avatarUploadRes="getAvatarUploadRes"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-tag type="danger" v-if="!form.id">
                  注: 密码默认 <code style="font-weight: bold">123456</code>
                </el-tag>
              </el-col>
              <span style="float: right">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
              </span>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane v-if="form.id" name="updatePwd" label="修改密码">
          <reset-pwd @closeDialog="cancel"/>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 分配用户角色对话框   -->
    <el-dialog :title="title" :visible="openAssign" width="800px" @close="openAssign = false" append-to-body>
      <el-table ref="assignRoleTable" :data="assignRoleList" :loading="loading" border style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="roleCode" label="角色编码" min-width="180"></el-table-column>
        <el-table-column prop="roleName" label="角色名称" min-width="180"></el-table-column>
        <el-table-column prop="remark" label="备注" min-width="250"></el-table-column>
      </el-table>
      <el-pagination
        style="float: right"
        @size-change="handleAssignSizeChange"
        @current-change="handleAssignCurrentChange"
        :current-page="assignQueryForm.pageNo"
        :page-size="assignQueryForm.pageSize"
        :page-sizes="[10, 20, 30, 40, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="assignTotal">
      </el-pagination>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openAssign = false">取 消</el-button>
        <el-button type="primary" @click="submitUserRole">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框   -->
    <el-dialog title="导入数据" :visible.sync="openImport" width="400px" append-to-body>
      <el-upload
        class="upload-demo"
        drag
        accept=".xlsx"
        :action="baseUrl + '/sys/user/import'"
        :headers="{'Authorization': 'Bearer ' + getToken()}"
        :multiple="false"
        :limit="1"
        :on-success="handleImportSuccess">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          <el-link type="primary" @click="handleImportTemp">下载导入模板</el-link>
          <div>只能上传excel文件，且不超过1MB</div>
        </div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import {
  addUser, delUser,
  getAssignRoleList,
  getRoleIdByUserId, getUserInfo,
  listUser, updateUser,
} from "@/api/system/user"
import TreeSelect from "@/components/TreeSelect/index.vue"
import Password from 'vue-password-strength-meter'
import {assignUserRole} from "@/api/system/role";
import AvatarUpload from "@/components/AvatarUpload/index.vue";
import ResetPwd from "@/views/system/user/resetPwd.vue";
import {getToken} from "@/utils/auth";
import axios from "axios";

export default {
  name: "EbSysUser",
  components: {
    ResetPwd,
    AvatarUpload,
    TreeSelect,
    Password
  },
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
      activeName: 'baseInfo',
      userId: null,
      parentList: [],
      userList: [],
      total: 0,
      queryForm: {
        pageNo: 1,
        pageSize: 10,
        username: '',
        realName: '',
        phone: '',
        departmentId: null
      },
      assignQueryForm: {
        pageNo: 1,
        pageSize: 10,
        userId: null
      },
      assignRoleList: [],
      assignTotal: 0,
      hasRoleIdList: [],
      loading: false,
      title: '',
      open: false,
      form: {},
      // 表单校验
      rules: {
        username: [
          {required: true, message: "用户名不能为空", trigger: "blur"}
        ],
        password: [
          {required: true, message: "用户名不能为空", trigger: "blur"}
        ],
        departmentId: [
          {required: true, message: "所属部门不能为空", trigger: "blur"}
        ],
        realName: [
          {required: true, message: "姓名不能为空", trigger: "blur"}
        ],
        phone: [
          {required: true, message: "电话不能为空", trigger: "blur"}
        ],
        gender: [
          {required: true, message: "性别不能为空", trigger: "blur"}
        ]
      },
      treeSelectKey: Date.now(),
      openAssign: false,
      selectedRow: null,
      openImport: false
    };
  },
  created() {
    this.getList()
  },
  computed: {},
  methods: {
    getToken,
    async handleAssign(row) {
      this.selectedRow = row
      this.loading = true
      this.assignQueryForm.userId = this.$store.getters.userId
      await getAssignRoleList(this.assignQueryForm).then(response => {
        this.assignRoleList = response.data.records
        this.assignTotal = response.data.total
        this.title = `分配用户【${row.username}】`
        this.openAssign = true
        this.loading = false
      })
      await getRoleIdByUserId({userId: this.selectedRow.id}).then(response => {
        this.hasRoleIdList = response.data
        setTimeout(() => {
          this.assignRoleList.forEach(role => {
            this.hasRoleIdList.forEach(hasRole => {
              if (role.id === hasRole) {
                this.$refs.assignRoleTable.toggleRowSelection(role, true)
              }
            })
          })
        })
      })
    },
    handleAssignSizeChange() {

    },
    handleAssignCurrentChange() {

    },
    submitUserRole() {
      let data = {
        userId: this.selectedRow.id,
        roleList: this.$refs.assignRoleTable.selection.map(item => {
          return item.id
        })
      }
      assignUserRole(data).then(response => {
        if (response.code === 200) {
          this.$message.success('分配角色成功')
          this.handleAssign(this.selectedRow)
        } else {
          this.$message.error('分配角色失败！')
        }
      })
    },
    getList() {
      this.loading = true
      listUser(this.queryForm).then(response => {
        let data = response.data
        this.userList = data.records
        this.total = data.total
        this.loading = false
      })
    },
    resetQuery() {
      this.queryForm = {
        pageNo: 1,
        pageSize: 10,
        username: '',
        realName: '',
        phone: '',
        departmentId: null
      }
      this.getList()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        username: null,
        password: null,
        isAccountNonExpired: null,
        isAccountNonLocked: null,
        isCredentialsNonExpired: null,
        isEnabled: null,
        phone: null,
        email: null,
        avatar: null,
        isAdmin: null,
        realName: null,
        nickName: null,
        departmentId: null,
        departmentName: null,
        gender: null,
        createTime: null,
        updateTime: null,
        isDelete: null
      };
    },
    /**
     * 新增按钮
     */
    handlerAdd() {
      this.reset();
      this.treeSelectKey = Date.now()
      this.open = true;
      this.title = "添加用户";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUser(this.form).then(response => {
              if (response.code === 200) {
                this.$message.success("修改成功")
                this.open = false
                this.getList()
              } else {
                this.$message.error("修改失败")
              }
            })
          } else {
            addUser(this.form).then(response => {
              if (response.code === 200) {
                this.$message.success('新增成功')
                this.open = false;
                this.getList();
              } else {
                this.$message.error('新增失败')
              }
            });
          }
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getUserInfo(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户";
      });
    },
    // 删除用户
    handleDelete(row) {
      this.$confirm(`是否确认删除"${row.username}"?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delUser(row.id).then(response => {
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error('删除失败')
          }
        })
      }).catch(() => {
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    handleCurrentChange(val) {
      this.queryForm.pageNo = val
      this.getList()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getList()
    },
    getAvatarUploadRes(val) {
      // 更新用户头像数据
      let data = {
        id: this.form.id,
        avatar: val.data
      }
      updateUser(data).then(response => {
        this.$message.success("上传成功!")
        // 强制更新头像上传组件 刷新图片
        this.$refs.avatarUpload.$forceUpdate()
      })
    },
    // 下载导入模板
    handleImportTemp() {
      axios.get(this.baseUrl + '/sys/user/import/template', {
        headers: {
          'Authorization': 'Bearer ' + getToken()
        },
        responseType: 'blob'
      }).then(response => {
        let blob = new Blob([response.data], {type: 'application/vnd.ms-excel'})
        let url = window.URL.createObjectURL(blob)
        let a = document.createElement("a")
        a.href = url
        //文件名
        a.download = 'template.xlsx'
        a.click()
      })
    },
    handleImportSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.$message.success(response.msg)
      } else {
        this.$message.error(response.msg)
      }
      this.openImport = false
      this.getList()
    },
    // 导出
    handleOutput() {
      axios.get(this.baseUrl + '/sys/user/output', {
        headers: {
          'Authorization': 'Bearer ' + getToken()
        },
        responseType: 'blob'
      }).then(response => {
        let blob = new Blob([response.data], {type: 'application/vnd.ms-excel'})
        let url = window.URL.createObjectURL(blob)
        let a = document.createElement("a")
        a.href = url
        //文件名
        a.download = 'output.xlsx'
        a.click()
      })
    },
  }
};
</script>
<style scoped lang="scss">

</style>
