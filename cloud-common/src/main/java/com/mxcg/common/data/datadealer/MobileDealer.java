package com.mxcg.common.data.datadealer;

import com.mxcg.common.data.StringCaseType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MobileDealer extends StringPatternDealer
{
    
    @Override
    public StringCaseType iniCaseType()
    {
        return StringCaseType.nooperation;
    }
    
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
        if (phoneNumber == null || phoneNumber.equals("") || phoneNumber.toLowerCase().equals("null"))
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
        
        String p = valiPhoneNumber(phoneNumber);
        return p;
    }
    
    private String valiPhoneNumber(String phoneNumber)
    {
        String regex = "^17901|^17951|^17909|^17909|^11808|^86|^\\+86|^\\+ 86| |-";
        regex = "^179\\d{2}|^11808|^86|^\\+86|^\\+ 86| |-";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        while (matcher.find())
        {
            if (phoneNumber.startsWith("86") && phoneNumber.length() < 11)
                break;//类似86062069这样的手机号不能把86去了
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
}
