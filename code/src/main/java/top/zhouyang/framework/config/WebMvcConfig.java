package top.zhouyang.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.zhouyang.framework.interceptor.TokenInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${custom.profile}")
    private String profile;
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/common/file/**")
                .addResourceLocations("file:" + profile);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor);
    }
}
