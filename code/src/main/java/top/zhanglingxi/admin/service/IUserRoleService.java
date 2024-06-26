package top.zhanglingxi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.zhanglingxi.admin.domain.UserRole;

import java.util.List;

/**
 * 用户-角色关系
 * @author Zhang linxi
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 保存用户角色管理
     * @param userId 用户ID
     * @param roleIdList 角色列表
     * @return 是否成功
     */
    boolean saveUserRole(Long userId, List<Long> roleIdList);
}
