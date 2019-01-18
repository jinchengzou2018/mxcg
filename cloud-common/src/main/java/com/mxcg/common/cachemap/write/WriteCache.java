package com.mxcg.common.cachemap.write;

import com.mxcg.core.log.SimpleLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/**
 * 
 * 异步写缓存
 * 
 * @author  wyw
 * @version  [版本号, 2018年8月1日]
 */
public abstract class WriteCache<V> implements WriteCacheInterface<V>
{
    
    private List<V> writeCache;
    
    private final Lock writeCacheLock = new ReentrantLock();
    
    private final Condition notFull = writeCacheLock.newCondition();//写线程条件 
    
    private final Condition notEmpty = writeCacheLock.newCondition();//读线程条件 
    
    private WriteThread writeThread;
    
    public WriteCache()
    {
        writeCache = new ArrayList<V>(getMaxWriteCacheSize());
        
        writeThread = new WriteThread();
        writeThread.setName("CacheBaseMap WriteThread");
        writeThread.setDaemon(true);
        writeThread.start();
    }
    
    /**
     * 把item放到写缓存，如果缓存满，则等待
     * @param item
     */
    @Override
    public void putIntoWriteCache(V item)
    {
        writeCacheLock.lock();
        try
        {
            while (getMaxWriteCacheSize() == writeCache.size())//如果队列满了 
                notFull.await();
            writeCache.add(item);
            notEmpty.signal();//唤醒读线程
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            writeCacheLock.unlock();
        }
        
    }
    
    @Override
    public int getWriteCacheSize()
    {
        return writeCache.size();
    }
    
    public List<V> take()
        throws InterruptedException
    {
        writeCacheLock.lock();
        try
        {
            while (writeCache.size() == 0)//如果队列为空
                notEmpty.await();//阻塞读线程
                
            List<V> list = new ArrayList<>();
            list.addAll(writeCache);
            writeCache.clear();
            
            notFull.signal();//唤醒写线程
            return list;
        }
        finally
        {
            writeCacheLock.unlock();
        }
    }
    
    /************************************************
     * 延写缓存 *
     ************************************************/
    /**
     * 批量处理数据
     * @param values
     * @throws Exception 
     */
    protected abstract void synWrite(List<V> values)
        throws Exception;
    
    @Override
    public synchronized void flush()
    {
    }
    
    protected void afterFlush(List<V> writecache)
    {
    }
    
    protected List<V> beforeFlush(List<V> writecache)
    {
        return writecache;
    }
    
    class WriteThread extends Thread
    {
        @Override
        public void run()
        {
            List<V> tmp = new ArrayList<V>(1000);
            long t = System.currentTimeMillis() + 10000;
            while (true)
            {
                try
                {
                    List<V> list = beforeFlush(take());
                    if (list.size() > 0)
                    {
                        try
                        {
                            synWrite(list);
                        }
                        catch (Exception e)
                        {
                            logException(e);
                        }
                        afterFlush(list);
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    protected void logDebug(String msg)
    {
        SimpleLog.outDebug(msg);
    }
    
    protected void logException(Exception e)
    {
        SimpleLog.outException(e);
    }
}
