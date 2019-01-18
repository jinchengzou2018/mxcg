package com.mxcg.config;

import com.mxcg.common.Constant;
import com.mxcg.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/30 10:05
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig  extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private SecurityConfigProperties properties;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AuthenticationManager authenticationManager;
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        /**
//         * 配置客户端详情信息(Client Details)
//         * clientId：（必须的）用来标识客户的Id。
//         * secret：（需要值得信任的客户端）客户端安全码，如果有的话。
//         * scope：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
//         * authorizedGrantTypes：此客户端可以使用的授权类型，默认为空。
//         * authorities：此客户端可以使用的权限（基于Spring Security authorities）。
//         */
//        System.out.println("@@@@@@@@@@@@@@@@@@@@");
//
//      System.out.println(properties.getClientId());
//        System.out.println(properties.getClientSecret());
//        System.out.println(properties.getScope());
//        clients.inMemory()
//                .withClient(properties.getClientId())
//                .secret(properties.getClientSecret())
//                .scopes(properties.getScope())
//                // 支持的授权模式, 共4种, 这里配置了最常用的3种
//                .authorizedGrantTypes("password", "refresh_token", "authorization_code");
//
//
//        System.out.println("AuthorizationServerSecurityConfigurer is complete.");
//    }
//
//
//
//
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        /**
         * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
         */
        endpoints
                // token存储
                .tokenStore(tokenStore())
                // 自定义token生成方案
                .accessTokenConverter(accessTokenConverter())
                // 身份认证管理器, 主要用于"password"授权模式
                .authenticationManager(authenticationManager)
                // 配合身份认证管理器, 检查用户名密码有效性
                .userDetailsService(userDetailsService());

        endpoints.reuseRefreshTokens(true);


        System.out.println("AuthorizationServerSecurityConfigurer is complete.");

    }
//
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /**
         * 配置令牌端点(Token Endpoint)的安全约束.
         */
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()");
        System.out.println("AuthorizationServerSecurityConfigurer is complete.");
        //log.debug("AuthorizationServerSecurityConfigurer is complete.");
    }
//
//
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }
//
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
//
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
    @Bean
    public AccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 设置签名
        jwtAccessTokenConverter.setSigningKey(Constant.Auth.JWT_SIGNING_KEY);
        return jwtAccessTokenConverter;
    }



    private static final String DEMO_RESOURCE_ID = "order";


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("cloud");
        //配置两个客户端,一个用于password认证一个用于client认证
        clients.inMemory()
                .withClient("cloud")
                //  .resourceIds(DEMO_RESOURCE_ID)
                .authorizedGrantTypes("password", "refresh_token", "authorization_code")
                .scopes("webapp")
                .secret(finalSecret);

    }

//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints
//                .tokenStore(new RedisTokenStore(redisConnectionFactory))
//                .authenticationManager(authenticationManager)
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//        //允许表单认证
//        oauthServer.allowFormAuthenticationForClients();
//    }


}