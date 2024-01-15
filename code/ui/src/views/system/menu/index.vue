<template>
  <div class="app-container">
    <el-button type="success" icon="el-icon-plus" @click="handlerAdd">新增</el-button>
    <el-table
      :data="menuList"
      :height="tableHeight"
      style="width: 100%;margin-top: 20px;"
      row-key="id"
      border
      :tree-props="{children: 'children'}">
      <el-table-column prop="label" label="菜单名称" min-width="180"></el-table-column>
      <el-table-column prop="type" label="菜单类型" align="center" min-width="100">
        <template v-slot="scope">
          <el-tag size="normal" v-if="scope.row.type===0">目录</el-tag>
          <el-tag size="normal" type="success" v-else-if="scope.row.type===1">菜单</el-tag>
          <el-tag size="normal" type="warning" v-else>按钮</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="icon" label="菜单图标" align="center" min-width="100">
        <template v-slot="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="路由名称" align="center" min-width="120"></el-table-column>
      <el-table-column prop="path" label="路由地址" align="center" min-width="180"></el-table-column>
      <el-table-column prop="url" label="组件路径" align="center" min-width="180"
                       show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" align="center" min-width="250" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" @click="handlerUpdate(scope.row)">
            编辑
          </el-button>
          <el-button type="danger" icon="el-icon-delete" @click="handlerDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>


    <!--    //添加或者编辑对话框-->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="menuForm" :model="menu" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属菜单" prop="parentName">
              <tree-select :key="treeSelectKey" :props="props" :options="parentList" :value="menu.parentId"
                           @getValue="getParentMenuId($event)"></tree-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="type">
              <el-radio-group v-model="menu.type" @input="handlerTypeChange">
                <el-radio :label="0">目录</el-radio>
                <el-radio :label="1">菜单</el-radio>
                <el-radio :label="2">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <!-- 只有当菜单类型为目录 和 菜单才能设置菜单图标 -->
          <el-col :span="12" v-if="menu.type !== 2">
            <el-form-item label="菜单图标">
              <MyIcon @selectIcon="setIcon" ref="child"></MyIcon>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="label">
              <el-input v-model="menu.label" :maxlength="50" show-word-limit placeholder="请输入菜单名称"></el-input>
            </el-form-item>
          </el-col>
          <!-- 只有当菜单类型为 目录 或 菜单 才能设置路由名称 -->
          <el-col :span="12" v-if="menu.type !== 2">
            <el-form-item label="路由名称" prop="name">
              <el-input v-model="menu.name" :maxlength="50" show-word-limit placeholder="请输入路由名称 "></el-input>
            </el-form-item>
          </el-col>
          <!-- 只有当菜单类型为目录 和 菜单才能设置路由地址 -->
          <el-col :span="12" v-if="menu.type !== 2">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="menu.path" :maxlength="100" show-word-limit placeholder="请输入路由地址"></el-input>
            </el-form-item>
          </el-col>
          <!-- 只有当菜单类型为 菜单 才能设置组件地址 -->
          <el-col :span="12">
            <el-form-item label="组件地址" prop="url" v-if="menu.type !== 0 && menu.type !== 2">
              <el-input v-model="menu.url " :maxlength="100" show-word-limit placeholder="请输入组件路由"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限编码" prop="code">
              <el-input v-model="menu.code" :maxlength="50" show-word-limit placeholder="请输入权限编码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单序号">
              <el-input-number v-model="menu.orderNum" placeholder="请输入菜单序号" controls-position="right" :min="0"
                               :max="999999" :step="1"></el-input-number>
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
  getMenuList,
  getMenuParentList,
  addMenu,
  getMenu,
  updateMenu,
  deleteMenu
} from "@/api/system/menu";
import MyIcon from "@/components/system/MyIcon.vue";
import TreeSelect from "@/components/TreeSelect/index"
import {deleteDepartment} from "@/api/department";

