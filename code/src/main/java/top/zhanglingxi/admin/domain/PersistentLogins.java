package top.zhanglingxi.admin.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 持久化记住我
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("persistent_logins")
public class PersistentLogins  implements Serializable {
    public static final Long SerialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 序列化号
     */
    private String series;

    /**
     * 凭证
     */
    private String token;

    /**
     * 最近使用时间
     */
    private Date lastUsed;
}
