package com.mxcg.core.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.mxcg.core.log.SimpleLog;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;


/**
 *  获取当前上下文请求信息
 */
public final class WebContextUtil
{

    /**
     * 获取当前上下文授权信息
     * @return
     */
    public static Authentication getAuthentication()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
    
    /**
     * 获取当前上下文token的信息
     * @return
     */
    public static Object getDetails()
    {
        Authentication authentication = getAuthentication();
        if (authentication == null)
        {
            return null;
        }
        else
        {
            return authentication.getDetails();
        }
    }
    
    /**
     *  获取当前登入用户的访问accessToken
     * @return
     */
    public static String getAccessToken()
    {
        
        if (getDetails() == null)
        {
            return null;
        }
        else
        {
            if (getDetails() instanceof OAuth2AuthenticationDetails)
            {
                return ((OAuth2AuthenticationDetails)getDetails()).getTokenValue();
            }
            else if (getDetails() instanceof WebAuthenticationDetails)
            {
                return null;
            }
            else
            {
                return null;
            }
            
        }
    }

    public static Long getUserkey()
    {
        if (getAuthentication() == null) return null;
        Authentication authentication = getAuthentication();
        if (authentication instanceof OAuth2Authentication)
        {
            if(authentication.getName() == null || authentication.getName().length() == 0)
                return null;
            try
            {
                return Long.parseLong(authentication.getName());
            }
            catch(Exception e)
            {
                SimpleLog.outWaring(authentication.getName() +" -- :"+e.getMessage());
                return null;
            }
        }
        else if (authentication instanceof UsernamePasswordAuthenticationToken)
        {
            TofocusUser user = getTofocusUser((UsernamePasswordAuthenticationToken)authentication);
            if (user != null) return user.getUserkey();
        }
        else if (authentication instanceof AnonymousAuthenticationToken)
        {
        }
        else
        {
            SimpleLog.outWaring(authentication.getClass().toString());
        }
        return null;
    }
    
    public static String getUserid()
    {
        if (getAuthentication() == null) return null;
        Authentication authentication = getAuthentication();
        if (authentication instanceof OAuth2Authentication)
        {
            Authentication userAuther = ((OAuth2Authentication)authentication).getUserAuthentication();
            if(userAuther instanceof UsernamePasswordAuthenticationToken)
            {
                Object principal = ((UsernamePasswordAuthenticationToken)userAuther).getPrincipal();
                if(principal.getClass().getName().equals("cn.tofocus.core.security.TofocusUser"))
                {
                    Method method = null;
                    try
                    {
                        method = principal.getClass().getMethod("getUserid", null);
                        Object result = method.invoke(principal, null);
                        if(result != null)
                            return result.toString();
                        else
                            return null;
                    }
                    catch (NoSuchMethodException e)
                    {
                        SimpleLog.outException(e);
                    }
                    catch (SecurityException e)
                    {
                        SimpleLog.outException(e);
                    }
                    catch (IllegalAccessException e)
                    {
                        SimpleLog.outException(e);
                    }
                    catch (IllegalArgumentException e)
                    {
                        SimpleLog.outException(e);
                    }
                    catch (InvocationTargetException e)
                    {
                        SimpleLog.outException(e);
                    }
                }
                else
                {
                    Object details = ((UsernamePasswordAuthenticationToken)userAuther).getDetails();
                    if (details instanceof Map)
                    {
                        Map<String, Object> m = ((Map<String, Map<String, Object>>)details).get("principal");
                        if(m!= null)
                            return (String)m.get("userid");
                    }
                    else
                    {
                    }
                }
            }
            else
            {
                SimpleLog.outWaring("authentication.userAuther.class : " + userAuther.getClass().toString());
            }
        }
        else if (authentication instanceof UsernamePasswordAuthenticationToken)
        {
            TofocusUser user = getTofocusUser((UsernamePasswordAuthenticationToken)authentication);
            if (user != null) return user.getUserid();
        }
        else if (authentication instanceof AnonymousAuthenticationToken)
        {
        }
        else
        {
            SimpleLog.outWaring(authentication.getClass().toString());
        }
        return null;
    }
    
