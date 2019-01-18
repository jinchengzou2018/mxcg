package com.mxcg.common.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 *
 * @author hh
 * @version [版本号, 2013年12月20日]
 */
public final class StringUtil
{
    public static final String ENCODING_GB = "GB2312";
    
    public static final String ENCODING_ISO = "ISO-8859-1";
    
    public static final String ENCODING_UTF8 = "UTF-8";
    
    public static final String ENCODING_GBK = "GBK";
    
    public final static String BLANK_CHARS = " \t\n\r";
    
    public final static SimpleDateFormat SDF_MONTH = new SimpleDateFormat("yyyy-MM");
    
    public final static SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
    
    public final static SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    
    public static final String[] TMP_STRS = {};
    
    private StringUtil()
    {
    }
    
    public static String cutstringbyUTF8byte(String s, int maxlength)
    {
        if (s == null || s.length() * 3 <= maxlength)
        {
            return s;
        }
        else
        {
            int index = -1;
            int current = 0;
            for (int j = 0; j < s.length(); j++)
            {
                if (s.charAt(j) < 128)
                {
                    current = current + 1;
                }
                else if (s.charAt(j) >= 2048)
                {
                    current = current + 3;
                }
                else
                {
                    current = current + 2;
                }
                if (current > maxlength)
                {
                    break;
                }
                else
                {
                    index++;
                }
            }
            return s.substring(0, index + 1);
        }
    }
    
    /**
     * listToString 的反向操作，并去掉结尾的连续空元素
     *
     * @param str
     * @param split
     * @return
     */
    public static List<String> stringToListTrim(String str, String split)
    {
        List<String> codes = new ArrayList<String>();
        if (null == str)
            return codes;
        String[] temp = str.trim().split(split);
        return Arrays.asList(temp);
    }
    
    /**
     * listToString 的反向操作
     *
     * @param str
     * @param split
     * @return
     */
    public static List<String> stringToList(String str, String split)
    {
        List<String> codes = new ArrayList<String>();
        if (null == str)
            return codes;
        String[] temp = str.trim().split(split, -1);
        return Arrays.asList(temp);
    }
    
    public static String listToString(Collection<String> str, String split)
    {
        if (CollectionUtil.isEmpty(str))
            return null;
        StringBuilder bu = new StringBuilder();
        int pos = 0;
        for (String temp : str)
        {
            pos++;
            if (temp != null && !temp.equals(""))
                bu.append(temp.trim());
            if (pos < str.size())
                bu.append(split);
        }
        return bu.toString();
    }
    
    public static String listToString(List<List<String>> str, String split, String groupsplit)
    {
        if (CollectionUtil.isEmpty(str) || isEmpty(split) || isEmpty(groupsplit))
            return null;
        StringBuilder bu = new StringBuilder();
        for (List<String> temp : str)
        {
            bu.append(listToString(temp, split));
            bu.append(groupsplit);
        }
        return bu.toString();
    }
    
    public static String arrayToString(String[] str, String split)
    {
        if (str != null && str.length > 0)
        {
            if (isEmpty(split))
                split = ",";
            StringBuilder bu = new StringBuilder();
            for (String temp : str)
            {
                bu.append(split + temp);
            }
            return bu.substring(split.length()).toString();
            
        }
        else
        {
            return "";
        }
    }
    
    public static String arrayToString(List<String> str, String split)
    {
        if (str != null && str.size() > 0)
        {
            if (isEmpty(split))
                split = ",";
            StringBuilder bu = new StringBuilder();
            for (String temp : str)
            {
                bu.append(split + temp);
            }
            return bu.substring(split.length()).toString();
            
        }
        else
        {
            return "";
        }
    }
    
    public static String toUpperCase(String msgstr)
    {
        if (null == msgstr || msgstr.length() < 0)
            return null;
        return msgstr.toUpperCase();
    }
    
