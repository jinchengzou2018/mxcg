package com.mxcg.core.exception.util;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * ExceptionBean
 * 
 * @author  huhao
 * @version  [版本号, 2013-9-29]
 */
public class ExceptionBean implements Serializable
{
    private static final long serialVersionUID = -3884608712568370005L;
    
    private String code;
    
    private String message;
    
    private Object parameters[];
    
    private String customizedMessage;//开发自定义的描述
    
    private Map<String, Object> attributes;
    
    public ExceptionBean()
    {
        code = "UNKNOW_ERROR";
        message = null;
        parameters = null;
        customizedMessage = null;
    }
    
    public Map<String, Object> getAttributes()
    {
        return attributes;
    }
    
    public void setAttributes(Map<String, Object> attrs)
    {
        attributes = attrs;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    /**
     * 获取自定义描述
     * @return 自定义描述
     */
    public String getCustomizedMessage()
    {
        return customizedMessage;
    }
    
    /**
     * 设置自定义描述
     */
    public void setCustomizedMessage(String customizedMessage)
    {
        this.customizedMessage = customizedMessage;
    }

    public Object[] getParameters()
    {
        if (parameters != null)
        {
            Object[] result = new Object[parameters.length];
            System.arraycopy(parameters, 0, result, 0, parameters.length);
            return result;
        }
        else
            return null;
    }
    
    public void setParameters(Object parameters[])
    {
        Object[] result = new Object[parameters.length];
        System.arraycopy(parameters, 0, result, 0, parameters.length);
        this.parameters = result;
    }
    
}