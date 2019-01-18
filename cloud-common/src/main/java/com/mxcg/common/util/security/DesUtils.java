package com.mxcg.common.util.security;

import com.mxcg.common.util.Bytes;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DesUtils
{
    
    private final static String DES = "DES";
    
    private final static String CIPHER_ALGORITHM = "DES/ECB/NoPadding";

    private static final byte[] Mackey = Bytes.HexTobyteArray("C9EEDBDAB5C2BFA8BFC6BCBCB9C9B7DD");
    
    private static Cipher macCipher;
    
    static {

        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        try
        {
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(Mackey);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作,NoPadding为填充方式 默认为PKCS5Padding
            macCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            macCipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        }
        catch (NoSuchAlgorithmException e)
        {
            // LOG.error("算数错误", e);
        }
        catch (InvalidKeyException e)
        {
            // LOG.error("无效key错误", e);
        }
        catch (InvalidKeySpecException e)
        {
            // LOG.error("无效key戳无", e);
        }
        catch (NoSuchPaddingException e)
        {
            // LOG.error("填充错误", e);
        }
    }

    public static byte[] macEncrypt(byte[] src)
    {
        try
        {
            // 正式执行加密操作
            return macCipher.doFinal(src);
        }
        catch (IllegalBlockSizeException e)
        {
            // LOG.error("非法数据块", e);
        }
        catch (BadPaddingException e)
        {
            // LOG.error("错误的填充", e);
        }
        return null;
    }
    
    
    /**
     * 加密
     * 
     * @param src
     *            数据源
     * @param key
     *            密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     * @throws DesException
     */
    public static byte[] encrypt(byte[] src, byte[] key)
    {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        try
        {
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作,NoPadding为填充方式 默认为PKCS5Padding
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(src);
        }
        catch (NoSuchAlgorithmException e)
        {
            // LOG.error("算数错误", e);
        }
        catch (InvalidKeyException e)
        {
            // LOG.error("无效key错误", e);
        }
        catch (InvalidKeySpecException e)
        {
            // LOG.error("无效key戳无", e);
        }
        catch (NoSuchPaddingException e)
        {
            // LOG.error("填充错误", e);
        }
        catch (IllegalBlockSizeException e)
        {
            // LOG.error("非法数据块", e);
        }
        catch (BadPaddingException e)
        {
            // LOG.error("错误的填充", e);
        }
        return null;
    }
    
    /**
     * 生成密钥
     * 
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] initKey()
        throws NoSuchAlgorithmException
    {
        KeyGenerator kg = KeyGenerator.getInstance(DES);
        kg.init(56);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }
    
    /**
     * 解密
     * 
     * @param src
     *            数据源
     * @param key
     *            密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     * @throws DesException
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key)
    {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        try
        {
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 现在，获取数据并解密
            // 正式执行解密操作
            return cipher.doFinal(src);
        }
        catch (NoSuchAlgorithmException e)
        {
            // LOG.error("算数错误", e);
        }
        catch (InvalidKeyException e)
        {
            // LOG.error("无效key错误", e);
        }
        catch (InvalidKeySpecException e)
        {
            // LOG.error("无效key戳无", e);
        }
        catch (NoSuchPaddingException e)
        {
            // LOG.error("填充错误", e);
        }
        catch (IllegalBlockSizeException e)
        {
            // LOG.error("非法数据块", e);
        }
        catch (BadPaddingException e)
        {
            // LOG.error("错误的填充", e);
        }
        return null;
    }
    
}