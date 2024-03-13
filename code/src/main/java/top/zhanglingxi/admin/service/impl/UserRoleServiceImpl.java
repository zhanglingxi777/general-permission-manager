package top.zhanglingxi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhanglingxi.admin.domain.UserRole;
import top.zhanglingxi.admin.mapper.UserRoleMapper;
import top.zhanglingxi.admin.service.IUserRoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Override
    public boolean saveUserRole(Long userId, List<Long> roleIdList) {
        // 删除该用户的所有角色列表
        LambdaQueryWrapper<UserRole> urQw = new LambdaQueryWrapper<>();
        urQw.eq(Objects.nonNull(userId), UserRole::getUserId, userId);
        urQw.in(Objects.nonNull(roleIdList), UserRole::getRoleId, roleIdList);
        if (count(urQw) > 0) {
            if (!remove(urQw)) {
                return false;
            }
        }
        // 新增用户的角色列表
        if (!roleIdList.isEmpty()) {
            ArrayList<UserRole> userRoleList = new ArrayList<>();
            for (Long roleId : roleIdList) {
                UserRole ebSysUserRole = new UserRole(userId, roleId);
                userRoleList.add(ebSysUserRole);
            }
            return saveBatch(userRoleList);
        }
        return false;
    }
}
