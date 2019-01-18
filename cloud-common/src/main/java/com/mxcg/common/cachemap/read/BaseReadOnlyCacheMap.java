package com.mxcg.common.cachemap.read;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.core.page.PageResult;
import com.mxcg.jpa.FindRequest;
import org.apache.commons.logging.impl.SimpleLog;
import org.springframework.data.domain.Page;



/**
 * 最基本的只读缓存Map抽象类。实现读写锁，线程安全。
 * 可设置缓存最大容量。
 * 更新机制支持FIFO和LRU。
 * 适用用于数据量巨大，非易变，无数据总量，排序，索引需求的，KeyValue形式的数据缓存。
 */
public abstract class BaseReadOnlyCacheMap<K, V extends HasPkey<K>>
    implements BaseReadOnlyCacheMapInterface<K, V>, IndexOperation<K, V>, DataQuery<K, V>
{
    
    private ReadCache<K, V> readCache = new ReadCache<K, V>();
    
    /**
     * 结果集缓存
     */
    private MapQueue<FindRequest<V>, Page<V>> resultCache = new MapQueue<>();
    
    public BaseReadOnlyCacheMap()
    {
        readCache = new ReadCache<K, V>();
        readCache.setDataQuery(this);
        readCache.setIndexOperation(this);
        readCache.setCacheNull(isCacheNull());
        readCache.setTimeout(limittimeout(getCacheTimeout()));
        readCache.setCleartype(getClearType());
        readCache.setMaxsize(limitMaxsize(getMaxsize()));
        readCache.setMinsize(limitMinsize(getMinsize()));
        
        resultCache = new MapQueue<>();
        resultCache.setCacheNull(false);
        resultCache.setTimeout(limittimeout(getCacheTimeout()));
        resultCache.setCleartype(getResultClearRype());
        resultCache.setMaxsize(limitMaxsize(getResultMaxsize()));
        resultCache.setMinsize(limitMinsize(getResultMinsize()));
    }
    
    private long limittimeout(long timeout)
    {
        int mintimeout = 200;
        long maxtimeout = 1000L * 3600;
        if (timeout < 1)
        {
            return maxtimeout;
        }
        else if (timeout < mintimeout)
        {
            return mintimeout;
        }
        else
        {
            return timeout;
        }
    }

    private int limitMaxsize(int maxsize)
    {
        if(maxsize < 0)
            return 0;
        else
            return maxsize;
    }

    private int limitMinsize(int minsize)
    {
        int maxsize = limitMaxsize(getResultMaxsize());
        if (minsize > maxsize / 2)
            return maxsize / 2;
        else
            return minsize;
    }
    
    protected abstract DataQuery<K, V> dataQuery();
    
    /************************************************
     * 公开方法 *
     ************************************************/
    
    /**
     * 判断缓存是否为空
     * 
     * @return
     */
    @Override
    public boolean isEmpty()
    {
        return readCache.isEmpty() && resultCache.isEmpty();
    }
    
    /**
     * 返回缓存的值数量。
     * 
     * @return
     */
    @Override
    public int size()
    {
        return readCache.size() + resultCache.size();
    }
    
    
    /**
     * 清空读缓存
     */
    public final void clear()
    {
        readCache.clear();
        resultCache.clear();
        clearIndex();
    }
    
    /************************************************
     * 数据查询 *
     ************************************************/
    /**
     * 缓存中是否包含指定键
     * 
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(K key)
    {
        return readCache.containsKey(key);
    }
    
    /**
     * 返回指定键所映射的值；如果对于该键来说，此映射不包含任何映射关系，则返回 null。
     * 
     * @param key
     * @return
     */
    @Override
    public V get(K key)
    {
        if (key != null)
        {
            if (isReadCacheEnabled())
            {
                return readCache.get(key);
            }
            else
            {
                try
                {
                    return dataQuery().synget(key);
                }
                catch (Exception e)
                {
                    logException(e);
                    return null;
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 按主键批量查找
     * 
     * @param keys
     * @return
     */
    @Override
    public List<V> get(Collection<K> keys)
    {
        if (keys != null)
        {
            if (isReadCacheEnabled())
            {
                return readCache.get(keys);
            }
            else
            {
                List<V> r = new ArrayList<V>();
                try
                {
                    for (V v : dataQuery().syngetAll(keys))
                    {
                        if (v != null)
                        {
                            r.add(v);
                        }
                    }
                }
                catch (Exception e)
                {
                    logException(e);
                    r = new ArrayList<V>();
                }
                return r;
            }
        }
        else
        {
            return new ArrayList<V>();
        }
    }
    
    @Override
    public List<V> findAll()
    {
        try
        {
            FindRequest<V> parameter = null;
            Page<V> result = resultCache.get(parameter);
            if(result == null)
            {
                List<V> list = dataQuery().synfindAll();
                PageResult<V> p = new PageResult<V>(list, null, list.size());
                resultCache.add(null, p);
                int count = 0;
                int max = 100;
                for(V v : list)
                {
                    readCache.add(v);
                    count ++;
                    if(count > max)
                        break;
                }
                return list;
            }
            else
                return result.getContent();
        }
        catch (Exception e)
        {

            return new ArrayList<V>();
        }
    }

    @Override
    public Page<V> findAll(FindRequest<V> parameter)
    {
        try
        {
            Page<V> page = resultCache.get(parameter);
            if(page == null)
            {
                page = dataQuery().synfindAll(parameter);
                resultCache.add(parameter, page);
                if(page.getContent() != null)
                {
                    int count = 0;
                    int max = 100;
                    for(V v : page.getContent())
                    {
                        readCache.add(v);
                        count ++;
                        if(count > max)
                            break;
                    }
                }
            }
            return page;
        }
        catch (Exception e)
        {

            return new PageResult<V>();
        }
    }
    
    
    /**
     * 给子类调用增加外部数据到缓存
     * <功能详细描述>
     * @param value
     */
    protected final void addIntoCache(V value)
    {
        readCache.add(value);
    }
    
    /**
     * 从缓存删除值
     * 
     * @param key
     */
    protected final void removeFromCache(K key)
    {
        readCache.remove(key);
    }
    
    protected final void expireResultCache()
    {
        resultCache.expireAll();
    }
    
    protected void logException(String msg, Exception e)
    {
        //        if (log != null) {
        //            log.outException(msg, e);
        //        }

    }
    
    protected void logException(Exception e)
    {
        //        if (log != null) {
        //            log.outException(e);
        //        }

    }
    
    protected void logDebug(String msg)
    {
        //        if (log != null) {
        //            log.outDebug(msg);
        //        }

    }
    
    protected void logErr(String msg)
    {
        //        if (log != null) {
        //            log.outErr(msg);
        //        }

    }
    
    protected void logWaring(String msg)
    {
        //        if (log != null) {
        //            log.outWaring(msg);
        //        }

    }

    @Override
    public List<V> synfindAll()
        throws Exception
    {
        return dataQuery().synfindAll();
    }

    @Override
    public Page<V> synfindAll(FindRequest<V> parameter)
        throws Exception
    {
        return dataQuery().synfindAll(parameter);
    }

    @Override
    public V synget(K key)
        throws Exception
    {
        return dataQuery().synget(key);
    }

    @Override
    public List<V> syngetAll(Collection<K> keys)
        throws Exception
    {
        return dataQuery().syngetAll(keys);
    }

}
