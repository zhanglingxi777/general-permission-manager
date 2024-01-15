<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="6">
        <el-form ref="queryForm" :model="queryForm" label-width="80px">
          <el-form-item label="部门名称">
            <el-input v-model="queryForm.department" placeholder="请输入部门名称"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="18">
        <el-button type="primary" icon="el-icon-search" @click="getList">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handlerAdd">新增</el-button>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-table v-loading="loading" :data="departmentList" border style="width: 100%" row-key="id"
                  :tree-props="{children: 'children', hasChildren: 'hasChildren'}" default-expand-all>
          <el-table-column prop="department" label="部门名称" min-width="180"></el-table-column>
          <el-table-column align="center" prop="parentName" label="所属部门" min-width="180"></el-table-column>
          <el-table-column align="center" prop="phone" label="部门电话" min-width="180"></el-table-column>
          <el-table-column align="center" prop="address" label="部门位置" min-width="180"></el-table-column>
          <el-table-column align="center" label="操作" min-width="180">
            <template slot-scope="scope">
              <el-button v-if="scope.row.id !== 1" type="primary" icon="el-icon-edit" @click="handlerUpdate(scope.row)">编辑</el-button>
              <el-button v-if="scope.row.id !== 1" type="danger" icon="el-icon-delete" @click="handlerDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="pid">
              <tree-select :key="treeSelectKey" :props="props" :options="optionData" :value="form.pid" @getValue="getParentDeptId($event)"></tree-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="department">
              <el-input v-model="form.department" placeholder="请输入部门名称" type="text"
                        :maxlength="50" show-word-limit/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="部门电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入部门电话"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入部门地址"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" :max="9999999"
                               :step="1"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDepartmentList,
  getDepartmentParentList,
  addDepartment,
  deleteDepartment,
  getDepartment,
  updateDepartment,
} from "@/api/department";
import TreeSelect from "@/components/TreeSelect/index";

export default {
  name: "Department",
  components: {TreeSelect},
  data() {
    return {
      queryForm: {
        department: null
      },
      // 表单参数
      form: {},
      // 弹出层标题
      title: "",
      // 表单校验
      rules: {
        department: [
          {required: true, message: "部门名称不能为空", trigger: "blur"}
        ],
        pid: [
          {required: true, message: "所属部门编号不能为空", trigger: "blur"}
        ],
      },
      loading: false,
      open: false,
      departmentList: [],
      parentList: [],
      props: {
        // 配置项（必选）
        value: "id",
        label: "department",
        children: "children"
      },
      treeSelectKey: Date.now()
    };
  },
  computed: {
    optionData() {
      return this.parentList.filter(father => {
        let branchArr = this.parentList.filter(child => father.id === child.pid)
        branchArr.length > 0 ? (father.children = branchArr) : ""
        return father.pid === 0
      })
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true;
      getDepartmentList(this.queryForm).then(response => {
        this.departmentList = response.data
        this.loading = false
      })
    },
    getParentList() {
      getDepartmentParentList().then(response => {
        this.parentList = response.data
      })
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        department: null,
        phone: null,
        address: null,
        pid: null,
        parentName: null,
        orderNum: null,
        isDelete: null
      };
    },
    resetQuery() {
      this.queryForm = {
        department: null
      }
      this.getList()
    },
    handlerAdd() {
      this.getParentList()
      this.treeSelectKey = Date.now()
      this.reset()
      this.open = true
      this.title = "添加部门"
    },
    handlerUpdate(row) {
      this.getParentList()
      this.treeSelectKey = Date.now()
      getDepartment(row.id).then(response => {
        this.form = response.data
        this.title = '修改部门信息'
        this.open = true
      })
    },
    // 删除部门并且判断该部门是否有子部门，如果有子部门那么不能删除
    handlerDelete(row) {
      this.$confirm(`是否确定删除"${row.department}"?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDepartment(row.id).then(response => {
          this.getList()
          this.$message.success('删除成功!')
        })
      }).catch(() => {})
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDepartment(this.form).then(response => {
              this.$message.success('修改成功')
              this.open = false
              this.getList()
            });
          } else {
            addDepartment(this.form).then(response => {
              this.$message.success('新增成功')
              this.open = false
              this.getList()
            });
          }
        }
      });
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    getParentDeptId(event) {
      this.form.pid = event
    }
  }
};
</script>
