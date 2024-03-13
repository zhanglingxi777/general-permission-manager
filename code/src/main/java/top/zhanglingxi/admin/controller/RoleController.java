package top.zhanglingxi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhanglingxi.admin.domain.Role;
import top.zhanglingxi.admin.domain.UserRole;
import top.zhanglingxi.admin.domain.dto.RolePermissionDTO;
import top.zhanglingxi.admin.domain.dto.UserRoleDTO;
import top.zhanglingxi.admin.domain.vo.RolePermissionVO;
import top.zhanglingxi.admin.domain.vo.query.RoleQueryVO;
import top.zhanglingxi.admin.service.IPermissionService;
import top.zhanglingxi.admin.service.IRolePermissionService;
import top.zhanglingxi.admin.service.IRoleService;
import top.zhanglingxi.admin.service.IUserRoleService;
import top.zhanglingxi.common.domain.AjaxResult;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色Controller
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController {
    private final IRoleService roleService;
    private final IPermissionService permissionService;
    private final IRolePermissionService rolePermissionService;
    private final IUserRoleService userRoleService;

    @Autowired
    public RoleController(IRoleService roleService, IPermissionService permissionService,
                          IRolePermissionService rolePermissionService, IUserRoleService userRoleService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
        this.userRoleService = userRoleService;
    }

    /**
     * 获取角色列表
     *
     * @param queryVO 查询条件
     * @return 角色列表
     */
    @GetMapping
    public AjaxResult list(RoleQueryVO queryVO) {
        IPage<Role> page = new Page<>(queryVO.getPageNo(), queryVO.getPageSize());
        roleService.selectRoleList(page, queryVO);
        return AjaxResult.success(page);
    }

    /**
     * 查询该用户拥有的角色列表
     *
     * @param userId 用户Id
     * @return 角色列表
     */
    @GetMapping("/assign/hasRole")
    public AjaxResult getRoleByUserId(@RequestParam Long userId) {
        LambdaQueryWrapper<UserRole> urQw = new LambdaQueryWrapper<>();
        urQw.eq(Objects.nonNull(userId), UserRole::getUserId, userId);
        List<UserRole> list = userRoleService.list(urQw);
        List<Long> roleIdList = list.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        return AjaxResult.success(roleIdList);
    }

    /**
     * 分配权限-查询权限数据
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 权限数据
     */
    @GetMapping("/assign")
    public AjaxResult getAssignPermissionTree(Long userId, Long roleId) {
        RolePermissionVO vo = permissionService.selectPermissionTree(userId, roleId);
        return AjaxResult.success(vo);
    }

    /**
     * 分配权限-保存权限数据
     *
     * @param dto 权限数据
     * @return 成功/失败
     */
    @PostMapping("/assign")
    public AjaxResult assignRolePermission(@RequestBody RolePermissionDTO dto) {
        if (rolePermissionService.saveRolePermission(dto.getRoleId(), dto.getPermissionIdList())) {
            return AjaxResult.success("分配成功");
        }
        return AjaxResult.error("分配失败");
    }

    /**
     * 分配角色
     *
     * @return 成功/失败
     */
    @PostMapping("/assign/role")
    public AjaxResult assignRole(@RequestBody UserRoleDTO userRoleDTO) {
        if (userRoleService.saveUserRole(userRoleDTO.getUserId(), userRoleDTO.getRoleList())) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 获取角色详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(roleService.selectRoleById(id));
    }

    /**
     * 新增角色
     */
    @PostMapping
    public AjaxResult add(@RequestBody Role ebSysRole) {
        int res = roleService.insertRole(ebSysRole);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 修改角色
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Role ebSysRole) {
        int res = roleService.updateRole(ebSysRole);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        int res = roleService.deleteRoleById(id);
        if (res > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error(res == -1 ? "该角色已被分配，无法删除！" : "删除失败");
    }
}
