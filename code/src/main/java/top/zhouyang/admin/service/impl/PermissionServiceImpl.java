package top.zhouyang.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhouyang.admin.domain.Permission;
import top.zhouyang.admin.domain.User;
import top.zhouyang.admin.domain.vo.RolePermissionVO;
import top.zhouyang.admin.mapper.PermissionMapper;
import top.zhouyang.admin.mapper.RoleMapper;
import top.zhouyang.admin.mapper.UserMapper;
import top.zhouyang.admin.service.IPermissionService;
import top.zhouyang.admin.service.IMenuTree;
import top.zhouyang.common.utils.DateUtils;

import java.util.*;

/**
 * 权限Service业务层处理
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    private final PermissionMapper permissionMapper;
    private final IMenuTree menuTree;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper permissionMapper, IMenuTree menuTree, UserMapper userMapper, RoleMapper roleMapper) {
        this.permissionMapper = permissionMapper;
        this.menuTree = menuTree;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    /**
     * 查询权限
     *
     * @param id 权限主键
     * @return 权限
     */
    @Override
    public Permission selectPermissionById(Long id) {
        return permissionMapper.selectPermissionById(id);
    }

    /**
     * 查询权限列表
     *
     * @param ebSysPermission 权限
     * @return 权限
     */
    @Override
    public List<Permission> selectPermissionList(Permission ebSysPermission) {
        List<Permission> list = permissionMapper.selectPermissionList(ebSysPermission);
        return this.menuTree.makeMenuTree(list, 0L);
    }

    @Override
    public List<Permission> selectPermissionByRoleId(Long roleId) {
        return baseMapper.selectPermissionByRoleId(roleId);
    }

    @Override
    public RolePermissionVO selectPermissionTree(Long userId, Long roleId) {
        // 查询当前用户信息
        User user = userMapper.selectUserById(userId);
        List<Permission> list;
        // 判断当前用户角色，如果是管理员，则查询所有权限，如果不是管理员，则只查询自己所拥有的权限
        if (Objects.nonNull(user)) {
            if (Objects.nonNull(user.getIsAdmin()) && user.getIsAdmin() == 1) {
                // 查询所有权限
                list = list();
            } else {
                // 根据用户ID查询
                List<Long> roleIds = roleMapper.selectRoleIdsByUserId(userId);
                list = baseMapper.selectPermissionListByRoleIds(roleIds);
            }
            // 组装成树结构
            List<Permission> permissionList = menuTree.makeMenuTree(list, 0L);
            // 查询要分配角色额原有权限
            List<Permission> rolePermissionList = baseMapper.selectPermissionByRoleId(roleId);
            // 找出该角色存在的数据
            ArrayList<Long> idList = new ArrayList<>();
            Optional.ofNullable(list).orElse(new ArrayList<>())
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(item -> {
                        Optional.ofNullable(rolePermissionList).orElse(new ArrayList<>())
                                .stream()
                                .filter(Objects::nonNull)
                                .forEach(obj -> {
                                    if (item.getId().equals(obj.getId())) {
                                        idList.add(obj.getId());
                                    }
                                });
                    });
            // 创建
            RolePermissionVO vo = new RolePermissionVO();
            vo.setPermissionList(permissionList);
            vo.setCheckList(idList.toArray());
            return vo;
        }
        throw new IllegalArgumentException("查询用户信息失败");
    }

    @Override
    public List<Permission> selectParentPermissionList() {
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.in("type", Arrays.asList(0, 1));
        qw.orderByAsc("order_num");
        List<Permission> list = permissionMapper.selectList(qw);
        Permission permission = new Permission();
        permission.setId(0L);
        permission.setParentId(-1L);
        permission.setLabel("顶级菜单");
        list.add(permission);
        return menuTree.makeMenuTree(list, -1L);
    }

    /**
     * 新增权限
     *
     * @param ebSysPermission 权限
     * @return 结果
     */
    @Override
    public int insertPermission(Permission ebSysPermission) {
        // parent_name 根据parent_id查询 parent_id 为0：顶级菜单 为其他 从表中查询
        // is_delete = 0
        ebSysPermission.setIsDelete(0L);
        if (ebSysPermission.getParentId() == 0L) {
            ebSysPermission.setParentName("顶级菜单");
        } else {
            Permission parent = permissionMapper.selectPermissionById(ebSysPermission.getParentId());
            ebSysPermission.setParentName(parent.getLabel());
        }
        ebSysPermission.setCreateTime(DateUtils.getNowDate());

        return permissionMapper.insertPermission(ebSysPermission);
    }

    /**
     * 修改权限
     *
     * @param ebSysPermission 权限
     * @return 结果
     */
    @Override
    public int updatePermission(Permission ebSysPermission) {
        ebSysPermission.setUpdateTime(DateUtils.getNowDate());
        return permissionMapper.updatePermission(ebSysPermission);
    }

    /**
     * 批量删除权限
     *
     * @param ids 需要删除的权限主键
     * @return 结果
     */
    @Override
    public int deletePermissionByIds(Long[] ids) {
        return permissionMapper.deletePermissionByIds(ids);
    }

    /**
     * 删除权限信息
     *
     * @param id 权限主键
     * @return 结果
     */
    @Override
    public int deletePermissionById(Long id) {
        return permissionMapper.deletePermissionById(id);
    }

    @Override
    public boolean checkHasChildrenPermission(Long id) {
        LambdaQueryWrapper<Permission> qw = new LambdaQueryWrapper<>();
        qw.eq(Objects.nonNull(id), Permission::getParentId, id);
        Long count = permissionMapper.selectCount(qw);
        return count != 0;
    }

    @Override
    public List<Permission> selectPermissionListByRoleIds(List<Long> roles) {
        return permissionMapper.selectPermissionListByRoleIds(roles);
    }
}
