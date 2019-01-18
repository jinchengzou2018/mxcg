package com.mxcg.common.util.date;

import com.mxcg.common.util.CollectionUtil;
import com.mxcg.common.util.StringUtil;
import com.mxcg.core.exception.TofocusException;
import com.mxcg.core.log.SimpleLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * 日期工具集
 * @author  huhao
 * @version  [版本号, 2014-2-13]
 */
@SuppressWarnings("static-access")
public final class DateUtil
{
    public static final String date_format_1 = "^[0-9]{4}-\\d{1,2}-\\d{1,2}.*\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    
    public static final String date_format_2 = "^[0-9]{4}-\\d{1,2}-\\d{1,2}.*\\d{1,2}:\\d{1,2}$";
    
    public static final String date_format_3 = "^[0-9]{4}-\\d{1,2}-\\d{1,2}$";
    
    public static final String date_format_3_1 = "^[0-9]{4}-\\d{1,2}$";
    
    public static final String date_format_3_2 = "^\\d{1,2}-\\d{1,2}-\\d{2}$";
    
    public static final String date_format_4 = "^[0-9]{4}\\d{1,2}\\d{1,2}.*\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    
    public static final String date_format_5 = "^[0-9]{4}\\d{1,2}\\d{1,2}.*\\d{1,2}:\\d{1,2}$";
    
    public static final String date_format_6 = "^[0-9]{4}\\d{1,2}\\d{1,2}$";
    
    public static final String date_format_6_1 = "^[0-9]{4}\\d{1,2}$";// modify Bug 795
    
    public static final String date_format_7 = "^\\d{1,2}:\\d{1,2}:\\d{4}.*\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    
    public static final String date_format_8 = "^\\d{1,2}:\\d{1,2}:\\d{4}.*\\d{1,2}:\\d{1,2}$";
    
    public static final String date_format_9 = "^\\d{1,2}:\\d{1,2}:\\d{4}$";
    
    public static final String date_format_10 = "^\\d{1,2}:\\d{1,2}:\\d{2}.*\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    
    public static final String date_format_11 = "^\\d{1,2}:\\d{1,2}:\\d{2}.*\\d{1,2}:\\d{1,2}$";
    
    public static final String date_format_12 = "^\\d{1,2}:\\d{1,2}:\\d{2}$";
    
    public static final String date_format_13 = "^[0-9]{4}年\\d{1,2}月\\d{1,2}日\\d{1,2}时\\d{1,2}分\\d{1,2}秒$";
    
    public static final String date_format_13_1 = "^[0-9]{4}年\\d{1,2}月\\d{1,2}日.*\\d{1,2}时\\d{1,2}分\\d{1,2}秒$";
    
    public static final String date_format_14 = "^[0-9]{4}年\\d{1,2}月\\d{1,2}日\\d{1,2}时\\d{1,2}分$";
    
    public static final String date_format_14_1 = "^[0-9]{4}年\\d{1,2}月\\d{1,2}日.*\\d{1,2}时\\d{1,2}分$";
    
    public static final String date_format_15 = "^[0-9]{4}年\\d{1,2}月\\d{1,2}日$";
    
    public static final String date_format_16 = "^[0-9]{2}年\\d{1,2}月\\d{1,2}日\\d{1,2}时\\d{1,2}分\\d{1,2}秒$";
    
    public static final String date_format_17 = "^[0-9]{2}年\\d{1,2}月\\d{1,2}日\\d{1,2}时\\d{1,2}分$";
    
    public static final String date_format_18 = "^[0-9]{2}年\\d{1,2}月\\d{1,2}日$";
    
    public static final String date_format_18_1 = "^[0-9]{2}年\\d{1,2}月$";
    
    public static final String date_format_19 = "^[0-9]{4}:\\d{1,2}:\\d{1,2}$";//(方法入口已将.替换为:)匹配2014.10.21式的日期 modify Bug 698
    
    public static final String date_format_19_1 = "^[0-9]{4}:\\d{1,2}:\\d{1,2}.*\\d{1,2}:\\d{1,2}:\\d{1,2}$";//(方法入口已将.替换为:)匹配2014.10.21 12:12:10 modify Bug 855
    
    public static final String date_format_19_2 = "^[0-9]{4}:\\d{1,2}:\\d{1,2}.*\\d{1,2}:\\d{1,2}$";//(方法入口已将.替换为:)匹配2014.10.21 12:12 modify Bug 855
    
    public static final String date_format_19_3 = "^[0-9]{4}:\\d{1,2}:\\d{1,2}.*\\d{1,2}$";//(方法入口已将.替换为:)匹配2014.10.21 12:12 modify Bug 855
    
    public static final String date_format_20 = "^[0-9]{4}:\\d{1,2}$";//(方法入口已将.替换为:)匹配2014.10式的日期
    
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    private static Set<Character> timeunits = new HashSet<Character>()
    {
        {
            add('星');
            add('期');
            add('年');
            add('月');
            add('日');
            add('时');
            add('点');
            add('分');
            add('秒');
            add('上');
            add('下');
            add('午');
            add('a');
            add('p');
            add('m');
        }
    };
    
    private static Map<Character, Character> digitMap = new HashMap<Character, Character>()
    {
        {
            put('一', '1');
            put('二', '2');
            put('三', '3');
            put('四', '4');
            put('五', '5');
            put('六', '6');
            put('七', '7');
            put('八', '8');
            put('九', '9');
            put('零', '0');
            put('两', '2');
            put('1', '1');
            put('2', '2');
            put('3', '3');
            put('4', '4');
            put('5', '5');
            put('6', '6');
            put('7', '7');
            put('8', '8');
            put('9', '9');
            put('0', '0');
        }
    };
    
