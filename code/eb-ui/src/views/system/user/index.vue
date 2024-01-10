<template>
  <div class="app-container">
    <el-row>
      <!-- 部门树     -->
      <el-col :span="4">
        <el-tree
          :data="departmentTreeData"
          :props="defaultProps"
          @node-click="handleNodeClick"
          default-expand-all
          :expand-on-click-node="false"
        ></el-tree>
      </el-col>
      <!-- 用户信息     -->
      <el-col :span="20">
        <el-row>
          <el-form ref="queryForm" :model="queryForm" label-width="80px">
            <el-row :gutter="10">
              <el-col :span="6">
                <el-form-item label="用户名">
                  <el-input v-model="queryForm.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="姓名">
                  <el-input v-model="queryForm.realName" placeholder="请输入姓名"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="电话">
                  <el-input v-model="queryForm.phone" placeholder="请输入电话"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-button type="primary" icon="el-icon-search" @click="getList">查询</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                <el-button type="success" icon="el-icon-plus" @click="handlerAdd">新增</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-row>

        <el-row>
          <el-table v-loading="loading" :data="userList" border style="width: 100%">
            <el-table-column label="头像" align="center" prop="avatar" show-overflow-tooltip min-width="120">
              <template slot-scope="scope">
                <img :src="scope.row.avatar" style="width: 50px;height: 50px;border-radius: 5px;"/>
              </template>
            </el-table-column>
            <el-table-column label="用户名" align="center" prop="username" show-overflow-tooltip min-width="120"/>
            <el-table-column label="姓名" align="center" prop="realName" show-overflow-tooltip min-width="120"/>
            <el-table-column label="所属部门名称" align="center" prop="departmentName" show-overflow-tooltip
                             min-width="180"/>
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
    <el-dialog :title="title" :visible="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input maxlength="20" show-word-limit :disabled="Boolean(form.id)" v-model="form.username"
                        placeholder="请输入用户名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="!Boolean(form.id)">
            <el-form-item label="密码" prop="password">
              <el-input maxlength="20" show-word-limit v-model="form.password"
                        placeholder="请输入密码"/>
              <password v-model="form.password" :strength-meter-only="true" @score="showScore"
                        @feedback="showFeedback"/>
              <!--              <password v-model="form.password" :toggle="true" @score="showScore" @feedback="showFeedback"/>-->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="departmentId">
              <tree-select :key="treeSelectKey" :props="defaultProps" :options="departmentTreeData"
                           :value="form.departmentId ? form.departmentId : null"
                           @getValue="getParentDepartmentId($event)"></tree-select>
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
            <el-form-item label="昵称" prop="nickName">
              <el-input maxlength="50" show-word-limit v-model="form.nickName" placeholder="请输入昵称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :label="0">男</el-radio>
                <el-radio :label="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input maxlength="50" show-word-limit v-model="form.email" placeholder="请输入邮箱"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="头像" prop="avatar">
              <el-upload
                ref="avatarUpload"
                class="avatar-uploader"
                :action="baseUrl+'/common/upload/avatar/'+ Date.now() + '-' + Math.floor((Math.random() * 9000) + 1000)"
                :multiple="false"
                :show-file-list="false"
                accept=".jpg,.png,.jpeg"
                :limit="1"
                :headers="{'Authorization': 'Bearer ' + getToken()}"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
                <img v-if="form.avatar" :src="avatarBase64Data" class="avatar"
                     alt="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配用户角色对话框   -->
    <el-dialog :title="title" :visible="openAssign" width="800px" append-to-body>
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
  </div>
</template>

<script>
import {getDepartmentParentList} from "@/api/department"
import {
  addEbSysUser,
  delEbSysUser,
  getAssignRoleList,
  getEbSysUser,
  getRoleIdByUserId,
  listUser,
  updateEbSysUser
} from "@/api/system/user"
import TreeSelect from "@/components/TreeSelect/index.vue"
import {getToken} from "@/utils/auth"
import {getImage} from "@/api/common";
import Password from 'vue-password-strength-meter'
import {assignUserRole} from "@/api/system/role";

