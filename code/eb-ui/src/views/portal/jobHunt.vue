<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>在招职位</span>
      </div>
      <div class="text item">
        <el-form :model="queryForm" label-width="80px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="招聘类型">
                <el-select v-model="queryForm.huntType" placeholder="请选择">
                  <el-option
                      v-for="item in huntTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="职位">
                <el-cascader
                    v-model="queryForm.position"
                    :options="positionOptions"
                ></el-cascader>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="城市">
                <el-select v-model="queryForm.huntType" placeholder="请选择">
                  <el-option
                      v-for="item in huntTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="公司名">
                <el-input v-model="queryForm.company" placeholder="请输入内容"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <div class="position">
                <div class="position-header">
                  <div>Unity游戏开发工程师</div>
                  <div>7-14K * 13薪</div>
                </div>
                <div class="position-main">
                  广州 | 本科
                </div>
                <div class="position-footer">
                  <div class="company">
                    <el-avatar src=""></el-avatar>
                    <p class="company-name">艾克广电</p>
                  </div>
                  <div class="position-type">游戏</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="position">

              </div>
            </el-col>
            <el-col :span="6">
              <div class="position">

              </div>
            </el-col>
            <el-col :span="6">
              <div class="position">

              </div>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>

  </div>
</template>

<script>
export default {
  name: "jobHunt",
  data() {
    return {
      queryForm: {
        huntType: null,
        position: null,
        company: null
      },
      huntTypeOptions: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      positionOptions: [{
        value: 'zhinan',
        label: '指南',
        children: [{
          value: 'shejiyuanze',
          label: '设计原则',
          children: [{
            value: 'yizhi',
            label: '一致'
          }, {
            value: 'fankui',
            label: '反馈'
          }, {
            value: 'xiaolv',
            label: '效率'
          }, {
            value: 'kekong',
            label: '可控'
          }]
        }, {
          value: 'daohang',
          label: '导航',
          children: [{
            value: 'cexiangdaohang',
            label: '侧向导航'
          }, {
            value: 'dingbudaohang',
            label: '顶部导航'
          }]
        }]
      }, {
        value: 'zujian',
        label: '组件',
        children: [{
          value: 'basic',
          label: 'Basic',
          children: [{
            value: 'layout',
            label: 'Layout 布局'
          }, {
            value: 'color',
            label: 'Color 色彩'
          }, {
            value: 'typography',
            label: 'Typography 字体'
          }, {
            value: 'icon',
            label: 'Icon 图标'
          }, {
            value: 'button',
            label: 'Button 按钮'
          }]
        }, {
          value: 'form',
          label: 'Form',
          children: [{
            value: 'radio',
            label: 'Radio 单选框'
          }, {
            value: 'checkbox',
            label: 'Checkbox 多选框'
          }, {
            value: 'input',
            label: 'Input 输入框'
          }, {
            value: 'input-number',
            label: 'InputNumber 计数器'
          }, {
            value: 'select',
            label: 'Select 选择器'
          }, {
            value: 'cascader',
            label: 'Cascader 级联选择器'
          }, {
            value: 'switch',
            label: 'Switch 开关'
          }, {
            value: 'slider',
            label: 'Slider 滑块'
          }, {
            value: 'time-picker',
            label: 'TimePicker 时间选择器'
          }, {
            value: 'date-picker',
            label: 'DatePicker 日期选择器'
          }, {
            value: 'datetime-picker',
            label: 'DateTimePicker 日期时间选择器'
          }, {
            value: 'upload',
            label: 'Upload 上传'
          }, {
            value: 'rate',
            label: 'Rate 评分'
          }, {
            value: 'form',
            label: 'Form 表单'
          }]
        }, {
          value: 'data',
          label: 'Data',
          children: [{
            value: 'table',
            label: 'Table 表格'
          }, {
            value: 'tag',
            label: 'Tag 标签'
          }, {
            value: 'progress',
            label: 'Progress 进度条'
          }, {
            value: 'tree',
            label: 'Tree 树形控件'
          }, {
            value: 'pagination',
            label: 'Pagination 分页'
          }, {
            value: 'badge',
            label: 'Badge 标记'
          }]
        }, {
          value: 'notice',
          label: 'Notice',
          children: [{
            value: 'alert',
            label: 'Alert 警告'
          }, {
            value: 'loading',
            label: 'Loading 加载'
          }, {
            value: 'message',
            label: 'Message 消息提示'
          }, {
            value: 'message-box',
            label: 'MessageBox 弹框'
          }, {
            value: 'notification',
            label: 'Notification 通知'
          }]
        }, {
          value: 'navigation',
          label: 'Navigation',
          children: [{
            value: 'menu',
            label: 'NavMenu 导航菜单'
          }, {
            value: 'tabs',
            label: 'Tabs 标签页'
          }, {
            value: 'breadcrumb',
            label: 'Breadcrumb 面包屑'
          }, {
            value: 'dropdown',
            label: 'Dropdown 下拉菜单'
          }, {
            value: 'steps',
            label: 'Steps 步骤条'
          }]
        }, {
          value: 'others',
          label: 'Others',
          children: [{
            value: 'dialog',
            label: 'Dialog 对话框'
          }, {
            value: 'tooltip',
            label: 'Tooltip 文字提示'
          }, {
            value: 'popover',
            label: 'Popover 弹出框'
          }, {
            value: 'card',
            label: 'Card 卡片'
          }, {
            value: 'carousel',
            label: 'Carousel 走马灯'
          }, {
            value: 'collapse',
            label: 'Collapse 折叠面板'
          }]
        }]
      }, {
        value: 'ziyuan',
        label: '资源',
        children: [{
          value: 'axure',
          label: 'Axure Components'
        }, {
          value: 'sketch',
          label: 'Sketch Templates'
        }, {
          value: 'jiaohu',
          label: '组件交互文档'
        }]
      }]
    }
  },
  methods: {}
}
</script>

<style scoped>
.app-container {
  background-color: #f7f8f9;
}

.box-card {
  width: 1200px;
  margin: 20px auto;
}

.position {
  height: 100px;
  background-color: #f8f8f8;
  border-radius: 5px;
  margin: 0 10px;
}
.position:hover {
  transform: translate(0, -10px);
  -webkit-transform: translate(0, -10px);
  -moz-transform: translate(0, -10px);
  -o-transform: translate(0, -10px);
  -ms-transform: translate(0, -10px);
  box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.2);
  background-color: white;
  transition: all 0.3s;
}
.position-header {
  display: flex;
  font-size: 14px;
  justify-content: space-between;
}

.position-header > div {
  margin: 5px;
}

.position-header > div:last-child {
  color: #ff591b;
}

.position-main {
  font-size: 12px;
  color: #bbbbbb;
  margin: 5px;
}

.position-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 5px;
}

.company {
  display: flex;
}

.company-name {
  margin-left: 5px;
  line-height: 40px;
  font-size: 14px;
}

.position-type {
  font-size: 12px;
  color: #bbbbbb;
}
</style>