    public static String getNickname()
    {
        if (getAuthentication() == null) return null;
        Authentication authentication = getAuthentication();
        if (authentication instanceof OAuth2Authentication)
        {
            Authentication userAuther = ((OAuth2Authentication)authentication).getUserAuthentication();
            if(userAuther instanceof UsernamePasswordAuthenticationToken)
            {
                Object principal = ((UsernamePasswordAuthenticationToken)userAuther).getPrincipal();
                if(principal.getClass().getName().equals("cn.tofocus.core.security.TofocusUser"))
                {
                    Method method = null;
                    try
                    {
                        method = principal.getClass().getMethod("getNickname", null);
                        Object result = method.invoke(principal, null);
                        if(result != null)
                            return result.toString();
                        else
                            return null;
                    }
                    catch (NoSuchMethodException e)
                    {
                        SimpleLog.outException(e);
                    }
                    catch (SecurityException e)
                    {
                        SimpleLog.outException(e);
                    }
                    catch (IllegalAccessException e)
                    {
                        SimpleLog.outException(e);
                    }
                    catch (IllegalArgumentException e)
                    {
                        SimpleLog.outException(e);
                    }
                    catch (InvocationTargetException e)
                    {
                        SimpleLog.outException(e);
                    }
                }
                else
                {
                    Object details = ((UsernamePasswordAuthenticationToken)userAuther).getDetails();
                    if (details instanceof Map)
                    {
                        Map<String, Object> m = ((Map<String, Map<String, Object>>)details).get("principal");
                        if(m!= null)
                            return (String)m.get("nickname");
                    }
                    else
                    {
                    }
                }
            }
            else
            {
                SimpleLog.outWaring("authentication.userAuther.class : " + userAuther.getClass().toString());
            }
        }
        else if (authentication instanceof UsernamePasswordAuthenticationToken)
        {
            TofocusUser user = getTofocusUser((UsernamePasswordAuthenticationToken)authentication);
            if (user != null) return user.getNickname();
        }
        else if (authentication instanceof AnonymousAuthenticationToken)
        {
        }
        else
        {
            SimpleLog.outWaring(authentication.getClass().toString());
        }
        return null;
    }

    public static String getClientid()
    {
        if (getAuthentication() == null) return null;
        Authentication authentication = getAuthentication();
        if (authentication instanceof OAuth2Authentication)
        {
            OAuth2Request oAuth2Request = ((OAuth2Authentication)authentication).getOAuth2Request();
            if(oAuth2Request != null)
                return oAuth2Request.getClientId();
        }
        else if (authentication instanceof UsernamePasswordAuthenticationToken)
        {
        }
        else if (authentication instanceof AnonymousAuthenticationToken)
        {
        }
        else
        {
            SimpleLog.outWaring("authentication.class : " + authentication.getClass().toString());
        }
        return null;
    }
    
    public static String getRemoteAddress()
    {
        if (getAuthentication() == null) return null;
        Authentication authentication = getAuthentication();
        if (authentication instanceof OAuth2Authentication)
        {
            String remoteAddress = null;
            Object details = authentication.getDetails();
            if(details instanceof OAuth2AuthenticationDetails)
            {
                remoteAddress = ((OAuth2AuthenticationDetails)details).getRemoteAddress();
            }
            else
            {
                SimpleLog.outWaring("authentication.details.class : " + details.getClass().toString());
            }
            Authentication userAuther = ((OAuth2Authentication)authentication).getUserAuthentication();
            if(userAuther instanceof UsernamePasswordAuthenticationToken)
            {
                Object subdetails = ((UsernamePasswordAuthenticationToken)userAuther).getDetails();
                if(subdetails instanceof WebAuthenticationDetails)
                {
                    remoteAddress = ((WebAuthenticationDetails)subdetails).getRemoteAddress();
                }
                else
                {
                    SimpleLog.outWaring("authentication.userAuther.subdetails.class : " + subdetails.getClass().toString());
                }
            }
            else
            {
                SimpleLog.outWaring("authentication.userAuther.class : " + userAuther.getClass().toString());
            }
            return remoteAddress;
        }
        else if (authentication instanceof UsernamePasswordAuthenticationToken)
        {
            Object details = ((UsernamePasswordAuthenticationToken)authentication).getDetails();
            if (details instanceof WebAuthenticationDetails)
                return ((WebAuthenticationDetails)details).getRemoteAddress();
        }
        else if (authentication instanceof AnonymousAuthenticationToken)
        {
        }
        else
        {
            SimpleLog.outWaring(authentication.getClass().toString());
        }
        return null;
    }
    
    private static TofocusUser getTofocusUser(UsernamePasswordAuthenticationToken authentication)
    {
        Object principal = ((UsernamePasswordAuthenticationToken)authentication).getPrincipal();
        if (principal instanceof TofocusUser)
        {
            return ((TofocusUser)principal);
        }
        else
        {
            SimpleLog.outWaring("principal.class : " + principal.getClass().toString());
            return null;
        }
    }

    public static Object getTokenType()
    {
        if (getDetails() == null)
        {
            return null;
        }
        else
        {
            if (getDetails() instanceof OAuth2AuthenticationDetails)
            {
                return ((OAuth2AuthenticationDetails)getDetails()).getTokenType();
            }
            else if (getDetails() instanceof WebAuthenticationDetails)
            {
                return null;
            }
            else
            {
                return null;
            }
            
        }
    }

}
