package com.mxcg.core.log;

public interface LogInterface
{

    /**
     * 输出文字，在所有级别上输出
     * 谨慎使用，以免输出过多文本，常用于日志开始记录，服务启动中止之类分界信息
     * @param msg 文本信息
     */
    boolean outString(String msg);
    
    /**
     * 输出文字，在所有级别上输出
     * 谨慎使用，以免输出过多文本，常用于日志开始记录，服务启动中止之类分界信息
     * @param logfile
     * @param msg
     */
    boolean outString(String logfile, String msg);
    
    /**
     * 输出错误信息
     * 在所有级别中显示，用于提示致命错误，不宜多用
     * @param logfile
     * @param msg
     */
    boolean outErr(String logfile, String msg);
    
    boolean outWaring(String logfile, String msg);
    
    /**
     * 输出错误信息
     * 在所有级别中显示，用于提示致命错误，不宜多用
     * @param msg
     */
    boolean outErr(String msg);

    boolean outWaring(String msg);
    
    /**
     * 打印异常信息
     * 在Detail及以下级别打印异常message
     * Detail以上级别打印异常堆栈
     * @param msg
     * @param e
     */
    boolean outException(String msg, Throwable e);
    
    /**
     * 打印异常信息
     * 在Detail及以下级别打印异常message
     * Detail以上级别打印异常堆栈
     * @param e
     */
    boolean outException(Throwable e);
    
    /**
     * 打印异常信息
     * 在Detail及以下级别打印异常message
     * Detail以上级别打印异常堆栈
     * @param logfile
     * @param msg
     * @param e
     */
    boolean outException(String logfile, String msg, Throwable e);
    
    
    /**
     * 输出Info信息
     * 在日志级别大于Mini时输出
     * 用于输出系统信息，通知，提示 
     * @param msg
     */
    boolean outInfo(String msg);
    
    /**
     * 输出Info信息
     * 在日志级别大于Mini时输出
     * 用于输出系统信息，通知，提示 
     * @param logfile
     * @param msg
     */
    boolean outInfo(String logfile, String msg);
    
    /**
     * 输出Detail信息
     * 在日志级别大于Info时输出
     * 用于输出信息详细数据
     * @param msg
     */
    boolean outDetail(String msg);
    
    /**
     * 输出Detail信息
     * 在日志级别大于Info时输出
     * 用于输出信息详细数据
     * @param logfile
     * @param msg
     */
    boolean outDetail(String logfile, String msg);
    
    /**
     * 输出Debug信息
     * 在日志级别等于Debug时输出
     * 用于输出最详细的数据，用于检查排错，可能会生成大量的日志文件
     * @param msg
     */
    boolean outDebug(String msg);
    
    /**
     * 输出Debug信息
     * 在日志级别等于Debug时输出
     * 用于输出最详细的数据，用于检查排错，可能会生成大量的日志文件
     * @param logfile
     * @param msg
     */
    boolean outDebug(String logfile, String msg);
    
}
