package top.zhouyang.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户下拉框VO类
 */
@Data
public class UserSelectVO implements Serializable {
    public static final Long SerialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;
}
