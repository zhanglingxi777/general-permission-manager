package top.zhanglingxi.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO implements Serializable {
    public static final Long SerialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 介绍
     */
    private String introduction;

    /**
     * 角色权限集合
     */
    private Object[] roles;

    /**
     * 角色集合
     */
    private List<String> roleLCodeList;
}
