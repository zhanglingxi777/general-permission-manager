package top.zhouyang.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.zhouyang.admin.domain.User;
import top.zhouyang.admin.domain.vo.query.UserQueryVO;

import java.util.List;

/**
 * 用户Service接口
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
public interface IUserService extends IService<User> {
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
     * 分页查询用户信息
     *
     * @param queryVO 查询条件
     * @return 分页用户信息
     */
    IPage<User> selectUserListByPage(IPage<User> page, UserQueryVO queryVO);

    /**
     * 获取登录用户信息
     *
     * @return 登录用户信息
     */
    User getLoginUser();

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
     * 批量删除用户
     *
     * @param ids 需要删除的用户主键集合
     * @return 结果
     */
    int deleteUserByIds(Long[] ids);

    /**
     * 删除用户信息
     *
     * @param id 用户主键
     * @return 结果
     */
    int deleteUserById(Long id);

    /**
     * 重置密码
     *
     * @param userId 用户Id
     * @param oldPwd 原密码
     * @param newPwd 新密码
     * @return 是否成功
     */
    boolean resetPwd(Long userId, String oldPwd, String newPwd);
}
