# 灵溪通用权限管理系统
灵溪通用权限管理系统，基于springboot+vue技术栈，实现了基础的RBAC

## 目录结构

```
general-permission-manager
├─ code                 # 代码文件
│   ├─ pom.xml          # pom文件
│   ├─ src              # 后端代码
│   └─ ui               # 前端代码
└─ resource             # 资源文件
    ├─ document         # 文档
    └─ sql              # 数据库sql
```

## 安装

```batch
# 克隆项目
https://gitee.com/zhanglingxi777/general-permission-manager.git

# 进入项目目录
cd general-permission-manager

# 前端
## 进入前端项目目录
cd code/ui

## 安装依赖
npm install

## 本地开发 启动项目
npm run dev

# 后端
## 使用idea打开code文件夹，配置Maven，等待项目构建完毕
```

## 功能介绍
```
- 角色管理
- 用户管理
- 菜单管理
- 登录日志
- 登录用户
```