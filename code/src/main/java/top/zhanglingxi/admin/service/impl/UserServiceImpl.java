package top.zhanglingxi.admin.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhanglingxi.admin.domain.User;
import top.zhanglingxi.admin.domain.UserRole;
import top.zhanglingxi.admin.domain.vo.query.UserQueryVO;
import top.zhanglingxi.admin.mapper.UserMapper;
import top.zhanglingxi.admin.mapper.UserRoleMapper;
import top.zhanglingxi.admin.service.IUserService;
import top.zhanglingxi.common.utils.DateUtils;
import top.zhanglingxi.common.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Service业务层处理
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Value("${custom.aes.key}")
    private String aesKey;
    private final UserRoleMapper ebSysUserRoleMapper;

    @Autowired
    public UserServiceImpl(UserRoleMapper ebSysUserRoleMapper) {
        this.ebSysUserRoleMapper = ebSysUserRoleMapper;
    }

    /**
     * 查询用户
     *
     * @param id 用户主键
     * @return 用户
     */
    @Override
    public User selectUserById(Long id) {
        return baseMapper.selectUserById(id);
    }

    @Override
    public User selectUserByUsername(String username) {
        return baseMapper.selectUserByUsername(username);
    }

    @Override
    public IPage<User> selectUserListByPage(IPage<User> page, UserQueryVO queryVO) {
        LambdaQueryWrapper<User> userQw = new LambdaQueryWrapper<>();
//        userQw.eq(Objects.nonNull(queryVO.getDepartmentId()), User::getDepartmentId, queryVO.getDepartmentId());
        // 用户名
        userQw.like(StringUtils.isNotEmpty(queryVO.getUsername()), User::getUsername, queryVO.getUsername());
        // 真是姓名
        userQw.like(StringUtils.isNotEmpty(queryVO.getRealName()), User::getRealName, queryVO.getRealName());
        // 电话
        userQw.like(StringUtils.isNotEmpty(queryVO.getPhone()), User::getPhone, queryVO.getPhone());
        return baseMapper.selectPage(page, userQw);
    }

    @Override
    public User getLoginUser() {
        // TODO getLoginUser
        return null;
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (Objects.nonNull(user)) {
            return user;
        }
        throw new RuntimeException("获取登录用户信息失败");*/
    }

    /**
     * 查询用户列表
     *
     * @param ebSysUser 用户
     * @return 用户
     */
    @Override
    public List<User> selectUserList(User ebSysUser) {
        return baseMapper.selectUserList(ebSysUser);
    }

    /**
     * 新增用户
     *
     * @param ebSysUser 用户
     * @return 结果
     */
    @Override
    public int insertUser(User ebSysUser) {
        String username = ebSysUser.getUsername();
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(username), User::getUsername, username);
        Long count = baseMapper.selectCount(qw);
        if (count > 0) {
            // username 已存在
            throw new IllegalArgumentException("用户名已存在！");
        }
        // 设置默认值
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // password
        ebSysUser.setPassword(passwordEncoder.encode("123456"));
        // is_admin
        ebSysUser.setIsAdmin(0);
        // login_error_num
        ebSysUser.setLoginErrorNum(0);
        ebSysUser.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insertUser(ebSysUser);
    }

    /**
     * 修改用户
     *
     * @param ebSysUser 用户
     * @return 结果
     */
    @Override
    public int updateUser(User ebSysUser) {
        ebSysUser.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateUser(ebSysUser);
    }

    /**
     * 批量删除用户
     *
     * @param ids 需要删除的用户主键
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] ids) {
        return baseMapper.deleteUserByIds(ids);
    }

    /**
     * 删除用户信息
     *
     * @param id 用户主键
     * @return 结果
     */
    @Override
    public int deleteUserById(Long id) {
        // 获取用户信息
        User user = baseMapper.selectUserById(id);
        if (user.getIsAdmin() == 1) {
            throw new RuntimeException("不能删除超级管理员！！！");
        }
        // 删除用户角色关系
        LambdaQueryWrapper<UserRole> urQw = new LambdaQueryWrapper<>();
        urQw.eq(UserRole::getUserId, id);
        List<UserRole> userRoleList = ebSysUserRoleMapper.selectList(urQw);
        List<Long> userRoleIdList = userRoleList.stream().map(UserRole::getId).collect(Collectors.toList());
        if (!userRoleIdList.isEmpty()) {
            ebSysUserRoleMapper.deleteBatchIds(userRoleIdList);
        }
        // 删除用户信息
        return baseMapper.deleteUserById(id);
    }

    @Override
    public boolean resetPwd(Long userId, String oldPwd, String newPwd) {
        User user = getById(userId);

        // 解密密码
        AES aes = SecureUtil.aes(aesKey.getBytes());
        oldPwd = aes.decryptStr(oldPwd);
        newPwd = aes.decryptStr(newPwd);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(oldPwd, user.getPassword())) {
            // oldPwd正确 更新用户密码
            if (oldPwd.equals(newPwd)) {
                throw new IllegalArgumentException("新密码与原密码相同，请输入不同的密码！");
            }

            user.setPassword(passwordEncoder.encode(newPwd));
            return updateById(user);
        }
        throw new IllegalArgumentException("原密码不正确，请重新输入！");
    }
}
