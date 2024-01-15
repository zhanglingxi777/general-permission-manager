<template>
  <div class="chooseIcons">
    <el-popover placement="bottom" width="450px" trigger="hover">
                  <span
                    slot="reference"
                    style="display: inline-block; width: 200px; height: 20px; line-height: 33px">
                    <i :class="userChooseIcon"></i>
                    {{ userChooseIcon }}
                  </span>
      <div class="iconList">
        <i
          v-for="item in iconList"
          :key="item"
          :class="item"
          @click="setIcon(item)"
          style="font-size: 20px"
        ></i>
      </div>
    </el-popover>
  </div>
</template>
<script>

import {elementIcons} from "@/utils/icons";

export default {
  name: "MyIcon",
  data() {
    return {
      iconList: [], //图标列表
      userChooseIcon: "",// 用户所选中的图标
    }
  },
  methods: {
    /**
     * 查询图标列表
     */
    getIconList() {
      this.iconList = elementIcons
    },
    /**
     * 设置图标
     */
    setIcon(icon) {
      this.userChooseIcon = icon
      //把选中的图标传递给父组件
      this.$emit("selectIcon", icon);//selectIcon为自定义的名称，icon位这个名称传递的数据
    },
  },
  created() {
    this.getIconList()
  }
}
</script>

<style scoped lang="scss">
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
