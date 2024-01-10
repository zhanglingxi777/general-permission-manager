package top.zhouyang.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.zhouyang.admin.domain.Role;

import java.util.List;

/**
 * 角色Mapper接口
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询角色
     *
     * @param id 角色主键
     * @return 角色
     */
    Role selectRoleById(Long id);

    /**
     * 查询角色列表
     *
     * @param ebSysRole 角色
     * @return 角色集合
     */
    List<Role> selectRoleList(Role ebSysRole);

    /**
     * 新增角色
     *
     * @param ebSysRole 角色
     * @return 结果
     */
    int insertRole(Role ebSysRole);

    /**
     * 修改角色
     *
     * @param ebSysRole 角色
     * @return 结果
     */
    int updateRole(Role ebSysRole);

    /**
     * 删除角色
     *
     * @param id 角色主键
     * @return 结果
     */
    int deleteRoleById(Long id);

    /**
     * 批量删除角色
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRoleByIds(Long[] ids);

    /**
     * 根据用户ID查询该用户的角色ID
     *
     * @param userId 用户ID
     * @return 用户的角色ID
     */
    List<Long> selectRoleIdsByUserId(Long userId);
}
