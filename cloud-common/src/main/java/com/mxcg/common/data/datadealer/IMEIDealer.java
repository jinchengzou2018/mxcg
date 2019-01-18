package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;
import com.mxcg.common.util.StringUtil;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class IMEIDealer extends StringPatternDealer
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
    
    //        return "^\\d{14,15}$";
    
    @Override
    public String convert(String s)
    {
        s = s.trim();
        String regex = " |/|-";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find())
        {
            s = matcher.replaceAll("");
        }
        regex = "^[A-Fa-f0-9]{14,}$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(s);
        
        if (matcher.matches())
        {
            try
            {
                String s14 = s.substring(0, 14);
                String s15 = null;
                //判断是否MEID
                if (isMEID(s14))
                    s15 = checkMEID(s14);
                else
                    s15 = checkIMEI(s14);
                
                return s15;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        else
            return null;
    }
    
    private String checkIMEI(String s14)
    {
        String reverseStr = StringUtil.reverse(s14);//反序
        int d1 = Integer.valueOf(reverseStr.substring(0, 1));
        int d2 = Integer.valueOf(reverseStr.substring(1, 2));
        int d3 = Integer.valueOf(reverseStr.substring(2, 3));
        int d4 = Integer.valueOf(reverseStr.substring(3, 4));
        int d5 = Integer.valueOf(reverseStr.substring(4, 5));
        int d6 = Integer.valueOf(reverseStr.substring(5, 6));
        int d7 = Integer.valueOf(reverseStr.substring(6, 7));
        int d8 = Integer.valueOf(reverseStr.substring(7, 8));
        int d9 = Integer.valueOf(reverseStr.substring(8, 9));
        int d10 = Integer.valueOf(reverseStr.substring(9, 10));
        int d11 = Integer.valueOf(reverseStr.substring(10, 11));
        int d12 = Integer.valueOf(reverseStr.substring(11, 12));
        int d13 = Integer.valueOf(reverseStr.substring(12, 13));
        int d14 = Integer.valueOf(reverseStr.substring(13, 14));
        
        int v1 = d1*2;
        int v3 = d3*2;
        int v5 = d5*2;
        int v7 = d7*2;
        int v9 = d9*2;
        int v11 = d11*2;
        int v13 = d13*2;
        
        int a = (v1/10 + v1%10) + d2 + (v3/10 + v3%10) + d4 + (v5/10 + v5%10) 
                + d6 + (v7/10 + v7%10) + d8 + (v9/10 + v9%10) + d10 + (v11/10 + v11%10) 
                + d12 + (v13/10 + v13%10) + d14;
        
        String lastNum = "";
        if (a%10 == 0)  lastNum = "0";
        else lastNum =  String.valueOf(10 - a%10);
        return s14 + lastNum;
    }
    
    private String checkMEID(String s14)
    {
        // A0000045C4EE9A
        String myStr[] = {"a", "b", "c", "d", "e", "f"};
        int sum = 0;
        for (int i = 0; i < s14.length(); i++)
        {
            String param = s14.substring(i, i + 1);
            for (int j = 0; j < myStr.length; j++)
            {
                if (param.equalsIgnoreCase(myStr[j]))
                {
                    param = "1" + String.valueOf(j);
                }
            }
            if (i % 2 == 0)
            {
                sum = sum + Integer.parseInt(param);
            }
            else
            {
                sum = sum + 2 * Integer.parseInt(param) % 16;
                sum = sum + 2 * Integer.parseInt(param) / 16;
            }
        }
        String lastStr = "";
        if (sum % 16 == 0)
        {
            lastStr =  "0";
        }
        else
        {
            int result = 16 - sum % 16;
            if (result > 9)
            {
                result += 65 - 10;
                lastStr = String.valueOf((char)result);
            }
            else
            {
                lastStr = String.valueOf(String.valueOf(result).charAt(0));
            }
        }
        return s14 + lastStr;
    }
    
    private boolean isMEID(String s14)
    {
        char[] d = s14.toUpperCase().toCharArray();
        boolean flag = false;
        for (char s : d)
        {
            if (s >= 'A' && s <= 'F')
            {
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    /**
     * 计算IMEI验证码的步骤：
     *  ------TAC------------     --FAC-     ------SNR-----------
     *  D14 D13 D12 D11 D10 D9    D8  D7    D6  D5  D4  D3  D2  D1  D0
     *  3   5   3   1   1   4     0  0      8   0   9  6   3    6  6
     * 1、把IMEI的奇数位数*2，如：D1,D3,D5,……D13
     *    D13  D11  D9  D7  D5   D3   D1
     *     10   2   8   0   0    12   12
     * 2、将计算得到的7个奇数位数字分别以个位数相加（如果得到的是个两位数，则十位和个位分别当成个位数来相加），
     *    再加上7个偶数位数字，如：D2,D4,D6……D14
     *    3+1+0+3+2+1+8+0+0+8+0+9+1+2+3+1+2=44
     * 3、如果第2步计算得到的数字末位为0，则验证码数字为0。如果第2步计算结果末位数不是0，则以大于第2步计算结果
     *    的以0结尾的双位整数减去第2步的计算结果，所获得的个位数即为验证码。
     *    D0 = 50 -44 =6
     */
    
    /**
     * MEID校验码算法： 
     * (1).将偶数位数字分别乘以2，分别计算个位数和十位数之和，注意是16进制数 
     * (2).将奇数位数字相加，再加上上一步算得的值 
     * (3).如果得出的数个位是0则校验位为0，否则为10(这里的10是16进制,，即十进制的16)减去个位数 
     * 如：AF 01 23 45 0A BC DE 偶数位乘以2得到F*2=1E 1*2=02 3*2=06 5*2=0A A*2=14 C*2=1C E*2=1C,
     * 计算奇数位数字之和和偶数位个位十位之和，得到 A+(1+E)+0+2+2+6+4+A+0+(1+4)+B+(1+8)+D+(1+C)=64 => 校验位 16-4
     * 
     * IMEI校验码算法：
     * (1).将偶数位数字分别乘以2，分别计算个位数和十位数之和
     * (2).将奇数位数字相加，再加上上一步算得的值
     * (3).如果得出的数个位是0则校验位为0，否则为10减去个位数
     * 如：35 89 01 80 69 72 41 偶数位乘以2得到 5*2=10 9*2=18 1*2=02 0*2=00 9*2=18 2*2=04 1*2=02,
     * 计算奇数位数字之和和偶数位个位十位之和，得 到 3+(1+0)+8+(1+8)+0+(0+2)+8+(0+0)+6+(1+8)+7+(0+4)+4+(0+2)=63 => 校验 位 10-3 = 7
     */
}
