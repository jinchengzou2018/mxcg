package com.mxcg.core.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public abstract class Dump
{
    private LinkedBlockingQueue<Logmsg> logQueue = new LinkedBlockingQueue<Logmsg>(10000);
    
    protected String logfileDirection = "";
    
    protected String logfileName = "";
    
    protected String datastr = "";
    
    private boolean splitbyDay = true;
    
    private boolean closeing = false;
    
    private boolean closed = false;
    
    public Dump()
    {
        String assemblylogfile = System.getProperty("user.dir");
        String assemblylogPath = assemblylogfile + "/";
        logfileDirection = assemblylogPath;
        Thread t = new WriteThread();
        t.setDaemon(true);
        t.start();
    }
    
    protected final boolean setLogMsg(String logfile, String msg)
    {
        if (closeing)
            return false;
        Logmsg m = new Logmsg(logfile, msg);
        try
        {
            logQueue.put(m);
            return true;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    protected final boolean setLogMsg(String logfile, String lvl, String msg)
    {
        if (closeing)
            return false;
        Logmsg m = new Logmsg(logfile, "|" + lvl + "| >>>>:" + msg);
        try
        {
            logQueue.put(m);
            return true;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    class WriteThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                Map<String, LinkedList<byte[]>> msgmap = new HashMap<String, LinkedList<byte[]>>();
                try
                {
                    Logmsg msg = logQueue.poll(1, TimeUnit.SECONDS);
                    if(msg != null)
                        putmsg(msgmap, msg);
                    for (int i = 0; i < 9999; i++)
                    {
                        Logmsg msg1 = logQueue.poll();
                        if (msg1 == null)
                            break;
                        else
                            putmsg(msgmap, msg1);
                    }
                }
                catch (InterruptedException e1)
                {
                    e1.printStackTrace();
                }
                
                if (msgmap.size() > 0)
                {
                    FileOutputStream out = null;
                    for (String filepath : msgmap.keySet())
                    {
                        boolean done = false;
                        while (!done)
                        {
                            try
                            {
                                if (filepath != "")
                                {
                                    File file = new File(filepath);
                                    if (!file.getParentFile().exists())
                                    {
                                        file.getParentFile().mkdirs();
                                    }
                                    out = new FileOutputStream(filepath + ext(), true);
                                    for (byte[] buff : msgmap.get(filepath))
                                    {
                                        out.write(buff);
                                    }
                                    out.flush();
                                }
                                done = true;
                            }
                            //文件被占用
                            catch (FileNotFoundException e)
                            {
                                done = false;
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                done = true;
                            }
                            finally
                            {
                                if (out != null)
                                    try
                                    {
                                        out.close();
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                out = null;
                            }
                            //如果文件被占用，延时1秒重试
                            if (!done)
                            {
                                System.out.println(filepath + ext() + "文件建立失败，1秒后重试");
                                try
                                {
                                    sleep(1000);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                if (closeing && logQueue.size() == 0)
                {
                    closed = true;
                    break;
                }
            }
        }
        
        private void putmsg(Map<String, LinkedList<byte[]>> msgmap, Logmsg msg)
        {
            String path = getFilePath(msg.file);
            LinkedList<byte[]> msglist = msgmap.get(path);
            if (msglist == null)
            {
                msglist = new LinkedList<byte[]>();
                msgmap.put(path, msglist);
            }
            msglist.add(makebuff(msg.msg));
        }
    }
    
    protected String prex()
    {
        return "";
    }
    
    private byte[] makebuff(String msg)
    {
        byte[] result = null;
        try
        {
            String s = prex() + msg + System.lineSeparator();
            result = s.getBytes("UTF8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = (prex() + msg + System.lineSeparator()).getBytes();
        }
        return result;
    }
    
    protected abstract String ext();
    
    private String getFilePath(String file)
    {
        String logfile = "";
        datastr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (file != null)
        {
            if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") == 0)
            {
                if (file.substring(1, 2).equals(":"))
                {
                    logfile = file;
                }
                else
                {
                    logfile = logfileDirection + file;
                }
            }
            else
            {
                if (file.substring(0, 1).equals("/"))
                {
                    logfile = file;
                }
                else
                {
                    logfile = logfileDirection + file;
                }
            }
        }
        else
        {
            logfile = logfileDirection + logfileName;
        }
        if (logfile != "")
        {
            if (splitbyDay)
                return logfile + datastr;
            else
                return logfile;
        }
        else
        {
            return logfile;
        }
    }
    
    class Logmsg
    {
        private String file;
        
        private String msg;
        
        private Logmsg(String m)
        {
            file = null;
            msg = m;
        }
        
        private Logmsg(String f, String m)
        {
            file = f;
            msg = m;
        }
    }
    
    public boolean isSplitbyDay()
    {
        return splitbyDay;
    }
    
    public void setSplitbyDay(boolean splitbyDay)
    {
        this.splitbyDay = splitbyDay;
    }
    
    public void close()
    {
        this.closeing = true;
        while (!closed)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void setLogfileDirection(String logfileDirection)
    {
        this.logfileDirection = logfileDirection;
    }

    public void setLogfileName(String logfileName)
    {
        this.logfileName = logfileName;
    }
}
