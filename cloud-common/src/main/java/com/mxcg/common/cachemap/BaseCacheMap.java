package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.common.cachemap.bean.Operate;
import com.mxcg.common.cachemap.read.BaseReadOnlyCacheMap;
import com.mxcg.common.cachemap.write.KVWriteCache;
import com.mxcg.common.cachemap.write.WriteCacheGroupItem;
import com.mxcg.common.cachemap.write.WriteCacheItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * 
 * 基本的可写缓存Map抽象类
 * 在BaseReadOnlyCacheMap基础上，提供写缓存功能。实现读写锁，线程安全。
 *
 */
public abstract class BaseCacheMap<K, V extends HasPkey<K>> extends BaseReadOnlyCacheMap<K, V>
    implements BaseCacheMapInterface<K, V>
{
    
    private MapWriteCache writeCache = new MapWriteCache();
    
    public BaseCacheMap()
    {
        super();
    }
    
    /**
     * 如果键存在则替换已有值, 不存在就新增值
     * @param value 值
     */
    @Override
    public V put(V value)
    {
        if (value != null)
        {
            addIntoCache(value);
            putIntoWriteCache(value, Operate.put);
            return value;
        }
        else
        {
            return null;
        }
    }

    protected abstract DataGroupWriter<K, V> dataGroupWriter();
    
    /************************************************
     * 数据删除 *
     ************************************************/
    /**
     * 删除指定键的值
     * 删除成功，true，
     * 指定键为空，指定键不存在，则删除失败，返回false
     * 
     * @param key 主键
     */
    @Override
    public boolean removeById(K key)
    {
        if (key != null)
        {
            removeFromCache(key);
            putIntoWritecache(key, Operate.del);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 按照值的主键来删除数据
     * 删除成功，true，删除失败，返回false
     * 值为空，值的主键为空，值的主键不存在，则删除失败，返回false
     * 
     * @param value 值
     * @return 删除成功，true，删除失败，返回false
     */
    @Override
    public boolean remove(V value)
    {
        if ((value != null) && (value.getPkey() != null))
        {
            removeFromCache(value.getPkey());
            putIntoWritecache(value.getPkey(), Operate.del);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private void putIntoWriteCache(V value, Operate operate)
    {
        if (value != null)
        {
            WriteCacheItem<K, V> item = new WriteCacheItem<K, V>(value.getPkey(), operate, value);
            writeCache.putIntoWriteCache(item);
        }
        expireResultCache();
    }
    
    private void putIntoWritecache(K key, Operate operate)
    {
        if (key != null)
        {
            WriteCacheItem<K, V> item = new WriteCacheItem<K, V>(key, operate, null);
            writeCache.putIntoWriteCache(item);
        }
        expireResultCache();
    }
    
    @Override
    public int getWriteCacheSize()
    {
        return writeCache.getWriteCacheSize();
    }
    
    /************************************************
     * 数据查询*
     ************************************************/
    
    @Override
    public V get(K key)
    {
        if (key != null)
        {
            WriteCacheItem<K, V> item = null;
            
            item = writeCache.get(key);
            
            if (item != null)
            {
                if (item.getOperate().equals(Operate.del))
                {
                    return null;
                }
                else
                {
                    return item.getValue();
                }
            }
            else
            {
                return super.get(key);
            }
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public List<V> get(Collection<K> keys)
    {
        if (keys != null)
        {
            Collection<K> needgetkeys = new ArrayList<K>();
            List<V> result = new ArrayList<V>();
            for (K k : keys)
            {
                WriteCacheItem<K, V> item = writeCache.get(k);
                if (item != null)
                {
                    if (!item.getOperate().equals(Operate.del))
                    {
                        result.add(item.getValue());
                    }
                }
                else
                {
                    needgetkeys.add(k);
                }
            }
            result.addAll(super.get(needgetkeys));
            return result;
        }
        else
        {
            return null;
        }
    }
    
    /************************************************
     * 延写缓存 *
     ************************************************/

    /**
     * 初始化主键。如果需要在增加时自动生成主键的，重载这个方法
     * @param arg1
     */
    protected void inikey(V value)
    {
    }
    
    protected int iniMaxWriteCacheSize()
    {
        return 1000;
    }
    
    @Override
    public synchronized void flush()
    {
        writeCache.flush();
    }
    
    class MapWriteCache extends KVWriteCache<K, V>
    {
        
        @Override
        protected void synWrite(List<WriteCacheItem<K, V>> values)
            throws Exception
        {
            dataGroupWriter().synBatchWrite(group(values));
        }

        @Override
        public int getMaxWriteCacheSize()
        {
            return iniMaxWriteCacheSize();
        }
    }

    private List<WriteCacheGroupItem<K, V>> group(List<WriteCacheItem<K, V>> items)
    {
        List<WriteCacheGroupItem<K,V>> grouplist = new ArrayList<>();
        WriteCacheGroupItem<K,V> group = null;
        for(WriteCacheItem<K, V> item : items)
        {
            if(group == null || !group.getOperate().equals(item.getOperate()))
            {
                if(group != null) grouplist.add(group);
                group = new WriteCacheGroupItem<K,V>(item);
            }
            else
            {
                group.add(item);
            }
        }
        if(group != null) grouplist.add(group);
        return grouplist;
    }

    @Override
    public List<V> putAll(Collection<V> values)
    {
        List<V> list = new ArrayList<V>(values.size());
        for(V value : values)
        {
            V v = put(value);
            list.add(v);
        }
        return list;
    }

    @Override
    public boolean removeAllById(Collection<K> keys)
    {
        boolean result = true;
        for(K key : keys)
        {
            result = result && removeById(key);
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<V> values)
    {
        boolean result = true;
        for(V value : values)
        {
            result = result && remove(value);
        }
        return result;
    }
}
