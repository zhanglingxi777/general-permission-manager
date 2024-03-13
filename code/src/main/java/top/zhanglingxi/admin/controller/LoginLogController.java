package top.zhanglingxi.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhanglingxi.admin.domain.LoginLog;
import top.zhanglingxi.admin.domain.vo.LoginLogVO;
import top.zhanglingxi.admin.service.ILoginLogService;
import top.zhanglingxi.common.domain.AjaxResult;

/**
 * 登录日志Controller
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 *
 */
@RestController
@RequestMapping("/system/loginLog")
public class LoginLogController {
    @Autowired
    private ILoginLogService loginLogService;
    /**
     * 获取登录日志列表
     */
    @GetMapping("/list")
    public AjaxResult getList(LoginLogVO queryVO){
        Page<LoginLog> page = new Page<>(queryVO.getPageNo(), queryVO.getPageSize());
        loginLogService.selectLoginLogList(page, queryVO);
        return AjaxResult.success(page);
    }

    /**
     * 获取登录日志详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(loginLogService.selectLoginLogById(id));
    }

    /**
     * 新增登录日志
     */
    @PostMapping
    public AjaxResult add(@RequestBody LoginLog ebSysLoginLog) {
        int res = loginLogService.insertLoginLog(ebSysLoginLog);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 修改登录日志
     */
    @PutMapping
    public AjaxResult edit(@RequestBody LoginLog ebSysLoginLog) {
        int res = loginLogService.updateLoginLog(ebSysLoginLog);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 删除登录日志
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int res = loginLogService.deleteLoginLogByIds(ids);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
