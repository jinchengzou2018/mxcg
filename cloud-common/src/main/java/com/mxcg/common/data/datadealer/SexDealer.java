package com.mxcg.common.data.datadealer;


import com.mxcg.common.data.StringCaseType;

public class SexDealer extends StringPatternDealer
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    @Override
    public StringCaseType iniCaseType()
    {
        return StringCaseType.lowercase;
    }

    @Override
    public String convert(String s)
    {      
        if (s.equals("男") || s.equals("male") || s.equals("m"))
        {
            return "男";
        }
        else if (s.equals("女") || s.equals("female") || s.equals("f"))
        {
            return "女";
        }
        else
        {
            return "未知";
        }
    }
    
}
