package top.zhanglingxi.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.zhanglingxi.admin.domain.LoginLog;
import top.zhanglingxi.admin.domain.vo.LoginLogVO;

/**
 * 登录日志Service接口
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
public interface ILoginLogService extends IService<LoginLog> {
    /**
     * 查询登录日志
     *
     * @param id 登录日志主键
     * @return 登录日志
     */
    LoginLog selectLoginLogById(Long id);

    /**
     * 查询登录日志列表
     *
     * @return 登录日志集合
     */
    Page<LoginLog> selectLoginLogList(Page<LoginLog> page, LoginLogVO queryVO);

    /**
     * 新增登录日志
     *
     * @param ebSysLoginLog 登录日志
     * @return 结果
     */
    int insertLoginLog(LoginLog ebSysLoginLog);

    /**
     * 修改登录日志
     *
     * @param ebSysLoginLog 登录日志
     * @return 结果
     */
    int updateLoginLog(LoginLog ebSysLoginLog);

    /**
     * 批量删除登录日志
     *
     * @param ids 需要删除的登录日志主键集合
     * @return 结果
     */
    int deleteLoginLogByIds(Long[] ids);

    /**
     * 删除登录日志信息
     *
     * @param id 登录日志主键
     * @return 结果
     */
    int deleteLoginLogById(Long id);
}
