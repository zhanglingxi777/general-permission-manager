package top.zhanglingxi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.zhanglingxi.admin.domain.UserRole;

/**
 * 用户-角色关系表
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
