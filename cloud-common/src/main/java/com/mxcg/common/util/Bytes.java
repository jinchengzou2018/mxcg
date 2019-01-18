package com.mxcg.common.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

/**
 * 字节转换工具
 * 
 * @author  wyw
 * @version  [版本号, 2018年9月14日]
 */
public class Bytes
{
    public Bytes()
    {
    }
    
    /**
     * 字节数组复制
     * @param tgtBytes
     * @param tgtOffset
     * @param srcBytes
     * @param srcOffset
     * @param srcLength
     * @return
     */
    public static int putBytes(byte tgtBytes[], int tgtOffset, byte srcBytes[], int srcOffset, int srcLength)
    {
        System.arraycopy(srcBytes, srcOffset, tgtBytes, tgtOffset, srcLength);
        return tgtOffset + srcLength;
    }
    
    /**
     * 字节数组设置
     * @param bytes
     * @param offset
     * @param b
     * @return
     */
    public static int putByte(byte bytes[], int offset, byte b)
    {
        bytes[offset] = b;
        return offset + 1;
    }
    
    /**
     * ByteBuffer转byte数组
     * @param bb
     * @return
     */
    public static byte[] toBytes(ByteBuffer bb)
    {
        int length = bb.limit();
        byte result[] = new byte[length];
        System.arraycopy(bb.array(), bb.arrayOffset(), result, 0, length);
        return result;
    }
    
    /**
     * 字节数组转字符串
     * @param b
     * @return
     */
    public static String toString(byte b[])
    {
        return toString(b, "UTF-8");
    }

    public static String toString(byte b[], String code)
    {
        if (b == null)
            return null;
        else
            return toString(b, 0, b.length, code);
    }
    
    public static String toString(byte b1[], String sep, byte b2[])
    {
        return (new StringBuilder()).append(toString(b1, 0, b1.length))
            .append(sep)
            .append(toString(b2, 0, b2.length))
            .toString();
    }
    
    /**
     * 字节数组转字符串
     * @param b
     * @param off
     * @param len
     * @return
     */
    public static String toString(byte b[], int off, int len)
    {
        return toString(b, off, len, "UTF-8");
    }
    
    public static String toString(byte b[], int off, int len, String code)
    {
        if (b == null)
            return null;
        if (len == 0)
            return "";
        try
        {
            return new String(b, off, len, code);
        }
        catch (UnsupportedEncodingException e)
        {
        }
        return null;
    }
    
    public static String toStringBinary(byte b[])
    {
        if (b == null)
            return "null";
        else
            return toStringBinary(b, 0, b.length);
    }
    
    public static String toStringBinary(ByteBuffer buf)
    {
        if (buf == null)
            return "null";
        else
            return toStringBinary(buf.array(), buf.arrayOffset(), buf.limit());
    }
    
    public static String toStringBinary(byte b[], int off, int len)
    {
        StringBuilder result = new StringBuilder();
        for (int i = off; i < off + len; i++)
        {
            int ch = b[i] & 255;
            if (ch >= 48 && ch <= 57 || ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122
                || " `~!@#$%^&*()-_=+[]{}|;:'\",.<>/?".indexOf(ch) >= 0)
                result.append((char)ch);
            else
                result.append(String.format("\\x%02X", new Object[] {Integer.valueOf(ch)}));
        }
        
        return result.toString();
    }
    
    private static boolean isHexDigit(char c)
    {
        return c >= 'A' && c <= 'F' || c >= '0' && c <= '9';
    }
    

    public static String byteArrayToHex(byte[] resultByteArray)
    {
        if(resultByteArray == null) return null;
        int bytelength = resultByteArray.length;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};// 用来将字节转换成16进制表示的字符
        
