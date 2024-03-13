package top.zhanglingxi.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.zhanglingxi.admin.domain.Permission;
import top.zhanglingxi.admin.domain.vo.RolePermissionVO;

import java.util.List;

/**
 * 权限Service接口
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
public interface IPermissionService extends IService<Permission> {
    /**
     * 查询权限
     *
     * @param id 权限主键
     * @return 权限
     */
    Permission selectPermissionById(Long id);

    /**
     * 查询权限列表
     *
     * @param ebSysPermission 权限
     * @return 权限集合
     */
    List<Permission> selectPermissionList(Permission ebSysPermission);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色Id
     * @return 权限列表
     */
    List<Permission> selectPermissionByRoleId(Long roleId);

    /**
     * 查询分配权限树列表
     *
     * @param userId 用户Id
     * @param roleId 角色Id
     * @return 权限树列表
     */
    RolePermissionVO selectPermissionTree(Long userId, Long roleId);

    /**
     * 查询上级菜单列表
     *
     * @return 上级菜单列表
     */
    List<Permission> selectParentPermissionList();

    /**
     * 新增权限
     *
     * @param ebSysPermission 权限
     * @return 结果
     */
    public int insertPermission(Permission ebSysPermission);

    /**
     * 修改权限
     *
     * @param ebSysPermission 权限
     * @return 结果
     */
    public int updatePermission(Permission ebSysPermission);

    /**
     * 批量删除权限
     *
     * @param ids 需要删除的权限主键集合
     * @return 结果
     */
    public int deletePermissionByIds(Long[] ids);

    /**
     * 删除权限信息
     *
     * @param id 权限主键
     * @return 结果
     */
    public int deletePermissionById(Long id);

    public boolean checkHasChildrenPermission(Long id);

    /**
     * 根据权限Id列表查询权限信息
     *
     * @param roles 权限Id列表
     * @return 权限信息
     */
    List<Permission> selectPermissionListByRoleIds(List<Long> roles);
}