    /**
     * 对字符串用 符号token进行分离字符，保存在数组中，默认下不会返回分隔符。
     * 比如1,2,3,4的字符，分隔后的结果是{1,2,3,4}
     *
     * @param s
     * @param token
     * @return
     */
    public static String[] split(String s, String token)
    {
        if (s == null)
        {
            return null;
        }
        StringTokenizer st = new StringTokenizer(s, token);
        int size = st.countTokens();
        String[] result = new String[size];
        for (int i = 0; i < size; i++)
        {
            result[i] = st.nextToken();
        }
        
        return result;
    }
    
    /**
     * 全角转半角
     *
     * @param QJstr 字符串
     * @return 转换后的字符串
     * @see full2HalfChange2
     */
    @Deprecated
    public static String full2HalfChange(String QJstr)
        throws UnsupportedEncodingException
    {
        StringBuffer outStrBuf = new StringBuffer("");
        String Tstr = "";
        byte[] b = null;
        
        for (int i = 0; i < QJstr.length(); i++)
        {
            Tstr = QJstr.substring(i, i + 1);
            
            // 全角空格转换成半角空格   
            if (Tstr.equals("　"))
            {
                outStrBuf.append(" ");
                continue;
            }
            // 得到 unicode 字节数据
            b = Tstr.getBytes("unicode");
            
            if (b[2] == -1)
            {
                // 表示全角 
                b[3] = (byte)(b[3] + 32);
                b[2] = 0;
                outStrBuf.append(new String(b, "unicode"));
            }
            else
            {
                outStrBuf.append(Tstr);
            }
        } // end for.   
        
        return outStrBuf.toString();
    }
    
    /**
     * 全角转半角并去不可见字符
     *
     * @param QJstr 原始字符
     * @return
     */
    public static String full2HalfChange2(String QJstr)
    {
        StringBuffer outStrBuf = new StringBuffer("");
        for (int m = 0; m < QJstr.length(); m++)
        {
            char ch = QJstr.charAt(m);
            int chi = (int)ch;
            if (chi > 0)
            {
                if (Character.isSpaceChar(chi))
                    ch = ' ';
                else if (chi > 65279)
                {
                    byte b0 = (byte)chi;
                    ch = (char)(byte)(b0 + 32);
                }
                outStrBuf.append(ch);
            }
        }
        return outStrBuf.toString();
    }
    
    public static String safeSolrString2(String str)
    {
        if ("~".equals(str))
            return "";
        StringBuffer outStrBuf = new StringBuffer();
        for (int m = 0; m < str.length(); m++)
        {
            char ch = str.charAt(m);
            int chi = (int)ch;
            if (Character.isLetterOrDigit(ch) || ch == '?' || ch == '*' || ch == '(' || ch == ')' || ch == '"'
                || ch == '~')
            {
                if (chi > 65279)
                {
                    byte b0 = (byte)chi;
                    ch = (char)(byte)(b0 + 32);
                }
                outStrBuf.append(ch);
            }
            else
            {
                outStrBuf.append(' ');
            }
        }
        return outStrBuf.toString();
    }
    
    /**
     * 核采三期检索用 是数字的情况模糊查询
     * @param str
     * @return
     */
    public static String isContainChineseLikeQuery(String str)
    {
        String Result = str;
        if (!isContainChinese(str))
        {
            Result = "*" + Result + "*";
        }
        return Result;
    }
    
