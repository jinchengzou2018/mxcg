package com.mxcg.common.util.security;


import com.mxcg.common.util.Bytes;

public class MacEcbUtils
{
    
    private static byte byteXOR(byte src, byte src1)
    {
        return (byte)((src & 0xFF) ^ (src1 & 0xFF));
    }
    
    protected static void bytesXOR(byte[] src, byte[] src1, byte[] dest)
    {
        for (int i = 0; i < dest.length; i++)
        {
            byte b = i >= src.length ? 0 : src[i];
            byte b1 = i >= src1.length ? 0 : src1[i];
            dest[i] = byteXOR(b, b1);
        }
    }
    
    /**
     * mac计算,数据不为8的倍数，需要补0，将数据8个字节进行异或，再将异或的结果与下一个8个字节异或，一直到最后，将异或后的数据进行DES计算
     * 
     * @param key
     * @param Input
     * @return
     */
    private static byte[] clacMac(byte[] in, int start, int end)
    {
        int index = start;
        byte[] oper1 = new byte[8];
        byte[] tmp = new byte[8];
        while(index < end)
        {
            for(int i = 0; i<tmp.length; i++)
            {
                tmp[i] = index+i < end ?  in[index+i]:0;
            }
            index = index + tmp.length;
            bytesXOR(oper1, tmp, oper1);
        }
        return calc(oper1);
    }
    
    protected static byte[] calc(byte[] oper1)
    {
        // 将异或运算后的最后8个字节（RESULT BLOCK）转换成16个HEXDECIMAL：
        byte[] resultBlock = Bytes.byteArrayToHex(oper1).getBytes();
        // 取前8个字节用mkey1，DES加密
        byte[] front8 = new byte[8];
        System.arraycopy(resultBlock, 0, front8, 0, 8);
        byte[] behind8 = new byte[8];
        System.arraycopy(resultBlock, 8, behind8, 0, 8);
        
        
        byte[] desfront8 = DesUtils.macEncrypt(front8);
        // 将加密后的结果与后8 个字节异或：
        byte[] resultXOR = new byte[8];
        bytesXOR(desfront8, behind8, resultXOR);
        
        // 用异或的结果TEMP BLOCK 再进行一次单倍长密钥算法运算
        byte[] buff = DesUtils.macEncrypt(resultXOR);
        
        byte[] retBuf = new byte[4];
        System.arraycopy(buff, 0, retBuf, 0, 4);
        return retBuf;
    }

    public static int getMac(byte[] content)
    {
        byte[] b = clacMac(content, 0, content.length);
        return Bytes.toInt(b);
    }
    
    public static String getMacByHexString(String content)
    {
        byte[] contentbyte = (Bytes.HexTobyteArray(content));
        byte[] b = clacMac(contentbyte, 0, contentbyte.length);
        return Bytes.byteArrayToHex(b);
    }

    public static String getMac(String content)
    {
        byte[] contentbyte = (Bytes.toBytes(content));
        byte[] b = clacMac(contentbyte, 0, contentbyte.length);
        return Bytes.byteArrayToHex(b);
    }
    
    public static void main(String[] args)
    {
        String string = getMacByHexString(
            "00010000008801000000000144430030010000020000013935303230303032333235303030303030303030303030021111111111111111111100000000003400201612071806500000000000000000000000000000000000000000");
        System.err.println(string);
    }

}