    private DateUtil()
    {
    }
    
    public static String formatGMTDate(Date date)
    {
        if (null == date)
            return null;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        format1.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss.SSS");
        format2.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format1.format(date) + "T" + format2.format(date) + "Z";
    }
    
    public static String formatGMTDate(String date, String format)
    {
        Date startTimeDate = formatDateStr(date, format);
        return formatGMTDate(startTimeDate);
    }
    
    public static String formatDate(Date date, String pattern)
    {
        if (null == date || null == pattern)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String formatDate(Date date)
    {
        if (null == date)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    
    /**
     *
     * 1900年的的日期全部去除
     * @param date
     * @return
     */
    public static String FilterDate(String date)
    {
        
        try
        {
            String dateEmpty = "";
            if (StringUtil.isEmpty(date))
                return dateEmpty;
            
            Date d = DateUtil.formatDateStr(date);
            
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            Calendar bf = Calendar.getInstance();
            bf.set(1901, 0, 1);
            
            if (c.before(bf))
                return dateEmpty;
            
        }
        catch (Exception e)
        {
            SimpleLog.outErr("日期转换不正确");
            e.printStackTrace();
        }
        return date;
    }
    
    public static Date formatDateStr(String datestr, String pattern)
    {
        if (null == datestr || null == pattern || datestr.length() == 0)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date da = null;
        try
        {
            da = format.parse(datestr);
        }
        catch (ParseException e)
        {
            //            SimpleLog.outException(null, "日期数据解析失败, datestr=" + datestr + ", pattern=" + pattern, e);
            throw new TofocusException("日期数据解析失败", e);
        }
        return da;
    }
    
    /**
     * 不满13位在后面补0的转Date
     */
    public static Date QQlongtoDate(Long value)
    {
        if (value == null || value <= 0L)
            return null;
        
        String sdate = value.toString();
        int zeroCnt = 13 - sdate.length();
        if (sdate.length() < 13)
        {
            for (int i = 0; i < zeroCnt; i++)
            {
                sdate = sdate + "0";
            }
        }
        Date date = new Date(Long.valueOf(sdate));
        return date;
    }
    
    private static List<Integer> getSpecialCharIndexs(String dateString)
    {
        List<Integer> indexs = new ArrayList<Integer>();
        char[] f = dateString.toCharArray();
        for (int i = 0; i < f.length; i++)
        {
            if (f[i] == ':' || f[i] == '.')
            {
                indexs.add(i);
            }
        }
        return indexs;
    }
    
    public static Date formatDateStr(Object datestr)
        throws Exception
    {
        if (null == datestr)
            return null;
        Date trydate = null;
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            trydate = format.parse(datestr.toString());
        }
        catch (Exception e)
        {
        }
        if (trydate != null)
            return trydate;
        
        //格式意义不明，例如”06-10-15“，很难知道这个是”2006年10月15日“，还是”2015年6月10日“，还是”2015年10月6日“?
        //当遇到类似06-10月-15格式的时间时，统一按日-月-年意义的格式去做转换。
        if (Pattern.matches(date_format_3_2, datestr.toString()))
        {
            try
            {
                String[] splites = datestr.toString().split("-");
                int d1 = Integer.valueOf(splites[0]);
                int d2 = Integer.valueOf(splites[1]);
                int d3 = Integer.valueOf(splites[2]);
                if (d1 <= 12)
                {
                    if (d2 <= 12)
                    {
                        if (d3 <= 12)
                        {
                            trydate = DateUtil.formatDateStr(datestr.toString(), "dd-MM-yy");//此格式优先
                        }
                        else if (d3 <= 31)
                        {
                            trydate = DateUtil.formatDateStr(datestr.toString(), "yy-MM-dd");
                        }
                    }
                    else if (d2 <= 31)
                    {
                        if (d3 <= 12)
                        {
                            trydate = DateUtil.formatDateStr(datestr.toString(), "MM-dd-yy");
                        }
                        else if (d3 <= 31)
                        {
                            trydate = DateUtil.formatDateStr(datestr.toString(), "MM-dd-yy");
                            
                        }
                    }
                }
                else
                {
                    if (d2 <= 12)
                    {
                        if (d3 <= 12)
                        {
                            trydate = DateUtil.formatDateStr(datestr.toString(), "dd-MM-yy");//此格式优先
                        }
                        else if (d3 <= 31)
                        {
                            trydate = DateUtil.formatDateStr(datestr.toString(), "yy-MM-dd");
                        }
                    }
                }
            }
            catch (Exception e)
            {
            }
            if (trydate != null)
                return trydate;
        }
        else if (Pattern.matches("^\\d{1,2}-\\d{1,2}月-\\d{2}$", datestr.toString()))
        {
            try
            {
                trydate = DateUtil.formatDateStr(datestr.toString(), "dd-MM月-yy");
            }
            catch (Exception e)
            {
            }
            if (trydate != null)
                return trydate;
        }
        
        String dateString = null;
        Date d = null;
        Boolean isPM = null;
        try
        {
            //有中文单位的数字
            ArrayList<NumberItem> years = new ArrayList<NumberItem>();
            ArrayList<NumberItem> months = new ArrayList<NumberItem>();
            ArrayList<NumberItem> days = new ArrayList<NumberItem>();
            ArrayList<NumberItem> hours = new ArrayList<NumberItem>();
            ArrayList<NumberItem> minutes = new ArrayList<NumberItem>();
            ArrayList<NumberItem> seconds = new ArrayList<NumberItem>();
            ArrayList<NumberItem> weeks = new ArrayList<NumberItem>();
            ArrayList<NumberItem> others = new ArrayList<NumberItem>(); //无单位数字
            //数字
            ArrayList<NumberItem> numbers = new ArrayList<NumberItem>();
            //分割
            ArrayList<String> spans = new ArrayList<String>();
            dateString = StringUtil.full2HalfChange2(datestr.toString()).trim().toLowerCase();
            int numberindex = 0;
            int index = 0;
            int tenindex = -1000;
            StringBuffer numberBuf = new StringBuffer();
            StringBuffer spanBuf = new StringBuffer();
            //分离数字和非数字
            while (index < dateString.length())
            {
                while (index < dateString.length())
                {
                    char c = dateString.charAt(index);
                    if (isDigit(c))
                    {
                        break;
                    }
                    else if (c == '十')
                    {
                        numberBuf.append('1');
                        tenindex = index;
                        index++;
                        break;
                    }
                    else
                    {
                        spanBuf.append(c);
                        index++;
                    }
                }
                spans.add(spanBuf.toString());
                while (index < dateString.length())
                {
                    char c = dateString.charAt(index);
                    if (isDigit(c))
                    {
                        numberBuf.append(convertDigit(c));
                        index++;
                    }
                    else if (c == '十')
                    {
                        tenindex = index;
                        index++;
                    }
                    else
                    {
                        if (tenindex + 1 == index)
                        {
                            numberBuf.append('0');
                        }
                        break;
                    }
                }
                if (numberBuf.length() > 0)
                {
                    String content = numberBuf.toString();
                    NumberItem n = new NumberItem(numberindex, content);
                    numbers.add(n);
                    numberindex++;
                }
                tenindex = -1000;
                numberBuf = new StringBuffer();
                spanBuf = new StringBuffer();
            }
            //去除多余字符
            ArrayList<String> tspans = new ArrayList<String>();
            for (int i = 0; i < spans.size(); i++)
            {
                int idx = 0;
                StringBuffer sb = new StringBuffer();
                while (idx < spans.get(i).length())
                {
                    char c = spans.get(i).charAt(idx);
                    if (timeunits.contains(c))
                        sb.append(c);
                    idx++;
                }
                tspans.add(sb.toString());
            }
            spans = tspans;
            //提取明确的时间
            for (int i = 0; i < numbers.size(); i++)
            {
                NumberItem n = numbers.get(i);
                String prespan = "";
                String afterspan = "";
                if (i < spans.size())
                    prespan = spans.get(i);
                if (i + 1 < spans.size())
                    afterspan = spans.get(i + 1);
                if ((prespan.endsWith("下午") || prespan.endsWith("pm"))
                    || (afterspan.endsWith("下午") || afterspan.endsWith("pm")))
                    isPM = true;
                else if ((prespan.endsWith("上午") || prespan.endsWith("am"))
                    || (afterspan.endsWith("上午") || afterspan.endsWith("am")))
                    isPM = false;
                if (afterspan.startsWith("年"))
                {
                    n.setUnit(NumberDateUnit.年);
                    years.add(n);
                }
                else if (afterspan.startsWith("月"))
                {
                    n.setUnit(NumberDateUnit.月);
                    months.add(n);
                }
                else if (afterspan.startsWith("日"))
                {
                    n.setUnit(NumberDateUnit.日);
                    days.add(n);
                }
                else if (afterspan.startsWith("时"))
                {
                    n.setUnit(NumberDateUnit.时);
                    hours.add(n);
                }
                else if (afterspan.startsWith("点"))
                {
                    n.setUnit(NumberDateUnit.时);
                    hours.add(n);
                }
                else if (afterspan.startsWith("分"))
                {
                    n.setUnit(NumberDateUnit.分);
                    minutes.add(n);
                }
                else if (afterspan.startsWith("秒"))
                {
                    n.setUnit(NumberDateUnit.秒);
                    seconds.add(n);
                }
                else if (prespan.endsWith("星期"))
                {
                    n.setUnit(NumberDateUnit.星期);
                    weeks.add(n);
                }
                else
                {
                    others.add(n);
                }
            }
            
            //组合时间
            if (years.size() == 0)
            {
                others = guessYMDorder(others);
                String smonth = null;
                String sday = null;
                String shour = null;
                String sminute = null;
                String ssecond = null;
                if (months.size() > 0)
                    smonth = months.get(0).getContent();
                if (days.size() > 0)
                    sday = days.get(0).getContent();
                if (hours.size() > 0)
                    shour = hours.get(0).getContent();
                if (minutes.size() > 0)
                    sminute = minutes.get(0).getContent();
                if (seconds.size() > 0)
                    ssecond = seconds.get(0).getContent();
                d = ParseOtherDate(others, -1, 6, isPM, null, smonth, sday, shour, sminute, ssecond);
                if (d != null)
                    return d;
            }
            else
            {
                for (int i1 = 0; i1 < years.size(); i1++)
                {
                    NumberItem year = years.get(i1);
                    String syear = year.getContent();
                    if (months.size() == 0)
                    {
                        d = ParseOtherDate(others, year.getIndex(), 1, isPM, syear, "01", "01", "00", "00", "00");
                        if (d != null)
                            return d;
                    }
                    else
                    {
                        for (int i2 = 0; i2 < months.size(); i2++)
                        {
                            NumberItem month = months.get(i2);
                            String smonth = month.getContent();
                            if (days.size() == 0)
                            {
                                d = ParseOtherDate(others,
                                    month.getIndex(),
                                    2,
                                    isPM,
                                    syear,
                                    smonth,
                                    "01",
                                    "00",
                                    "00",
                                    "00");
                                if (d != null)
                                    return d;
                            }
                            else
                            {
                                for (int i3 = 0; i3 < days.size(); i3++)
                                {
                                    NumberItem day = days.get(i3);
                                    String sday = day.getContent();
                                    if (hours.size() == 0)
                                    {
                                        d = ParseOtherDate(others,
                                            day.getIndex(),
                                            3,
                                            isPM,
                                            syear,
                                            smonth,
                                            sday,
                                            "00",
                                            "00",
                                            "00");
                                        if (d != null)
                                            return d;
                                    }
                                    else
                                    {
                                        for (int i4 = 0; i4 < hours.size(); i4++)
                                        {
                                            NumberItem hour = hours.get(i4);
                                            String shour = null;
                                            int h = Integer.parseInt(hour.getContent());
                                            if (isPM == null)
                                            {
                                                shour = hour.getContent();
                                            }
                                            else if (isPM)
                                            {
                                                if (h != 12)
                                                    shour = String.valueOf(h + 12);
                                                else
                                                    shour = hour.getContent();
                                            }
                                            else
                                            {
                                                if (h == 12)
                                                    shour = "0";
                                                else
                                                    shour = hour.getContent();
                                            }
                                            if (minutes.size() == 0)
                                            {
                                                d = ParseOtherDate(others,
                                                    hour.getIndex(),
                                                    4,
                                                    isPM,
                                                    syear,
                                                    smonth,
                                                    sday,
                                                    shour,
                                                    "00",
                                                    "00");
                                                if (d != null)
                                                    return d;
                                            }
                                            else
                                            {
                                                for (int i5 = 0; i5 < minutes.size(); i5++)
                                                {
                                                    NumberItem minute = minutes.get(i5);
                                                    String sminute = minute.getContent();
                                                    if (seconds.size() == 0)
                                                    {
                                                        d = ParseOtherDate(others,
                                                            minute.getIndex(),
                                                            5,
                                                            isPM,
                                                            syear,
                                                            smonth,
                                                            sday,
                                                            shour,
                                                            sminute,
                                                            "00");
                                                        if (d != null)
                                                            return d;
                                                    }
                                                    else
                                                    {
                                                        for (int i6 = 0; i6 < seconds.size(); i6++)
                                                        {
                                                            NumberItem second = seconds.get(i6);
                                                            String ssecond = second.getContent();
                                                            d = ParseDate(syear, smonth, sday, shour, sminute, ssecond);
                                                            if (d != null)
                                                                return d;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (d == null)
                throw new TofocusException("时间类型转换错误");
        }
        catch (Exception e)
        {
            //            SimpleLog.outException(null, "日期数据解析失败, datestr=" + datestr, e);
            throw e;
        }
        return d;
    }
    
    private static Date ParseOtherDate(ArrayList<NumberItem> others, int afterindex, int inisize, Boolean isPM,
        String... snumber)
    {
        if (others.size() == 0)
        {
            return ParseDate(snumber);
        }
        else
        {
            String[] s = new String[6];
            for (int i = 0; i < inisize; i++)
            {
                s[i] = snumber[i];
            }
            for (NumberItem number : others)
            {
                number.reset();
                while (!number.isguessfinished())
                {
                    number.guess(afterindex, s, isPM);
                    if (s[5] != null)
                    {
                        Date d = ParseDate(s);
                        if (d != null)
                            return d;
                        s = new String[6];
                        for (int i = 0; i < inisize; i++)
                        {
                            s[i] = snumber[i];
                        }
                    }
                }
            }
            return ParseDate(s);
        }
    }
    
    //猜想年月日顺序
    private static ArrayList<NumberItem> guessYMDorder(ArrayList<NumberItem> list)
    {
        ArrayList<NumberItem> result = new ArrayList<>();
        if (list.size() == 0)
            return list;
        else if (list.size() == 1)
            return list;
        else if (list.size() == 2)
        {
            if (list.get(0).getContent().length() < list.get(1).getContent().length())
            {
                result.add(list.get(1));
                result.add(list.get(0));
            }
        }
        else
        {
            if (list.get(0).getContent().length() == 4)
            {
                result.add(list.get(0));
                int n2 = Integer.parseInt(list.get(1).getContent());
                int n3 = Integer.parseInt(list.get(2).getContent());
                if (n2 > 12 && n3 <= 12)
                {
                    result.add(list.get(2));
                    result.add(list.get(1));
                }
                else
                {
                    result.add(list.get(1));
                    result.add(list.get(2));
                }
            }
            else if (list.get(0).getContent().length() > 4)
            {
                return list;
            }
            else
            {
                int n2 = Integer.parseInt(list.get(1).getContent());
                int n3 = Integer.parseInt(list.get(2).getContent());
                if (n2 > 12)
                {
                    result.add(list.get(2));
                    result.add(list.get(0));
                    result.add(list.get(1));
                }
                else if (n3 > 31)
                {
                    result.add(list.get(2));
                    result.add(list.get(1));
                    result.add(list.get(0));
                }
                else
                {
                    result.add(list.get(0));
                    result.add(list.get(1));
                    result.add(list.get(2));
                }
            }
            for (int i = 3; i < list.size(); i++)
            {
                result.add(list.get(i));
            }
        }
        return result;
    }
    
    private static Date ParseDate(String... numberItems)
    {
        if (numberItems.length < 6)
            return null;
        for (int i = 0; i < numberItems.length; i++)
        {
            if (numberItems[i] == null)
            {
                if (i == 0)
                    return null;
                else if (i < 3)
                    numberItems[i] = "01";
                else
                    numberItems[i] = "00";
            }
            //                return null;
        }
        if (numberItems[0].length() == 1)
            numberItems[0] = "0" + numberItems[0];
        StringBuffer sb = new StringBuffer().append(numberItems[0]).append("-");
        sb.append(numberItems[1]).append("-");
        sb.append(numberItems[2]).append(" ");
        sb.append(numberItems[3]).append(":");
        sb.append(numberItems[4]).append(":");
        sb.append(numberItems[5]);
        Date d = null;
        if (numberItems[0].length() <= 2)
            d = ParseDate(sb.toString(), "yy-MM-dd HH:mm:ss");
        else
            d = ParseDate(sb.toString(), "yyyy-MM-dd HH:mm:ss");
        return d;
    }
    
    private static Date ParseDate(String s, String pattern)
    {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        Date da = null;
        try
        {
            da = format.parse(s);
        }
        catch (ParseException e)
        {
            format = new SimpleDateFormat(pattern + "z");
            try
            {
                da = format.parse(s + "CST");
            }
            catch (ParseException e1)
            {
                SimpleLog.outException(null, "日期数据解析失败, datestr=" + s, e);
            }
        }
        return da;
    }
    
    public static boolean isDigit(char c)
    {
        return digitMap.containsKey(c);
    }
    
    public static String convertDigit(char c)
    {
        return digitMap.get(c).toString();
    }
    
    /**
     * 尝试转换字符串为Date类型，转换失败抛出异常，由调用者处理
     * @param datestr
     * @return date
     * @throws Exception
     */
    public static Date formatDateStrLegend(Object datestr)
        throws Exception
    {
        if (null == datestr)
            return null;
        String dateString = null;
        Date d = null;
        try
        {
            dateString = StringUtil.full2HalfChange2(datestr.toString()).trim();
            
            if (Pattern.matches("^[0-9]{4}-\\d{1,2}-\\d{1,2}.*\\d{1,2}:\\d{1,2}:\\d{1,2}:\\d{1,3}$", dateString))
            {
                //处理类似于2014-6-12 09:36:15:106这种时间格式 
                List<Integer> indexs = getSpecialCharIndexs(dateString);
                if (indexs.size() >= 3)
                    dateString = dateString.substring(0, indexs.get(2));
            }
            else if (Pattern.matches("^[0-9]{4}\\/\\d{1,2}\\/\\d{1,2}\\/\\d{1,2}:\\d{1,2}:\\d{1,2}$", dateString))
            {
                //处理类似于2014/12/1/0:03:35这种时间格式 ,转换后得到2014/12/1 0:03:35
                int pos = -1;
                for (int k = 0; k < dateString.length(); k++)
                {
                    if ('/' == dateString.charAt(k))
                    {
                        pos = k;
                    }
                }
                if (pos != -1)
                {
                    dateString = new StringBuffer(dateString).replace(pos, pos + 1, " ").toString();
                }
            }
            
            //尝试把特殊符号替换掉
            dateString = dateString.replaceAll("\\.", ":").replaceAll("\\/", ":");
            
            //TODO 尝试转换字符串为Date类型，转换失败抛出异常，由调用者处理
            if (Pattern.matches(date_format_1, dateString))
            {
                if (dateString.indexOf("上午") != -1 || dateString.indexOf("am") != -1 || dateString.indexOf("AM") != -1)
                {
                    dateString = dateString.replace("上午", "").replace("am", "").replace("AM", "");
                    d = DateUtil.formatDateStr(dateString, "yyyy-MM-dd HH:mm:ss");
                }
                else if (dateString.indexOf("下午") != -1 || dateString.indexOf("pm") != -1
                    || dateString.indexOf("PM") != -1)
                {
                    dateString = dateString.replace("下午", "").replace("pm", "").replace("PM", "");
                    d = DateUtil.formatDateStr(dateString, "yyyy-MM-dd HH:mm:ss");
                    Calendar c = Calendar.getInstance();
                    c.setTime(d);
                    c.add(Calendar.HOUR_OF_DAY, 12);
                    d = c.getTime();
                }
                else
                {
                    d = DateUtil.formatDateStr(dateString, "yyyy-MM-dd HH:mm:ss");
                }
            }
            else if (Pattern.matches(date_format_2, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy-MM-dd HH:mm");
            }
            else if (Pattern.matches(date_format_3, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy-MM-dd");
            }
            else if (Pattern.matches(date_format_3_2, dateString))
            {
                try
                {
                    String[] splites = dateString.split("-");
                    int yearormonth = Integer.valueOf(splites[0]);
                    int monthorday = Integer.valueOf(splites[1]);
                    int dayoryear = Integer.valueOf(splites[2]);
                    if (monthorday <= 12)
                    {
                        if (dayoryear <= 31)
                        {
                            d = DateUtil.formatDateStr(dateString, "yy-MM-dd");
                        }
                        else
                        {
                            if (yearormonth <= 12)
                            {
                                d = DateUtil.formatDateStr(dateString, "MM-dd-yy");
                            }
                        }
                    }
                    else if (monthorday <= 31)
                    {
                        if (yearormonth <= 12)
                        {
                            d = DateUtil.formatDateStr(dateString, "MM-dd-yy");
                        }
                    }
                }
                catch (Exception e)
                {
                    d = DateUtil.formatDateStr(dateString, "MM-dd-yy");
                }
            }
            else if (Pattern.matches(date_format_3_1, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy-MM");
            }
            else if (Pattern.matches(date_format_4, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyyMMdd HH:mm:ss");
            }
            else if (Pattern.matches(date_format_5, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyyMMdd HH:mm");
            }
            else if (Pattern.matches(date_format_6, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyyMMdd");
            }
            else if (Pattern.matches(date_format_6_1, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyyMM");
            }
            else if (Pattern.matches(date_format_7, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "MM:dd:yyyy HH:mm:ss");
            }
            else if (Pattern.matches(date_format_8, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "MM:dd:yyyy HH:mm");
            }
            else if (Pattern.matches(date_format_9, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "MM:dd:yyyy");
            }
            else if (Pattern.matches(date_format_10, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "MM:dd:yy HH:mm:ss");
            }
            else if (Pattern.matches(date_format_11, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "MM:dd:yy HH:mm");
            }
            else if (Pattern.matches(date_format_12, dateString))
            {
                try
                {
                    String[] splites = dateString.split(":");
                    int yearormonth = Integer.valueOf(splites[0]);
                    int monthorday = Integer.valueOf(splites[1]);
                    int dayoryear = Integer.valueOf(splites[2]);
                    /**
                     * 分为年在前面和月在前面
                     *
                     * 月在前面：(中间是day),后面是年
                     * 月<=12,day<=31
                     * 如： 01：12：45
                     *
                     * 年在前面，中间是月,后面是day
                     * monthorday <=12,day<=31
                     * 11:12:12  月:日:年 , 年:月:日, 
                     * 11:12:30  年:月:日, 
                     * 11:25:30  月:日:年, 
                     */
                    if (monthorday <= 12)
                    {
                        if (dayoryear <= 31)
                        {
                            d = DateUtil.formatDateStr(dateString, "yy:MM:dd");
                        }
                        else
                        {
                            if (yearormonth <= 12)
                            {
                                d = DateUtil.formatDateStr(dateString, "MM:dd:yy");
                            }
                        }
                    }
                    else if (monthorday <= 31)
                    {
                        if (yearormonth <= 12)
                        {
                            d = DateUtil.formatDateStr(dateString, "MM:dd:yy");
                        }
                    }
                }
                catch (Exception e)
                {
                    d = DateUtil.formatDateStr(dateString, "MM:dd:yy");
                }
            }
            else if (Pattern.matches(date_format_13, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy年MM月dd日HH时mm分ss秒");//
            }
            else if (Pattern.matches(date_format_13_1, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy年MM月dd日 HH时mm分ss秒");//
            }
            else if (Pattern.matches(date_format_14, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy年MM月dd日HH时mm分");
            }
            else if (Pattern.matches(date_format_14_1, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy年MM月dd日 HH时mm分");
            }
            else if (Pattern.matches(date_format_15, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy年MM月dd日");
            }
            else if (Pattern.matches(date_format_16, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yy年MM月dd日HH时mm分ss秒");
            }
            else if (Pattern.matches(date_format_17, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yy年MM月dd日HH时mm分");
            }
            else if (Pattern.matches(date_format_18, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yy年MM月dd日");
            }
            else if (Pattern.matches(date_format_18_1, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yy年MM月");
            }
            else if (Pattern.matches(date_format_19, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy:MM:dd");
            }
            else if (Pattern.matches(date_format_19_1, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy:MM:dd HH:mm:ss");
            }
            else if (Pattern.matches(date_format_19_2, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy:MM:dd HH:mm");
            }
            else if (Pattern.matches(date_format_19_3, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy:MM:dd HH");
            }
            else if (Pattern.matches(date_format_20, dateString))
            {
                d = DateUtil.formatDateStr(dateString, "yyyy:MM");
            }
        }
        catch (Exception e)
        {
            SimpleLog.outException(null, "日期数据解析失败, datestr=" + datestr, e);
            throw e;
        }
        return d;
    }
    
    /**
     * 返回一个月的开始
     * month=0表示是当前的月份
     * year = 0 表示当前的年份
     * @param year
     * @param month
     * @return
     */
    public static Date getBeginOfMonth(int year, int month)
    {
        GregorianCalendar cal = new GregorianCalendar();
        if (month == -1)
            month = cal.get(cal.MONTH);
        if (year == 0)
            year = cal.get(cal.YEAR);
        cal.set(year, month, 1, 0, 0, 1);
        return cal.getTime();
    }
    
    /**
     *
     * 返回一个月的结束
     * @param year
     * @param month
     * @return
     */
    public static Date getEndOfMonth(int year, int month)
    {
        month = month - 1;
        GregorianCalendar cal = new GregorianCalendar();
        if (month == -1)
            month = cal.get(cal.MONTH);
        if (year == 0)
            year = cal.get(cal.YEAR);
        
        cal.set(year, month, cal.getActualMaximum(cal.DAY_OF_MONTH), 23, 59, 59);
        cal.set(cal.DAY_OF_MONTH, cal.getActualMaximum(cal.DAY_OF_MONTH));
        
        return cal.getTime();
    }
    
    /**
     * 返回一天的开始 
     * @param year:0表示当年
     * @param month:0表示当月
     * @param day:表示当日
     * @return
     */
    public static Date getBeginOfDay(int year, int month, int day)
    {
        month = month - 1;
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        if (month == -1)
            month = cal.get(cal.MONTH);
        if (year == 0)
            year = cal.get(cal.YEAR);
        if (day == 0)
            day = cal.get(cal.DAY_OF_MONTH);
        cal.set(year, month, day, 0, 0, 1);
        Date d = cal.getTime();
        
        return d;
    }
    
    /**
     * 返回一天的开始
     * @param year:0表示当年
     * @param month:0表示当月
     * @param day:表示当日
     * @return
     */
    public static Date getDay(int year, int month, int day, int hour, int minute, int second)
    {
        month = month - 1;
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        if (month == -1)
            month = cal.get(cal.MONTH);
        if (year == 0)
            year = cal.get(cal.YEAR);
        if (day == 0)
            day = cal.get(cal.DAY_OF_MONTH);
        cal.set(year, month, day, hour, minute, second);
        Date d = cal.getTime();
        
        return d;
    }
    
    /**
     *
     * @param year:year = 0表示是当前年
     * @param month month = 0 表示是当前月
     * @param day =0 表示是当天
     * @return
     */
    public static Date getEndOfDay(int year, int month, int day)
    {
        month = month - 1;
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        if (month == -1)
            month = cal.get(cal.MONTH);
        if (year == 0)
            year = cal.get(cal.YEAR);
        if (day == 0)
            day = cal.get(cal.DAY_OF_MONTH);
        cal.set(year, month, day, 23, 59, 59);
        Date d = cal.getTime();
        return d;
    }
    
    /**
     * 比较时间大小，如果返回值{@literal >}0表示d1{@literal >}d2,返回值 = 0 表示 d1 = d2 
     * @param d1
     * @param d2
     * @return
     */
    public static long compareDate(Date d1, Date d2)
    {
        if (d1 == null || d2 == null)
            return 0;
        
        return (d1.getTime() - d2.getTime());
    }
    
    /**
     * 比较日期大小，是否同一天，如果返回值{@literal >}0表示d1{@literal >}d2,返回值 = 0 表示 d1 = d2
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean isSameDate(Date date1, Date date2)
    {
        if (date1 == null || date2 == null)
        {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        
        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
        
        return isSameDate;
    }
    
    /**
     * 根据
     * @param d
     * @param field :field的值包括: cal.HOUR; cal.DAY_OF_MONTH; cal.WEEK_OF_YEAR; 
     * @return
     */
    public static int getTimeFieldValue(Date d, int field)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(field);
    }
    
    public static String strDateTimeNow()
    {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    
    public static long parseTimeStrToSec(String timeStr)
    {
        
        if (StringUtil.isEmpty(timeStr))
        {
            return 0;
        }
        if (timeStr.matches("^(\\d{1,2})[:时](\\d{1,2})[:分](\\d{1,2})[秒]?$"))
        {
            Pattern pattern = Pattern.compile("^(\\d{1,2})[:时](\\d{1,2})[:分](\\d{1,2})[秒]?$");
            Matcher matcher = pattern.matcher(timeStr);
            long time = 0;
            if (matcher.find())
            {
                time += new Integer(matcher.group(1)) * 60 * 60;
                time += new Integer(matcher.group(2)) * 60;
                time += new Integer(matcher.group(3));
                return time;
            }
        }
        if (timeStr.matches("^\\d+[s秒]?$"))
        {
            if (timeStr.contains("s") || timeStr.contains("秒"))
            {
                return Long.parseLong(timeStr.substring(0, timeStr.length() - 1));
            }
            else
            {
                return Long.parseLong(timeStr);
            }
        }
        
        if (timeStr.matches("^\\d+[m分]$") || timeStr.matches("^\\d+分钟$"))
        {
            return Long.parseLong(timeStr.substring(0, timeStr.length() - 1)) * 60;
        }
        
        if (timeStr.matches("^(\\d+)分(\\d+)秒$"))
        {
            long min = Long.parseLong(timeStr.substring(0, timeStr.indexOf("分")));
            long sec = Long.parseLong(timeStr.substring(timeStr.indexOf("分") + 1, timeStr.indexOf("秒")));
            return min * 60 + sec;
        }
        
        if (timeStr.matches("^\\d+(时|小时)(\\d+)分$"))
        {
            long h = Long.parseLong(timeStr.substring(0, timeStr.indexOf("时")).replaceAll("\\D", ""));
            long min = Long.parseLong(timeStr.substring(timeStr.indexOf("时") + 1, timeStr.indexOf("分")));
            return h * 3600 + min * 60;
        }
        
        if (timeStr.matches("^\\d+(时|小时)(\\d+)分(\\d+)秒$"))
        {
            long h = Long.parseLong(timeStr.substring(0, timeStr.indexOf("时")).replaceAll("\\D", ""));
            long min = Long.parseLong(timeStr.substring(timeStr.indexOf("时") + 1, timeStr.indexOf("分")));
            long sec = Long.parseLong(timeStr.substring(timeStr.indexOf("分") + 1, timeStr.indexOf("秒")));
            return h * 3600 + min * 60 + sec;
        }
        
        return 0;
    }
    
    /**
     * 猜测时间的秒数
     * @param time
     * @return
     */
    public static long parseTimeSecond(String time)
    {
        long result = 0;
        try
        {
            if (time == null)
                return result;
            if (Pattern.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$", time))
            {
                int hour = Integer.valueOf(time.substring(0, time.indexOf(":")));
                int minite = Integer.valueOf(time.substring(time.indexOf(":") + 1, time.lastIndexOf(":")));
                int sec = Integer.valueOf(time.substring(time.lastIndexOf(":") + 1));
                
                result = hour * 60 * 60 + minite * 60 + sec;
                return result;
            }
            ArrayList<NumberItem> hours = new ArrayList<NumberItem>();
            ArrayList<NumberItem> minutes = new ArrayList<NumberItem>();
            ArrayList<NumberItem> seconds = new ArrayList<NumberItem>();
            ArrayList<NumberItem> others = new ArrayList<NumberItem>(); //无单位数字
            //数字
            ArrayList<NumberItem> numbers = new ArrayList<NumberItem>();
            //分割
            ArrayList<String> spans = new ArrayList<String>();
            String dateString = StringUtil.full2HalfChange2(time).trim().toLowerCase();
            int numberindex = 0;
            int index = 0;
            int tenindex = -1000;
            StringBuffer numberBuf = new StringBuffer();
            StringBuffer spanBuf = new StringBuffer();
            //分离数字和非数字
            while (index < dateString.length())
            {
                while (index < dateString.length())
                {
                    char c = dateString.charAt(index);
                    if (DateUtil.isDigit(c))
                    {
                        break;
                    }
                    else if (c == '十')
                    {
                        numberBuf.append('1');
                        tenindex = index;
                        index++;
                        break;
                    }
                    else
                    {
                        spanBuf.append(c);
                        index++;
                    }
                }
                spans.add(spanBuf.toString());
                while (index < dateString.length())
                {
                    char c = dateString.charAt(index);
                    if (DateUtil.isDigit(c))
                    {
                        numberBuf.append(DateUtil.convertDigit(c));
                        index++;
                    }
                    else if (c == '十')
                    {
                        tenindex = index;
                        index++;
                    }
                    else
                    {
                        if (tenindex + 1 == index)
                        {
                            numberBuf.append('0');
                        }
                        break;
                    }
                }
                if (numberBuf.length() > 0)
                {
                    String content = numberBuf.toString();
                    NumberItem n = new NumberItem(numberindex, content);
                    numbers.add(n);
                    numberindex++;
                }
                tenindex = -1000;
                numberBuf = new StringBuffer();
                spanBuf = new StringBuffer();
            }
            //去除多余字符
            ArrayList<String> tspans = new ArrayList<String>();
            for (int i = 0; i < spans.size(); i++)
            {
                int idx = 0;
                StringBuffer sb = new StringBuffer();
                while (idx < spans.get(i).length())
                {
                    char c = spans.get(i).charAt(idx);
                    if (timeunits.contains(c))
                        sb.append(c);
                    idx++;
                }
                tspans.add(sb.toString());
            }
            spans = tspans;
            //提取明确的时间
            for (int i = 0; i < spans.size(); i++)
            {
                String span = spans.get(i);
                if (i > 0)
                {
                    if (span.startsWith("时"))
                    {
                        numbers.get(i - 1).setUnit(NumberDateUnit.时);
                        hours.add(numbers.get(i - 1));
                    }
                    else if (span.startsWith("点"))
                    {
                        numbers.get(i - 1).setUnit(NumberDateUnit.时);
                        hours.add(numbers.get(i - 1));
                    }
                    else if (span.startsWith("分"))
                    {
                        numbers.get(i - 1).setUnit(NumberDateUnit.分);
                        minutes.add(numbers.get(i - 1));
                    }
                    else if (span.startsWith("秒"))
                    {
                        numbers.get(i - 1).setUnit(NumberDateUnit.秒);
                        seconds.add(numbers.get(i - 1));
                    }
                    else
                    {
                        others.add(numbers.get(i - 1));
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(hours))
            {
                for (NumberItem it : hours)
                {
                    int h = Integer.valueOf(it.getContent());
                    result += h * 60 * 60;
                }
            }
            if (CollectionUtil.isNotEmpty(minutes))
            {
                for (NumberItem it : minutes)
                {
                    int m = Integer.valueOf(it.getContent());
                    result += m * 60;
                }
            }
            if (CollectionUtil.isNotEmpty(seconds))
            {
                for (NumberItem it : seconds)
                {
                    int s = Integer.valueOf(it.getContent());
                    result += s;
                }
            }
        }
        catch (Exception e)
        {
            result = 0;
        }
        return result;
    }
    
    /**
     * 得到本周周一
     * 
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek()
    {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return FORMAT.format(c.getTime());
    }
    
    /**
     * 得到本周周日
     * 
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek()
    {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return FORMAT.format(c.getTime());
    }
    
    /**
     * 得到本周周一
     * 
     * @return yyyy-MM-dd
     */
    public static Date getMondayOfThisWeek2()
    {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return c.getTime();
    }
    
    /**
     * 得到本周周日
     * 
     * @return yyyy-MM-dd
     */
    public static Date getSundayOfThisWeek2()
    {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return c.getTime();
    }
}
