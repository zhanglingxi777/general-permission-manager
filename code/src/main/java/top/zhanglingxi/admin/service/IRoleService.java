package top.zhanglingxi.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.zhanglingxi.admin.domain.Role;
import top.zhanglingxi.admin.domain.vo.query.RoleQueryVO;

import java.util.List;

/**
 * 角色Service接口
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
public interface IRoleService extends IService<Role> {
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
     * @param page    分页参数
     * @param queryVO 查询参数
     * @return 角色集合
     */
    IPage<Role> selectRoleList(IPage<Role> page, RoleQueryVO queryVO);

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
     * 批量删除角色
     *
     * @param ids 需要删除的角色主键集合
     * @return 结果
     */
    int deleteRoleByIds(Long[] ids);

    /**
     * 删除角色信息
     *
     * @param id 角色主键
     * @return 结果
     */
    int deleteRoleById(Long id);

    /**
     * 根据用户ID查询该用户的角色ID
     *
     * @param userId 用户ID
     * @return 用户的角色ID
     */
    List<Long> selectRoleIdsByUserId(Long userId);
}
