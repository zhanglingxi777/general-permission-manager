package top.zhanglingxi.admin.domain.vo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.zhanglingxi.admin.domain.Role;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQueryVO extends Role implements Serializable {
    public static final Long SerialVersionUID = 1L;

    /**
     * 页码
     */
    private Long pageNo = 1L;

    /**
     * 每页显示数量
     */
    private Long pageSize = 10L;

    /**
     * 用户Id
     */
    private Long userId;
}
