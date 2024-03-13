package top.zhanglingxi.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO implements Serializable {
    public static final Long SerialVersionUID = 1L;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * token
     */
    private String token;
}
