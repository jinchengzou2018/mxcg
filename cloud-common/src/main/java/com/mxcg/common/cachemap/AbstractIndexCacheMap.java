package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.ComparatorIndex;
import com.mxcg.common.cachemap.bean.HasIndex;
import com.mxcg.common.cachemap.util.CnCharToEn;
import com.mxcg.core.exception.TofocusException;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;



/**
 * 带索引缓存Map抽象类。线程安全，异步写缓存。
 * 可设置缓存最大容量。更新机制支持FIFO和LRU。
 * 初始化时加载所有主键和索引。
 * 适用于访问频繁，独占的数据，有全量需求，有快速索引需求的数据缓存。

 */
public abstract class AbstractIndexCacheMap<K, Idx extends Comparable<Idx>, V extends HasIndex<K, Idx>>
    extends AbstractCacheMap<K, V>
{
    private HashMap<Idx, Set<K>> index;
    
    private ReadWriteLock indexLock;
    
    private boolean indexUniq = false;
    
    public AbstractIndexCacheMap()
    {
        super();
    }
    
    /************************************************
     * 初始化 
     * @throws CacheException *
     ************************************************/
    @Override
    protected final HashSet<K> loadKey()
    {
        if (null == indexLock)
        {
            indexLock = new ReentrantReadWriteLock();
        }
        indexLock.writeLock().lock();
        try
        {
            Map<K, List<Idx>> keymap = null;
            try
            {
                keymap = loadIndex();
            }
            catch (Exception e)
            {
                logException(this.getClass().getName() + " 缓存索引初始化失败", e);
                throw new TofocusException(TofocusException.CacheResultCode.CACHE_INIERR,
                    this.getClass().getName() + " 缓存读取初始化数据失败", e);
            }
            HashMap<Idx, Set<K>> result = new HashMap<Idx, Set<K>>(100);
            HashSet<K> keys = new HashSet<K>();
            if (keymap != null)
            {
                for (Entry<K, List<Idx>> idx : keymap.entrySet())
                {
                    keys.add(idx.getKey());
                    // 重建索引
                    for (Idx i : idx.getValue())
                    {
                        if (result.containsKey(i))
                        {
                            result.get(i).add(idx.getKey());
                        }
                        else
                        {
                            Set<K> k = new HashSet<K>();
                            k.add(idx.getKey());
                            result.put(i, k);
                        }
                    }
                }
            }
            
            this.index = result;
            return keys;
        }
        finally
        {
            indexLock.writeLock().unlock();
        }
    }
    
    /**
     * 加载索引
     * <功能详细描述>
     * @return
     * @throws Exception
     */
    protected abstract Map<K, List<Idx>> loadIndex()
        throws Exception;
    
    /************************************************
     * 属性设置 *
     ************************************************/
    
    public boolean isIndexUniq()
    {
        return indexUniq;
    }
    
    public void setIndexUniq(boolean indexUniq)
    {
        this.indexUniq = indexUniq;
    }
    
    /************************************************
     * 数据查询 *
     ************************************************/
    /**
    * 精确查询索引
    * 
    * @param idx 索引内容
    */
    public List<V> searchByIndex(Idx idx)
    {
        if (idx != null)
        {
            List<V> result = new ArrayList<V>();
            Set<K> keys = null;
            indexLock.readLock().lock();
            try
            {
                keys = index.get(idx);
            }
            finally
            {
                indexLock.readLock().unlock();
            }
            if ((keys != null) && (keys.size() > 0))
            {
                result = get(keys);
            }
            return result;
        }
        else
        {
            return new ArrayList<V>();
        }
    }
    
    public List<V> searchByIndex2(Idx idx)
    {
        return searchByIndex2(idx, -1);
    }
    
    /**
     * 模糊查询索引, 非String类型等同于searchbyindex
     * 
     * @param idx
     * @return
     */
    public List<V> searchByIndex2(Idx idx, int max)
    {
        int count = 0;
        if (idx != null)
        {
            List<V> result = new ArrayList<V>();
            Set<K> key = new HashSet<K>();
            indexLock.readLock().lock();
            try
            {
                if (idx instanceof String)
                {
                    String sarg = (String)idx;
                    for (Idx s : index.keySet())
                    {
                        if (max > 0 && count >= max)
                        {
                            break;
                        }
                        if (s.toString().indexOf(sarg) > -1)
                        {
                            for (K k : index.get(s))
                            {
                                if (!key.contains(k))
                                {
                                    key.add(k);
                                    result.add(get(k));
                                    count++;
                                }
                            }
                        }
                    }
                }
                else
                {
                    if (index.containsKey(idx))
                    {
                        for (K k : index.get(idx))
                        {
                            if (!key.contains(k))
                            {
                                key.add(k);
                                result.add(get(k));
                                count++;
                            }
                        }
                    }
                }
            }
            finally
            {
                indexLock.readLock().unlock();
            }
            if (result.size() > max)
            {
                return result.subList(0, max);
            }
            else
            {
                return result;
            }
        }
        else
        {
            return new ArrayList<V>();
        }
    }
    
    public List<V> searchByIndex3(Idx idx)
    {
        return searchByIndex3(idx, -1);
    }
    
    /**
     * 模糊查询,中文拼音首字母索引, 非String类型等同于searchbyindex
     * 
     * @param idx
     * @return
     */
    public List<V> searchByIndex3(Idx idx, int max)
    {
        int count = 0;
        if (idx != null)
        {
            List<V> result = new ArrayList<V>();
            Set<K> key = new HashSet<K>();
            indexLock.readLock().lock();
            try
            {
                if (idx instanceof String)
                {
                    String sarg = ((String)idx).toUpperCase();
                    for (Idx s : index.keySet())
                    {
                        if (max > 0 && count >= max)
                        {
                            break;
                        }
                        if (s.toString().toUpperCase().indexOf(sarg) > -1)
                        {
                            for (K k : index.get(s))
                            {
                                if (!key.contains(k))
                                {
                                    key.add(k);
                                    result.add(get(k));
                                    count++;
                                }
                            }
                        }
                        else if (CnCharToEn.cn2py(s.toString()).startsWith(sarg))
                        {
                            for (K k : index.get(s))
                            {
                                if (!key.contains(k))
                                {
                                    key.add(k);
                                    result.add(get(k));
                                    count++;
                                }
                            }
                        }
                    }
                }
                else
                {
                    if (index.containsKey(idx))
                    {
                        for (K k : index.get(idx))
                        {
                            if (!key.contains(k))
                            {
                                key.add(k);
                                result.add(get(k));
                                count++;
                            }
                        }
                    }
                }
            }
            finally
            {
                indexLock.readLock().unlock();
            }
            if (result.size() > max)
            {
                return result.subList(0, max);
            }
            else
            {
                return result;
            }
        }
        else
        {
            return new ArrayList<V>();
        }
    }
    
    public List<V> getAll(boolean sortbyindex)
    {
        List<V> result = findAll();
        if (sortbyindex)
        {
            Collections.sort(result, new ComparatorIndex<K, Idx>());
        }
        return result;
    }
    
    public List<V> get(Collection<K> keys, boolean sortbyindex)
    {
        List<V> result = null;
        if (sortbyindex)
        {
            result = get(keys);
            Collections.sort(result, new ComparatorIndex<K, Idx>());
        }
        return result;
    }
    
    /************************************************
     * 数据修改 *
     ************************************************/
    /**
     * 如果新增值，并自动递增主键
     * 新增成功，返回新增的值
     * 新增值为空，或主键为空，或者主键冲突，或者索引为空，或者索引违反唯一性，则新增失败，返回null
     * 
     * @param value
     * @return
     */
    @Override
    public V add(V value)
    {
        //判断空值和索引唯一性
        if (value != null)
        {
            //索引必须非空
            if (value.getIndex() == null || value.getIndex().size() == 0)
            {
                logErr("索引不可为空：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.INDEX_IS_NULL,
                    "索引不可为空：" + value.toString());
            }
            //索引除重
            if (checkIndexDup(value.getIndex()))
            {
                logErr("索引违反唯一性：" + value.getIndex().toString());
                throw new TofocusException(TofocusException.CacheResultCode.INDEX_IS_UNUNIQ,
                    "索引违反唯一性：" + value.toString());
            }
            V result = super.add(value);
            // 加入索引
            if (result != null)
            {
                removeIndex(result.getPkey());
                addToIndex(result.getPkey(), result.getIndex());
            }
            return result;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 如果键存在则替换已有值
     * 替换成功，返回替换后的值
     * 替换值为空，替换值主键为空，被替换值不存在，或者索引为空，或者索引违反唯一性，则替换失败，返回null
     * 
     * @param value
     * @return
     */
    @Override
    public V update(V value)
    {
        //判断空值
        if (value != null)
        {
            //索引必须非空
            if (value.getIndex() == null)
            {
                logErr("索引不可为空：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.INDEX_IS_NULL,
                    "索引不可为空：" + value.toString());
            }
            V olddata = get(value.getPkey());
            //旧值不存在，修改失败
            if (olddata == null)
            {
                logErr("要修改的值不存在：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.OLD_VALUE_IS_NOT_EXIST,
                    "要修改的值不存在：" + value.toString());
            }
            //新旧值索引不同，检查新索引的唯一性
            if (!olddata.getIndex().equals(value.getIndex()))
            {
                if (checkIndexDup(value.getIndex()))
                {
                    logErr("索引违反唯一性：" + value.getIndex().toString());
                    throw new TofocusException(TofocusException.CacheResultCode.INDEX_IS_UNUNIQ,
                        "索引违反唯一性：" + value.toString());
                }
            }
            V result = super.update(value);
            // 加入索引
            if (result != null)
            {
                removeIndex(result.getPkey());
                addToIndex(result.getPkey(), result.getIndex());
            }
            return result;
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public V put(V value)
    {
        //判断空值
        if (value != null)
        {
            //索引必须非空
            if (value.getIndex() == null)
            {
                logErr("索引不可为空：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.INDEX_IS_NULL,
                    "索引不可为空：" + value.toString());
            }
            V olddata = super.get(value.getPkey());
            //旧值不存在，添加
            if (olddata == null)
            {
                //索引除重
                if (checkIndexDup(value.getIndex()))
                {
                    logErr("索引违反唯一性：" + value.getIndex().toString());
                    throw new TofocusException(TofocusException.CacheResultCode.INDEX_IS_UNUNIQ,
                        "索引违反唯一性：" + value.toString());
                }
                V result = super.add(value);
                // 加入索引
                if (result != null)
                {
                    removeIndex(result.getPkey());
                    addToIndex(result.getPkey(), result.getIndex());
                }
                return result;
            }
            else
            {
                //新旧值索引不同，检查新索引的唯一性
                if (!olddata.getIndex().equals(value.getIndex()))
                {
                    if (checkIndexDup(value.getIndex()))
                    {
                        logErr("索引违反唯一性：" + value.getIndex().toString());
                        throw new TofocusException(TofocusException.CacheResultCode.INDEX_IS_UNUNIQ,
                            "索引违反唯一性：" + value.toString());
                    }
                }
                V result = super.update(value);
                // 加入索引
                if (result != null)
                {
                    removeIndex(result.getPkey());
                    addToIndex(result.getPkey(), result.getIndex());
                }
                return result;
            }
        }
        else
        {
            return null;
        }
    }
    
    /************************************************
     * 数据删除 *
     ************************************************/
    /**
     * 删除指定键的值
     * 删除成功，true，
     * 指定键为空，指定键不存在，则删除失败，返回false
     * 
     * @param key
     * @return
     */
    @Override
    public boolean removeById(K key)
    {
        if (key != null)
        {
            boolean result = super.removeById(key);
            removeIndex(key);
            return result;
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
     * @param value
     * @return
     */
    @Override
    public boolean remove(V value)
    {
        if (value != null)
        {
            if (value.getIndex() == null)
            {
                logErr("索引不可为空：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.INDEX_IS_NULL,
                    "索引不可为空：" + value.toString());
            }
            boolean result = super.remove(value);
            removeIndex(value.getPkey());
            return result;
        }
        else
        {
            return false;
        }
    }
    
    private void removeIndex(K key)
    {
        // 删除索引
        indexLock.writeLock().lock();
        try
        {
            for (Set<K> ks : index.values())
            {
                if (ks.contains(key))
                {
                    ks.remove(key);
                }
            }
        }
        finally
        {
            indexLock.writeLock().unlock();
        }
    }
    
    private void addToIndex(K k, List<Idx> idx)
    {
        indexLock.writeLock().lock();
        try
        {
            for (Idx i : idx)
            {
                if (index.containsKey(i))
                {
                    index.get(i).add(k);
                }
                else
                {
                    Set<K> ks = new HashSet<K>();
                    ks.add(k);
                    index.put(i, ks);
                }
            }
        }
        finally
        {
            indexLock.writeLock().unlock();
        }
    }
    
    private boolean checkIndexDup(List<Idx> idx)
    {
        //判断是否索引是否唯一
        if (indexUniq)
        {
            indexLock.readLock().lock();
            try
            {
                for (Idx i : idx)
                {
                    if (index.containsKey(i) && (index.get(i).size() > 0))
                    {
                        return true;
                    }
                }
                return false;
            }
            finally
            {
                indexLock.readLock().unlock();
            }
        }
        else
        {
            return false;
        }
    }
    
    protected Map<K, List<Idx>> converToIdIndex(Map<K, Idx> map)
    {
        Map<K, List<Idx>> result = new HashMap<K, List<Idx>>(100);
        for (Entry<K, Idx> e : map.entrySet())
        {
            List<Idx> list = new ArrayList<Idx>();
            list.add(e.getValue());
            result.put(e.getKey(), list);
        }
        return result;
    }
    
}
