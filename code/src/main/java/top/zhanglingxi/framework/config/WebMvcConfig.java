package top.zhanglingxi.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.zhanglingxi.framework.interceptor.TokenInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${custom.profile}")
    private String profile;
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/common/file/**")
                .addResourceLocations("file:" + profile)
                .resourceChain(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 0 tokenInterceptor
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .order(0)
                .excludePathPatterns("/sys/user/vcImage", "/sys/user/login", "/common/file/**", "/public/**", "/error");
    }
}