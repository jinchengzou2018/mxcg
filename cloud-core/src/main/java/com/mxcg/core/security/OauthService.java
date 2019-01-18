package com.mxcg.core.security;

import com.mxcg.core.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(value = "account-service")
public interface OauthService
{
    
    @RequestMapping(method = RequestMethod.POST, value = "/oauth/token")
    OAuth2AccessToken token(@RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam("grant_type") String grant_type,
                            @RequestParam("client_id") String client_id,
                            @RequestParam("client_secret") String client_secret,
                            @RequestParam("scope") String scope);
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
    Result<String> revokeToken(@RequestParam("access_token") String access_token,
                               @RequestParam("client_id") String client_id,
                               @RequestParam("client_secret") String client_secret);
}
