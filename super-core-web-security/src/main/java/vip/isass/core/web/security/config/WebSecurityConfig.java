package vip.isass.core.web.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import vip.isass.core.web.security.authentication.jwt.EndsConfiguration;
import vip.isass.core.web.security.authentication.jwt.JwtAuthenticationFilter;
import vip.isass.core.web.security.authentication.jwt.JwtCacheService;
import vip.isass.core.web.security.authentication.ms.MsAuthenticationFilter;
import vip.isass.core.web.security.authentication.ms.MsAuthenticationHeaderProvider;
import vip.isass.core.web.security.metadata.SecurityMetadataSourceProviderManager;
import vip.isass.core.web.security.processor.AffirmativeBasedPostProcessor;
import vip.isass.core.web.security.processor.FilterSecurityInterceptorSourcePostProcessor;
import vip.isass.core.web.uri.UriPrefixProvider;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Rain
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private List<AuthenticationProvider> authenticationProvider;

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    private SecurityMetadataSourceProviderManager securityMetadataSourceProviderManager;

    @Resource
    private UriPrefixProvider uriPrefixProvider;

    @Resource
    private MsAuthenticationHeaderProvider msAuthenticationHeaderProvider;

    @Resource
    private PermitUrlConfiguration permitUrlConfiguration;

    @Resource
    private JwtCacheService jwtCacheService;

    @Resource
    private EndsConfiguration endsConfiguration;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);

        // 使用自定义身份验证组件
        authenticationProvider.forEach(auth::authenticationProvider);
        auth.eraseCredentials(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 跨域
            .cors()

            .and()

            // 基于jwt，无需预防CSRF攻击
            .csrf().disable()

            // 基于jwt，无需session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()

            // 禁用缓存
            .headers().cacheControl().disable()

            .and()

            // 配置请求授权
            .authorizeRequests()
            // 允许以下请求
            .antMatchers(permitUrlConfiguration.getPermitUrls()
                .toArray(new String[permitUrlConfiguration.getPermitUrls().size()]))
            .permitAll()

            // 不能写这个，写了就代表只要登录了，就可以访问
            // .anyRequest().authenticated()

            // 添加自定义角色获取器
            .withObjectPostProcessor(new FilterSecurityInterceptorSourcePostProcessor(
                requestMappingHandlerMapping,
                securityMetadataSourceProviderManager,
                uriPrefixProvider,
                permitUrlConfiguration.getPermitUrls()))
            .withObjectPostProcessor(new AffirmativeBasedPostProcessor())

            .and()

            // 验证token
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtCacheService, endsConfiguration))

            // 验证微服务之间调用的权限
            .addFilter(new MsAuthenticationFilter(authenticationManager()))

            .anonymous();

    }


}
