package com.mxcg.common.util;

import com.mxcg.core.enums.SexEnum;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**  
 * <p>  
 * 身份证合法性校验  <br>
 * </p>  
 * <p>  
 * --15位身份证号码：第7、8位为出生年份(两位数)，第9、10位为出生月份，
 *                  第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。  <br>
 * --18位身份证号码：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，
 *                  第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。  
 * </p>  
 */
@SuppressWarnings( {"unchecked", "unused", "all"})
public class IdcardValidator
{
    private static Map<String, String> areaMap = new HashMap<String, String>();
    
    // 每位加权因子   
    private static int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
    
    // 第18位校检码   
    private static String verifyCode[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
    
    static {
        areaMap.put("11", "北京");
        areaMap.put("12", "天津");
        areaMap.put("13", "河北");
        areaMap.put("14", "山西");
        areaMap.put("15", "内蒙古");
        areaMap.put("21", "辽宁");
        areaMap.put("22", "吉林");
        areaMap.put("23", "黑龙江");
        areaMap.put("31", "上海");
        areaMap.put("32", "江苏");
        areaMap.put("33", "浙江");
        areaMap.put("34", "安徽");
        areaMap.put("35", "福建");
        areaMap.put("36", "江西");
        areaMap.put("37", "山东");
        areaMap.put("41", "河南");
        areaMap.put("42", "湖北");
        areaMap.put("43", "湖南");
        areaMap.put("44", "广东");
        areaMap.put("45", "广西");
        areaMap.put("46", "海南");
        areaMap.put("50", "重庆");
        areaMap.put("51", "四川");
        areaMap.put("52", "贵州");
        areaMap.put("53", "云南");
        areaMap.put("54", "西藏");
        areaMap.put("61", "陕西");
        areaMap.put("62", "甘肃");
        areaMap.put("63", "青海");
        areaMap.put("64", "宁夏");
        areaMap.put("65", "新疆");
        areaMap.put("71", "台湾");
        areaMap.put("81", "香港");
        areaMap.put("82", "澳门");
        areaMap.put("91", "国外");
    }
    
    /**  
     * 验证身份证的合法性  
     * @param idcard idcard
     * @return   b
     */
    public IdcardObject validate(String idcard)
    {
        IdcardObject object = new IdcardObject();
        if (is15Idcard(idcard))
        {
            idcard = getIdcard18(idcard);
        }
        if (isValid18Idcard(idcard))
        {
            object.isIdCard = true;
            object.idCard = idcard;
            object.birthday = idcard.substring(6, 14);
            object.sex = (Integer.valueOf(idcard.substring(16, 17)) % 2 == 0) ? SexEnum.女 : SexEnum.男;
        }
        else
        {
            object.isIdCard = false;
            object.idCard = null;
        }
        return object;
    }
    
    /**  
     * <p>  
     * 判断18位身份证的合法性  
     * </p>  
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。  
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。  
     * <p>  
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。  
     * </p>
     * <p>  
     * 1.前1、2位数字表示：所在省份的代码；
     * 2.第3、4位数字表示：所在城市的代码； 
     * 3.第5、6位数字表示：所在区县的代码；  
     * 4.第7~14位数字表示：出生年、月、日； 
     * 5.第15、16位数字表示：所在地的派出所的代码；  
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；  
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。  
     * </p>  
     * <p>  
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4  
     * 2 1 6 3 7 9 10 5 8 4 2  
     * </p>  
     * <p>  
     * 2.将这17位数字和系数相乘的结果相加。  
     * </p>
     * <p>  
     * 3.用加出来和除以11，看余数是多少？  
     * </p>
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。  
     * <p>  
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。  
     * </p>  
     *   
     * @param idcard  id
     * @return  b
     */
    public boolean isValid18Idcard(String idcard)
    {
        // 非18位为假   
        if (null == idcard || idcard.length() != 18)
        {
            return false;
        }
        
        // 获取前17位   
        String idcard17 = idcard.substring(0, 17);
        // 获取第18位   
        String idcard18Code = idcard.substring(17, 18);
        char c[] = null;
        String checkCode = "";
        // 是否都为数字   
        if (isDigital(idcard17))
        {
            c = idcard17.toCharArray();
        }
        else
        {
            //dealMap(map, "身份证的前17位不全是数字,错误证号("+idcard+");");
            //map.put("身份证的前17位不全是数字,错误证号("+idcard+");", -1);
            return false;
        }
        
        // ================ 出生年月是否有效 ================     
        String strYear = idcard.substring(6, 10);// 年份     
        String strMonth = idcard.substring(10, 12);// 月份     
        String strDay = idcard.substring(12, 14);// 月份     
        if (!isDate(strYear + "-" + strMonth + "-" + strDay))
        {
            //dealMap(map, "身份证生日无效,错误证号("+idcard+");");
            //map.put("身份证生日无效,错误证号("+idcard+");", -1);
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        //去掉生日范围验证 at 2017/08/25
        //try
        //{
        //    if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
        //        || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0)
        //    {
                //dealMap(map, "身份证生日不在有效范围,错误证号("+idcard+");");
                //map.put("身份证生日不在有效范围,错误证号("+idcard+");", -1);
        //         return false;
        //    }
        //}
        //catch (Exception e)
        //{
        //    return false;
        //}
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0)
        {
            //dealMap(map, "身份证月份无效,错误证号("+idcard+");");
            //map.put("身份证月份无效,错误证号("+idcard+");", -1);
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0)
        {
            //dealMap(map, "身份证日期无效,错误证号("+idcard+");");
            //map.put("身份证日期无效,错误证号("+idcard+");", -1);
            return false;
        }
        // =====================(end)=====================     
        
        // ================ 地区码是否有效 ================     
        Map h = getAreaCode();
        if (h.get(idcard.substring(0, 2)) == null)
        {
            //dealMap(map, "身份证地区编码错误,错误证号("+idcard+");");
            //map.put("身份证地区编码错误,错误证号("+idcard+");", -1);
            return false;
        }
        // ==============================================     
        
        // ================ 判断最后一位的值 ================  
        if (!validateLastCode(idcard)) return false;
        
        return true;
    }
    
    /**
     * 验证身份证的最后一位代码
     */
    private static boolean validateLastCode(String idcard)
    {
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++)
        {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(idcard.charAt(i))) * power[i];
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = verifyCode[modValue];
        
        if (!strVerifyCode.equals(idcard.substring(idcard.length()-1)))
        {
            //dealMap(map, "身份证的最后一位错误,应为("+strVerifyCode+"),错误证号("+idcard+");");
           //map.put("身份证的最后一位错误,应为("+strVerifyCode+"),错误证号("+idcard+");", -1);
           return false;
        }
        return true;
    }
    
