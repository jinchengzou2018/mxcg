package com.mxcg.core.log;

/**
 * 
 * 文本文件输出
 *
 */
public class TxtFileWriter extends Dump
{
    public TxtFileWriter()
    {
        super();
        setSplitbyDay(false);
    }

    public boolean write(String filepath, String msg)
    {
        return setLogMsg(filepath, msg);
    }
    
    @Override
    protected String ext()
    {
        return ".txt";
    }
}
