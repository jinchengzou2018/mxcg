package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;

import java.util.regex.Pattern;



public class CarNoDealer extends StringPatternDealer
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
    
    //        return "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$";
    
    @Override
    /**
     * 车牌号:统一格式为浙C12345,浙C1234学,WJ浙0306X、浙C0995警、浙O69686、蒙O8FZ65
     * 
     */
    public String convert(String data)
    {
        data = data.replaceAll("[\\.\\·\\﹡\\*\\-\\—\\#\\?\\'\\=\\_\\~\\`\\!\\@\\$\\%\\^\\+\\:\\;\\?\\/\\&\\(\\)\\[\\]\\{\\}\\\"\\<\\>\\,\\.\\|\\\\]", "");//modify Bug 740
        
        //处理电动车
        String diandongche = "^[A-Z]{2}[A-Z_0-9]{6}$";
        if (Pattern.matches(diandongche, data))
        {
            return data;
        }
        //普通车牌
        String commonregex = "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{4}[A-Z0-9学警]$";
        Pattern commonpattern = Pattern.compile(commonregex);
        if (commonpattern.matcher(data).matches())
        {
            return data;
        }
        //武警车牌
        String WJregex = "^WJ[\u4e00-\u9fa5]{1}[A-Z_0-9]{5}$";
        Pattern WJpattern = Pattern.compile(WJregex);
        if (WJpattern.matcher(data).matches())
        {
            return data;
        }
        
        //特殊武警车牌
        //其中“T”为交通部队警种代号，“D”、“H”、“S”分别表示武警水电、黄金、森林部队; “X”、“B”、“J”分别表示公安消防、边防、警卫部队
        String SpSoregex = "^WJ[0-9]{2}[0-9TDHSXBJ]{1}[0-9]{4}$";
        Pattern SpSopattern = Pattern.compile(SpSoregex);
        if (SpSopattern.matcher(data).matches())
        {
            return data;
        }
        
        //军车
        //军牌由代表大军区（北、沈、兰、济、南、广、成）/军种（海、空）/
        //四总部（军）/军委（军）的汉字、 代表单位的字母和5位数字标号组成，式样为[北A-12345]、[海A-12345]、[空A-12345]、[军A-12345]。 汉字的“北、沈、兰、济、南、广、成”分别代表北京军区、
        //沈阳军区、兰州军区、济南军区、南京军区、广州军区、成都军区，“海、空”代表海军、空军，“军”代表四总部和军委
        //已包括在第一种情形，这里不做判断
        
        
        return null;
        
        /*String regex1 = "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{4}[A-Z0-9学警]$";
        String regex2 = "^[\u4e00-\u9fa5]{1}[A-Z]{1}·[A-Z_0-9]{4}[A-Z0-9学警]$";
        String replaceMent = "·";
        Pattern pattern = Pattern.compile(regex1);
        
        if (pattern.matcher(data).matches())
        {
            return data;
        }
        else
        {
            Pattern pattern2 = Pattern.compile(regex2);
            if (pattern2.matcher(data).matches())
            {
                pattern2 = Pattern.compile(replaceMent);
                Matcher m = pattern2.matcher(data);
                if (m.find())
                {
                    data = m.replaceAll("");
                }
            }
            else
            {
                return null;
            }
        }
        return data;*/
    }
}