export default {
  name: "Permission",
  components: {
    TreeSelect,
    MyIcon,
  },
  data() {
    return {
      menuList: [],//菜单列表
      tableHeight: null,//表格总高度
      open: false,
      title: '',
      treeSelectKey: Date.now(),
      props: {
        // 配置项（必选）
        value: 'id',
        label: 'label',
        children: 'children'
      },
      //菜单属性
      menu: {},
      rules: {
        type: [
          {required: true, trigger: 'blur', message: '请选择菜单属性'}
        ],
        label: [
          {required: true, trigger: 'blur', message: '请选择菜单名称'}
        ],
        name: [
          {required: true, trigger: 'blur', message: '请选择路由名称'}
        ],
        path: [
          {required: true, trigger: 'blur', message: '请选择路由路径'}
        ],
        url: [
          {required: true, trigger: 'blur', message: '请选择组件路径'}
        ],
        code: [
          {required: true, trigger: 'blur', message: '请输入权限字段'}
        ],
      },
      parentList: []
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 180
    })
  },
  methods: {
    /**
     * 查询菜单列表
     */
    getList() {
      getMenuList().then(res => {
        this.menuList = res.data
      })
    },
    /**
     * 打开新增窗口
     */
    handlerAdd() {

      this.getParentList()
      this.treeSelectKey = Date.now()
      this.reset()
      this.open = true
      this.title = "新增菜单"
      this.$nextTick(() => {
        this.$refs.child.userChooseIcon = ""
      })
    },
    getParentList() {
      getMenuParentList().then(response => {
        this.parentList = response.data
      })
    },
    setIcon(icon) {
      this.menu.icon = icon
    },
    /**
     * 清空表单数据
     */
    reset() {
      this.menu = {
        id: null,
        type: 1,
        parentId: null,
        parentName: null,
        label: null,
        icon: null,
        name: null,
        path: null,
        url: null,
        code: null,
        orderNum: null,
      }
    },
    /**
     * 确认提交按钮事件
     */
    submitForm() {
      console.log(this.menu)
      //表单验证
      this.$refs.menuForm.validate((valid) => {
        if (valid) {
          if (this.menu.id != null) {
            updateMenu(this.menu).then(res => {
              this.$message.success("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMenu(this.menu).then(res => {
              this.$message.success("添加成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /**
     * 取消提交按钮事件
     */
    cancel() {
      this.open = false;
    },
    getParentMenuId(val) {
      this.menu.parentId = val
    },
    handlerTypeChange(label) {
      this.menu.type = label
    },
    /**
     * 编辑菜单
     */
    handlerUpdate(row) {
      this.open = true
      this.reset()
      getMenu(row.id).then(res => {
        this.menu = res.data
        console.log(this.menu)
        this.title = '修改菜单信息'
        this.$nextTick(() => {
          this.$refs.child.userChooseIcon = this.menu.icon
        })
        this.getParentList()
        this.treeSelectKey = Date.now()
      })
    },
    // 删除部门并且判断该部门是否有子部门，如果有子部门那么不能删除
    handlerDelete(row) {
      this.$confirm(`是否确定删除"${row.label}"?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenu(row.id).then(response => {
          this.getList()
          this.$message.success('删除成功!')
        })
      }).catch(() => {
      })
    },
  }
};
</script>
<style scoped>
.iconList {
  width: 400px;
  height: 300px;
  overflow-y: scroll;
  overflow-x: hidden;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;

  i {
    display: inline-block;
    width: 60px;
    height: 45px;
    color: #000000;
    font-size: 20px;
    border-radius: 4px;
    cursor: pointer;
    text-align: center;
    line-height: 45px;
    margin: 5px;

    &:hover {
      color: orange;
      border-color: orange;
    }
  }
}

.chooseIcons {
  width: 175px;
  background-color: #FFFFFF;
  background-image: none;
  border-radius: 4px;
  border: 1px solid #DCDFE6;
  box-sizing: border-box;
  color: #686266;
  display: inline-block;
  font-size: inherit;
  height: 33px;
  line-height: 25px;
  outline: none;
  padding: 0 15px;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
}
</style>