    public static boolean isContainChinese(String str)
    {
        
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find())
        {
            return true;
        }
        return false;
    }
    
    public static String safeSolrString(String str)
    {
        if ("~".equals(str))
            return "";
        StringBuffer outStrBuf = new StringBuffer();
        for (int m = 0; m < str.length(); m++)
        {
            char ch = str.charAt(m);
            int chi = (int)ch;
            if (Character.isLetterOrDigit(ch) || ch == '?' || ch == '*' || ((m == str.length() - 1) && ch == '~'))
            {
                if (chi > 65279)
                {
                    byte b0 = (byte)chi;
                    ch = (char)(byte)(b0 + 32);
                }
                outStrBuf.append(ch);
            }
            else
            {
                outStrBuf.append(' ');
            }
        }
        return outStrBuf.toString();
    }
    
    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str)
    {
        if (str == null)
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches())
        {
            return false;
        }
        return true;
    }
    
    /**
     * 判断是否是数字
     *
     * @param str
     * @param regex 正则表达式
     * @return
     */
    public static boolean isNumeric(String str, String regex)
    {
        Pattern pattern = Pattern.compile("[0-9" + regex + "]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches())
        {
            return false;
        }
        return true;
    }
    
    /**
     * 替换特殊字符
     *
     * @param str
     * @param replacement 要替换的字符
     * @return
     */
    public static String replaceAll(String str, String replacement)
    {
        String reg = "[|@#$%^&*:：;；,，.。?？ ]";
        if (isNotEmpty(str))
        {
            String result = str.trim().replaceAll(reg, replacement);
            if (result.endsWith(replacement))
            {
                result = result.substring(0, result.lastIndexOf(replacement));
            }
            return result;
        }
        return "";
    }
    
    /**
     * 替换特殊字符
     *
     * @param str
     * @return
     */
    public static String[] split(String str)
    {
        String reg = "[|@#$%^&*:：;；,，.。?？ ]";
        if (isNotEmpty(str))
        {
            String[] result = str.split(reg);
            return result;
        }
        return null;
    }
    
    /**
     * 把字符数组转换成整型数组
     * @param type 1:主叫
     * @return
     */
    public static String convertDataCollectType(Integer type)
    {
        String result = "";
        switch (type)
        {
            case 1:
                result = "被叫";
                break;
            case 2:
                result = "主叫";
                break;
            default:
                result = "未知状态";
                break;
        }
        return result;
    }
    
    /**
     * 统计指定字符串出现的次数
     * @param str
     * @return 次数
     */
    public static int getCountOfAppeared(String str, String sub)
    {
        if (str == null || sub == null)
            return 0;
        int index = 0;
        int count = 0;
        while ((index = str.indexOf(sub, index)) != -1)
        {
            index = index + sub.length();
            count++;
        }
        /*while((index=str.indexOf(sub))!=-1)  
        {  
            str = str.substring(index+sub.length());  
            count++;  
        } */
        return count;
    }
    
    public static boolean isEmpty(String str)
    {
        return str == null || "".equals(str.trim());
    }
    
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }
    
    public static boolean isBlank(String str)
    {
        return str == null || "".equals(str.trim());
    }
    
    /**
     * 取字符串左边指定长度的字符
     * 
     * @param str
     *          String 字符串
     * @param len
     *          int 长度
     * @return String 结果
     */
    public static String left(String str, int len)
    {
        int l = str.length();
        if (len > l)
        {
            return str;
        }
        return str.substring(0, len);
    }
    
    /**
    * 取字符串左边“总长度-指定长度”个数的字符
    * 
    * @param str
    *          String 字符串
    * @param subLen
    *          要减去的长度
    * @return String 结果
    */
    public static String leftSub(String str, int subLen)
    {
        int len = str.length();
        if (subLen > len)
            return "";
        return left(str, len - subLen);
    }
    
    /**
     * 取字符串右边指定长度的字符
     * 
     * @param str
     *          String 字符串
     * @param len
     *          int 长度
     * @return String 结果
     */
    public static String right(String str, int len)
    {
        int l = str.length();
        if (len > l)
        {
            return str;
        }
        return str.substring(l - len);
    }

    public static String reverse(String str)
    {
        char[] c = str.toCharArray();
        char[] d = new char[c.length];
        for(int i = 0; i< c.length; i++)
        {
            d[d.length - 1 - i] = c[i];
        }
        return d.toString();
    }
}
