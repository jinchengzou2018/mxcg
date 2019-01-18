package com.mxcg.common.data.datadealer;


import com.mxcg.common.data.StringCaseType;
import com.mxcg.common.util.IdcardValidator;
import com.mxcg.common.util.StringUtil;


import java.util.Arrays;
import java.util.List;



public class IDCardDealer extends StringPatternDealer
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    private static final List<Character> code = Arrays.asList(new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' , 'X'});

    @Override
    public StringCaseType iniCaseType()
    {
        return StringCaseType.uppercase;
    }

    @Override
    public String convert(String s)
    {
        String idcard = StringUtil.full2HalfChange2(s).trim();
        
        //提取keyword
        StringBuffer newIdCard = new StringBuffer();
        for (int i = 0; i < idcard.length(); i++)
        {
            char n = idcard.charAt(i);
            if (code.contains(n))
            {
                newIdCard.append(n);
            }
        }
        
        idcard = newIdCard.toString();
        IdcardValidator vali = new IdcardValidator();
        //若为15位，转成18位
        if (vali.is15Idcard(idcard))
        {
            idcard = vali.getIdcard18(idcard);
        }
        else
        {
            if (idcard.length() > 18) idcard = idcard.substring(0, 18);//如果长度大于等于18，截掉
        }
        
        if (vali.isValid18Idcard(idcard))
        {
            return idcard;
        }
        else
        {
            return null;
        }

    }
}