        // 用字节表示就是 16 个字节
        char str[] = new char[bytelength * 2];// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
        // 进制需要 32 个字符
        int k = 0;// 表示转换结果中对应的字符位置
        for (int i = 0; i < bytelength; i++)
        {// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
         // 进制字符的转换
            byte byte0 = resultByteArray[i];// 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换,// >>>
            // 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换
        }
        return new String(str);// 换后的结果转换为字符串
    }

    public static byte[] HexTobyteArray(String Hex)
    {
        if(Hex == null) return null;
        char hexDigits[] = Hex.toCharArray();
        int endpos = hexDigits.length;
        if((hexDigits.length % 2) == 1) endpos--;
        byte[] result = new byte[hexDigits.length/2];
        for(int i=0; i<endpos; i=i+2)
        {
            int h = i;
            int l = i+1;
            result[i/2]=(byte)(HexTobyte(hexDigits[h]) << 4 | HexTobyte(hexDigits[l]));
        }
        return result;
    }
    
    public static byte HexTobyte(char H)
    {
        if(H == '0')
        {
            return 0;
        }
        else if(H=='1')
        {
            return 1;
        }
        else if(H=='2')
        {
            return 2;
        }
        else if(H=='3')
        {
            return 3;
        }
        else if(H=='4')
        {
            return 4;
        }
        else if(H=='5')
        {
            return 5;
        }
        else if(H=='6')
        {
            return 6;
        }
        else if(H=='7')
        {
            return 7;
        }
        else if(H=='8')
        {
            return 8;
        }
        else if(H=='9')
        {
            return 9;
        }
        else if(H=='a')
        {
            return 10;
        }
        else if(H=='A')
        {
            return 10;
        }
        else if(H=='b')
        {
            return 11;
        }
        else if(H=='B')
        {
            return 11;
        }
        else if(H=='c')
        {
            return 12;
        }
        else if(H=='C')
        {
            return 12;
        }
        else if(H=='d')
        {
            return 13;
        }
        else if(H=='D')
        {
            return 13;
        }
        else if(H=='e')
        {
            return 14;
        }
        else if(H=='E')
        {
            return 14;
        }
        else if(H=='f')
        {
            return 15;
        }
        else if(H=='F')
        {
            return 15;
        }
        else
        {
            return 0;
        }
    }
    
    
