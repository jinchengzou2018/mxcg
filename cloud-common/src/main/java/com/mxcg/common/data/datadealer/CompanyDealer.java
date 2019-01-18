package com.mxcg.common.data.datadealer;


import com.mxcg.common.data.StringCaseType;

public class CompanyDealer extends StringPatternDealer
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

    @Override
    public String convert(String s)
    {
        return s;
    }

    
}
