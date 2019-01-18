package com.mxcg.common.cachemap.read;

import com.mxcg.common.cachemap.bean.HasPkey;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ReadCache<K, V extends HasPkey<K>> extends MapQueue<K, V>
{
    private DataQuery<K, V> dataQuery;
    
    private IndexOperation<K, V> indexOperation;
    
    public void setDataQuery(DataQuery<K, V> dataQuery)
    {
        this.dataQuery = dataQuery;
    }
    
    public void setIndexOperation(IndexOperation<K, V> indexOperation)
    {
        this.indexOperation = indexOperation;
    }
    
    @Override
    protected void afterClear()
    {
        indexOperation.clearIndex();
    }

    @Override
    protected void afterRemoveByKey(K key)
    {
        indexOperation.removeFromIndex(key);
    }
    
    @Override
    protected void afterRemove(V v)
    {
        indexOperation.removeFromIndex(v);
    }
    
    @Override
    protected V noMatchOperation(K key)
    {
        V result = null;
        try
        {
            result = dataQuery.synget(key);
        }
        catch (Exception e)
        {
            //SimpleLog.outException(e);
        }
        addIntoCache2(key, result);
        return result;
    }
    
    @Override
    protected void afterAdd(V v)
    {
        indexOperation.addIntoIndex(v);
    }
    
    public void add(V value)
    {
        if(value != null)
            add(value.getPkey(), value);
    }
    
    @Override
    protected Map<K, V> noMatchOperation(Collection<K> keys)
    {
        Map<K, V> r = new HashMap<K, V>();
        try
        {
            List<V> list = dataQuery.syngetAll(keys);
            for (V v : list)
            {
                r.put(v.getPkey(), v);
            }
        }
        catch (Exception e)
        {
            //SimpleLog.outException(e);
        }
        return r;
    }
}
