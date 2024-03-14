package top.zhanglingxi.framework.config.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.zhanglingxi.admin.domain.Permission;
import top.zhanglingxi.admin.domain.User;
import top.zhanglingxi.admin.service.IPermissionService;
import top.zhanglingxi.admin.service.IRoleService;
import top.zhanglingxi.admin.service.IUserService;

import java.util.List;
import java.util.Objects;

/**
 * 获取用户明细类
 *
 * @author Zhang Wenxu
 * @date 2024/03/14
 */
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final IUserService userService;
    private final IRoleService roleService;
    private final IPermissionService permissionService;

    @Autowired
    public UserDetailsServiceImpl(IUserService userService, IRoleService roleService, IPermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    /**
     * 根据username查询用户
     *
     * @param username 用户名
     * @return 用户详情西悉尼
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectUserByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误！");
        }
        List<Long> roles = roleService.selectRoleIdsByUserId(user.getId());
        List<Permission> permissions = permissionService.selectPermissionListByRoleIds(roles);
        // 设置菜单列表
        user.setPermissionList(permissions);
        String[] strings = permissions.stream().filter(Objects::nonNull)
                .map(Permission::getCode).filter(Objects::nonNull).toArray(String[]::new);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(strings);
        // 设置权限列表
        user.setAuthorities(authorities);
        return user;
    }
}
