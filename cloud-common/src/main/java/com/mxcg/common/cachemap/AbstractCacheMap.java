package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.core.exception.TofocusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;



/**
 * 基本缓存Map抽象类。线程安全，异步写缓存。
 * 可设置缓存最大容量。更新机制支持FIFO和LRU。
 * 初始化时加载所有主键。
 * 适用于访问频繁，独占的数据，有全量需求的数据缓存。

 */
public abstract class AbstractCacheMap<K, V extends HasPkey<K>> extends BaseCacheMap<K, V>
    implements CacheMapInterface<K, V>
{
    private Set<K> keySet = new HashSet<K>();
    
    private final ReadWriteLock keysLock = new ReentrantReadWriteLock();
    
    public AbstractCacheMap()
    {
        super();
//        reload();
    }
    
    /************************************************
     * 初始化 
     ************************************************/
    @PostConstruct
    public void reload()
    {
        keysLock.writeLock().lock();
        try
        {
            this.keySet = loadKey();
            clear();
        }
        catch (Exception e)
        {
            logException(this.getClass().getName() + " 缓存主键初始化失败", e);
            this.keySet.clear();
            throw new TofocusException(TofocusException.CacheResultCode.CACHE_INIERR,
                this.getClass().getName() + " 缓存读取初始化数据失败", e);
        }
        finally
        {
            keysLock.writeLock().unlock();
        }
    }
    
    /**
     * 加载主键
     * <功能详细描述>
     * @return
     * @throws Exception
     */
    protected abstract HashSet<K> loadKey()
        throws Exception;
    
    /************************************************
     * 属性设置 *
     ************************************************/
    /**
     * 返回数据是否为空
     */
    @Override
    public final boolean isEmpty()
    {
        keysLock.readLock().lock();
        try
        {
            return keySet.isEmpty();
        }
        finally
        {
            keysLock.readLock().unlock();
        }
    }
    
    /**
     * 返回主键的 Set视图。
     * 
     */
    public final List<K> getKeys()
    {
        keysLock.readLock().lock();
        try
        {
            List<K> result = new ArrayList<K>();
            result.addAll(keySet);
            return result;
        }
        finally
        {
            keysLock.readLock().unlock();
        }
    }
    
    /**
     * 返回数据量。
     */
    @Override
    public final int size()
    {
        keysLock.readLock().lock();
        try
        {
            return keySet.size();
        }
        finally
        {
            keysLock.readLock().unlock();
        }
    }
    
    public final int cachesize()
    {
        return super.size();
    }
    
    /************************************************
     * 数据查询 *
     ************************************************/
    /**
     * 是否包含指定键
     * 
     * @param key 主键
     */
    @Override
    public final boolean containsKey(K key)
    {
        if (key != null)
        {
            keysLock.readLock().lock();
            try
            {
                return keySet.contains(key);
            }
            finally
            {
                keysLock.readLock().unlock();
            }
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 根据一个主键返回对应的值；如果不存在，则返回 null。
     * 
     * @param key 主键
     */
    @Override
    public V get(K key)
    {
        if (key != null)
        {
            if (isReadCacheEnabled())
            {
                V result = null;
                keysLock.readLock().lock();
                try
                {
                    //判断主键是否存在
                    if (keySet.contains(key))
                    {
                        result = super.get(key);
                    }
                }
                finally
                {
                    keysLock.readLock().unlock();
                }
                return result;
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
    
    @Override
    public List<V> findAll()
    {
        if (isReadCacheEnabled())
        {
            List<K> ks = new ArrayList<K>();
            keysLock.readLock().lock();
            try
            {
                ks.addAll(keySet);
            }
            finally
            {
                keysLock.readLock().unlock();
            }
            return super.get(ks);
        }
        else
        {
            HashSet<K> keys;
            List<V> r = new ArrayList<V>();
            try
            {
                keys = loadKey();
                r.addAll(dataQuery().syngetAll(keys));
            }
            catch (Exception e)
            {
                logException(e);
            }
            return r;
        }
    }
    
    /**
     * 根据主键模糊查询, 非String类型等同于get
     * 已启用读缓存, 否则返回空列表
     * @param key
     * @return
     */
    public List<V> searchByKey(K key)
    {
        return searchByKey(key, -1);
    }
    
    /**
     * 根据主键模糊查询, 非String类型等同于get
     * 已启用读缓存, 否则返回空列表
     * @param key 主键
     * @param max 最多返回多少个结果
     * @return List结果集
     */
    public List<V> searchByKey(K key, int max)
    {
        int count = 0;
        List<V> result = new ArrayList<V>();
        Collection<K> keylist = new ArrayList<K>();
        if (key != null && isReadCacheEnabled())
        {
            keysLock.readLock().lock();
            try
            {
                if (key instanceof String)
                {
                    String skey = (String)key;
                    for (K k : keySet)
                    {
                        if (max > 0 && count >= max)
                        {
                            break;
                        }
                        if (k.toString().indexOf(skey) > -1)
                        {
                            keylist.add(k);
                            count++;
                        }
                    }
                }
                else
                {
                    if (keySet.contains(key))
                    {
                        keylist.add(key);
                    }
                }
                if (keylist.size() > 0)
                {
                    result = super.get(keylist);
                }
            }
            finally
            {
                keysLock.readLock().unlock();
            }
        }
        return result;
    }
    
    public List<V> searchByKeyStartsWith(K key)
    {
        return searchByKeyStartsWith(key, -1);
    }
    
    public List<V> searchByKeyStartsWith(K key, int max)
    {
        int count = 0;
        List<V> result = new ArrayList<V>();
        Collection<K> keylist = new ArrayList<K>();
        if (key != null && isReadCacheEnabled())
        {
            keysLock.readLock().lock();
            try
            {
                if (key instanceof String)
                {
                    String skey = (String)key;
                    for (K k : keySet)
                    {
                        if (max > 0 && count >= max)
                        {
                            break;
                        }
                        if (k.toString().startsWith(skey))
                        {
                            keylist.add(k);
                            count++;
                        }
                    }
                }
                else
                {
                    if (keySet.contains(key))
                    {
                        keylist.add(key);
                    }
                }
                if (keylist.size() > 0)
                {
                    result = super.get(keylist);
                }
            }
            finally
            {
                keysLock.readLock().unlock();
            }
        }
        return result;
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
                List<K> ks = new ArrayList<K>();
                keysLock.readLock().lock();
                try
                {
                    for (K k : keys)
                    {
                        if (keySet.contains(k))
                        {
                            ks.add(k);
                        }
                    }
                }
                finally
                {
                    keysLock.readLock().unlock();
                }
                return super.get(ks);
            }
            else
            {
                List<V> r = new ArrayList<V>();
                try
                {
                    r.addAll(dataQuery().syngetAll(keys));
                }
                catch (Exception e)
                {
                    logException(e);
                }
                return r;
            }
        }
        else
        {
            return new ArrayList<V>();
        }
    }
    
    /************************************************
     * 数据修改 *
     ************************************************/
    /**
     * 如果新增值，并自动递增主键
     * 新增成功，返回新增的值
     * 新增值为空，或主键为空，或者主键冲突，则新增失败，返回null
     * 
     * @param value
     * @return
     */
    public V add(V value)
    {
        if (value != null)
        {
            //初始化递增主键
            inikey(value);
            //主键必须非空
            if (value.getPkey() == null)
            {
                logErr("主键不可为空：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.KEY_IS_NULL, "主键不可为空：" + value.toString());
            }
            V result = null;
            keysLock.writeLock().lock();
            try
            {
                if (!keySet.contains(value.getPkey()))
                {
                    result = super.put(value);
                    // 更新主键;
                    keySet.add(value.getPkey());
                }
                else
                {
                    logErr("主键已存在：" + value.toString());
                    throw new TofocusException(TofocusException.CacheResultCode.KEY_IS_EXIST,
                        "主键已存在：" + value.toString());
                }
            }
            finally
            {
                keysLock.writeLock().unlock();
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
     * 替换值为空，替换值主键为空，被替换值不存在，则替换失败，返回null
     * 
     * @param value
     * @return
     */
    public V update(V value)
    {
        if (value != null)
        {
            //主键必须非空
            if (value.getPkey() == null)
            {
                logErr("主键不可为空：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.KEY_IS_NULL, "主键不可为空：" + value.toString());
            }
            keysLock.writeLock().lock();
            try
            {
                if (keySet.contains(value.getPkey()))
                {
                    V result = super.put(value);
                    return result;
                }
                else
                {
                    logErr("主键不存在：" + value.toString());
                    throw new TofocusException(TofocusException.CacheResultCode.KEY_IS_NOT_EXIST,
                        "主键不存在：" + value.toString());
                }
            }
            finally
            {
                keysLock.writeLock().unlock();
            }
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public V put(V value)
    {
        if (value != null)
        {
            //主键必须非空
            if (value.getPkey() == null)
            {
                logErr("主键不可为空：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.KEY_IS_NULL, "主键不可为空：" + value.toString());
            }
            keysLock.writeLock().lock();
            try
            {
                //主键存在则替换，主键不存在则增加
                if (!keySet.contains(value.getPkey()))
                {
                    V result = super.put(value);
                    // 更新主键;
                    keySet.add(value.getPkey());
                    return result;
                }
                else
                {
                    V result = super.put(value);
                    return result;
                }
            }
            finally
            {
                keysLock.writeLock().unlock();
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
            keysLock.writeLock().lock();
            try
            {
                if (keySet.contains(key))
                {
                    boolean result = super.removeById(key);
                    keySet.remove(key);
                    return result;
                }
                else
                {
                    return false;
                }
            }
            finally
            {
                keysLock.writeLock().unlock();
            }
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
     */
    @Override
    public boolean remove(V value)
    {
        if (value != null)
        {
            //主键必须非空
            if (value.getPkey() == null)
            {
                logErr("主键不可为空：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.KEY_IS_NULL, "主键不可为空：" + value.toString());
            }
            keysLock.writeLock().lock();
            try
            {
                if (keySet.contains(value.getPkey()))
                {
                    boolean result = super.removeById(value.getPkey());
                    keySet.remove(value.getPkey());
                    return result;
                }
                else
                {
                    logErr("主键不存在，删除失败：" + value.toString());
                    throw new TofocusException(TofocusException.CacheResultCode.KEY_IS_NOT_EXIST,
                        "主键不存在，删除失败：" + value.toString());
                }
            }
            finally
            {
                keysLock.writeLock().unlock();
            }
        }
        else
        {
            return false;
        }
    }
    
    public List<V> addAll(Collection<V> values)
    {
        List<V> list = new ArrayList<V>(values.size());
        for(V value : values)
        {
            V v = add(value);
            list.add(v);
        }
        return list;
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

    public List<V> updateAll(Collection<V> values)
    {
        List<V> list = new ArrayList<V>(values.size());
        for(V value : values)
        {
            V v = update(value);
            list.add(v);
        }
        return list;
    }
}
