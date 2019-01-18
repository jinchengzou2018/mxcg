package com.mxcg.common.cachemap.read;

import com.mxcg.common.cachemap.bean.ClearType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class MapQueue<K, V>
{
    /**
     * 读缓存
     */
    private HashMap<K, V> readCache = new HashMap<K, V>();
    
    /**
     * 主键链表
     */
    private LinkedList<K> keylist = new LinkedList<K>();
    
    private HashMap<K, TimeState> state = new HashMap<K, TimeState>();
    
    private final Lock readCacheLock = new ReentrantLock();
    
    private ClearType clearType = ClearType.LRU;
    
    private boolean cacheNull = true;
    
    private long timeout = 1000L * 3600;
    
    private int maxsize = 10000;
    
    private int minsize = 5000;
    
    private long nextCleanExpireTime = System.currentTimeMillis() + timeout;
    
    private class TimeState
    {
        long createTime;
        
        long outTime;

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("TimeState [createTime=");
            builder.append(createTime);
            builder.append(", outTime=");
            builder.append(outTime);
            builder.append("]");
            return builder.toString();
        }
    }
    
    public ClearType getCleartype()
    {
        return clearType;
    }
    
    public void setCleartype(ClearType cleartype)
    {
        this.clearType = cleartype;
    }
    
    public boolean isCacheNull()
    {
        return cacheNull;
    }
    
    public void setCacheNull(boolean cacheNull)
    {
        this.cacheNull = cacheNull;
    }
    
    public long getTimeout()
    {
        return timeout;
    }
    
    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }
    
    public int getMaxsize()
    {
        return maxsize;
    }
    
    public void setMaxsize(int maxsize)
    {
        this.maxsize = maxsize;
    }
    
    public int getMinsize()
    {
        return minsize;
    }
    
    public void setMinsize(int minsize)
    {
        this.minsize = minsize;
    }
    
    public boolean isEmpty()
    {
        readCacheLock.lock();
        try
        {
            return readCache.isEmpty();
        }
        finally
        {
            readCacheLock.unlock();
        }
    }
    
    public int size()
    {
        readCacheLock.lock();
        try
        {
            return readCache.size();
        }
        finally
        {
            readCacheLock.unlock();
        }
    }
    
    public void clear()
    {
        
        readCacheLock.lock();
        try
        {
            readCache.clear();
            keylist.clear();
            state.clear();
        }
        finally
        {
            readCacheLock.unlock();
        }
    }
    
    public void expireAll()
    {
        //最大时间
        int max = 3000;
        readCacheLock.lock();
        try
        {
            for(Entry<K, TimeState> e : state.entrySet())
            {
                TimeState ts = e.getValue();
                if(System.currentTimeMillis() - ts.outTime > max)
                {
                    ts.outTime = System.currentTimeMillis() + max;
                }
            }
        }
        finally
        {
            readCacheLock.unlock();
        }
    }
    
    public boolean containsKey(K key)
    {
        if (key != null)
        {
            readCacheLock.lock();
            try
            {
                return readCache.containsKey(key) && state.get(key).outTime > System.currentTimeMillis();
            }
            finally
            {
                readCacheLock.unlock();
            }
        }
        else
        {
            return false;
        }
    }
    
    public List<K> getCachedKey()
    {
        readCacheLock.lock();
        List<K> ks = new ArrayList<K>();
        try
        {
            ks.addAll(keylist);
        }
        finally
        {
            readCacheLock.unlock();
        }
        return ks;
    }
    
    public V get(K key)
    {
        V result = null;
        readCacheLock.lock();
        try
        {
            TimeState ts = state.get(key);
            if (ts != null && ts.outTime < System.currentTimeMillis())
            {
                //超时
                V v = readCache.remove(key);
                state.remove(key);
                keylist.remove(key);
                if(v == null)
                    afterRemoveByKey(key);
                else
                    afterRemove(v);
                result = noMatchOperation(key);
                return result;
            }
            else
            {
                //缓存里没有数据，从外部获取数据，有数据就更新队列
                if (!readCache.containsKey(key))
                {
                    result = noMatchOperation(key);
                }
                else
                {
                    result = readCache.get(key);
                    switch (clearType)
                    {
                        case FIFO:
                            break;
                        case LRU:
                            keylist.remove(key);
                            keylist.push(key);
                            break;
                        default:
                            break;
                    }
                }
                return result;
            }
        }
        finally
        {
            readCacheLock.unlock();
        }
    }
    
    public List<V> get(Collection<K> keys)
    {
        
        boolean hasnocachedkey = false;
        Map<K, V> result = new HashMap<K, V>(100);
        List<V> res = new ArrayList<V>();
        readCacheLock.lock();
        try
        {
            //遍历主键，把主键分为有缓存和无缓存的主键
            for (K k : keys)
            {
                if (readCache.containsKey(k))
                {
                    V d = readCache.get(k);
                    result.put(k, d);
                    if (d != null)
                    {
                        res.add(d);
                    }
                }
                else
                {
                    hasnocachedkey = true;
                    break;
                }
            }
            //如果有没缓存的数据
            if (hasnocachedkey)
            {
                res = getAndAdd(keys);
            }
            else
            {
                for (Entry<K, V> entry : result.entrySet())
                {
                    switch (clearType)
                    {
                        case FIFO:
                            break;
                        case LRU:
                            keylist.remove(entry.getKey());
                            keylist.push(entry.getKey());
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        finally
        {
            readCacheLock.unlock();
        }
        return res;
    }
    
    private List<V> getAndAdd(Collection<K> keys)
    {
        List<V> result = new ArrayList<V>();
        Collection<K> nocachedkey = new ArrayList<K>();
        //遍历主键，把主键分为有缓存和无缓存的主键
        for (K k : keys)
        {
            if (readCache.containsKey(k))
            {
                V d = readCache.get(k);
                if (d != null)
                {
                    result.add(d);
                }
                switch (clearType)
                {
                    case FIFO:
                        break;
                    case LRU:
                        keylist.remove(k);
                        keylist.push(k);
                        break;
                    default:
                        break;
                }
            }
            else
            {
                nocachedkey.add(k);
            }
        }
        if (nocachedkey.size() > 0)
        {
            //从外部载入未缓存数据，加入结果集
            Map<K, V> r = noMatchOperation(nocachedkey);
            if (r.size() != nocachedkey.size())
            {
                System.out.println(nocachedkey + "r.size:" + r.size());
            }
            Map<K, V> update = new HashMap<K, V>(100);
            for (K k : nocachedkey)
            {
                V v = r.get(k);
                update.put(k, v);
                if (v != null)
                {
                    result.add(v);
                }
            }
            addIntoCache(update);
        }
        return result;
    }
    
    /**
     * 加入值到缓存，并维持容量
     * 
     * @param arg0
     */
    private void addIntoCache(Map<K, V> values)
    {
        if (values != null)
        {
            for (Entry<K, V> entry : values.entrySet())
            {
                if (!cacheNull && entry.getValue() == null)
                {
                    continue;
                }
                
                if (readCache.containsKey(entry.getKey()))
                {
                    // 更新主键
                    switch (clearType)
                    {
                        case FIFO:
                            break;
                        case LRU:
                            keylist.remove(entry.getKey());
                            keylist.push(entry.getKey());
                            break;
                        default:
                            break;
                    }
                }
                else
                {
                    // 更新主键
                    switch (clearType)
                    {
                        case FIFO:
                            keylist.push(entry.getKey());
                            break;
                        case LRU:
                            keylist.push(entry.getKey());
                            break;
                        default:
                            break;
                    }
                }
                // 加入缓存
                readCache.put(entry.getKey(), entry.getValue());
                afterAdd(entry.getValue());
                //更新时间
                TimeState st = state.get(entry.getKey());
                if (st == null)
                {
                    st = new TimeState();
                }
                st.createTime = System.currentTimeMillis();
                st.outTime = st.createTime + timeout;
                state.put(entry.getKey(), st);
            }
            // 检查容量
            checkSize();
        }
    }
    
    public void add(K key, V value)
    {
        readCacheLock.lock();
        try
        {
            addIntoCache2(key, value);
        }
        finally
        {
            readCacheLock.unlock();
        }
    }
    
    public void remove(K key)
    {
        
        if (key != null)
        {
            readCacheLock.lock();
            try
            {
                keylist.remove(key);
                V v = readCache.remove(key);
                if(v == null)
                    afterRemoveByKey(key);
                else
                    afterRemove(v);
                state.remove(key);
            }
            finally
            {
                readCacheLock.unlock();
            }
        }
    }
    
    /**
     * 加入值到缓存，并维持容量
     * 
     * @param arg0
     */
    protected final void addIntoCache2(K k, V v)
    {
        if (k != null)
        {
            if (!cacheNull && v == null)
            {
                return;
            }
            // 加入缓存
            if (readCache.containsKey(k))
            {
                //更新
                switch (clearType)
                {
                    case FIFO:
                        break;
                    case LRU:
                        keylist.remove(k);
                        keylist.push(k);
                        break;
                    default:
                        break;
                }
            }
            else
            {
                //新增
                switch (clearType)
                {
                    case FIFO:
                        keylist.push(k);
                        break;
                    case LRU:
                        keylist.push(k);
                        break;
                    default:
                        break;
                }
            }
            readCache.put(k, v);
            afterAdd(v);
            //更新时间
            TimeState st = state.get(k);
            if (st == null)
            {
                st = new TimeState();
            }
            st.createTime = System.currentTimeMillis();
            st.outTime = st.createTime + timeout;
            state.put(k, st);
            
            // 检查容量
            checkSize();
        }
    }
    
    private void checkSize()
    {
        if(System.currentTimeMillis() > nextCleanExpireTime)
        {
            //清理过期缓存
            Iterator<Entry<K, TimeState>> iter = state.entrySet().iterator();
            while(iter.hasNext())
            {
                Entry<K, TimeState> entry = iter.next();
                if(entry.getValue().outTime < System.currentTimeMillis())
                {
                    V v = readCache.remove(entry.getKey());
                    keylist.remove(entry.getKey());
                    if(v == null)
                        afterRemoveByKey(entry.getKey());
                    else
                        afterRemove(v);
                    iter.remove();
                }
            }
        }
        if (maxsize > 0)
        {
            //控制缓存队列大小
            if (readCache.size() > maxsize)
            {
                if ((minsize > 0) && (maxsize > minsize + 10))
                {
                    while (keylist.size() > minsize)
                    {
                        K k = keylist.pollLast();
                        if (null != k)
                        {
                            V v = readCache.remove(k);
                            state.remove(k);
                            if(v == null)
                                afterRemoveByKey(k);
                            else
                                afterRemove(v);
                        }
                    }
                }
                else
                {
                    readCache.clear();
                    afterClear();
                    keylist.clear();
                    state.clear();
                }
            }
        }
    }
    
    protected void afterClear()
    {
        
    }
    
    protected void afterRemoveByKey(K key)
    {
        
    }
    
    protected void afterRemove(V v)
    {
        
    }
    
    protected void afterAdd(V v)
    {
        
    }
    
    /**
     * 没有匹配的缓存时的后续操作
     * @param key
     */
    protected V noMatchOperation(K key)
    {
        return null;
    }
    
    /**
     * 没有匹配的缓存时的后续操作
     * @param key
     */
    protected Map<K, V> noMatchOperation(Collection<K> keys)
    {
        return new HashMap<K, V>();
    }
}
