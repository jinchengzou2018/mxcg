package com.mxcg.common.util;

import com.mxcg.core.exception.TofocusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class Util
{
    private static final long[] mask = new long[64];

    static
    {
        mask[63] = 1;
        for(int i = 62; i>=0; i--)
        {
            mask[i] = mask[i+1] * 2;
        }
    }
    
    /**
     * 按位存储的long转换为按字符串形式拼接的枚举值数据
     * @param enumtype
     * @param value
     * @return
     */
    public static String bitMask2EnumString(Class enumtype, long value)
    {
        if (!enumtype.isEnum())
            throw new TofocusException(TofocusException.REQUIRED_PRARAM_ERROR, enumtype.getName() + "不是枚举值类型");
        Object[] enums = enumtype.getEnumConstants();
        StringBuilder sb = new StringBuilder(64);
        boolean[] list = getbitIndex(value);
        boolean splitchart = false;
        for(int i =0 ;i<list.length; i++)
        {
            if(i<enums.length && list[i])
            {
                if(splitchart)
                    sb.append(",");
                sb.append(enums[i].toString());
                splitchart = true;
            }
        }
        return sb.toString();
    }
    
    /**
     * 枚举值转换为按位存储的long
     * <p>
     * 将按字符串形式的枚举值数据转换成按位存储的long。枚举值数量大于64个的，高于64的枚举值会被忽略</p>
     * @param enumtype 枚举类型
     * @param value 枚举的value，用“,”连接
     * @return
     */
    public static long enumString2bitMask(Class enumtype, String value)
    {
        long result = 0;
        if (!enumtype.isEnum())
            throw new TofocusException(TofocusException.REQUIRED_PRARAM_ERROR, enumtype.getName() + "不是枚举值类型");
        if (value == null)
            return 0;
        String[] split = value.split(",");
        Object[] enums = enumtype.getEnumConstants();
        for (int i = 0; i < split.length; i++)
        {
            for (int j = 0; j < enums.length; j++)
            {
                if(j > mask.length - 1)
                    break;
                if(enums[j].toString().equals(split[i]))
                {
                    result = setbit(result, j);
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * 按位修改long数值
     * @param value
     * @param index
     * @return
     */
    public static long setbit(long value, int index)
    {
        return (value | mask[index]);
    }
    
    /**
     * long数值转换为位开关
     * @param value
     * @return
     */
    public static boolean[] getbitIndex(long value)
    {
        boolean[] result = new boolean[64];
        int move = 64;
        for(int i = 0; i< 64; i++)
        {
            move --; 
            if((mask[i] & value) >>> move == 1)
            {
                result[i] = true;
            }
        }
        return result;
    }
    
    /**
     * long数值转换为位开关，每一位各自转成long数值
     * @param value
     * @return
     */
    public static List<Long> getbitIndexList(long value)
    {
        List<Long> result = new ArrayList<Long>();
        long l = 1;
        boolean[] bitIndex = getbitIndex(value);
        for(int i = 63; i>= 0; i--)
        {
            if(bitIndex[i])
                result.add(l);
            l = l<<1;
        }
        return result;
    }
    
    /**
     * 获得随机UUID
     * @return
     */
    public static String getUUID()
    {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
    
    /**
     * 压缩byte数组
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] compress(byte[] data)
        throws IOException
    {
        byte[] zip = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            try
            {
                gzip.write(data);
                gzip.finish();
                zip = bos.toByteArray();
            }
            finally
            {
                gzip.close();
            }
        }
        finally
        {
            bos.close();
        }
        return zip;
    }
    
    /**
     * 解压byte数组
     * @param zip
     * @return
     * @throws IOException
     */
    public static byte[] decompress(byte[] zip)
        throws IOException
    {
        byte[] result = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(zip);
        try
        {
            GZIPInputStream gzip = new GZIPInputStream(bis);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try
            {
                
                byte[] buff = new byte[1024];
                int num = -1;
                while ((num = gzip.read(buff, 0, buff.length)) != -1)
                {
                    bos.write(buff, 0, num);
                }
                result = bos.toByteArray();
            }
            finally
            {
                bos.close();
                gzip.close();
            }
        }
        finally
        {
            bis.close();
        }
        return result;
    }
    
    /**
     * 去掉地区编码的后置0
     * @param areaid
     * @return
     */
    public static String compressAreaCode(String areaid)
    {
        String a = normalizeAreaCode(areaid);
        if(a.substring(2).equals("0000"))
            return a.substring(0, 2);
        else if (a.substring(4).equals("00"))
            return a.substring(0, 4);
        else
            return a;
    }
    
    /**
     * 规范化地区编号
     * @param areaid
     * @return
     */
    public static String normalizeAreaCode(String areaid)
    {
        if (areaid.length() > 6)
        {
            areaid = areaid.substring(0, 6);
        }
        if (areaid.length() < 6)
        {
            StringBuilder sb = new StringBuilder(areaid);
            for (int i = 0; i < 6 - areaid.length(); i++)
            {
                sb.append('0');
            }
            areaid = sb.toString();
        }
        return areaid;
    }
    
    /**
     * 把地区code拆分出三层编码
     * @param areaid
     * @return
     */
    public static String[] splitAreaCode(String areaid)
    {
        String[] result = new String[3];
        areaid = normalizeAreaCode(areaid);
        result[0] = areaid.substring(0, 2);
        result[1] = areaid.substring(2, 4);
        result[2] = areaid.substring(4, 6);
        return result;
    }
    
    /**
     * 提取地区Code的父级，包括自身
     * @param areaid
     * @return
     */
    public static String[] expendAreaCode(String areaid)
    {
        String[] c = splitAreaCode(areaid);
        int level = 0;
        if (c[2].equals("00"))
        {
            if (c[1].equals("00"))
            {
                level = 1;
            }
            else
            {
                level = 2;
            }
        }
        else
        {
            level = 3;
        }
        String[] result = new String[level];
        for (int i = 0; i < level; i++)
        {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j <= i; j++)
            {
                sb.append(c[j]);
            }
            result[i] = sb.toString();
        }
        return result;
    }
    
    /**
     * 获取文件扩展名 
     * @param filename
     * @return
     */
    public static String getNamefromFilename(String filename)
    {
        int index = filename.lastIndexOf(".");
        String name = filename;
        if (index != -1)
        {
            name = filename.substring(0, index);
        }
        return name;
    }
    
    /**
     * 获取文件扩展名 
     * @param filename
     * @return
     */
    public static String getExtfromFilename(String filename)
    {
        int index = filename.lastIndexOf(".");
        String extname = null;
        if (index != -1)
        {
            extname = filename.substring(index + 1);
        }
        return extname;
    }
}
