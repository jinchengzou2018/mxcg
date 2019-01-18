package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;
import com.mxcg.common.util.StringUtil;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class PhoneNumDealer extends StringPatternDealer
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    private static List<String[]> patterns = new ArrayList<String[]>();
    
    static 
    {
        patterns.add(new String[]{"(", ")"});
        patterns.add(new String[]{"（", "）"});
        patterns.add(new String[]{"[", "]"});
        patterns.add(new String[]{"【", "】"});
        patterns.add(new String[]{"<", ">"});
        patterns.add(new String[]{"{", "}"});
    }
    
    @Override
    public StringCaseType iniCaseType()
    {
        return StringCaseType.nooperation;
    }

//        return "^(\\d{3,4}-?)?\\d{7,8}-?(\\d{0,4})|(((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8})$";

    @Override
    /**
     * 手机常见的不规范格式有:8613333333333,+8613333333333,+ 8613333333333, 133-3333-3333,133 3333 3333 
     * 都要统一成13333333333
     *10658139110321283376
     *106571610207680000
     */
    public String convert(String metaData)
    {
        String phoneNumber = metaData;
        if(phoneNumber==null ||phoneNumber.equals("")|| phoneNumber.toLowerCase().equals("null"))
        {
            return "";
        }
        
        //处理如"18957073253曾18967098951",18967780998贺85393333
        phoneNumber = phoneNumber.replaceAll("[\u4e00-\u9fa5]", " ");
        
        //处理'13872005663
        phoneNumber = phoneNumber.replaceAll("[\\·\\﹡\\*\\#\\?\\'\\=\\_\\#\\~\\`\\!\\@\\$\\%\\^\\+\\?\\&\\\"]", "");
        
        //phoneNumber = phoneNumber.replace("'", "").replace("\"", "").replace("=", "").replace("_", "").replace("#", "").replace("*", "");
        //phoneNumber = phoneNumber.replace("~", "").replace("`", "").replace("!", "").replace("@", "").replace("$", "").replace("%", "")
        //    .replace("^", "").replace("+", "").replace(":", "").replace(";", "").replace("?", "").replace(".", "").replace("/", "");
            
        //破折号替换为英文减号
        phoneNumber = phoneNumber.replace("—", "-").replace("﹣", "-");
        
        //处理如13616774101(奶奶)，13587648728(父)这样的电话号码
        try
        {
            for (String[] pattern : patterns)
            {
                String pa1 = pattern[0];
                String pa2 = pattern[1];
                //if (phoneNumber.contains(pa1) && phoneNumber.contains(pa2))
                //{
                //处理类似的手机："13868632109（房东）15968737468（租）88341258"
                while (phoneNumber.contains(pa1) && phoneNumber.contains(pa2))
                {
                    String temp = phoneNumber.substring(phoneNumber.indexOf(pa1), phoneNumber.indexOf(pa2) + 1);
                    phoneNumber = phoneNumber.replace(temp, ",");
                }
                //}
            }
        }
        catch (Exception e)
        {
            return null;
        }
        
        //不做验证的格式
        if (metaData.startsWith("1065") || metaData.startsWith("1069") || metaData.startsWith("10086")
            || metaData.startsWith("125200") || metaData.startsWith("125831"))
            return metaData;
        
        String[] phoneNumbers = phoneNumber.split("[\\,\\/\\，\\|\\;\\。\\:\\.\\、\\\\]{1}| |\n");
        //多个号码的情况
        if (phoneNumbers.length >= 1)
        {
            Set<String> multiPhone = new HashSet<String>();
            for (String temp : phoneNumbers)
            {
                String valied = valiPhoneNumber(temp.trim());
                //如果temp=""的话,也会通过校验 
                if (valied != null && ! valied.equals(""))//
                {
                    multiPhone.add(valied);
                }
            }
            if (multiPhone.size() > 0)
                return StringUtil.listToString(multiPhone, ",");
            else
            {
                return dealConsequentPhones(phoneNumber);//做最后一次努力，哈哈
            }
        }
        //单个号码
        else
        {
            String p = valiPhoneNumber(phoneNumber);
            if (p == null)//做最后一次努力，哈哈
            {
                return dealConsequentPhones(phoneNumber);
            }
            return p;
        }
    }
    
    private String valiPhoneNumber(String phoneNumber)
    {
        String regex = "^17901|^17951|^17909|^17909|^11808|^86|^\\+86|^\\+ 86| |-";
        regex = "^179\\d{2}|^11808|^86|^\\+86|^\\+ 86| |-";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        while (matcher.find())
        {
            if (phoneNumber.startsWith("86") && phoneNumber.length() < 11) break;//类似86062069这样的手机号不能把86去了
            phoneNumber = matcher.replaceAll("");
        }
        
        regex =
            "^(\\+)?(\\d{3,6})|(\\+)?(\\d{3,4}-?)?\\d{7,8}-?(\\d{0,4})||(((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8})$";
        pattern = Pattern.compile(regex);
        if (!pattern.matcher(phoneNumber).matches())
            return null;
        else
        {
            return phoneNumber;
        }
    }
    
    //处理1885878068713566253881这样的手机号
    private String dealConsequentPhones(String phoneNumber)
    {
        StringBuffer s = new StringBuffer();
        int splitsize = 0;
        int pos = 0;
        try
        {
            for (int i=0;i<phoneNumber.length();i++)
            {
                splitsize++;
                if (splitsize == 11)
                {
                    int nextpos = pos + splitsize;
                    s.append(phoneNumber.substring(pos, nextpos)).append(",");
                    pos = nextpos;
                    splitsize = 0;
                }
            }
            if (pos < phoneNumber.length()-1) s.append(phoneNumber.substring(pos, phoneNumber.length()));
            String newphone =  s.toString();
            String[] phoneNumbers = newphone.split("[\\,\\/\\，\\|\\;\\。\\:\\.\\、\\\\]{1}| |\n");
            //多个号码的情况
            if (phoneNumbers.length >= 1)
            {
                Set<String> multiPhone = new HashSet<String>();
                for (String temp : phoneNumbers)
                {
                    String valied = valiPhoneNumber(temp.trim());
                    //如果temp=""的话,也会通过校验 
                    if (valied != null && !valied.equals(""))//
                    {
                        multiPhone.add(valied);
                    }
                }
                if (multiPhone.size() > 0) return StringUtil.listToString(multiPhone, ",");
            }
            else return null;//真处理不了
        }
        catch(Exception e)
        {
            return null;//处理不了
        }
        return null;
    }
    
}
