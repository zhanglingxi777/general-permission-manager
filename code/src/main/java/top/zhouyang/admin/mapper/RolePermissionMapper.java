package top.zhouyang.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.zhouyang.admin.domain.RolePermission;

/**
 * 角色-菜单关系表
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
}
