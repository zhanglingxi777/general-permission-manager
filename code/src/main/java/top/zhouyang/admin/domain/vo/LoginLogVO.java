package top.zhouyang.admin.domain.vo;

import lombok.Data;
import top.zhouyang.admin.domain.LoginLog;

import java.io.Serializable;

@Data
public class LoginLogVO  extends LoginLog implements Serializable  {
    public static final Long SerialVersionUID = 1L;
    /**
     * 页码
     */
    private Long pageNo = 1L;

    /**
     * 每页显示数量
     */
    private Long pageSize = 10L;
}
