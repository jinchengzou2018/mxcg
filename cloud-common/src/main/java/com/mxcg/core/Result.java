package com.mxcg.core;

import com.mxcg.core.exception.TofocusException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;



/**
 * 调用的返回值类
 * 
 * @author wyw
 * 
 */
public class Result<T> implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    private String code = TofocusException.SUCCESS_CODE;
    
    private String descript = "Operation succeeded";
    
    private T result;
    
    public Result()
    {
        
    }

    public Result(T t)
    {
        result = t;
    }
    
    /**
     * @return 错误码，"0"表示成功，其他表示各种失败。
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * @param code 错误码，"0"表示成功，其他表示各种失败。
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * @return 失败描述
     */
    public String getDescript()
    {
        return descript;
    }
    
    /**
     * @param descript 失败描述
     */
    public void setDescript(String descript)
    {
        this.descript = descript;
    }
    
    /**
     * 获取返回的结果集。
     * 
     * @return 调用返回的结果集
     */
    public T getResult()
    {
        return result;
    }
    
    /**
     * 设置返回的结果集。
     * 结果集里的Object可以是普通java类或者接口里的公开类。
     * 
     * @param result 调用返回的结果
     */
    public void setResult(T result)
    {
        this.result = result;
    }
    
    /**
     * 重写toString方法
     * @return str
     */
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        Object res = null;
        if (null != result)
        {
            if (result instanceof List<?>)
            {
                res = "list个数" + ((List<?>)result).size();
            }
            else if (result instanceof Map)
            {
                res = "map个数" + ((Map<?, ?>)result).size();
            }
            else
            {
                res = result;
            }
        }
        str.append("Result[").append("code=").append(code).append(", descript=").append(descript).append(", result=").append(res).append(']');
        return str.toString();
    }
    
    public boolean isSuccess()
    {
        return TofocusException.SUCCESS_CODE.equals(code);
    }
}
