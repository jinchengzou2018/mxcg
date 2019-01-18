package com.mxcg.common.util;

public interface Byteable
{
    /**
     * 将类转为byte[]
     * @return
     */
    public abstract byte[] tobyte();
    /**
     * 转换byte[]到类
     * @param values
     */
    public abstract void frombyte(byte[] values, int pos, int length);
    public abstract void frombyte(byte[] values, int pos);
    public abstract void frombyte(byte[] values);
}
