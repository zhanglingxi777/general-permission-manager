package top.zhanglingxi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.zhanglingxi.admin.domain.LoginLog;

import java.util.List;

/**
 * 登录日志Mapper接口
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
    /**
     * 查询登录日志
     *
     * @param id 登录日志主键
     * @return 登录日志
     */
    public LoginLog selectLoginLogById(Long id);

    /**
     * 查询登录日志列表
     *
     * @param ebSysLoginLog 登录日志
     * @return 登录日志集合
     */
    public List<LoginLog> selectLoginLogList(LoginLog ebSysLoginLog);

    /**
     * 新增登录日志
     *
     * @param ebSysLoginLog 登录日志
     * @return 结果
     */
    public int insertLoginLog(LoginLog ebSysLoginLog);

    /**
     * 修改登录日志
     *
     * @param ebSysLoginLog 登录日志
     * @return 结果
     */
    public int updateLoginLog(LoginLog ebSysLoginLog);

    /**
     * 删除登录日志
     *
     * @param id 登录日志主键
     * @return 结果
     */
    public int deleteLoginLogById(Long id);

    /**
     * 批量删除登录日志
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLoginLogByIds(Long[] ids);
}
