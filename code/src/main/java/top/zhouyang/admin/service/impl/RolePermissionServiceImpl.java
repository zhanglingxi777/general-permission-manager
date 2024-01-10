package top.zhouyang.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhouyang.admin.domain.RolePermission;
import top.zhouyang.admin.mapper.RolePermissionMapper;
import top.zhouyang.admin.service.IRolePermissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {
    @Override
    public boolean saveRolePermission(Long roleId, List<Long> permissionIds) {
        // 1.删除该角色所有权限
        LambdaQueryWrapper<RolePermission> rpQw = new LambdaQueryWrapper<>();
        rpQw.eq(Objects.nonNull(roleId), RolePermission::getRoleId, roleId);
        long count = count(rpQw);
        if (count != 0) {
            if (!remove(rpQw)) {
                return false;
            }
        }
        // 2.新增角色权限
        ArrayList<RolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            rolePermissions.add(new RolePermission(roleId, permissionId));
        }
        return saveBatch(rolePermissions);
    }
}
