package top.zhanglingxi.framework.config;

import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import top.zhanglingxi.framework.config.security.custom.AnonymousAuthenticationHandler;
import top.zhanglingxi.framework.config.security.custom.CustomAccessDeniedHandler;
import top.zhanglingxi.framework.config.security.custom.CustomLogoutSuccessHandler;
import top.zhanglingxi.framework.config.security.custom.CustomPersistentTokenBasedRememberMeServices;
import top.zhanglingxi.framework.config.security.filter.CustomJwtFilter;
import top.zhanglingxi.framework.config.security.filter.CustomLoginFilter;
import top.zhanglingxi.framework.config.security.service.UserDetailsServiceImpl;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

/**
 * 描述信息
 *
 * @author Zhang Wenxu
 * @date 2024/03/14
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final AnonymousAuthenticationHandler anonymousAuthenticationHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final DataSource dataSource;
    private final CustomLoginFilter loginFilter;
    private final CustomJwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, CustomLogoutSuccessHandler logoutSuccessHandler, AnonymousAuthenticationHandler anonymousAuthenticationHandler, CustomAccessDeniedHandler accessDeniedHandler, AuthenticationConfiguration authenticationConfiguration, DataSource dataSource, CustomLoginFilter loginFilter, CustomJwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.anonymousAuthenticationHandler = anonymousAuthenticationHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationConfiguration = authenticationConfiguration;
        this.dataSource = dataSource;
        this.loginFilter = loginFilter;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用csrf
                .csrf().disable()
                // 不创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 配置 userDetailsService
                .and()
                .userDetailsService(userDetailsService)
                // 开启表单登录
                .formLogin()
                .and()
                // 退出登录配置
                .logout()
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", HttpMethod.GET.name())
                ))
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .authorizeRequests()
                // 登录接口 和 获取验证码接口 设置为匿名登录
                .antMatchers("/login", "/login/vcImage").anonymous()
                // 以 public 开头的接口为公开接口 设置为允许所有人访问
                .mvcMatchers("/common", "/error", "/doc.html").permitAll()
                .regexMatchers(".*/avatar/.*").permitAll()
                // 其他请求需要验证
                .anyRequest().authenticated()
                .and()
                // 异常处理配置
                .exceptionHandling()
                // 匿名用户无权限访问处理
                .authenticationEntryPoint(anonymousAuthenticationHandler)
                // 认证用户无权限访问处理
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                // cors配置
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                // 替换 UsernamePasswordAuthenticationFilter
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, LogoutFilter.class)
                // remember me配置
                .rememberMe()
                // 配置 token 持久化
                .tokenRepository(persistentTokenRepository())
                .rememberMeServices(customPersistentTokenBasedRememberMeServices())
                // remember me过期时间 单位s
                .tokenValiditySeconds(3600)
                .userDetailsService(userDetailsService);
        return http.build();

    }

    /**
     * 配置CORS规则
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:81"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 配置 AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 配置PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置 PersistentTokenRepository
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    /**
     * 配置自定义记住我service
     */
    @Bean
    public CustomPersistentTokenBasedRememberMeServices customPersistentTokenBasedRememberMeServices() {
        return new CustomPersistentTokenBasedRememberMeServices(UUID.fastUUID().toString(), userDetailsService, persistentTokenRepository());
    }
}
