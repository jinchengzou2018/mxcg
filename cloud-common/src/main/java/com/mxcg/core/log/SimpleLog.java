package com.mxcg.core.log;

/**
 * 
 * 简单的本地日志
 * <p>
 * 单例模式，支持日志分级，通过配置文件和控制台等级，实现日志的控制台和文件输出
 * </p>
 */
public class SimpleLog
{
    private static SimpleLog instance = new SimpleLog();
    
    private Log log;

    private SimpleLog()
    {
        super();
        log = new Log();
        String logfilename =System.getProperty("@appId", "");
        log.setLogfileName("log/"+logfilename+"_");
        log.outInfo("PID = "+System.getProperty("PID", ""));
    }
    
    public static SimpleLog getInstance()
    {
        return instance;
    }
    
    /**
     * @param logfilepath
     *            日志路径
     * @param logfile
     *            日志文件名
     */
    public static void init(String logfilepath, String logfile)
    {
        getInstance().log.setLogfileDirection(logfilepath + "/");
        getInstance().log.setLogfileName(logfile);
    }
    
    public static void init(String logfile)
    {
        getInstance().log.setLogfileName(logfile);
    }
    
    /**
     * 输出文字，在所有级别上输出
     * 谨慎使用，以免输出过多文本，常用于日志开始记录，服务启动中止之类分界信息
     * @param msg 文本信息
     */
    public static boolean outString(String msg)
    {
        return outString(null, msg);
    }
    
    /**
     * 输出文字，在所有级别上输出
     * 谨慎使用，以免输出过多文本，常用于日志开始记录，服务启动中止之类分界信息
     * @param logfile
     * @param msg
     */
    public static boolean outString(String logfile, String msg)
    {
        return getInstance().log.outString(logfile, msg);
    }
    
    /**
     * 输出错误信息
     * 在所有级别中显示，用于提示致命错误，不宜多用
     * @param logfile
     * @param msg
     */
    public static boolean outErr(String logfile, String msg)
    {
        return getInstance().log.outErr(logfile, msg);
    }
    
    public static boolean outWaring(String logfile, String msg)
    {
        return getInstance().log.outWaring(logfile, msg);
    }
    
    /**
     * 输出错误信息
     * 在所有级别中显示，用于提示致命错误，不宜多用
     * @param msg
     */
    public static boolean outErr(String msg)
    {
        return outErr(null, msg);
    }

    public static boolean outWaring(String msg)
    {
        return outWaring(null, msg);
    }
    
    /**
     * 打印异常信息
     * 在Detail及以下级别打印异常message
     * Detail以上级别打印异常堆栈
     * @param msg
     * @param e
     */
    public static boolean outException(String msg, Throwable e)
    {
        return outException(null, msg, e);
    }
    
    /**
     * 打印异常信息
     * 在Detail及以下级别打印异常message
     * Detail以上级别打印异常堆栈
     * @param e
     */
    public static boolean outException(Throwable e)
    {
        return outException(null, e);
    }
    
    /**
     * 打印异常信息
     * 在Detail及以下级别打印异常message
     * Detail以上级别打印异常堆栈
     * @param logfile
     * @param msg
     * @param e
     */
    public static boolean outException(String logfile, String msg, Throwable e)
    {
        return getInstance().log.outException(logfile, msg, e);
    }
    
    /**
     * 输出Info信息
     * 在日志级别大于Mini时输出
     * 用于输出系统信息，通知，提示 
     * @param msg
     */
    public static boolean outInfo(String msg)
    {
        return outInfo(null, msg);
    }
    
    /**
     * 输出Info信息
     * 在日志级别大于Mini时输出
     * 用于输出系统信息，通知，提示 
     * @param logfile
     * @param msg
     */
    public static boolean outInfo(String logfile, String msg)
    {
        return getInstance().log.outInfo(logfile, msg);
    }
    
    /**
     * 输出Detail信息
     * 在日志级别大于Info时输出
     * 用于输出信息详细数据
     * @param msg
     */
    public static boolean outDetail(String msg)
    {
        return outDetail(null, msg);
    }
    
    /**
     * 输出Detail信息
     * 在日志级别大于Info时输出
     * 用于输出信息详细数据
     * @param logfile
     * @param msg
     */
    public static boolean outDetail(String logfile, String msg)
    {
        return getInstance().log.outDetail(logfile, msg);
    }
    
    /**
     * 输出Debug信息
     * 在日志级别等于Debug时输出
     * 用于输出最详细的数据，用于检查排错，可能会生成大量的日志文件
     * @param msg
     */
    public static boolean outDebug(String msg)
    {
        return outDebug(null, msg);
    }
    
    /**
     * 输出Debug信息
     * 在日志级别等于Debug时输出
     * 用于输出最详细的数据，用于检查排错，可能会生成大量的日志文件
     * @param logfile
     * @param msg
     */
    public static boolean outDebug(String logfile, String msg)
    {
        return getInstance().log.outDebug(logfile, msg);
    }
    
    
    public static String getLogfileDirection()
    {
        return instance.log.getLogfileDirection();
    }

    public void setConfig(LogConfig config)
    {
        log.setConfig(config);
    }
    
}
