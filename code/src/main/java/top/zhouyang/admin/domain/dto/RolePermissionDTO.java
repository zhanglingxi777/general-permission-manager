package top.zhouyang.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用于给角色分配权限时保存选中的权限数据
 */
@Data
public class RolePermissionDTO implements Serializable {
    public static final Long SerialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限菜单ID集合
     */
    private List<Long> permissionIdList;
}
