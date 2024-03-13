package top.zhanglingxi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhanglingxi.admin.domain.Role;
import top.zhanglingxi.admin.domain.RolePermission;
import top.zhanglingxi.admin.domain.User;
import top.zhanglingxi.admin.domain.UserRole;
import top.zhanglingxi.admin.domain.vo.query.RoleQueryVO;
import top.zhanglingxi.admin.mapper.RoleMapper;
import top.zhanglingxi.admin.mapper.RolePermissionMapper;
import top.zhanglingxi.admin.mapper.UserMapper;
import top.zhanglingxi.admin.mapper.UserRoleMapper;
import top.zhanglingxi.admin.service.IRoleService;
import top.zhanglingxi.common.utils.DateUtils;
import top.zhanglingxi.common.utils.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 角色Service业务层处理
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Service
@Transactional
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper, UserMapper userMapper, UserRoleMapper userRoleMapper, RolePermissionMapper rolePermissionMapper) {
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    /**
     * 查询角色
     *
     * @param id 角色主键
     * @return 角色
     */
    @Override
    public Role selectRoleById(Long id) {
        return roleMapper.selectRoleById(id);
    }

    @Override
    public IPage<Role> selectRoleList(IPage<Role> page, RoleQueryVO queryVO) {
        LambdaQueryWrapper<Role> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(queryVO.getRoleName()), Role::getRoleName, queryVO.getRoleName());
        qw.orderByAsc(Role::getId);
        // 获取用户信息
        Long userId = queryVO.getUserId();
        User user = userMapper.selectUserById(userId);
        if (Objects.nonNull(user)) {
            if (Objects.nonNull(user.getIsAdmin())) {
                if (user.getIsAdmin() != 1) {
                    // 如果该用户不是管理员，则该用户只能查看自己新增的角色列表
                    qw.eq(Objects.nonNull(queryVO.getCreateUser()), Role::getCreateUser, queryVO.getCreateUser());
                }
                return roleMapper.selectPage(page, qw);
            }
            log.error("当前用户的 is_admin 属性值为空，user_id = {}！", userId);
            throw new IllegalArgumentException("请求参数异常！");
        }
        log.error("根据请求参数 user_id = {} 查询不到用户信息！", userId);
        throw new IllegalArgumentException("请求参数异常！");
    }


    /**
     * 新增角色
     *
     * @param ebSysRole 角色
     * @return 结果
     */
    @Override
    public int insertRole(Role ebSysRole) {
        ebSysRole.setCreateTime(DateUtils.getNowDate());
        ebSysRole.setUpdateTime(DateUtils.getNowDate());
        return roleMapper.insertRole(ebSysRole);
    }

    /**
     * 修改角色
     *
     * @param ebSysRole 角色
     * @return 结果
     */
    @Override
    public int updateRole(Role ebSysRole) {
        ebSysRole.setUpdateTime(DateUtils.getNowDate());
        return roleMapper.updateRole(ebSysRole);
    }

    /**
     * 批量删除角色
     *
     * @param ids 需要删除的角色主键
     * @return 结果
     */
    @Override
    public int deleteRoleByIds(Long[] ids) {
        return roleMapper.deleteRoleByIds(ids);
    }

    /**
     * 删除角色信息
     *
     * @param id 角色主键
     * @return 结果
     */
    @Override
    public int deleteRoleById(Long id) {
        // 判断该角色是否被分配给用户，已分配不允许删除
        LambdaQueryWrapper<UserRole> urQw = new LambdaQueryWrapper<>();
        urQw.eq(UserRole::getRoleId, id);
        Long count = userRoleMapper.selectCount(urQw);
        if (count > 0) {
            return -1;
        }
        // 删除角色权限关系
        LambdaQueryWrapper<RolePermission> rpQw = new LambdaQueryWrapper<>();
        rpQw.eq(RolePermission::getRoleId, id);
        rolePermissionMapper.delete(rpQw);
        // 删除角色信息
        return roleMapper.deleteRoleById(id);
    }

    @Override
    public List<Long> selectRoleIdsByUserId(Long userId) {
        return roleMapper.selectRoleIdsByUserId(userId);
    }
}
