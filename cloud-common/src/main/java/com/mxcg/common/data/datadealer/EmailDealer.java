package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class EmailDealer extends StringPatternDealer
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    @Override
    public StringCaseType iniCaseType()
    {
        // TODO Auto-generated method stub
        return StringCaseType.lowercase;
    }

//        return "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    @Override
    public String convert(String s)
    {
        s=s.replaceAll(" ","");
        // TODO Auto-generated method stub
        String regex = "^\\w+([-+.]\\w+)*@(\\w|[\\u4e00-\\u9fa5])+([-.](\\w|[\\u4e00-\\u9fa5])+)*\\.(\\w|[\\u4e00-\\u9fa5])+([-.](\\w|[\\u4e00-\\u9fa5])+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if(matcher.matches())
            return s;
        else return null;
    }
    
}
