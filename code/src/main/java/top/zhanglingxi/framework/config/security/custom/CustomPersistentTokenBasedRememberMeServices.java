package top.zhanglingxi.framework.config.security.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义以持久化token为基础的记住我服务
 * @author Zhang Lingxi
 */
@Slf4j
public class CustomPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {
    public CustomPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        Object paramValue = request.getAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER);
        if (paramValue == null
                || !"true".equalsIgnoreCase(paramValue.toString())
                && !"on".equalsIgnoreCase(paramValue.toString())
                && !"yes".equalsIgnoreCase(paramValue.toString())
                && !"1".equals(paramValue)) {
            this.logger.debug(LogMessage.format("Did not send remember-me cookie (principal did not set parameter '%s')", parameter));
            return false;
        } else {
            return true;
        }
    }
}
