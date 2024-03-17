package top.zhanglingxi.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * webMvc配置
 * @author Zhang Lingxi
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${custom.profile}")
    private String profile;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/common/file/**")
                .addResourceLocations("file:" + profile)
                .resourceChain(false);
    }
}
