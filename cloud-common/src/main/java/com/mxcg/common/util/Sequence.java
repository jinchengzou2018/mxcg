package com.mxcg.common.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 序列号生成器
 * 
 * @author  wyw
 * @version  [版本号, 2018年9月14日]
 */
public class Sequence
{
    private int id = 1;
    
    private Lock lock = new ReentrantLock();
    
    public void setId(int id)
    {
        lock.lock();
        try
        {
            this.id = id;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public int current()
    {
        lock.lock();
        try
        {
            return id;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public String currentString()
    {
        int result = current();
        return idFormat.format(result);
    }
    
    public int next()
    {
        lock.lock();
        try
        {
            int result = id;
            id++;
            return result;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public String nextString()
    {
        int result = next();
        return idFormat.format(result);
    }
    
    public String nextStringwithTime()
    {
        int result = next();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(date);
        return time + "_" + idFormat.format(result);
    }
    
    protected static final NumberFormat idFormat = NumberFormat.getInstance();
    static
    {
        idFormat.setGroupingUsed(false);
        idFormat.setMinimumIntegerDigits(4);
    }
}