    private static void dealMap(Map map, String message)
    {
        StringBuffer sb = new StringBuffer();
        if (map.containsKey(-1))
        {
            sb.append(map.get(-1));
            sb.append(message);
            map.put(-1, sb.toString());
        }
        else
        {
            map.put(-1, message);
        }
    }
    
    /**  
     * 将15位的身份证转成18位身份证  
     *   
     * @param idcard  idcard
     * @return  idcard
     */
    public String getIdcard18(String idcard)
    {
        String idcard17 = null;
        // 非15位身份证   
        if (null == idcard || idcard.length() != 15)
        {
            return null;
        }
        
        if (isDigital(idcard))
        {
            // 获取出生年月日   
            String birthday = idcard.substring(6, 12);
            Date birthdate = null;
            try
            {
                birthdate = new SimpleDateFormat("yyyyMMdd").parse("19" + birthday);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            Calendar cday = Calendar.getInstance();
            cday.setTime(birthdate);
            String year = String.valueOf(cday.get(Calendar.YEAR));
            
            idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);
            
            char c[] = idcard17.toCharArray();
            String checkCode = "";
            
            if (null != c)
            {
                int bit[] = new int[idcard17.length()];
                
                // 将字符数组转为整型数组   
                bit = converCharToInt(c);
                int sum17 = 0;
                sum17 = getPowerSum(bit);
                
                // 获取和值与11取模得到余数进行校验码   
                checkCode = getCheckCodeBySum(sum17);
                // 获取不到校验位   
                if (null == checkCode)
                {
                    return null;
                }
                
                // 将前17位与第18位校验码拼接   
                idcard17 += checkCode;
            }
        }
        else
        { // 身份证包含数字   
            return null;
        }
        return idcard17;
    }
    
    /**  
     * 15位身份证号码的基本数字和位数验校  
     *   
     * @param idcard  idcard
     * @return  b
     */
    public boolean is15Idcard(String idcard)
    {
        return idcard == null || "".equals(idcard) ? false
            : Pattern.matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$", idcard);
    }
    
    /**  
     * 18位身份证号码的基本数字和位数验校  
     *   
     * @param idcard  idcard
     * @return  b
     */
    private boolean is18Idcard(String idcard)
    {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$",
            idcard);
    }
    
    /**  
     * 数字验证  
     *   
     * @param str  str
     * @return  b
     */
    private boolean isDigital(String str)
    {
        return str == null || "".equals(str) ? false : str.matches("^[0-9]*$");
    }
    
    /**  
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值  
     *   
     * @param bit  bit
     * @return  int
     */
    private int getPowerSum(int[] bit)
    {
        int sum = 0;
        
        if (power.length != bit.length)
        {
            return sum;
        }
        
        for (int i = 0; i < bit.length; i++)
        {
            for (int j = 0; j < power.length; j++)
            {
                if (i == j)
                {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }
    
    /**  
     * 将和值与11取模得到余数进行校验码判断  
     * 
     * @param sum17  sum17
     * @return 校验位  
     */
    private String getCheckCodeBySum(int sum17)
    {
        String checkCode = null;
        switch (sum17 % 11)
        {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "X";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
        }
        return checkCode;
    }
    
    /**  
     * 将字符数组转为整型数组  
     *   
     * @param c  c
     * @return  int[]
     * @throws NumberFormatException  
     */
    private int[] converCharToInt(char[] c)
        throws NumberFormatException
    {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c)
        {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }
    
    /**   
    * 功能：设置地区编码   
      * @return Hashtable 对象   
     */
    @SuppressWarnings("unchecked")
    private static Map getAreaCode()
    {
        Map map = areaMap;
        return map;
    }
    
    /**   
     * 功能：判断字符串是否为日期格式   
     * @param str   str
    * @return   b
    */
    private static boolean isDate(String strDate)
    {
        Pattern pattern =
            Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|" +
                    "([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|" +
                    "([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))" +
                    "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|" +
                    "(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *
     * 身份证对象
     *
     * @author  hh
     * @version  [版本号, 2016-3-21]
     */
    public class IdcardObject implements Serializable
    {
        private static final long serialVersionUID = 1L;
        /**
         * 是否是身份证
         */
        public boolean isIdCard;
        /**
         * 当isIdCard为false时，idCard为null
         */
        public String idCard;
        /**
         * 当isIdCard为false时，sex为null
         */
        public SexEnum sex;
        /**
         * 当isIdCard为false时，birthday为null
         */
        public String birthday;
    }

    public String convert18bitTo15bit(String idcard)
    {
        int temp = Integer.valueOf(idcard.substring(6, 8));
        if(temp >= 20)
        {
            return idcard;
        }
        else
        {
            return idcard.substring(0, 6) + idcard.substring(8, 17);
        }
    }

}
