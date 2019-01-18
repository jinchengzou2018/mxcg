package com.mxcg.core.exception.util;

import com.mxcg.core.config.XmlConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;


/**
 * 
 * 错误码配置
 * <p>
 * 自动加载资源目录下的errcode-cfg/errcode*.xml文件，合并为错误代码
 * 

 */
public class ErrorConfig
{
    private static ErrorConfig instance = new ErrorConfig();
    
    private XmlConfig config;
    
    private ErrorConfig()
    {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try
        {
            Resource[] resources = resolver.getResources("classpath*:errcode-cfg/errcode*.xml");
            for (Resource res : resources)
            {
                if (res.exists())
                {
                    System.out.println(res.getURI().getPath());
                    if(config == null)
                    {
                        config = new XmlConfig(res);
                    }
                    else
                    {
                        XmlConfig tmp  = new XmlConfig(res);
                        config.merger(tmp);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static String getMsg(String exceptionCode)
    {
        return instance.config.getString(exceptionCode);
    }
}
