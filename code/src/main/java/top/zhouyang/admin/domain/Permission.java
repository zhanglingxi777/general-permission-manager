package top.zhouyang.admin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 权限对象 eb_sys_permission
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Data
@TableName("eb_sys_permission")
public class Permission {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    private String label;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 父权限名称
     */
    private String parentName;

    /**
     * 授权标识符
     */
    private String code;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 授权路径
     */
    private String url;

    /**
     * 权限类型（0目录1菜单2按钮）
     */
    private Long type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Long orderNum;

    /**
     * 是否删除（0未删除1已删除）
     */
    private Long isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子菜单列表
     */
    // 属性值为null时不进行序列化操作
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private List<Permission> children = new ArrayList<Permission>();

    /**
     * 用于前端判断是菜单、目录或按钮
     */
//    @TableField(exist = false)
//    private String value;

    /**
     * 是否展开
     */
    @TableField(exist = false)
    private Boolean open;
}
