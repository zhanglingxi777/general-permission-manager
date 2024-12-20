# 通用权限管理系统
通用权限管理系统，基于spring boot，vue和element-ui开发，使用spring security安全框架，实现了基础的RBAC功能。

**该项目是本人为完成学校Java Web大作业而开发的基础项目，仅供参考。**

账号: admin
密码: 123456

## 技术栈

### 前端
1. Vue
2. Element-ui
3. 。。。

### 后端
1. Spring Boot
2. Spring MVC
3. Spring Security
4. MySQL
5. 。。。

## 目录结构

```
general-permission-manager
├─ code                 # 代码文件
│   ├─ pom.xml          # pom文件
│   ├─ src              # 后端代码
│   └─ ui               # 前端代码
└─ resource             # 资源文件
    ├─ document         # 文档资源
    ├─ sql              # 数据库sql资源
    └─ image            # 图片资源
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

# 数据库
## 新建lingxi-basic数据库
## 在数据库中先执行sql文件夹中的ddl.sql，再执行data.sql
```

## 功能介绍

- 角色管理
    - 实现角色CRUD功能，可以给角色分配相应权限
- 用户管理
    - 实现用户CRUD，导入导出，分配角色功能
- 菜单管理
    - 实现菜单CRUD功能，以树状结构对菜单进行管理
- 登录日志
    - 实现用户登录日志的记录
- 登录用户
    - 查看在线用户，管理员可以强制用户退出

### 个人中心页面
![个人中心页面](resource/image/%E4%B8%AA%E4%BA%BA%E4%B8%AD%E5%BF%83.png)

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