package top.zhouyang.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhouyang.admin.domain.LoginLog;
import top.zhouyang.admin.domain.vo.LoginLogVO;
import top.zhouyang.admin.mapper.LoginLogMapper;
import top.zhouyang.admin.service.ILoginLogService;
import top.zhouyang.common.utils.StringUtils;

/**
 * 登录日志Service业务层处理
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Service
@Transactional
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {
    @Autowired
    private LoginLogMapper loginLogMapper;

    /**
     * 查询登录日志
     *
     * @param id 登录日志主键
     * @return 登录日志
     */
    @Override
    public LoginLog selectLoginLogById(Long id) {
        return loginLogMapper.selectLoginLogById(id);
    }

    /**
     * 查询登录日志列表
     */
    @Override
    public Page<LoginLog> selectLoginLogList(Page<LoginLog> page, LoginLogVO queryVO) {
        LambdaQueryWrapper<LoginLog> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(queryVO.getUsername()), LoginLog::getUsername, queryVO.getUsername());
        loginLogMapper.selectPage(page, qw);
        return page;
    }

    /**
     * 新增登录日志
     *
     * @param ebSysLoginLog 登录日志
     * @return 结果
     */
    @Override
    public int insertLoginLog(LoginLog ebSysLoginLog) {
        return loginLogMapper.insertLoginLog(ebSysLoginLog);
    }

    /**
     * 修改登录日志
     *
     * @param ebSysLoginLog 登录日志
     * @return 结果
     */
    @Override
    public int updateLoginLog(LoginLog ebSysLoginLog) {
        return loginLogMapper.updateLoginLog(ebSysLoginLog);
    }

    /**
     * 批量删除登录日志
     *
     * @param ids 需要删除的登录日志主键
     * @return 结果
     */
    @Override
    public int deleteLoginLogByIds(Long[] ids) {
        return loginLogMapper.deleteLoginLogByIds(ids);
    }

    /**
     * 删除登录日志信息
     *
     * @param id 登录日志主键
     * @return 结果
     */
    @Override
    public int deleteLoginLogById(Long id) {
        return loginLogMapper.deleteLoginLogById(id);
    }
}
