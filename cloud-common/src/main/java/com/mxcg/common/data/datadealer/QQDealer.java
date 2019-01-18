package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;

import java.util.regex.Pattern;


public class QQDealer extends StringPatternDealer
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    @Override
    public StringCaseType iniCaseType()
    {
        // TODO Auto-generated method stub
        return StringCaseType.nooperation;
    }

//        return "^\\d{5,}$";

    @Override
    public String convert(String s)
    {
        // TODO Auto-generated method stub
        s = s.trim();
        String regex = "^\\d{5,}$";
        Pattern pattern =Pattern.compile(regex);
        if(pattern.matcher(s).matches())
            return s;
        else
            return null;
    
    }
    
}
