package com.mxcg.common.util.security;

import com.mxcg.common.util.Bytes;
import com.mxcg.core.log.SimpleLog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/**
 * 
 * MD5 工具类
 *
 */
public class MD5
{
    /**
     * 计算byte数组的MD5
     * @param source
     * @return
     */
    public static String getMD5(byte[] source)
    {
        String s = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();// MD5 的计算结果是一个 128 位的长整数，
            s = Bytes.byteArrayToHex(tmp);
        }
        catch (NoSuchAlgorithmException e)
        {
            SimpleLog.outException(e);
        }
        return s;
    }

    /**
     * 计算byte数组的MD5
     * @param source
     * @return
     */
    public static byte[] getMD5byte(byte[] source)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();// MD5 的计算结果是一个 128 位的长整数，
            return tmp;
        }
        catch (NoSuchAlgorithmException e)
        {
            SimpleLog.outException(e);
            return null;
        }
    }

    /**
     * 计算字符串的MD5
     * @param source
     * @return
     */
    public static String getMD5(String source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算BigDecimal的MD5
     * @param source
     * @return
     */
    public static String getMD5(BigDecimal source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算boolean的MD5
     * @param source
     * @return
     */
    public static String getMD5(boolean source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算double的MD5
     * @param source
     * @return
     */
    public static String getMD5(double source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算float的MD5
     * @param source
     * @return
     */
    public static String getMD5(float source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算int的MD5
     * @param source
     * @return
     */
    public static String getMD5(int source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算long的MD5
     * @param source
     * @return
     */
    public static String getMD5(long source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算short的MD5
     * @param source
     * @return
     */
    public static String getMD5(short source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算ByteBuffer的MD5
     * @param source
     * @return
     */
    public static String getMD5(ByteBuffer source)
    {
        byte[] bytes = Bytes.toBytes(source);
        return getMD5(bytes);
    }

    /**
     * 计算InputStream的MD5
     * @param stream
     * @return
     */
    public static String getStreamMD5(InputStream stream)
    {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        DigestInputStream digestInputStream = null;
        String s = null;
        try
        {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            digestInputStream = new DigestInputStream(stream, messageDigest);
            
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0)
                ;
            
            // 获取最终的MessageDigest
            messageDigest = digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            s = Bytes.byteArrayToHex(resultByteArray);
        }
        catch (NoSuchAlgorithmException e)
        {
            SimpleLog.outException(e);
        }
        catch (FileNotFoundException e)
        {
            SimpleLog.outException(e);
        }
        catch (IOException e)
        {
            SimpleLog.outException(e);
        }
        finally
        {
            try
            {
                digestInputStream.close();
                stream.close();
            }
            catch (Exception e)
            {
                SimpleLog.outException(e);
            }
        }
        return s;
    }

    /**
     * 计算文件的MD5
     * @param filename
     * @return
     */
    public static String getFileMD5(String filename)
    {
        FileInputStream fileInputStream = null;
        String s = null;
        try
        {
            fileInputStream = new FileInputStream(filename);
            return getStreamMD5(fileInputStream);
        }
        catch (FileNotFoundException e)
        {
            SimpleLog.outException(e);
        }
        catch (Exception e)
        {
            SimpleLog.outException(e);
        }
        finally
        {
            try
            {
                fileInputStream.close();
            }
            catch (Exception e)
            {
                SimpleLog.outException(e);
            }
        }
        return s;
    }
    
}