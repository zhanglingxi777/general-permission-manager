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

### 用户管理页面
![用户管理页面](resource/image/%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86-%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png)

### 角色管理页面
+ 页面
![角色管理页面](resource/image/%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86-%E8%A7%92%E8%89%B2%E7%AE%A1%E7%90%86.png)
+ 给角色分配权限
![输入图片说明](resource/image/%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86-%E8%A7%92%E8%89%B2%E7%AE%A1%E7%90%86-%E5%88%86%E9%85%8D%E6%9D%83%E9%99%90.png)

### 菜单管理
![菜单管理](resource/image/%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86-%E8%8F%9C%E5%8D%95%E7%AE%A1%E7%90%86.png)

### 登录日志
![登录日志](resource/image/%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86-%E7%99%BB%E5%BD%95%E6%97%A5%E5%BF%97.png)

### 登录用户
![登录用户](resource/image/%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86-%E7%99%BB%E5%BD%95%E7%94%A8%E6%88%B7.png)