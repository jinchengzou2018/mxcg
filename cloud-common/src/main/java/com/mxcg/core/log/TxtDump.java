package com.mxcg.core.log;

/**
 * 
 * 文本文件输出
 * <p>
 * 自动按日期分割
 * </p>
 */
public class TxtDump extends Dump
{
    private static TxtDump instance = new TxtDump();
    
    private TxtDump()
    {
        super();
    }
    
    public static void init(String logfilepath, String logfile)
    {
        getInstance().logfileDirection = logfilepath + "/";
        getInstance().logfileName = logfile;
    }
    
    public static TxtDump getInstance()
    {
        return instance;
    }
    
    public static boolean write(String logfile, String msg)
    {
        return getInstance().setLogMsg(logfile, msg);
    }
    
    public static String getLogfileDirection()
    {
        return instance.logfileDirection;
    }

    @Override
    protected String ext()
    {
        return ".txt";
    }
}
