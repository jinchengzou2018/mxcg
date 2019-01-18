package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasTreeIndex;
import com.mxcg.core.exception.TofocusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;



/**
 * 带树索引缓存Map抽象类。线程安全，异步写缓存。
 * 可设置缓存最大容量。更新机制支持FIFO和LRU。
 * 初始化时加载所有主键和索引和树索引。
 * 适用于访问频繁，独占的数据，有全量需求，有快速索引和树结构索引需求的数据缓存。

 */
public abstract class AbstractTreeCacheMap<K extends Comparable<K>, V extends HasTreeIndex<K>>
    extends AbstractCacheMap<K, V> implements TreeCacheMapInterface<K, V>
{
    private HashMap<K, Set<K>> treeMap;
    
    private HashMap<K, K> parentMap;
    
    private ReadWriteLock treemapLock;
    
    private int maxDepth = 1000;
    
    private K rootId = null;
    
    /************************************************
     * 初始化 
     * @throws CacheException *
     ************************************************/
    @Override
    protected final HashSet<K> loadKey()
    {
        if (null == treemapLock)
        {
            treemapLock = new ReentrantReadWriteLock();
        }
        treemapLock.writeLock().lock();
        try
        {
            List<? extends HasTreeIndex<K>> data = null;
            try
            {
                data = initKey();
            }
            catch (Exception e)
            {
                logException(this.getClass().getName() + " 缓存读取初始化数据失败", e);
                throw new TofocusException(TofocusException.CacheResultCode.CACHE_INIERR,
                    this.getClass().getName() + " 缓存读取初始化数据失败", e);
            }
            HashSet<K> result = new HashSet<K>(100);
            HashMap<K, Set<K>> tmap = new HashMap<K, Set<K>>(100);
            HashMap<K, K> tparentmap = new HashMap<K, K>(100);
            if (data != null)
            {
                for (HasTreeIndex<K> d : data)
                {
                    result.add(d.getPkey());
                    //排除自引用节点
                    if (d.getPkey().equals(d.getParentId()))
                    {
                        d.setParentId(null);
                    }
                    tparentmap.put(d.getPkey(), d.getParentId());
                    if (!tmap.containsKey(d.getPkey()))
                    {
                        Set<K> k = new HashSet<K>();
                        tmap.put(d.getPkey(), k);
                    }
                    // 重建树结构
                    if (d.getParentId() != null)
                    {
                        if (tmap.containsKey(d.getParentId()))
                        {
                            tmap.get(d.getParentId()).add(d.getPkey());
                        }
                        else
                        {
                            Set<K> k = new HashSet<K>();
                            k.add(d.getPkey());
                            tmap.put(d.getParentId(), k);
                        }
                    }
                }
            }
            treeMap = tmap;
            parentMap = tparentmap;
            return result;
        }
        finally
        {
            treemapLock.writeLock().unlock();
        }
    }
    
    /**
     * 加载数据
     * <功能详细描述>
     * @return
     * @throws Exception
     */
    protected abstract List<? extends HasTreeIndex<K>> initKey()
        throws Exception;
    
    @Override
    public List<V> get(Collection<K> keys)
    {
        treemapLock.readLock().lock();
        try
        {
            Collection<V> list = super.get(keys);
            return updateCalValues(list);
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
    }
    
    @Override
    public V get(K key)
    {
        treemapLock.readLock().lock();
        try
        {
            V result = super.get(key);
            if (null != result)
            {
                result = updateCalValue(result);
            }
            return result;
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
    }
    
    @Override
    public List<V> findAll()
    {
        treemapLock.readLock().lock();
        try
        {
            //取所有值
            List<V> list = super.findAll();
            List<V> caledlist = updateCalValues(list);
            return caledlist;
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
    }
    
    /**
     * 获取顶节点
     * @return
     */
    @Override
    public List<V> getTop()
    {
        treemapLock.readLock().lock();
        try
        {
            List<K> topk = new ArrayList<K>();
            for (Entry<K, K> kv : parentMap.entrySet())
            {
                if ((kv.getValue() == null) || (kv.getValue().equals(rootId)))
                {
                    topk.add(kv.getKey());
                }
            }
            List<V> result = get(topk);
            return result;
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
    }
    
    @Override
    public V getParentNode(K nodeid)
    {
        treemapLock.readLock().lock();
        try
        {
            K parentid = parentMap.get(nodeid);
            if ((null == parentid) || (parentid.equals(rootId)))
            {
                return null;
            }
            else
            {
                return get(parentid);
            }
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
    }
    
    /**
     * 获取一个节点的所有子节点
     * @param parentid
     * @return
     */
    @Override
    public List<V> getAllSubnode(K parentid)
    {
        List<V> result = new ArrayList<V>();
        //从顶排序
        List<V> listTop = getSubnode(parentid);
        for (V top : listTop)
        {
            result.add(top);
            List<K> ids = getAllChildrenId(top.getPkey());
            result.addAll(get(ids));
        }
        return result;
    }
    
    /**
     * 获取一个节点的子节点
     * @param parentid
     * @return
     */
    @Override
    public List<V> getSubnode(K parentid)
    {
        treemapLock.readLock().lock();
        try
        {
            Set<K> childs = treeMap.get(parentid);
            List<V> result = get(childs);
            return result;
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
    }
    
    @Override
    public V add(V value)
    {
        treemapLock.writeLock().lock();
        try
        {
            if (!checkParentId(value))
            {
                logErr("父节点不存在：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.PARENT_NODE_IS_NOT_EXIST,
                    "父节点不存在：" + value.toString());
            }
            if (value.getPkey().equals(value.getParentId()))
            {
                logErr("父节点存在死循环：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.PARENTS_ID_IS_CYCLE,
                    "父节点存在死循环：" + value.toString());
            }
            V result = super.add(value);
            if (null != result)
            {
                putIntoTreeindex(result);
            }
            return result;
        }
        finally
        {
            treemapLock.writeLock().unlock();
        }
    }
    
    private boolean checkParentId(V value)
    {
        K pid = value.getParentId();
        if ((pid == null) || pid.equals(rootId))
        {
            return true;
        }
        else if (super.containsKey(pid))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public V update(V value)
    {
        treemapLock.writeLock().lock();
        try
        {
            if (!checkParentId(value))
            {
                logErr("父节点不存在：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.PARENT_NODE_IS_NOT_EXIST,
                    "父节点不存在：" + value.toString());
            }
            if (value.getPkey().equals(value.getParentId()))
            {
                logErr("父节点存在死循环：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.PARENTS_ID_IS_CYCLE,
                    "父节点存在死循环：" + value.toString());
            }
            K pid = value.getParentId();
            List<K> pids = getAllParentsId(pid, 0);
            for (K id : pids)
            {
                if (value.getPkey().equals(id))
                {
                    logErr("新的父节点存在死循环：" + value.toString());
                    throw new TofocusException(TofocusException.CacheResultCode.PARENTS_ID_IS_CYCLE,
                        "新的父节点存在死循环：" + value.toString());
                }
            }
            V result = super.update(value);
            if (null != result)
            {
                changeNode(result);
            }
            return result;
        }
        finally
        {
            treemapLock.writeLock().unlock();
        }
    }
    
    @Override
    public V put(V value)
    {
        treemapLock.writeLock().lock();
        try
        {
            if (!checkParentId(value))
            {
                logErr("父节点不存在：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.PARENT_NODE_IS_NOT_EXIST,
                    "父节点不存在：" + value.toString());
            }
            if (value.getPkey().equals(value.getParentId()))
            {
                logErr("父节点存在死循环：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.PARENTS_ID_IS_CYCLE,
                    "父节点存在死循环：" + value.toString());
            }
            
            if (containsKey(value.getPkey()))
            {
                K pid = value.getParentId();
                List<K> pids = getAllParentsId(pid, 0);
                for (K id : pids)
                {
                    if (value.getPkey().equals(id))
                    {
                        logErr("新的父节点存在死循环：" + value.toString());
                        throw new TofocusException(TofocusException.CacheResultCode.PARENTS_ID_IS_CYCLE,
                            "新的父节点存在死循环：" + value.toString());
                    }
                }
                V result = super.update(value);
                
                if (null != result)
                {
                    changeNode(result);
                }
                return result;
            }
            else
            {
                V result = super.add(value);
                if (null != result)
                {
                    putIntoTreeindex(result);
                }
                return result;
            }
        }
        finally
        {
            treemapLock.writeLock().unlock();
        }
    }
    
    public boolean removeNodeAndChildren(K key)
    {
        try
        {
            List<K> ids = getAllChildrenId(key);
            Collections.reverse(ids);
            for (K k : ids)
            {
                removeById(k);
            }
            removeById(key);
        }
        catch (Exception e)
        {
            logErr("删除节点失败, id=" + key);
            throw new TofocusException(TofocusException.CacheResultCode.DELETE_NODE_FAIL, "删除节点失败, id=" + key, e);
        }
        return true;
    }
    
    @Override
    public boolean removeById(K key)
    {
        treemapLock.writeLock().lock();
        try
        {
            if (hasChild(key))
            {
                logErr("存在子节点，不能删除：" + key.toString());
                throw new TofocusException(TofocusException.CacheResultCode.DELETE_NODE_FAIL,
                    "存在子节点，不能删除：" + key.toString());
            }
            boolean result = super.removeById(key);
            if (result)
            {
                removeFromTreeindex(key);
            }
            return result;
        }
        finally
        {
            treemapLock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean remove(V value)
    {
        treemapLock.writeLock().lock();
        try
        {
            if (value == null)
            {
                logErr("数据是空，不能删除：" + value);
                throw new TofocusException(TofocusException.CacheResultCode.DELETE_NODE_FAIL, "数据是空，不能删除：" + value);
            }
            if (hasChild(value.getPkey()))
            {
                logErr("存在子节点，不能删除：" + value.toString());
                throw new TofocusException(TofocusException.CacheResultCode.DELETE_NODE_FAIL,
                    "存在子节点，不能删除：" + value.toString());
            }
            boolean result = super.remove(value);
            if (result)
            {
                removeFromTreeindex(value.getPkey());
            }
            return result;
        }
        finally
        {
            treemapLock.writeLock().unlock();
        }
    }
    
    /**
     * 循环读取子节点
     * <功能详细描述>
     * @param key
     * @param sort
     * @return
     */
    private List<K> getAllChildrenId(K key)
    {
        treemapLock.readLock().lock();
        try
        {
            List<K> result = new ArrayList<K>();
            List<K> childs = new ArrayList<K>();
            childs.addAll(treeMap.get(key));
            for (K k : childs)
            {
                result.add(k);
                List<K> mychilds = getAllChildrenId(k);
                for (K k1 : mychilds)
                {
                    result.add(k1);
                }
            }
            return result;
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
    }
    
    private void putIntoTreeindex(V value)
    {
        if (value != null)
        {
            K k = value.getPkey();
            K pid = value.getParentId();
            Set<K> ks = new HashSet<K>();
            treeMap.put(k, ks);
            parentMap.put(k, pid);
            if (null != pid)
            {
                if (treeMap.containsKey(pid))
                {
                    treeMap.get(pid).add(k);
                }
            }
        }
    }
    
    private void removeFromTreeindex(K key)
    {
        if (key != null)
        {
            //移除子节点集合
            if (treeMap.containsKey(key))
            {
                treeMap.remove(key);
            }
            //从父节点的子节点集合中移除node id
            K pid = parentMap.get(key);
            if (null != pid)
            {
                if (treeMap.containsKey(pid))
                {
                    treeMap.get(pid).remove(key);
                }
            }
            parentMap.remove(key);
        }
    }
    
    private void changeNode(V node)
    {
        if (node != null)
        {
            K k = node.getPkey();
            K pid = node.getParentId();
            K oldpid = parentMap.get(node.getPkey());
            if (((pid == null) && (oldpid != null)) || ((pid != null) && (!pid.equals(oldpid))))
            {
                //从父节点的子节点集合中移除node id
                if (null != oldpid)
                {
                    if (treeMap.containsKey(oldpid))
                    {
                        treeMap.get(oldpid).remove(k);
                    }
                }
                //加入node到新父节点的子节点集合
                if (null != pid)
                {
                    if (treeMap.containsKey(pid))
                    {
                        treeMap.get(pid).add(k);
                    }
                }
                parentMap.put(k, pid);
            }
        }
    }
    
    private boolean hasChild(K key)
    {
        if (key != null)
        {
            boolean result = false;
            Set<K> childkeys = null;
            treemapLock.readLock().lock();
            try
            {
                childkeys = treeMap.get(key);
                if ((childkeys != null) && (childkeys.size() > 0))
                {
                    result = true;
                }
            }
            finally
            {
                treemapLock.readLock().unlock();
            }
            return result;
        }
        else
        {
            return false;
        }
    }
    
    private int calculateDepth(K id)
    {
        int result = -1;
        treemapLock.readLock().lock();
        try
        {
            List<K> parentsid = getAllParentsId(id, 0);
            result = parentsid.size() + 1;
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
        return result;
    }
    
    private List<V> updateCalValues(Collection<V> list)
    {
        List<V> result = new ArrayList<V>();
        for (V v : list)
        {
            V r = updateCalValue(v);
            if (r != null)
            {
                result.add(r);
            }
        }
        return result;
    }
    
    private V updateCalValue(V value)
    {
        value.setHasChild(hasChild(value.getPkey()));
        value.setDepth(calculateDepth(value.getPkey()));
        return value;
    }
    
    private List<K> getAllParentsId(K id, int count)
    {
        treemapLock.readLock().lock();
        try
        {
            K parentid = parentMap.get(id);
            if (!((null == parentid) || parentid.equals(rootId)))
            {
                if (count > maxDepth)
                {
                    throw new TofocusException(TofocusException.CacheResultCode.PARENTS_ID_IS_CYCLE, "父节点存在死循环：" + id);
                }
                List<K> result = getAllParentsId(parentid, count + 1);
                result.add(parentid);
                return result;
            }
            else
            {
                List<K> result = new ArrayList<K>();
                return result;
            }
        }
        finally
        {
            treemapLock.readLock().unlock();
        }
    }
    
    public List<V> getAllParentsNode(K id)
    {
        List<K> list = getAllParentsId(id, 0);
        List<V> kvs = new ArrayList<V>();
        for (K k : list)
        {
            kvs.add(get(k));
        }
        return kvs;
    }
    
    public K getRootid()
    {
        return rootId;
    }
    
    public void setRootid(K rootid)
    {
        this.rootId = rootid;
    }
}
