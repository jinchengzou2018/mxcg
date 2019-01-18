package com.mxcg.core.feign;

import com.mxcg.core.security.AutoLoginToken;
import com.mxcg.core.security.WebContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig implements RequestInterceptor
{
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    
    //  @Autowired
    //  private OAuth2RestTemplate oAuth2RestTemplate;
    
    @Autowired
    private AutoLoginToken autoLoginToken;
    
    @Override
    public void apply(RequestTemplate requestTemplate)
    {
        
        String accessToken = WebContextUtil.getAccessToken();
        //      if(accessToken == null){
        //          accessToken =oAuth2RestTemplate.getAccessToken().getValue();
        //      }
        if (accessToken != null)
        {
            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, accessToken));
        }
        else if (autoLoginToken.isEnable() && autoLoginToken.getToken() != null)
        {
            requestTemplate.header(AUTHORIZATION_HEADER,
                String.format("%s %s", BEARER_TOKEN_TYPE, autoLoginToken.getToken()));
        }
        
    }
    
}
