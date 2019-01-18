package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;

import java.util.regex.Pattern;




public class BankCardDealer extends StringPatternDealer
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    @Override
    public StringCaseType iniCaseType()
    {
        return StringCaseType.nooperation;
    }

//        return "^([0-9]{19})|([0-9]{16})$";

    @Override
    public String convert(String s)
    {
        s = s.trim();
        String regex="^([0-9]{19})|([0-9]{16})$";
        Pattern pattern = Pattern.compile(regex);
        if(pattern.matcher(s).matches())
            return s;
        else 
            return null;
    }
    
}
