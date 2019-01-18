package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;
import com.mxcg.common.util.StringUtil;


import java.io.Serializable;



/**
 * 
 * 文本处理器, 永远返回true
 * 并把文本进行规范化
 *
 */
public abstract class StringPatternDealer implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    public String deal(String data)
    {
        if (data != null)
        {
            String str;
            str = dealCase(data);
            String newstr = convert(str);
            return newstr;
        }
        else
        {
            return null;
        }
    }
    
    private String dealCase(String data)
    {
        String str = null;
        //大小写转换
        switch (iniCaseType())
        {
            case uppercase:
            {
                str = StringUtil.full2HalfChange2(data).trim().toUpperCase();
                break;
            }
            case lowercase:
            {
                str = StringUtil.full2HalfChange2(data).trim().toLowerCase();
                break;
            }
            case nooperation:
            {
                str = StringUtil.full2HalfChange2(data).trim();
                break;
            }
        }
        return str;
    }
    
    protected abstract StringCaseType iniCaseType();
    
    /**
     * 文本规范化
     * 转换失败或无效格式,返回null
     * @param s
     * @return
     */
    protected abstract String convert(String s);
    
}
