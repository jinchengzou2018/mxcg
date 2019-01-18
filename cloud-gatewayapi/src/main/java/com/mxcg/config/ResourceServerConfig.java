package com.mxcg.config;

import com.mxcg.common.Constant;
import com.mxcg.common.util.CollectionUtil;
import com.mxcg.common.web.entity.RetEntity;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/28 18:51
 * @description：for gateway config
 * @modified By：
 * @version: 1.0.0$
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {



    @Autowired
    private SecurityConfigProperties properties;

    @Autowired
    private DefaultWebSecurityExpressionHandler expressionHandler;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 允许iframe
        http.headers().frameOptions().disable()
                .and()
                // 授权请求
                .authorizeRequests()
                // 排除url
                .antMatchers(antMatchers()).permitAll()
                // 其余url需要通过权限认证
                .anyRequest().access("@permissionService.hasPermission(request,authentication)");
    }

    /**
     * 配置解决 spring-security-oauth问题
     * https://github.com/spring-projects/spring-security-oauth/issues/730
     *
     * @param applicationContext ApplicationContext
     * @return OAuth2WebSecurityExpressionHandler
     */
    @Bean
    public DefaultWebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.expressionHandler(expressionHandler);
        new OAuth2AuthenticationEntryPoint();
        resources.authenticationEntryPoint(new OAuth2AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                // 401 认证失败返回消息
                retOAuth2FailureMessage(request,response,authException);
            }
        });
        resources.accessDeniedHandler(new OAuth2AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {
                // 403 授权失败返回消息
                retOAuth2FailureMessage(request,response,authException);
            }
        });
    }

    private void retOAuth2FailureMessage(HttpServletRequest request, HttpServletResponse response, RuntimeException ex) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/javascript");

        RetEntity<String> retEntity = RetEntity.error();
        if(ex instanceof AuthenticationException) {
            retEntity.setCode(Constant.Code.SC_UNAUTHORIZED)
                    .setMessage(Constant.Message.SC_UNAUTHORIZED);

        } else if (ex instanceof AccessDeniedException) {
            retEntity.setCode(Constant.Code.SC_FORBIDDEN)
                    .setMessage(Constant.Message.SC_FORBIDDEN);
        }
        try(PrintWriter writer = response.getWriter()) {
            response.setStatus(retEntity.getCode());
            writer.println(retEntity.setBody(ex.getMessage()).toString());
            writer.flush();

            System.out.println(StrUtil.format("{}: {},{}, Message: {}",
                    request.getMethod(),request.getRequestURI(),request.getQueryString(),retEntity.toString()));
//            if(log.isDebugEnabled()) {
//                log.debug(StrUtil.format("{}: {},{}, Message: {}",
//                        request.getMethod(),request.getRequestURI(),request.getQueryString(),retEntity.toString()));
//            }
        } catch (Exception e) {
        //    log.error(e.getMessage(), e);
        }
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public String[] antMatchers() {
        List<String> excludeUrls = properties.getExcludeUrls();
        if(CollectionUtil.isEmpty(excludeUrls)) {
            return new String[0];
        }
        return excludeUrls.stream().toArray(String[]::new);

    }

//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration(
//            OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }
}
