package com.mxcg.core.queue;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.mxcg.core.exception.TofocusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class SendQueue<MSG, RESULT> implements Closeable
{
    private static final Logger log = LoggerFactory.getLogger(SendQueue.class);
    
    //发送回调
    private SendCallBack<RESULT> sendCallBack;
    
    //异常回调
    private ExceptionCallBack<MSG> exceptionCallBack;
    
    private LinkedBlockingQueue<MsgWithLock> queue = new LinkedBlockingQueue<MsgWithLock>();
    
    private List<SendThread> sendThreads = new ArrayList<SendThread>();
    
    private Boolean active = null;
    
    public void setSendCallBack(SendCallBack<RESULT> sendCallBack)
    {
        this.sendCallBack = sendCallBack;
    }
    
    protected ExceptionCallBack<MSG> getExceptionCallBack()
    {
        return exceptionCallBack;
    }
    
    public void setExceptionCallBack(ExceptionCallBack<MSG> exceptionCallBack)
    {
        this.exceptionCallBack = exceptionCallBack;
    }
    
    public synchronized void start()
    {
        if (active == null)
        {
            log.debug("启动" + sendThreadCount() + "个线程");
            active = true;
            for (int i = 0; i < sendThreadCount(); i++)
            {
                SendThread t = new SendThread();
                t.id = i;
                t.start();
                sendThreads.add(t);
            }
        }
    }
    
    @Override
    public void close()
    {
        if (active != null)
        {
            active = false;
            while (sendThreads.size() > 0)
            {
                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            log.debug("关闭");
        }
    }
    
    /**
     * 异步发送
     * @param msg
     */
    public void send(MSG msg)
    {
        try
        {
            if (active != null && active)
            {
                MsgWithLock m = new MsgWithLock();
                m.msg = msg;
                queue.put(m);
            }
            else
                throw new TofocusException(TofocusException.UNACTIVE, "发送队列未启动");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 同步发送
     * @param msg
     * @return
     */
    public RESULT syncSend(MSG msg)
        throws TofocusException
    {
        try
        {
            if (active != null && active)
            {
                MsgWithLock m = new MsgWithLock();
                m.msg = msg;
                m.lock = new ReentrantLock();
                m.condition = m.lock.newCondition();
                
                m.lock.lock();
                try
                {
                    queue.put(m);
                    m.condition.await();
                    if (m.e != null)
                        throw m.e;
                    else
                        return m.result;
                }
                finally
                {
                    m.lock.unlock();
                }
            }
            else
                throw new TofocusException(TofocusException.UNACTIVE, "发送队列未启动");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * 发送到远端
     * @param msg
     * @return
     */
    protected abstract RESULT sendToRemote(MSG msg)
        throws Exception;
    
    /**
     * 并发线程数
     * @return
     */
    protected int sendThreadCount()
    {
        return 1;
    }
    
    private class SendThread extends Thread
    {
        private int id;
        
        @Override
        public void run()
        {
            log.debug("线程" + id + "启动");
            while (true)
            {
                try
                {
                    //拉取一个消息，最多等待0.1秒
                    MsgWithLock m = queue.poll(100, TimeUnit.MILLISECONDS);
                    if (m != null && m.msg != null)
                    {
                        try
                        {
                            RESULT r = sendToRemote(m.msg);
                            m.result = r;
                            if (sendCallBack != null)
                                sendCallBack.done(r);
                        }
                        catch (Exception ex)
                        {
                            TofocusException e = new TofocusException(TofocusException.PRC_EXCEPTION, ex);
                            m.e = e;
                            if (exceptionCallBack != null)
                                exceptionCallBack.exception(m.msg, e);
                        }
                        
                        if (m.lock != null)
                        {
                            m.lock.lock();
                            try
                            {
                                m.condition.signal();
                            }
                            finally
                            {
                                m.lock.unlock();
                            }
                        }
                    }
                    else
                    {
                        if (!active)
                            break;
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            log.debug("线程" + id + "结束");
            sendThreads.remove(this);
        }
    }
    
    private class MsgWithLock
    {
        private MSG msg;
        
        private ReentrantLock lock;
        
        private Condition condition;
        
        private RESULT result;
        
        private TofocusException e;
    }
}
