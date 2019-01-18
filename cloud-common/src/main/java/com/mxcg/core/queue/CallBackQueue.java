package com.mxcg.core.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.core.exception.TofocusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class CallBackQueue<K, MSG extends HasPkey<K>, RESULT, BACK extends HasPkey<K>>
    extends SendQueue<MSG, RESULT>

{
    private static final Logger log = LoggerFactory.getLogger(CallBackQueue.class);
    
    private RemoteCallBack<MSG, BACK> callback;
    
    private Map<K, MsgPack> msgMap = new HashMap<>();
    
    private Lock lock = new ReentrantLock();
    
    public void setCallback(RemoteCallBack<MSG, BACK> callback) {
        this.callback = callback;
    }
    
    @Override
    public synchronized void start() {
        super.start();
        CheckCallBack checkCallBack = new CheckCallBack();
        checkCallBack.start();
    }
    
    @Override
    public void send(MSG msg) {
        lock.lock();
        try {
            msgMap.put(msg.getPkey(), new MsgPack(msg));
        } finally {
            lock.unlock();
        }
        super.send(msg);
    }
    
    @Override
    public RESULT syncSend(MSG msg) {
        lock.lock();
        try {
            msgMap.put(msg.getPkey(), new MsgPack(msg));
        } finally {
            lock.unlock();
        }
        return super.syncSend(msg);
    }
    
    /**
     * 从远端回调
     * @param back
     */
    public void callbackFromRemote(BACK back) {
        lock.lock();
        try {
            msgMap.remove(back.getPkey());
        } finally {
            lock.unlock();
        }
        if (callback != null)
            callback.callBack(back);
    }
    
    /**
     * 主动查询远端，没有结果返回null，延长查询间隔。抛异常则查询间隔不变
     * @param msg
     * @return
     */
    protected abstract BACK confirmFromRemote(MSG msg)
        throws Exception;
    
    /**
     * 主动检查间隔。每检查一次，下一次间隔*2
     * @return
     */
    protected int checkInterval() {
        return 60000;
    }
    
    /**
     * 最大主动检查次数
     * @return
     */
    protected int maxRetryTimes() {
        return 6;
    }
    
    class CheckCallBack extends Thread {
        
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(checkInterval());
                    //收集超时的消息
                    List<MsgPack> msgPacks = new ArrayList<>();
                    lock.lock();
                    long now = System.currentTimeMillis();
                    try {
                        for (Entry<K, MsgPack> entry : msgMap.entrySet()) {
                            MsgPack p = entry.getValue();
                            if (p.lastCheck + p.checkInterval < now) {
                                msgPacks.add(p);
                            }
                        }
                    } finally {
                        lock.unlock();
                    }
                    //遍历消息
                    for (MsgPack p : msgPacks) {
                        //如果已经回调，忽略掉
                        lock.lock();
                        try {
                            if (!msgMap.containsKey(p.msg.getPkey()))
                                continue;
                        } finally {
                            lock.unlock();
                        }
                        //主动查询
                        try {
                            BACK back = confirmFromRemote(p.msg);
                            //有结果就返回
                            if (back != null)
                                callbackFromRemote(back);
                            else {
                                //没有结果，更新重试间隔和次数
                                p.lastCheck = now;
                                p.retryTimes = p.retryTimes + 1;
                                p.checkInterval = p.checkInterval * 2;
                                //如果超过最大次数，移除，并报超时
                                if (p.retryTimes >= maxRetryTimes()) {
                                    lock.lock();
                                    try {
                                        msgMap.remove(p.msg.getPkey());
                                    } finally {
                                        lock.unlock();
                                    }
                                    callback.timeout(p.msg);
                                }
                            }
                        } catch (Exception e) {
                            if (getExceptionCallBack() != null)
                                getExceptionCallBack().exception(p.msg,
                                    new TofocusException(TofocusException.PRC_EXCEPTION, e));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private class MsgPack {
        private MSG msg;
        
        //重试次数
        private int retryTimes;
        
        //上次检查时间
        private long lastCheck;
        
        //检查间隔
        private int checkInterval;
        
        private MsgPack(MSG msg) {
            this.msg = msg;
            lastCheck = System.currentTimeMillis();
            checkInterval = checkInterval();
        }
    }
    
}
