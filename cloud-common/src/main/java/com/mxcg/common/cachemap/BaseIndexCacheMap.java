package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/**
 * 基本的可写带索引缓存Map抽象类
 *
 */
public abstract class BaseIndexCacheMap<K extends Comparable<K>, Idx extends Comparable<Idx>, V extends HasIndex<K, Idx>>
    extends BaseCacheMap<K, V>
{
    private HashMap<Idx, Set<K>> index = new HashMap<>();
    
    private final Lock indexLock = new ReentrantLock();
    
    @Override
    public void clearIndex()
    {
        indexLock.lock();
        try
        {
            index.clear();
        }
        finally
        {
            indexLock.unlock();
        }
    }

    @Override
    public void removeFromIndex(K key)
    {
        indexLock.lock();
        try
        {
            if (key != null)
            {
                Set<Idx> emptyidx = new HashSet<Idx>();
                for (Entry<Idx, Set<K>> e : index.entrySet())
                {
                    Set<K> set = e.getValue();
                    if (set != null)
                    {
                        set.remove(key);
                        if (set.size() == 0)
                        {
                            emptyidx.add(e.getKey());
                        }
                    }
                }
                for(Idx idx : emptyidx)
                {
                    index.remove(idx);
                }
            }
        }
        finally
        {
            indexLock.unlock();
        }
    }
    
    @Override
    public void removeFromIndex(V v)
    {
        indexLock.lock();
        try
        {
            if (v != null && v.getIndex() != null && v.getIndex().size() > 0)
            {
                List<Idx> idxlist = v.getIndex();
                for (Idx idx : idxlist)
                {
                    Set<K> set = index.get(idx);
                    if (set != null)
                    {
                        set.remove(v.getPkey());
                        if (set.size() == 0)
                        {
                            index.remove(idx);
                        }
                    }
                }
            }
        }
        finally
        {
            indexLock.unlock();
        }
    }
    
    @Override
    public void addIntoIndex(V v)
    {
        indexLock.lock();
        try
        {
            if (v != null && v.getIndex() != null && v.getIndex().size() > 0)
            {
                List<Idx> idxlist = v.getIndex();
                for (Idx idx : idxlist)
                {
                    Set<K> set = index.get(idx);
                    if (set == null)
                    {
                        set = new HashSet<K>();
                        index.put(idx, set);
                    }
                    set.add(v.getPkey());
                }
            }
        }
        finally
        {
            indexLock.unlock();
        }
    }
    
    public List<V> findByIndex(Idx idx)
    {
        Set<K> set = null;
        indexLock.lock();
        try
        {
            set = index.get(idx);
        }
        finally
        {
            indexLock.unlock();
        }
        if (set != null)
        {
            return get(set);
        }
        else
        {
            return getByIndex(idx);
        }
    }
    
    private List<V> getByIndex(Idx idx)
    {
        List<V> result = new ArrayList<V>();
        try
        {
            result = dataQueryByIndex().synfindByIndex(idx);
        }
        catch (Exception e)
        {
            logException(e);
        }
        for (V v : result)
        {
            addIntoCache(v);
        }
        return result;
    }

    protected abstract DataQueryByIndex<K, Idx, V> dataQueryByIndex();
    
}
