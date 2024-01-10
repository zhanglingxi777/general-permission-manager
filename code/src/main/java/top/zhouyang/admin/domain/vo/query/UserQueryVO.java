package top.zhouyang.admin.domain.vo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.zhouyang.admin.domain.User;

import java.io.Serializable;

/**
 * 用户查询VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryVO extends User implements Serializable {
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
