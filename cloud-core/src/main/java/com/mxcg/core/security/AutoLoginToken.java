package com.mxcg.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;



@Component
public class AutoLoginToken
{
    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    
    private GrantType grantType = GrantType.password;
    
    private String scope = "all";
    
    private String username;
    
    private String password;
    
    private boolean enable = false;
    
    private OAuth2AccessToken token = null;
    
    @Autowired
    private OauthService oauthService;
    
    public enum GrantType
    {
        implicit, password, authorization_code, refresh_token, client_credentials
    }
    
    public String getToken()
    {
        if (enable && grantType != null)
        {
            if (token == null || token.isExpired())
            {
                if (GrantType.password == grantType && (username == null || password == null))
                {
                    return null;
                }
                else
                {
                    try
                    {
                        token = oauthService.token(username, password, grantType.name(), clientId, clientSecret, scope);
                    }
                    catch(Exception e)
                    {
                        return null;
                    }
                }
            }
            return token.getValue();
        }
        return null;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public boolean isEnable()
    {
        return enable;
    }
    
    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }
    
    public void setGrantType(GrantType grantType)
    {
        this.grantType = grantType;
    }
}
