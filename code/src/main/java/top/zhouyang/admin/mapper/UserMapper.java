package top.zhouyang.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.zhouyang.admin.domain.User;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查询用户
     *
     * @param id 用户主键
     * @return 用户
     */
    User selectUserById(Long id);

    /**
     * 查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User selectUserByUsername(String username);

    /**
     * 查询用户列表
     *
     * @param ebSysUser 用户
     * @return 用户集合
     */
    List<User> selectUserList(User ebSysUser);

    /**
     * 新增用户
     *
     * @param ebSysUser 用户
     * @return 结果
     */
    int insertUser(User ebSysUser);

    /**
     * 修改用户
     *
     * @param ebSysUser 用户
     * @return 结果
     */
    int updateUser(User ebSysUser);

    /**
     * 删除用户
     *
     * @param id 用户主键
     * @return 结果
     */
    int deleteUserById(Long id);

    /**
     * 批量删除用户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteUserByIds(Long[] ids);
}
