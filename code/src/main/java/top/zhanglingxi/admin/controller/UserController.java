package top.zhanglingxi.admin.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zhanglingxi.admin.domain.*;
import top.zhanglingxi.admin.domain.vo.*;
import top.zhanglingxi.admin.domain.vo.query.ResetPwdQueryVO;
import top.zhanglingxi.admin.domain.vo.query.UserQueryVO;
import top.zhanglingxi.admin.service.*;
import top.zhanglingxi.common.domain.AjaxResult;
import top.zhanglingxi.common.utils.DateUtils;
import top.zhanglingxi.common.utils.RedisUtils;
import top.zhanglingxi.common.utils.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户Controller
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {
    @Value("${custom.aes.key}")
    private String aesKey;
    @Value("${user.password.maxRetryCount}")
    private Integer retryCount;
    @Value("${user.password.lockTime}")
    private Integer lockTime;

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IMenuTree menuTree;

    /**
     * 获取菜单列表
     *
     * @return 菜单列表
     */
    @GetMapping("/getMenuList")
    public AjaxResult getMenuList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();
        User user = userService.selectUserByUsername(loginUser.getUsername());
        List<Long> roleIds = roleService.selectRoleIdsByUserId(user.getId());
        List<Permission> permissionList = permissionService.selectPermissionListByRoleIds(roleIds);
        List<Permission> collect = permissionList.stream()
                .filter(item -> Objects.nonNull(item) && item.getType() != 2)
                .collect(Collectors.toList());
        List<RouterVO> routerVOList = menuTree.makeRouter(collect, 0L);
        return AjaxResult.success("菜单数据获取成功", routerVOList);
    }

    /**
     * 获取用户下拉框列表
     *
     * @return 用户下拉框列表
     */
    @GetMapping("/list/select")
    public AjaxResult getSelectList() {
        List<User> list = userService.list();
        ArrayList<UserSelectVO> selectList = new ArrayList<>();
        for (User user : list) {
            UserSelectVO selectVO = new UserSelectVO();
            BeanUtils.copyProperties(user, selectVO);
            selectList.add(selectVO);
        }
        return AjaxResult.success(selectList);
    }

    /**
     * 获取当前登录用户VO信息
     *
     * @return 当前登录用户VO信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();
        String username = loginUser.getUsername();
        User user = userService.selectUserByUsername(username);
        List<Long> roleIds = roleService.selectRoleIdsByUserId(user.getId());
        List<Permission> permissionList = permissionService.selectPermissionListByRoleIds(roleIds);
        Object[] roles = permissionList.stream().filter(Objects::nonNull).map(Permission::getCode).toArray();
        LambdaQueryWrapper<UserRole> qw = new LambdaQueryWrapper<>();
        qw.eq(Objects.nonNull(user.getId()), UserRole::getUserId, user.getId());
        List<UserRole> userRolelist = userRoleService.list(qw);
        ArrayList<String> roleList = new ArrayList<>();
        for (UserRole userRole : userRolelist) {
            Role role = roleService.selectRoleById(userRole.getRoleId());
            roleList.add(role.getRoleCode());
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(user.getId());
        userInfoVO.setName(user.getUsername());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setIntroduction(null);
        userInfoVO.setRoles(roles);
        userInfoVO.setRoleLCodeList(roleList);
        return AjaxResult.success("用户信息查询成功", userInfoVO);
    }

    /**
     * 分页查询用户信息
     *
     * @param queryVO 查询条件
     * @return 分页用户信息
     */
    @GetMapping
    public AjaxResult listByPage(UserQueryVO queryVO) {
        IPage<User> page = new Page<>(queryVO.getPageNo(), queryVO.getPageSize());
        userService.selectUserListByPage(page, queryVO);
        return AjaxResult.success(page);
    }

    /**
     * 获取用户详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(userService.selectUserById(id));
    }

    /**
     * 新增用户
     */
    @PostMapping
    public AjaxResult add(@RequestBody User addUser) {
        User user = userService.selectUserByUsername(addUser.getUsername());
        if (user != null) {
            return AjaxResult.error("该登录名称已被使用，请重新输入");
        }
        // 默认密码 123456
        AES aes = SecureUtil.aes(aesKey.getBytes());
        String password = aes.encryptBase64("123456");
        addUser.setPassword(password);
        int res = userService.insertUser(addUser);
        if (res > 0) {
            AjaxResult success = AjaxResult.success("用户添加成功");
            success.put("userId", addUser.getId());
            return success;
        }
        return AjaxResult.error("用户添加失败");
    }

    /**
     * 修改用户
     */
    @PutMapping
    public AjaxResult edit(@RequestBody User ebSysUser) {
        int res = userService.updateUser(ebSysUser);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        int res = userService.deleteUserById(id);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 重置密码
     *
     * @param queryVO 请求参数
     * @return 是否成功
     */
    @PutMapping("/pwd")
    public AjaxResult resetPwd(@RequestBody ResetPwdQueryVO queryVO) {
        try {
            if (userService.resetPwd(queryVO.getUserId(), queryVO.getOldPwd(), queryVO.getNewPwd())) {
                return AjaxResult.success("修改密码成功");
            }
            return AjaxResult.error("修改密码失败！");
        } catch (Exception e) {
            return AjaxResult.error("修改密码失败！");
        }
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    public AjaxResult importData(MultipartFile file) throws IOException {
        List<User> userList = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        try (InputStream is = file.getInputStream()) {
            EasyExcel.read(is, UserImportVO.class, new PageReadListener<UserImportVO>(dataList -> {
                if (dataList.isEmpty()) {
                    message.append("<p>导入数据为空！</p>");
                } else {
                    for (int i = 0; i < dataList.size(); i++) {
                        try {
                            UserImportVO data = dataList.get(i);
                            // 用户名
                            String username = data.getUsername();
                            if (StringUtils.isEmpty(username)) {
                                message.append("<p>").append("第 ").append(i + 1).append(" 行: </p>").append("<p>用户名不能为空</p>");
                            }
                            LambdaQueryWrapper<User> userQw = new LambdaQueryWrapper<>();
                            userQw.eq(StringUtils.isNotEmpty(username), User::getUsername, username);
                            User user = userService.getOne(userQw);
                            if (Objects.nonNull(user)) {
                                message.append("<p>").append("第 ").append(i + 1).append(" 行: </p>").append("<p>").append("用户名已存在").append("</p>");
                            }
                            User newUser = new User();
                            BeanUtils.copyProperties(data, newUser);
                            // 创建时间
                            newUser.setCreateTime(DateUtils.getNowDate());
                            // 默认密码 123456
                            AES aes = SecureUtil.aes(aesKey.getBytes());
                            String password = aes.encryptBase64("123456");
                            newUser.setPassword(password);
                            // 登录失败次数
                            newUser.setLoginErrorNum(0);
                            // 是否为管理员
                            newUser.setIsAdmin(0);
                            userList.add(newUser);
                        } catch (Exception e) {
                            message.append("<p>").append("第 ").append(i + 1).append(" 行: </p>").append("<p>").append(e.getMessage()).append("</p>");
                        }
                    }
                }
            })).sheet().doRead();
        }
        if (message.toString().isEmpty()) {
            userService.saveBatch(userList);
            return AjaxResult.success("导入成功");
        }
        message.append("<p>请检查数据正确性后再导入！</p>");
        return AjaxResult.error(message.toString());
    }

    /**
     * 导入模板
     */
    @GetMapping("/import/template")
    public void importTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=公司岗位导入模板.xlsx");
        response.reset(); // 重点突出
        try (ServletOutputStream os = response.getOutputStream()) {
            EasyExcel.write(os, UserImportVO.class)
                    .sheet("用户导入模板")
                    .doWrite(new ArrayList<>());
        }
    }

    /**
     * 导出
     */
    @GetMapping("/output")
    public void output(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + new Date().getTime() + ".xlsx");
        response.reset(); // 重点突出
        try (ServletOutputStream os = response.getOutputStream()) {
            List<User> list = userService.list();
            List<UserImportVO> importVOList = new ArrayList<>();
            list.forEach(item -> {
                UserImportVO importVO = new UserImportVO();
                BeanUtils.copyProperties(item, importVO);
                importVOList.add(importVO);
            });
            EasyExcel.write(os, UserImportVO.class)
                    .sheet("导出数据")
                    .doWrite(importVOList);
        }
    }
}