export default {
  name: "EbSysUser",
  components: {
    TreeSelect,
    Password
  },
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
      userId: null,
      defaultProps: {
        value: "id",
        label: "department",
        children: "children"
      },
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
      avatarBase64Data: null,
      treeSelectKey: Date.now(),
      pwdScore: null,
      pwdFeedback: {
        suggestions: null,
        warning: null
      },
      openAssign: false,
      selectedRow: null
    };
  },
  created() {
    this.getParentList()
    this.getList()
  },
  computed: {
    departmentTreeData() {
      return this.parentList.filter(father => {
        let branchArr = this.parentList.filter(child => father.id === child.pid)
        branchArr.length > 0 ? (father.children = branchArr) : ""
        return father.pid === 0
      })
    }
  },
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
    showScore(score) {
      console.log('💯', score)
      this.pwdScore = score
    },
    showFeedback({suggestions, warning}) {
      console.log('🙏', suggestions)
      console.log('⚠', warning)
      this.pwdFeedback.suggestions = suggestions
      this.pwdFeedback.warning = warning
    },
    getList() {
      this.loading = true
      listUser(this.queryForm).then(response => {
        let data = response.data
        this.userList = data.records
        this.userList.forEach(user => {
          let split = user.avatar.split('/');
          let params = {
            profile: split[0],
            imageName: split[1]
          }
          getImage(params).then(response => {
            user.avatar = response.data
          })
        })
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
      // 清空头像数据
      this.avatarBase64Data = null
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
            updateEbSysUser(this.form).then(response => {
              if (response.code === 200) {
                this.$message.success("修改成功")
                this.open = false
                this.getList()
              } else {
                this.$message.error("修改失败")
              }
            })
          } else {
            if (this.pwdScore < 3) {
              this.$message({
                type: 'warning',
                dangerouslyUseHTMLString: true,
                message: `<p>密码复杂度过低，请重新输入！</p>
                        <p>警告：${this.pwdFeedback.warning}</p>
                        <p>建议：${this.pwdFeedback.suggestions}</p>`
              })
              return
            }
            addEbSysUser(this.form).then(response => {
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
      getEbSysUser(row.id).then(response => {
        this.form = response.data;
        if (this.form.avatar) {
          let split = this.form.avatar.split('/');
          let params = {
            profile: split[0],
            imageName: split[1]
          }
          // 获取头像
          getImage(params).then(response => {
            this.avatarBase64Data = response.data
          })
        }
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
        delEbSysUser(row.id).then(response => {
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
    handleNodeClick(val) {
      this.queryForm.departmentId = val.id
      this.getList()
    },
    getParentList() {
      getDepartmentParentList().then(response => {
        this.parentList = response.data
      })
    },
    handleAvatarSuccess(res, file) {
      if (res.code === 200) {
        this.form.avatar = res.path
        let split = this.form.avatar.split('/');
        let params = {
          profile: split[0],
          imageName: split[1]
        }
        // 获取头像
        getImage(params).then(response => {
          this.avatarBase64Data = response.data
        })
        this.$message.success(res.msg)
      } else {
        this.$message.error(res.msg)
      }
    },
    beforeAvatarUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1
      const isJPG = file.type !== 'image/jpeg' || file.type !== 'image/png' || file.type !== 'image/jpg'
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG, JPEG 或 PNG 格式!');
      }
      if (!isLt1M) {
        this.$message.error('上传头像图片大小不能超过 1MB!');
      }
      return isJPG && isLt1M;
    },
    getParentDepartmentId(val) {
      this.form.departmentId = val;
    }
  }
};
</script>
<style lang="scss">
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;

  :hover {
    border-color: #409EFF;
  }
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
