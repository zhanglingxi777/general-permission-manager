package top.zhanglingxi.admin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色用户对象 tb_user_role
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Data
@NoArgsConstructor
@TableName("tb_user_role")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
