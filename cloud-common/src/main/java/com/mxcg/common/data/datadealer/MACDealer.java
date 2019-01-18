package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;
import com.mxcg.common.util.StringUtil;


import java.util.regex.Pattern;



public class MACDealer extends StringPatternDealer
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    @Override
    public StringCaseType iniCaseType()
    {
        // TODO Auto-generated method stub
        return StringCaseType.uppercase;
    }

    @Override
    /**
     * Mac地址格式一般是:66:AA:BB,现在需要把冒号给过滤掉
     */
    public String convert(String s)
    {   s=s.replaceAll(" ", "");
        String regex_null="^<[a-zA-Z]*>$|NOBLUETOOTHDEVICE.";
        Pattern pattern_null = Pattern.compile(regex_null);
        if(pattern_null.matcher(s).matches())
        {
            return "";
        }
        String regex = "^([0-9A-Z]{2}([-:]){0,}?){5}[0-9A-Z]{2}";
        Pattern pattern = Pattern.compile(regex);
        
        String []tokens={":","-"};
        // TODO Auto-generated method stub
        if(s==null)
            return null;
        if(pattern.matcher(s).matches())
        {
            if(s.indexOf(tokens[0])>-1)
            {
                String []args = StringUtil.split(s, tokens[0]);
                String newStr="";
                for(String s1:args)
                {
                    newStr=newStr+s1;
                }
                s = newStr;
            } else if(s.indexOf(tokens[1])>-1)
            {
                String []args = StringUtil.split(s, tokens[1]);
                String newStr="";
                for(String s1:args)
                {
                    newStr=newStr+s1;
                }
                s = newStr;
            }
 
            return s;
        }else 
            return null;
    }

    
}
