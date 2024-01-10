package top.zhouyang.admin.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色对象 eb_sys_role
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Data
@TableName("tb_role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编码
     */
    private String roleCode;

    /**
     * 名称
     */
    private String roleName;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
