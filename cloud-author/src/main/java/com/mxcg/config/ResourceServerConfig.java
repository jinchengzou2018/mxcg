package com.mxcg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/30 10:27
 * @description： OAuth2授权模块会提供用户信息, 是资源服务器
 * @modified By：
 * @version: $version$
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        /**
//         * 自定义权限保护规则
//         */
//        http
//                // 禁用csrf保护
//       .csrf().disable()
//                // 授权请求
//                .authorizeRequests()
//                .anyRequest().authenticated().antMatchers("/auth/**", "/login", "/signup", "/forgotPassword").permitAll()
//                .anyRequest().hasAnyRole("ANONYMOUS, USER");
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new OAuth2RequestedMatcher())
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .anyRequest().authenticated();


    }


    /**
//     * 定义OAuth2请求匹配器
//     */
//    private static class OAuth2RequestedMatcher implements RequestMatcher {
//        @Override
//        public boolean matches(HttpServletRequest request) {
//            String auth = request.getHeader("Authorization");
//            //判断来源请求是否包含oauth2授权信息,这里授权信息来源可能是头部的Authorization值以Bearer开头,或者是请求参数中包含access_token参数,满足其中一个则匹配成功
//            boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
//            boolean haveAccessToken = request.getParameter("access_token")!=null;
//            return haveOauth2Token || haveAccessToken;
//        }
//    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        /**
//         * 自定义权限保护规则
//         */
//        http
//                // 禁用csrf保护
//                .csrf().disable()
//                // 授权请求
//                .authorizeRequests()
//                .anyRequest().authenticated();
//    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.requestMatcher(new OAuth2RequestedMatcher())
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS).permitAll()
//                .anyRequest().authenticated();
//    }


    private static class OAuth2RequestedMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            String auth = request.getHeader("Authorization");
            //判断来源请求是否包含oauth2授权信息,这里授权信息来源可能是头部的Authorization值以Bearer开头,或者是请求参数中包含access_token参数,满足其中一个则匹配成功
            boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
            boolean haveAccessToken = request.getParameter("access_token")!=null;
            return haveOauth2Token || haveAccessToken;
        }
    }

}