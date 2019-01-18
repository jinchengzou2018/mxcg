package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;

import java.util.regex.Pattern;



public class IPDealer extends StringPatternDealer
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

//        return "^((2[0-4]\\d|25[0-5]|1\\d{2}|[1-9]?\\d)\\.){3}(2[0-4]\\d|25[0-5]|1\\d{2}|[1-9]?\\d)$";

    @Override
    public String convert(String s)
    {
        s=s.replaceAll(" ","");
        // TODO Auto-generated method stub
        String regex="^((2[0-4]\\d|25[0-5]|1\\d{2}|[1-9]?\\d)\\.){3}(2[0-4]\\d|25[0-5]|1\\d{2}|[1-9]?\\d)$";
        Pattern pattern = Pattern.compile(regex);
        if(pattern.matcher(s).matches())
            return s;
        else return null;
    }
    
}
