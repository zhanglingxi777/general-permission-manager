<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="6">
        <el-form ref="form" :model="queryForm" label-width="80px">
          <el-form-item label="角色名称">
            <el-input v-model="queryForm.roleName" placeholder="请输入角色名称"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="18">
        <el-button type="primary" icon="el-icon-search" @click="handlerQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handlerAdd">新增</el-button>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-table v-loading="loading" :data="roleList" border style="width: 100%">
          <el-table-column align="center" prop="id" label="角色编号" min-width="100"></el-table-column>
          <el-table-column align="center" prop="roleCode" label="角色编码" min-width="120"></el-table-column>
          <el-table-column align="center" prop="roleName" label="角色名称" min-width="120"></el-table-column>
          <el-table-column align="center" prop="remark" label="角色备注" min-width="180"></el-table-column>
          <el-table-column align="center" label="操作" min-width="250">
            <template slot-scope="scope">
              <el-button type="primary" icon="el-icon-edit" @click="handlerUpdate(scope.row)">编辑</el-button>
              <el-button type="danger" icon="el-icon-delete" @click="handlerDelete(scope.row)">删除</el-button>
              <el-button type="success" icon="el-icon-help" @click="handlerAssign(scope.row)">分配权限</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-pagination
          style="float: right"
          v-show="total > 0"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="queryForm.pageNo"
          :page-size="queryForm.pageSize"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30, 40, 50]"
          :total="total"
        ></el-pagination>
      </el-col>
    </el-row>

    <!-- 添加或修改角色对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="60px">
        <el-form-item label="编码" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入编码" :maxlength="100" show-word-limit/>
        </el-form-item>
        <el-form-item label="名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入名称" :maxlength="20" show-word-limit/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 8}" v-model="form.remark"
                    placeholder="请输入备注" :maxlength="200" show-word-limit/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配权限对话框   -->
    <el-dialog :title="title" :visible.sync="assignOpen" width="400px" append-to-body>
      <el-tree
        ref="assignTree"
        show-checkbox
        :data="assignTreeData"
        :props="defaultProps"
        highlight-current
        node-key="id"
        empty-text="暂无数据"
        default-expand-all
      ></el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="assignOpen = false">取 消</el-button>
        <el-button type="primary" @click="assignPermission">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {
  addRole,
  assignRolePermission,
  delRole,
  getAssignPermissionTree,
  getRole,
  listRole,
  updateRole
} from "@/api/system/role"
import leafUtils from "@/utils/leaf"

export default {
  name: "Role",
  data() {
    return {
      userId: null,
      queryForm: {
        pageNo: 1,
        pageSize: 10,
        roleName: null
      },
      total: 0,
      loading: false,
      roleList: [],
      title: "",
      open: false,
      assignOpen: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roleCode: [
          {required: true, message: "编码不能为空", trigger: "blur"}
        ],
        roleName: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ],
      },
      roleId: null,
      assignTreeData: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    };
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.queryForm.userId = this.$store.getters.userId
      listRole(this.queryForm).then(response => {
        let data = response.data
        this.roleList = data.records
        this.total = data.total
        this.loading = false
      })
    },
    resetQuery() {
      this.queryForm.roleName = null
      this.getList()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        roleCode: null,
        roleName: null,
        createUser: null,
        createTime: null,
        updateTime: null,
        remark: null,
        isDelete: null
      };
    },
    handleCurrentChange(val) {
      this.queryForm.pageNo = val
      this.getList()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getList()
    },
    handlerQuery() {
      this.queryForm.pageNo = 1
      this.getList()
    },
    handlerAdd() {
      this.reset();
      this.open = true;
      this.title = "添加角色";
    },
    handlerUpdate(row) {
      this.reset()
      getRole(row.id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改角色"
      })
    },
    handlerDelete(row) {
      this.$confirm(`是否确认删除"${row.roleName}"?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delRole(row.id).then(response => {
          this.$message.success("删除成功")
          this.getList()
        })
      }).catch(() => {
      })
    },
    handlerAssign(row) {
      this.roleId = row.id
      let params = {
        roleId: row.id,
        userId: this.$store.getters.userId
      }
      getAssignPermissionTree(params).then(response => {
        let {checkList, permissionList} = response.data
        let {setLeaf} = leafUtils()
        this.assignTreeData = setLeaf(permissionList)
        this.$nextTick(() => {
          let nodes = this.$refs.assignTree.children
          this.setChildren(nodes, checkList)

        })
      })
      this.title = `分配权限【${row.roleName}】`
      this.assignOpen = true
    },
    // 设置子节点
    setChildren(childrenNodes, checkList) {
      if (childrenNodes && childrenNodes.length > 0) {
        for (let i = 0; i < childrenNodes.length; i++) {
          let node = this.$refs.assignTree.getNode(childrenNodes[i])
          if (checkList && checkList.length > 0) {
            for (let j = 0; j < checkList.length; j++) {
              if (childrenNodes[i].id === checkList[j]) {
                if (childrenNodes[i].open) {
                  this.$refs.assignTree.setChecked(node, true)
                  break
                }
              }
            }
          }
          if (childrenNodes[i].children) {
            this.setChildren(childrenNodes[i].children, checkList)
          }
        }
      }
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRole(this.form).then(response => {
              this.$message.success("修改成功")
              this.open = false
              this.getList()
            });
          } else {
            this.form.createUser = this.$store.getters.userId
            addRole(this.form).then(response => {
              this.$message.success("新增成功")
              this.open = false
              this.getList()
            });
          }
        }
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    // 给角色分配权限
    assignPermission() {
      let children = this.$refs.assignTree.getCheckedKeys()
      let parent = this.$refs.assignTree.getHalfCheckedKeys()
      let ids = parent.concat(children)
      let data = {
        roleId: this.roleId,
        permissionIdList: ids
      }
      assignRolePermission(data).then(response => {
        if (response.code === 200) {
          this.$message.success(response.msg)
          this.assignOpen = false
        } else {
          this.$message.error(response.msg)
        }
      })
    }
  }
};
</script>
