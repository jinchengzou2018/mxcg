package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class IMSIDealer extends StringPatternDealer
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
//        return "^460(00|02|07|01|06|03|05|20)\\d{10}$";

    @Override
   
    public String convert(String s)
    {   
        s = s.replaceAll(" ", "");
        // TODO Auto-generated method stub
        String regex="^\\d{15}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if(matcher.matches())
            return s;
        else return null;
    }
    
}
