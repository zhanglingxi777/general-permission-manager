package top.zhouyang.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.zhouyang.admin.domain.RolePermission;

import java.util.List;

/**
 * 角色-菜单关系
 */
public interface IRolePermissionService extends IService<RolePermission> {
    /**
     * 保存角色权限关系
     *
     * @param roleId        角色Id
     * @param permissionIds 权限Id列表
     * @return 成功/失败
     */
    boolean saveRolePermission(Long roleId, List<Long> permissionIds);
}
