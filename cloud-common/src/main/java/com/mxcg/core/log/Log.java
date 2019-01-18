package com.mxcg.core.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log extends Dump implements LogInterface {
    
    private LogConfig config;
    
    /**
     * @param logfilepath 日志路径
     * @param logfile 日志文件名
     */
    public void init(String logfilepath, String logfile) {
        logfileDirection = logfilepath + "/";
        logfileName = logfile;
    }
    
    public void init(String logfile) {
        logfileName = logfile;
    }
    
    private String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
    
    /**
     * 输出文字，在所有级别上输出
     * 谨慎使用，以免输出过多文本，常用于日志开始记录，服务启动中止之类分界信息
     * @param msg 文本信息
     */
    public boolean outString(String msg) {
        return outString(null, msg);
    }
    
    /**
     * 输出文字，在所有级别上输出
     * 谨慎使用，以免输出过多文本，常用于日志开始记录，服务启动中止之类分界信息
     * @param logfile
     * @param msg
     */
    public boolean outString(String logfile, String msg) {
        if (msg != null) {
            String s = getTime() + " |String| >>>>:" + msg;
            System.out.println(s);
            return setLogMsg(logfile, "String", msg);
        }
        return true;
    }
    
    /**
     * 输出错误信息
     * 在所有级别中显示，用于提示致命错误，不宜多用
     * @param logfile
     * @param msg
     */
    public boolean outErr(String logfile, String msg) {
        if (msg != null) {
            System.out.println(getTime() + " |Error | >>>>:" + msg);
            return setLogMsg(logfile, "Error ", msg);
        }
        return true;
    }
    
    public boolean outWaring(String logfile, String msg) {
        if (msg != null) {
            System.out.println(getTime() + " |Waring| >>>>:" + msg);
            return setLogMsg(logfile, "Waring", msg);
        }
        return true;
    }
    
    /**
     * 输出错误信息
     * 在所有级别中显示，用于提示致命错误，不宜多用
     * @param msg
     */
    public boolean outErr(String msg) {
        return outErr(null, msg);
    }
    
    public boolean outWaring(String msg) {
        return outWaring(null, msg);
    }
    
    /**
     * 打印异常信息
     * 在Detail及以下级别打印异常message
     * Detail以上级别打印异常堆栈
     * @param msg
     * @param e
     */
    public boolean outException(String msg, Throwable e) {
        return outException(null, msg, e);
    }
    
    /**
     * 打印异常信息
     * 在Detail及以下级别打印异常message
     * Detail以上级别打印异常堆栈
     * @param e
     */
    public boolean outException(Throwable e) {
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
    public boolean outException(String logfile, String msg, Throwable e) {
        if (e != null) {
            String exceptionmsg = e.getMessage();
            StringBuffer tracks = new StringBuffer();
            tracks.append(System.lineSeparator()).append("[Class]: ").append(e.getClass()).append(System.lineSeparator()).append("[Error]: ")
                .append(msg).append(System.lineSeparator()).append("[Message]: ").append(exceptionmsg).append(System.lineSeparator())
                .append("[StackTrace]: ");
            StackTraceElement[] ele = e.getStackTrace();
            for (int i = 0; i < ele.length; i++) {
                tracks.append(System.lineSeparator()).append("    ").append(ele[i].toString());
            }
            tracks.append(System.lineSeparator()).append("    ......");
            if (consoleLevel().compareTo(LogLevel.Detail) > 0) {
                System.out.println(getTime() + " |Exception| >>>>:" + System.lineSeparator() + tracks.toString());
            } else {
                if (msg != null)
                    System.out
                        .println(getTime() + " |Exception| >>>>:" + msg + " " + e.getClass() + " - " + exceptionmsg);
                else
                    System.out.println(getTime() + " |Exception| >>>>:" + e.getClass() + " - " + exceptionmsg);
            }
            return setLogMsg(logfile, "Exception", tracks.toString());
        }
        return true;
    }
    
    protected LogLevel consoleLevel() {
        if (config != null)
            return config.getConsoleLogLevel();
        else
            return LogLevel.Debug;
    }
    
    protected LogLevel logLevel() {
        if (config != null)
            return config.getLogLevel();
        else
            return LogLevel.Debug;
    }
    
    /**
     * 输出Info信息
     * 在日志级别大于Mini时输出
     * 用于输出系统信息，通知，提示 
     * @param msg
     */
    public boolean outInfo(String msg) {
        return outInfo(null, msg);
    }
    
    /**
     * 输出Info信息
     * 在日志级别大于Mini时输出
     * 用于输出系统信息，通知，提示 
     * @param logfile
     * @param msg
     */
    public boolean outInfo(String logfile, String msg) {
        if ((msg != null) && (consoleLevel().compareTo(LogLevel.Mini) > 0)) {
            System.out.println(getTime() + " | Info | >>>>:" + msg);
        }
        if ((msg != null) && (logLevel().compareTo(LogLevel.Mini) > 0)) {
            return setLogMsg(logfile, " Info ", msg);
        }
        return true;
    }
    
    /**
     * 输出Detail信息
     * 在日志级别大于Info时输出
     * 用于输出信息详细数据
     * @param msg
     */
    public boolean outDetail(String msg) {
        return outDetail(null, msg);
    }
    
    /**
     * 输出Detail信息
     * 在日志级别大于Info时输出
     * 用于输出信息详细数据
     * @param logfile
     * @param msg
     */
    public boolean outDetail(String logfile, String msg) {
        if ((msg != null) && (consoleLevel().compareTo(LogLevel.Info) > 0)) {
            System.out.println(getTime() + " |Detail| >>>>:" + msg);
        }
        if ((msg != null) && (logLevel().compareTo(LogLevel.Info) > 0))
            return setLogMsg(logfile, "Detail", msg);
        return true;
    }
    
    /**
     * 输出Debug信息
     * 在日志级别等于Debug时输出
     * 用于输出最详细的数据，用于检查排错，可能会生成大量的日志文件
     * @param msg
     */
    public boolean outDebug(String msg) {
        return outDebug(null, msg);
    }
    
    /**
     * 输出Debug信息
     * 在日志级别等于Debug时输出
     * 用于输出最详细的数据，用于检查排错，可能会生成大量的日志文件
     * @param logfile
     * @param msg
     */
    public boolean outDebug(String logfile, String msg) {
        if ((msg != null) && (consoleLevel().compareTo(LogLevel.Detail) > 0)) {
            System.out.println(getTime() + " |Debug | >>>>:" + msg);
        }
        if ((msg != null) && (logLevel().compareTo(LogLevel.Detail) > 0))
            return setLogMsg(logfile, "Debug ", msg);
        return true;
    }
    
    public String getLogfileDirection() {
        return logfileDirection;
    }
    
    @Override
    protected String prex() {
        String time = getTime() + "  ";
        return time;
    }
    
    @Override
    protected String ext() {
        return ".log";
    }
    
    public void setConfig(LogConfig config) {
        this.config = config;
    }
    
}
