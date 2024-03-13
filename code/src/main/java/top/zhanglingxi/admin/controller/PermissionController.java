package top.zhanglingxi.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhanglingxi.admin.domain.Permission;
import top.zhanglingxi.admin.service.IPermissionService;
import top.zhanglingxi.common.domain.AjaxResult;

import java.util.List;

/**
 * 权限Controller
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@RestController
@RequestMapping("/sys/permission")
public class PermissionController {
    private final IPermissionService permissionService;

    @Autowired
    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 查询菜单列表
     *
     * @param permission 查询信息
     * @return 菜单列表
     */
    @GetMapping
    public AjaxResult list(Permission permission) {
        List<Permission> list = permissionService.selectPermissionList(permission);
        return AjaxResult.success(list);
    }

    /**
     * 获取上级菜单列表
     * @return 上级菜单列表
     */
    @GetMapping("/parent")
    public AjaxResult parentList() {
        List<Permission> list = permissionService.selectParentPermissionList();
        return AjaxResult.success(list);
    }

    /**
     * 获取权限详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(permissionService.selectPermissionById(id));
    }

    /**
     * 新增权限
     */
    @PostMapping
    public AjaxResult add(@RequestBody Permission ebSysPermission) {
        int res = permissionService.insertPermission(ebSysPermission);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 修改权限
     */
    @PutMapping


    public AjaxResult edit(@RequestBody Permission ebSysPermission) {
        int res = permissionService.updatePermission(ebSysPermission);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        Long id=ids[0];
        if(permissionService.checkHasChildrenPermission(id))
        {
            return AjaxResult.error("有子节点，不能删除");
        }
        int res = permissionService.deletePermissionByIds(ids);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
