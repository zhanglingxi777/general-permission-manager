package top.zhanglingxi.admin.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 eb_sys_user
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Data
@TableName("tb_user")
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否是管理员（0否1是）
     */
    private Integer isAdmin;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别（0男1女）
     */
    private String gender;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;


    /**
     * 登录错误次数
     */
    private Integer loginErrorNum;

    /**
     * 登录限制时间
     */
    private Date loginLimitTime;

    /**
     * 最后一次登录时间
     */
    private Date lastLoginTime;

    /**
     * 用户权限列表
     */
    @TableField(exist = false)
    private List<Permission> permissionList;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
