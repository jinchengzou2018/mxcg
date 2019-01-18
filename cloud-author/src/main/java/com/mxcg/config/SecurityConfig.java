package com.mxcg.config;

import com.mxcg.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/30 10:31
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

        /**
         * 注入用户信息服务
         * @return 用户信息服务对象
         */

//        private String clientId;
//         private String clientSecret;
//        private String scope;


//        @Bean
//        public UserDetailsService userDetailsService () {
//            System.out.println("@@@@@@@@@@@@@@www@@");
//            return new CustomUserDetailsService();
//    }
//
//        /**
//         * 全局用户信息
////         * @param auth 认证管理
////         * @throws Exception 用户认证异常信息
////         */
//       @Autowired
//        public void globalUserDetails (AuthenticationManagerBuilder auth) throws Exception {
//            System.out.println("@@@@@@@@@@@@@@@@");
//        auth.userDetailsService(userDetailsService());
//    }
////
//        /**
//         * 认证管理
//         * @return 认证管理对象
//         * @throws Exception 认证异常信息
//         */

//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.inMemoryAuthentication()
//                    .withUser("user_1").password("123456").authorities("USER")
//                    .and()
//                    .withUser("user_2").password("123456").authorities("USER");
//        }


//    @Override
//        @Bean
//        public AuthenticationManager authenticationManagerBean () throws Exception {
//            System.out.println("#################3");
//        return super.authenticationManagerBean();
//    }
//
        /**
//         * http安全配置
//         * @param http http安全对象
//         * @throws Exception http安全异常信息
//         */
//        @Override
//        protected void configure (HttpSecurity http) throws Exception {
//
//        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated().and()
//                .httpBasic().and().csrf().disable();
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//
//            auth
//                    .inMemoryAuthentication()
//                    .passwordEncoder(passwordEncoder())//在此处应用自定义PasswordEncoder
//                    .withUser("user")
//                    .password("password")
//                    .roles("USER");
//               //  auth.authenticationProvider(authProvider());
//    }




//    @Bean
//    public DaoAuthenticationProvider authProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//
//    }

//
//    /**
//     * 注入用户信息服务
//     * @return 用户信息服务对象
//     */
    @Bean
    public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
    }

    /**
     * 全局用户信息
     * @param auth 认证管理
     * @throws Exception 用户认证异常信息
//     */
//    @Autowired
//    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService())//配置自定义UserDetails
//                .passwordEncoder(passwordEncoder());//启用密码加密功能
//
//    }



    /**
     * 认证管理
     * @return 认证管理对象
     * @throws Exception 认证异常信息
//     */
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    /**
     * http安全配置
     * @param http http安全对象
     * @throws Exception http安全异常信息
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated().and()
//                .httpBasic().and().csrf().disable();
//    }





//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService(){
//        return new CustomUserDetailsService();
//    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.requestMatcher(new OAuth2RequestedMatcher())
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS)
//                .permitAll()
//                .anyRequest().authenticated()
//                .anyRequest().hasAnyRole("ANONYMOUS, USER");
//
//    }
////
////
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
//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//       BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String finalPassword = "{bcrypt}"+bCryptPasswordEncoder.encode("admin");
      // manager.createUser(User.withUsername("admin").password(finalPassword).authorities("USER").build());
//        return manager;
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .requestMatchers().anyRequest()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/*").permitAll();
//    }

    /**
     * Spring Boot 2 配置，这里要bean 注入
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
