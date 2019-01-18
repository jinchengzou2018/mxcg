package com.mxcg.common.util;

import java.math.BigDecimal;

/**
 * 
 * 中文数字
 * 
 * @author  wyw
 * @version  [版本号, 2018年9月14日]
 */
public class NumUtil
{
    protected static final char[] UPPER_NUMBERS = new char[] {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    
    protected static final char[] LOWER_NUMBERS = new char[] {'Ｏ', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
    
    public static BigDecimal DEC_100 = new BigDecimal(100);
    
    /**
     * 将数字转换为类似：二ＯＯ五年一月一日
     * 
     * @param s
     * @return
     */
    public static final String toChLower(String s)
    {
        char[] str = s.toCharArray();
        for (int i = 0; i < str.length; i++)
        {
            if (str[i] >= '0' && str[i] <= '9')
                str[i] = LOWER_NUMBERS[Integer.parseInt(String.valueOf(str[i]))];
        }
        return new String(str);
        
    }
    
    /**
     * 将数字转换为类似：贰零零五年壹月壹日
     * 
     * @param s
     * @return
     */
    public static final String toChUpper(String s)
    {
        char[] str = s.toCharArray();
        for (int i = 0; i < str.length; i++)
        {
            if (str[i] >= '0' && str[i] <= '9')
                str[i] = UPPER_NUMBERS[Integer.parseInt(String.valueOf(str[i]))];
        }
        return new String(str);
        
    }
    
    private static final String[] STR_CELL_UPPER =
        new String[] {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟"};
    
    /**
     * 将金额转换成大写，如：壹佰元叁角正
     * @param doubNumber
     * @return
     */
    public static final String jeToCh(double doubNumber)
    {
        // 否则输入出大写金额。
        StringBuffer buf = new StringBuffer();
        // 零数位标记
        boolean bZero = true;
        int CellIndex = 0;
        if (doubNumber == 0)
            return buf.append("零元整").toString(); //$NON-NLS-1$
        // 四舍五入
        double doubMoneyNumber = Math.round(doubNumber * 100);
        boolean bNegative = doubMoneyNumber < 0;
        doubMoneyNumber = Math.abs(doubMoneyNumber);
        while (doubMoneyNumber > 0)
        {
            // 整的处理(无小数位)
            if (CellIndex == 2 && buf.length() == 0)
                buf.append('整');
            // 非零数位的处理
            if (doubMoneyNumber % 10 > 0)
            {
                buf.insert(0, "" + UPPER_NUMBERS[(int)(doubMoneyNumber % 10)] + STR_CELL_UPPER[CellIndex]);
                bZero = false;
            }
            // 零数位的处理
            else
            {
                // 元的处理(个位)
                if (CellIndex == 2)
                {
                    // 段中有数字
                    if (doubMoneyNumber > 0)
                    {
                        buf.insert(0, STR_CELL_UPPER[CellIndex]);
                        bZero = true;
                    }
                }
                // 万、亿数位的处理
                else if (CellIndex == 6 || CellIndex == 10)
                {
                    // 段中有数字
                    if (doubMoneyNumber % 1000 > 0)
                        buf.insert(0, STR_CELL_UPPER[CellIndex]);
                    bZero = true;
                }
                // 前一数位非零的处理
                if (!bZero)
                    buf.insert(0, "" + UPPER_NUMBERS[0]);
                bZero = true;
            }
            doubMoneyNumber = Math.floor(doubMoneyNumber / 10);
            CellIndex++;
        }
        // 负数的处理
        if (bNegative)
            buf.insert(0, '负');
        return buf.toString();
    }
    
    /**
     * 精确到分的字符串转BIGDECIMAL类型
     * @param jyamt
     * @return
     */
    public static BigDecimal strToJe(String jyamt)
    {
        if (StringUtil.isEmpty(jyamt))
            return BigDecimal.ZERO;
        return new BigDecimal(jyamt).divide(DEC_100);
    }
    
    public static String jeToStr(BigDecimal amt)
    {
        if (amt == null)
            return "000000000000";
        amt = amt.setScale(2, BigDecimal.ROUND_HALF_UP);
        return StringUtil.right("000000000000" + amt.toString().replace(".", ""), 12);
    }
}