//    public static byte toBinaryFromHex(byte ch)
//    {
//        if (ch >= 65 && ch <= 70)
//            return (byte)(10 + (byte)(ch - 65));
//        else
//            return (byte)(ch - 48);
//    }
    
    /**
     * 转换unicode编码到byte，
     * "\\x81\\xA7\\xB4\\xF7\\x00\\x07\\x38\\x63\\x78\\x7F"
     * @param unicode
     * @return
     */
    public static byte[] unicodeToByteArray(String unicode)
    {
        byte b[] = new byte[unicode.length()];
        int size = 0;
        for (int i = 0; i < unicode.length(); i++)
        {
            char ch = unicode.charAt(i);
            if (ch == '\\' && unicode.length() > i + 1 && unicode.charAt(i + 1) == 'x')
            {
                char hd1 = unicode.charAt(i + 2);
                char hd2 = unicode.charAt(i + 3);
                if (isHexDigit(hd1) && isHexDigit(hd2))
                {
                    byte d = (byte)((HexTobyte(hd1) << 4) + HexTobyte(hd2));
                    b[size++] = d;
                    i += 3;
                }
            }
            else
            {
                b[size++] = (byte)ch;
            }
        }
        
        byte b2[] = new byte[size];
        System.arraycopy(b, 0, b2, 0, size);
        return b2;
    }
    
    public static byte[] toBytes(String s, String code)
    {
        try
        {
            if(s == null)
                return null;
            else
                return s.getBytes(code);
        }
        catch (UnsupportedEncodingException e)
        {
        }
        return null;
    }
    public static byte[] toBytes(String s)
    {
        return toBytes(s, "UTF-8");
    }

    public static byte[] toBytes(String s, int length, String code)
    {
        byte[] r = new byte[length];
        try
        {
            if(s != null)
            {
                byte[] t = s.getBytes(code);
                if(t.length == length)
                    r = t;
                else  if(t.length > length)
                    System.arraycopy(t, 0, r, 0, length);
                else
                {
                    System.arraycopy(t, 0, r, 0, t.length);
                    for(int i = t.length; i<=length-1; i++)
                    {
                        r[i] = ' ';
                    }
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
        }
        return r;
    }
    
    public static byte[] toBytes(String s, int length)
    {
        return toBytes(s, length, "UTF-8");
    }
    
    public static byte[] toBytes(Date d)
    {
        if (d == null)
            return null;
        else
            return toBytes(d.getTime());
    }
    
    public static byte[] toBytes(Byteable d)
    {
        if (d == null)
            return null;
        return d.tobyte();
    }
    
    public static byte[] toBytes(boolean b)
    {
        return (new byte[] {((byte)(b ? -1 : 0))});
    }
    
    public static byte[] toBytes(Boolean b)
    {
        if(b == null)
            return null;
        else
            return toBytes(b.booleanValue());
    }
    
    public static boolean toBoolean(byte b[])
    {
        if (b.length != 1)
            throw new IllegalArgumentException((new StringBuilder()).append("Array has wrong size: ")
                .append(b.length)
                .toString());
        else
            return b[0] != 0;
    }
    
    public static byte[] toBytes(long val)
    {
        byte b[] = new byte[8];
        for (int i = 7; i > 0; i--)
        {
            b[i] = (byte)(int)val;
            val >>>= 8;
        }
        
        b[0] = (byte)(int)val;
        return b;
    }

    public static byte[] toBytes(Long val)
    {
        if(val == null)
            return null;
        else
            return toBytes(val.longValue());
    }
    
    public static byte[] toCompBytes(long val)
    {
        val = val + Long.MAX_VALUE + 1;
        byte b[] = new byte[8];
        for (int i = 7; i > 0; i--)
        {
            b[i] = (byte)(int)val;
            val >>>= 8;
        }
        
        b[0] = (byte)(int)val;
        return b;
    }
    
    public static byte[] toSortBytes(Date date)
    {
        if (date == null)
            return toCompBytes(Long.MAX_VALUE);
        else
        {
            /*
             * TODO 正确的方法，待合适时修正
            byte[] tmp = toCompBytes(date.getTime());
            for(int i = 0;i<result.length; i++)
            {
                result[i] = (byte)~tmp[i];
            }
            return result;
            */
            return toCompBytes(Long.MAX_VALUE - date.getTime());
        }
    }
    
    public static byte[] toCompBytes(double val)
    {
        long val1;
        if (val < 0)
            val1 = Double.doubleToLongBits(1.0 + val);
        else
            val1 = Double.doubleToLongBits(val * -1);
        byte b[] = new byte[8];
        for (int i = 7; i > 0; i--)
        {
            b[i] = (byte)(int)val1;
            val1 >>>= 8;
        }
        
        b[0] = (byte)(int)val1;
        return b;
    }
    
    public static long toLong(byte bytes[])
    {
        return toLong(bytes, 0, 8);
    }
    
    public static long toLong(byte bytes[], int offset)
    {
        return toLong(bytes, offset, 8);
    }
    
    public static long toLong(byte bytes[], int offset, int length)
    {
        if (length != 8 || offset + length > bytes.length)
            throw explainWrongLengthOrOffset(bytes, offset, length, 8);
        long l = 0L;
        for (int i = offset; i < offset + length; i++)
        {
            l <<= 8;
            l ^= bytes[i] & 255;
        }
        
        return l;
    }

    public static Date toDate(byte[] bytes)
    {
        return toDate(bytes, 0);
    }

    public static Date toDate(byte[] bytes, int offset)
    {
        if (bytes == null)
            return null;
        else
        {
            long t = toLong(bytes, offset);
            return new Date(t);
        }
    }
    
    private static IllegalArgumentException explainWrongLengthOrOffset(byte bytes[], int offset, int length,
        int expectedLength)
    {
        String reason;
        if (length != expectedLength)
            reason =
                (new StringBuilder()).append("Wrong length: ")
                    .append(length)
                    .append(", expected ")
                    .append(expectedLength)
                    .toString();
        else
            reason =
                (new StringBuilder()).append("offset (")
                    .append(offset)
                    .append(") + length (")
                    .append(length)
                    .append(") exceed the")
                    .append(" capacity of the array: ")
                    .append(bytes.length)
                    .toString();
        return new IllegalArgumentException(reason);
    }
    
    public static int putLong(byte bytes[], int offset, long val)
    {
        if (bytes.length - offset < 8)
            throw new IllegalArgumentException((new StringBuilder()).append("Not enough room to put a long at offset ")
                .append(offset)
                .append(" in a ")
                .append(bytes.length)
                .append(" byte array")
                .toString());
        for (int i = offset + 7; i > offset; i--)
        {
            bytes[i] = (byte)(int)val;
            val >>>= 8;
        }
        
        bytes[offset] = (byte)(int)val;
        return offset + 8;
    }
    
    public static float toFloat(byte bytes[])
    {
        return toFloat(bytes, 0);
    }
    
    public static float toFloat(byte bytes[], int offset)
    {
        return Float.intBitsToFloat(toInt(bytes, offset, 4));
    }
    
    public static int putFloat(byte bytes[], int offset, float f)
    {
        return putInt(bytes, offset, Float.floatToRawIntBits(f));
    }
    
    public static byte[] toBytes(float f)
    {
        return toBytes(Float.floatToRawIntBits(f));
    }
    
    public static byte[] toBytes(Float f)
    {
        if(f == null)
            return null;
        else
            return toBytes(f.floatValue());
    }
    
    public static double toDouble(byte bytes[])
    {
        return toDouble(bytes, 0);
    }
    
    public static double toDouble(byte bytes[], int offset)
    {
        return Double.longBitsToDouble(toLong(bytes, offset, 8));
    }
    
    public static int putDouble(byte bytes[], int offset, double d)
    {
        return putLong(bytes, offset, Double.doubleToLongBits(d));
    }
    
    public static byte[] toBytes(double d)
    {
        return toBytes(Double.doubleToLongBits(d));
        //        return toBytes(Double.doubleToRawLongBits(d));
    }
    
    public static byte[] toBytes(Double d)
    {
        if(d == null)
            return null;
        else
            return toBytes(d.doubleValue());
    }
    
    public static byte[] toBytes(int val)
    {
        byte b[] = new byte[4];
        for (int i = 3; i > 0; i--)
        {
            b[i] = (byte)val;
            val >>>= 8;
        }
        
        b[0] = (byte)val;
        return b;
    }
    
    public static byte[] toBytes(Integer val)
    {
        if(val == null)
            return null;
        else
            return toBytes(val.intValue());
    }
    
    public static int toInt(byte bytes[])
    {
        return toInt(bytes, 0, 4);
    }
    
    public static int toInt(byte bytes[], int offset)
    {
        return toInt(bytes, offset, 4);
    }
    
    public static int toInt(byte bytes[], int offset, int length)
    {
        if (length != 4 || offset + length > bytes.length)
            throw explainWrongLengthOrOffset(bytes, offset, length, 4);
        int n = 0;
        for (int i = offset; i < offset + length; i++)
        {
            n <<= 8;
            n ^= bytes[i] & 255;
        }
        
        return n;
    }
    
    public static int putInt(byte bytes[], int offset, int val)
    {
        if (bytes.length - offset < 4)
            throw new IllegalArgumentException((new StringBuilder()).append("Not enough room to put an int at offset ")
                .append(offset)
                .append(" in a ")
                .append(bytes.length)
                .append(" byte array")
                .toString());
        for (int i = offset + 3; i > offset; i--)
        {
            bytes[i] = (byte)val;
            val >>>= 8;
        }
        
        bytes[offset] = (byte)val;
        return offset + 4;
    }
    
    public static byte[] toBytes(short val)
    {
        byte b[] = new byte[2];
        b[1] = (byte)val;
        val >>= 8;
        b[0] = (byte)val;
        return b;
    }
    
    public static byte[] toBytes(Short val)
    {
        if(val == null)
            return null;
        else
            return toBytes(val.shortValue());
    }

    public static byte[] toBytes(byte val)
    {
        byte b[] = new byte[1];
        b[0] = val;
        return b;
    }
    
    public static byte[] toBytes(Byte val)
    {
        if(val == null)
            return null;
        else
            return toBytes(val.byteValue());
    }
    
    public static short toShort(byte bytes[])
    {
        return toShort(bytes, 0, 2);
    }
    
    public static short toShort(byte bytes[], int offset)
    {
        return toShort(bytes, offset, 2);
    }
    
    public static short toShort(byte bytes[], int offset, int length)
    {
        if (length != 2 || offset + length > bytes.length)
        {
            throw explainWrongLengthOrOffset(bytes, offset, length, 2);
        }
        else
        {
            short n = 0;
            n ^= bytes[offset] & 255;
            n <<= 8;
            n ^= bytes[offset + 1] & 255;
            return n;
        }
    }
    
    public static byte[] getBytes(ByteBuffer buf)
    {
        int savedPos = buf.position();
        byte newBytes[] = new byte[buf.remaining()];
        buf.get(newBytes);
        buf.position(savedPos);
        return newBytes;
    }
    
    public static int putShort(byte bytes[], int offset, short val)
    {
        if (bytes.length - offset < 2)
        {
            throw new IllegalArgumentException(
                (new StringBuilder()).append("Not enough room to put a short at offset ")
                    .append(offset)
                    .append(" in a ")
                    .append(bytes.length)
                    .append(" byte array")
                    .toString());
        }
        else
        {
            bytes[offset + 1] = (byte)val;
            val >>= 8;
            bytes[offset] = (byte)val;
            return offset + 2;
        }
    }
    
    public static byte[] toBytes(BigDecimal val)
    {
        byte valueBytes[] = val.unscaledValue().toByteArray();
        byte result[] = new byte[valueBytes.length + 4];
        int offset = putInt(result, 0, val.scale());
        putBytes(result, offset, valueBytes, 0, valueBytes.length);
        return result;
    }
    
    public static BigDecimal toBigDecimal(byte bytes[])
    {
        return toBigDecimal(bytes, 0, bytes.length);
    }
    
    public static BigDecimal toBigDecimal(byte bytes[], int offset, int length)
    {
        if (bytes == null || length < 5 || offset + length > bytes.length)
        {
            return null;
        }
        else
        {
            int scale = toInt(bytes, offset);
            byte tcBytes[] = new byte[length - 4];
            System.arraycopy(bytes, offset + 4, tcBytes, 0, length - 4);
            return new BigDecimal(new BigInteger(tcBytes), scale);
        }
    }
    
    public static int putBigDecimal(byte bytes[], int offset, BigDecimal val)
    {
        if (bytes == null)
        {
            return offset;
        }
        else
        {
            byte valueBytes[] = val.unscaledValue().toByteArray();
            byte result[] = new byte[valueBytes.length + 4];
            offset = putInt(result, offset, val.scale());
            return putBytes(result, offset, valueBytes, 0, valueBytes.length);
        }
    }
    
    public static byte[] add(byte a[], byte b[], byte c[])
    {
        byte result[] = new byte[a.length + b.length + c.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        System.arraycopy(c, 0, result, a.length + b.length, c.length);
        return result;
    }
    
    public static byte[] head(byte a[], int length)
    {
        if (a.length < length)
        {
            return null;
        }
        else
        {
            byte result[] = new byte[length];
            System.arraycopy(a, 0, result, 0, length);
            return result;
        }
    }
    
    public static byte[] tail(byte a[], int length)
    {
        if (a.length < length)
        {
            return null;
        }
        else
        {
            byte result[] = new byte[length];
            System.arraycopy(a, a.length - length, result, 0, length);
            return result;
        }
    }
    
    public static int hashCode(byte bytes[], int offset, int length)
    {
        int hash = 1;
        for (int i = offset; i < offset + length; i++)
            hash = 31 * hash + bytes[i];
        
        return hash;
    }
    
    public static byte[][] toByteArrays(String t[])
    {
        byte result[][] = new byte[t.length][];
        for (int i = 0; i < t.length; i++)
            result[i] = toBytes(t[i]);
        
        return result;
    }
    
    public static byte[][] toByteArrays(String column)
    {
        return toByteArrays(toBytes(column));
    }
    
    public static byte[][] toByteArrays(byte column[])
    {
        byte result[][] = new byte[1][];
        result[0] = column;
        return result;
    }
    
    public static byte[] incrementBytes(byte value[], long amount)
    {
        byte val[] = value;
        if (val.length < 8)
        {
            byte newvalue[];
            if (val[0] < 0)
                newvalue = (new byte[] {-1, -1, -1, -1, -1, -1, -1, -1});
            else
                newvalue = new byte[8];
            System.arraycopy(val, 0, newvalue, newvalue.length - val.length, val.length);
            val = newvalue;
        }
        else if (val.length > 8)
            throw new IllegalArgumentException((new StringBuilder()).append("Increment Bytes - value too big: ")
                .append(val.length)
                .toString());
        if (amount == 0L)
            return val;
        if (val[0] < 0)
            return binaryIncrementNeg(val, amount);
        else
            return binaryIncrementPos(val, amount);
    }
    
    private static byte[] binaryIncrementPos(byte value[], long amount)
    {
        long amo = amount;
        int sign = 1;
        if (amount < 0L)
        {
            amo = -amount;
            sign = -1;
        }
        for (int i = 0; i < value.length; i++)
        {
            int cur = ((int)amo % 256) * sign;
            amo >>= 8;
            int val = value[value.length - i - 1] & 255;
            int total = val + cur;
            if (total > 255)
            {
                amo += sign;
                total %= 256;
            }
            else if (total < 0)
                amo -= sign;
            value[value.length - i - 1] = (byte)total;
            if (amo == 0L)
                return value;
        }
        
        return value;
    }
    
    private static byte[] binaryIncrementNeg(byte value[], long amount)
    {
        long amo = amount;
        int sign = 1;
        if (amount < 0L)
        {
            amo = -amount;
            sign = -1;
        }
        for (int i = 0; i < value.length; i++)
        {
            int cur = ((int)amo % 256) * sign;
            amo >>= 8;
            int val = (~value[value.length - i - 1] & 255) + 1;
            int total = cur - val;
            if (total >= 0)
                amo += sign;
            else if (total < -256)
            {
                amo -= sign;
                total %= 256;
            }
            value[value.length - i - 1] = (byte)total;
            if (amo == 0L)
                return value;
        }
        
        return value;
    }
    
    public static void writeStringFixedSize(DataOutput out, String s, int size)
        throws IOException
    {
        byte b[] = toBytes(s);
        if (b.length > size)
            throw new IOException((new StringBuilder()).append("Trying to write ")
                .append(b.length)
                .append(" bytes (")
                .append(toStringBinary(b))
                .append(") into a field of length ")
                .append(size)
                .toString());
        out.writeBytes(s);
        for (int i = 0; i < size - s.length(); i++)
            out.writeByte(0);
        
    }
    
    public static String readStringFixedSize(DataInput in, int size)
        throws IOException
    {
        byte b[] = new byte[size];
        in.readFully(b);
        int n;
        for (n = b.length; n > 0 && b[n - 1] == 0; n--)
            ;
        return toString(b, 0, n);
    }
    
    public static byte[] connectListByteArray(List<byte[]> values)
    {
        if (values.size() == 0)
        {
            return new byte[0];
        }
        else
        {
            int length = 0;
            for (byte[] b : values)
            {
                length = length + b.length;
            }
            byte[] indexb = new byte[length];
            int pos = 0;
            for (int i = 0; i < values.size(); i++)
            {
                System.arraycopy(values.get(i), 0, indexb, pos, values.get(i).length);
                pos = pos + values.get(i).length;
            }
            return indexb;
        }
    }
    
    public static byte[] connectListByteArrayByZero(List<byte[]> values)
    {
        if (values.size() == 0)
        {
            return new byte[0];
        }
        else
        {
            int length = 0;
            for (byte[] b : values)
            {
                length = length + b.length + 1;
            }
            byte[] indexb = new byte[length - 1];
            System.arraycopy(values.get(0), 0, indexb, 0, values.get(0).length);
            int pos = values.get(0).length + 1;
            for (int i = 1; i < values.size(); i++)
            {
                System.arraycopy(values.get(i), 0, indexb, pos, values.get(i).length);
                pos = pos + values.get(i).length + 1;
            }
            return indexb;
        }
    }
    
    public static final int SIZEOF_BOOLEAN = 1;
    
    public static final int SIZEOF_BYTE = 1;
    
    public static final int SIZEOF_CHAR = 2;
    
    public static final int SIZEOF_DOUBLE = 8;
    
    public static final int SIZEOF_FLOAT = 4;
    
    public static final int SIZEOF_INT = 4;
    
    public static final int SIZEOF_LONG = 8;
    
    public static final int SIZEOF_SHORT = 2;
    
    public static final int ESTIMATED_HEAP_TAX = 16;

}
