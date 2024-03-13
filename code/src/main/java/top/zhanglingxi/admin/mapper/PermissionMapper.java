package top.zhanglingxi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.zhanglingxi.admin.domain.Permission;

import java.util.List;

/**
 * 权限Mapper接口
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
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
     * 根据角色ID查询角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> selectPermissionByRoleId(Long roleId);

    /**
     * 新增权限
     *
     * @param ebSysPermission 权限
     * @return 结果
     */
    int insertPermission(Permission ebSysPermission);

    /**
     * 修改权限
     *
     * @param ebSysPermission 权限
     * @return 结果
     */
    int updatePermission(Permission ebSysPermission);

    /**
     * 删除权限
     *
     * @param id 权限主键
     * @return 结果
     */
    int deletePermissionById(Long id);

    /**
     * 批量删除权限
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deletePermissionByIds(Long[] ids);

    /**
     * 根据权限Id列表查询权限信息
     *
     * @param roles 权限Id列表
     * @return 权限信息
     */
    List<Permission> selectPermissionListByRoleIds(List<Long> roles);
}
