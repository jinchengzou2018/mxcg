package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;

import java.util.regex.Pattern;



public class PassPortDealer extends StringPatternDealer
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    @Override
    public StringCaseType iniCaseType()
    {
        return StringCaseType.uppercase;
    }

    @Override
    public String convert(String s)
    {
        s = s.trim();
        String regex="^1[45][0-9]{7}|G[0-9]{8}|P.[0-9]{7}|S.\\d{7}|S[0-9]{8}|D[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        if(pattern.matcher(s).matches())
        {
            return s;
        }else
            return null;
    }

//        return "^1[45][0-9]{7}|G[0-9]{8}|P.[0-9]{7}|S.\\d{7}|S[0-9]{8}|D[0-9]+$";
    
}
