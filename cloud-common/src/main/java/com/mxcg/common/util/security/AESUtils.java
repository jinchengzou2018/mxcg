package com.mxcg.common.util.security;

import com.mxcg.core.log.SimpleLog;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



/**
 * AES算法工具
 *
 */
public final class AESUtils
{
    private static final String SECKETKEY = "poi098&amp;^";
    
    private static final String TEMPKEY = "WHYNOT?";
    
    private static final int AESCONSTANT = 0xFF;
    
    private static final int NUMBER128 = 128;
    
    private static final int NUMBER256 = 256;
    
    private static final int NUMBER16 = 16;
    
    public static final int ONE = 1;
    
    public static final int TWO = 2;
    
    private AESUtils()
    {
        
    }
    
    /**
     * 对数据用AES128算法加密
     * 
     * @param data data
     *            要加密的字符串
     * @param key key
     *            密钥
     * @return 加密后的字符串
     */
    public static String encrypt(String data, String key)
    {
        byte[] decryptText = null;
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(genKey128(key), "AES");
            Cipher cipher;
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            decryptText = cipher.doFinal(data.getBytes());
        }
        catch (Exception e)
        {
            SimpleLog.outException(null, "encrypt error", e);
        }
        return parseByte2HexStr(decryptText);
    }
    
    /**
     * 对AES128算法加密的数据解密
     * 
     * @param data data
     *            要解密的字符串
     * @param key key
     *            密钥
     * @return 解密后的字符串
     */
    public static String decrypt(String data, String key)
    {
        byte[] plainText = null;
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(genKey128(key), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            plainText = cipher.doFinal(parseHexStr2Byte(data));
        }
        catch (Exception e)
        {
            SimpleLog.outException(null, "decrypt error", e);
        }
        return new String(plainText);
    }
    
    private static byte[] genKey(String key)
        throws NoSuchAlgorithmException
    {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(key.getBytes());
        kgen.init(NUMBER256, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }
    
    private static byte[] genKey128(String key)
        throws NoSuchAlgorithmException
    {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(key.getBytes());
        kgen.init(NUMBER128, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }
    
    /**
     * 获取密钥明文
     */
    public static String getKey()
    {
        return decrypt256(SECKETKEY, TEMPKEY);
    }
    
    /**
     * 对AES256算法加密的数据解密
     * 
     * @param data data
     *            要解密的字符串
     * @param key key
     *            密钥
     * @return 解密后的字符串
     */
    private static String decrypt256(String data, String key)
    {
        byte[] plainText = null;
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(genKey(key), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            plainText = cipher.doFinal(Base64.decode(data));
            
        }
        catch (Exception e)
        {
            SimpleLog.outException(null, "decrypt error", e);
        }
        return new String(plainText);
    }
    
    /**
     * 将二进制转换成16进制 
     * @param buf 二进制数组
     * @return String 返回字符串
     */
    private static String parseByte2HexStr(byte buf[])
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++)
        {
            String hex = Integer.toHexString(buf[i] & AESCONSTANT);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase(Locale.getDefault()));
        }
        return sb.toString();
    }
    
    /**
     * 将16进制转换为二进制 
     * @param hexStr 16进制字符串
     * @return byte[]
     */
    private static byte[] parseHexStr2Byte(String hexStr)
    {
        if (hexStr.length() < 1)
        {
            return null;
        }
        byte[] result = new byte[hexStr.length() / TWO];
        for (int i = 0; i < hexStr.length() / TWO; i++)
        {
            int high = Integer.parseInt(hexStr.substring(i * TWO, i * TWO + ONE), NUMBER16);
            int low = Integer.parseInt(hexStr.substring(i * TWO + ONE, i * TWO + TWO), NUMBER16);
            result[i] = (byte)(high * NUMBER16 + low);
        }
        return result;
    }
}
