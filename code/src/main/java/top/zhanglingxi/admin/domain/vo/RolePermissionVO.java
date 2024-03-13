package top.zhanglingxi.admin.domain.vo;

import lombok.Data;
import top.zhanglingxi.admin.domain.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装权限菜单数据回显VO
 */
@Data
public class RolePermissionVO implements Serializable {
    public static final Long SerialVersionUID = 1L;
    /**
     * 菜单数据
     */
    private List<Permission> permissionList = new ArrayList<>();

    /**
     * 该角色原有分配的菜单数据
     */
    private Object[] checkList;
}
