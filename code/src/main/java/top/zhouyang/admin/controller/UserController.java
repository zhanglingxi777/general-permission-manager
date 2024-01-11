package top.zhouyang.admin.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import top.zhouyang.admin.domain.Permission;
import top.zhouyang.admin.domain.Role;
import top.zhouyang.admin.domain.User;
import top.zhouyang.admin.domain.UserRole;
import top.zhouyang.admin.domain.vo.*;
import top.zhouyang.admin.domain.vo.query.ResetPwdQueryVO;
import top.zhouyang.admin.domain.vo.query.UserQueryVO;
import top.zhouyang.admin.service.*;
import top.zhouyang.common.domain.AjaxResult;
import top.zhouyang.common.utils.DateUtils;
import top.zhouyang.common.utils.RedisUtils;
import top.zhouyang.common.utils.StringUtils;
import top.zhouyang.framework.config.ThreadLocalConfig;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ThreadLocalConfig threadLocalConfig;
    @Value("${custom.jwt.key}")
    private String key;
    @Value("${user.password.maxRetryCount}")
    private Integer retryCount;
    @Value("${user.password.lockTime}")
    private Integer lockTime;

    /**
     * 获取菜单列表
     *
     * @return 菜单列表
     */
    @GetMapping("/getMenuList")
    public AjaxResult getMenuList() {
        Map map = threadLocalConfig.get();
        Object username = map.get("username");
        User user = userService.selectUserByUsername(username.toString());
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
     * 刷新token
     *
     * @return 新的token
     */
    @GetMapping("/refreshToken")
    public AjaxResult refreshToken() {
        Map map = threadLocalConfig.get();
        Object username = map.get("username");
        JWTSigner jwtSigner = JWTSignerUtil.hs256(key.getBytes());
        Date nowDate = DateUtils.getNowDate();
        Date expireDate = DateUtils.addMinutes(nowDate, 30);
        String token = JWT.create()
                .setExpiresAt(expireDate)
                .setPayload("username", username)
                .setSigner(jwtSigner)
                .sign();
        redisUtils.deleteObject("login:" + username);
        redisUtils.setCacheObject("login:" + username, token, 30, TimeUnit.MINUTES);
        TokenVO tokenVO = new TokenVO(expireDate.getTime(), token);
        return AjaxResult.success("token刷新成功", tokenVO);
    }

    /**
     * 获取验证码
     *
     * @return 验证码
     */
    @GetMapping("/vcImage")
    public AjaxResult image() {
        UUID uuid = UUID.randomUUID();
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 80, 4, 10);
        captcha.setGenerator(new RandomGenerator("0123456789", 4));
        captcha.createCode();
        String code = captcha.getCode();
        redisUtils.setCacheObject("image-code:" + uuid, code, 5, TimeUnit.MINUTES);
        Map<String, Object> data = new HashMap<>();
        data.put("uuid", uuid);
        data.put("image", captcha.getImageBase64Data());
        return AjaxResult.success(data);
    }

    /**
     * 用户登录
     *
     * @param loginReqVO 登录请求参数
     * @return 是否成功
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginReqVO loginReqVO) {
        String uuid = loginReqVO.getUuid();
        String verifyCode = loginReqVO.getVerifyCode();
        // 1.校验验证码
        String imageCodeCache = redisUtils.getCacheObject("image-code:" + uuid);
        if (StringUtils.isBlank(imageCodeCache)) {
            return AjaxResult.error("验证码已过期,请刷新验证码!");
        }
        if (!imageCodeCache.equalsIgnoreCase(verifyCode)) {
            return AjaxResult.error("验证码错误,请重新输入验证码!");
        }
        // 2.校验用户名和密码
        String username = loginReqVO.getUsername();
        User user = userService.selectUserByUsername(username);
        if (Objects.isNull(user)) {
            return AjaxResult.error("用户名或密码错误,请重新输入!");
        }
        // 判断用户是否超过最大登录失败次数
        // 获取用户被限制登录的时间
        Date limitTime = user.getLoginLimitTime();
        if (Objects.nonNull(limitTime)) {
            // 用户登录时间被限制
            Date nowDate = DateUtils.getNowDate();
            if (nowDate.before(limitTime)) {
                // 用户被限制登录
                long limit = limitTime.getTime() / 1000;
                long now = nowDate.getTime() / 1000;
                return AjaxResult.error(HttpStatus.FORBIDDEN.value(), "用户登录受限,请" + (limit - now) + "秒后登录!");
            }
        }
        // 获取用户登录失败次数
        Integer loginErrorNum = user.getLoginErrorNum();
        if (loginErrorNum >= retryCount) {
            // 限制用户登录时间
            Date now = DateUtils.getNowDate();
            Date lockDate = DateUtils.addMinutes(now, lockTime);
            user.setLoginLimitTime(lockDate);
            // 清空登录失败次数
            user.setLoginErrorNum(0);
            // 更新用户信息
            userService.updateById(user);
            return AjaxResult.error(HttpStatus.FORBIDDEN.value(), "用户登录失败次数超过限制,账号被锁定,请稍后再登录!");
        } else {
            // 判断用户是否登录成功
            String password = loginReqVO.getPassword();
            if (password.equals(user.getPassword())) {
                // 3.登录成功 生成token
                // 生成token
                JWTSigner jwtSigner = JWTSignerUtil.hs256(key.getBytes());
                Date nowDate = DateUtils.getNowDate();
                Date expireDate = DateUtils.addMinutes(nowDate, 30);
                String token = JWT.create()
                        .setExpiresAt(expireDate)
                        .setPayload("username", username)
                        .setSigner(jwtSigner)
                        .sign();
                // 获取token并存入redis中，以username为key 时效30分钟
                redisUtils.setCacheObject("login:" + username, token, 30, TimeUnit.MINUTES);
                // 修改用户最后一次登录时间
                user.setLastLoginTime(DateUtils.getNowDate());
                userService.updateById(user);
                // 返回数据
                AjaxResult success = AjaxResult.success("登录成功!");
                success.put("token", token);
                return success;
            } else {
                // 登录失败 用户登录失败次数 +1
                user.setLoginErrorNum(user.getLoginErrorNum() + 1);
                userService.updateById(user);
                return AjaxResult.error("用户名或密码错误,请重新输入!");
            }
        }
    }

    /**
     * 注销登录
     * @return 是否成功
     */
    @GetMapping("/logout")
    public AjaxResult logout() {
        Map map = threadLocalConfig.get();
        Object username = map.get("username");
        redisUtils.deleteObject("login:" + username);
        return AjaxResult.success("注销成功");
    }

    /**
     * 获取当前登录用户VO信息
     *
     * @return 当前登录用户VO信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        Map map = threadLocalConfig.get();
        Object username = map.get("username");
        User user = userService.selectUserByUsername(username.toString());
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
    public AjaxResult add(@RequestBody User ebSysUser) {
        User user = userService.selectUserByUsername(ebSysUser.getUsername());
        if (user != null) {
            return AjaxResult.error("该登录名称已被使用，请重新输入");
        }
        int res = userService.insertUser(ebSysUser);
        if (res > 0) {
            AjaxResult success = AjaxResult.success("用户添加成功");
            success.put("userId", ebSysUser.getId());
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
        if (userService.resetPwd(queryVO.getUserId(), queryVO.getOldPwd(), queryVO.getNewPwd())) {
            return AjaxResult.success("修改密码成功");
        }
        return AjaxResult.error("修改密码失败！");
    }

    /**
     * 获取登录用户信息列表
     *
     * @return 登录用户信息列表
     */
    @GetMapping("/login")
    public AjaxResult loginUser() {
        Collection<String> keys = redisUtils.keys("login:" + "*");
        List<LoginUserInfoVO> loginUserList = new ArrayList<>();
        keys.forEach(key -> {
            String[] split = key.split(":");
            int length = split.length;
            String username = split[length - 1];
            User user = userService.selectUserByUsername(username);
            Date lastLoginTime = user.getLastLoginTime();
            LoginUserInfoVO loginUser = new LoginUserInfoVO(username, lastLoginTime);
            loginUserList.add(loginUser);
        });
        return AjaxResult.success(loginUserList);
    }

    /**
     * 强制注销登录
     *
     * @param username 用户名
     * @return 是否成功
     */
    @GetMapping("/force/{username}")
    public AjaxResult forceLogout(@PathVariable String username) {
        if ("admin".equals(username)) {
            return AjaxResult.error("不允许注销超级管理员！");
        }
        if (redisUtils.deleteObject("login:" + username)) {
            return AjaxResult.success("注销成功");
        }
        return AjaxResult.error("注销失败！");
    }
}
