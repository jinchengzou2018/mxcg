package com.mxcg.common.cachemap.write;

import com.mxcg.common.cachemap.bean.HasPkey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/**
 * 
 * 增加KeyValue的缓存，支持对未写入的数据进行查询，防止异步写的数据延迟
 * 
 * @author  wyw
 * @version  [版本号, 2018年8月1日]
 */
public abstract class KVWriteCache<K, V extends HasPkey<K>>
    extends WriteCache<WriteCacheItem<K, V>>
{
    
    private Map<K, WriteCacheItem<K, V>> writeMap = new HashMap<K, WriteCacheItem<K, V>>();
    
    private final Lock writeMapLock = new ReentrantLock();
    
    public WriteCacheItem<K, V> get(K key)
    {
        WriteCacheItem<K, V> item = null;
        writeMapLock.lock();
        try
        {
        }
        finally
        {
            writeMapLock.unlock();
        }
        return item;
    }
    
    @Override
    /**
     * {@inheritDoc}
     * 优化写队列，减少写的请求
     */
    protected List<WriteCacheItem<K, V>> beforeFlush(List<WriteCacheItem<K, V>> items)
    {
        
        LinkedList<WriteCacheItem<K, V>> newtemp = new LinkedList<WriteCacheItem<K, V>>();
        Set<K> keys = new HashSet<K>();
        for (int i = items.size() - 1; i > -1; i--)
        {
            WriteCacheItem<K, V> item = items.get(i);
            if (!keys.contains(item.getKey()))
            {
                keys.add(item.getKey());
                newtemp.addFirst(item);
            }
        }
        return newtemp;
    }
    
    @Override
    /**
     * {@inheritDoc}
     * 清理map缓存
     */
    protected void afterFlush(List<WriteCacheItem<K, V>> writecache)
    {
        writeMapLock.lock();
        try
        {
            for (WriteCacheItem<K, V> item : writecache)
            {
                writeMap.remove(item.getKey());
            }
        }
        finally
        {
            writeMapLock.unlock();
        }
    }
    
}
