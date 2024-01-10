package top.zhouyang.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用于给用户分配角色时保存选中的角色数据
 */
@Data
public class UserRoleDTO implements Serializable {
    public static final Long SerialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 角色Id列表
     */
    private List<Long> roleList;
}
