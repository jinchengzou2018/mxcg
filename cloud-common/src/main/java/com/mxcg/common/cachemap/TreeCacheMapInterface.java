package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasTreeIndex;

import java.util.Collection;
import java.util.List;


/**
 * 
 * 树缓存接口
 * 
 * @author  wyw
 * @version  [版本号, 2018年9月17日]
 */
public interface TreeCacheMapInterface<K, V extends HasTreeIndex<K>>
{

    /**
     * 根据主键获取值
     * @param keys 主键
     */
    public List<V> get(Collection<K> keys);

    /**
     * 根据主键获取值
     * @param key 主键
     */
    public V get(K key);
    
    /**
     * 获取所有的值
     */
    public List<V> findAll();
    
    /**
     * 获取顶节点
     */
    public List<V> getTop();
    
    /**
     * 获取父节点
     * @param nodeid 当前节点主键
     */
    public V getParentNode(K nodeid);
    
    /**
     * 获取一个节点的所有子节点
     * @param parentid 父节点主键
     */
    public List<V> getAllSubnode(K parentid);
    
    /**
     * 获取一个节点的子节点
     * @param parentid 父节点主键
     */
    public List<V> getSubnode(K parentid);
    
    /**
     * 增加一个数据
     */
    public V put(V value);
    
    /**
     * 根据主键删除一个值
     */
    public boolean removeById(K key);
    
    /**
     * 删除一个值
     */
    public boolean remove(V value);
    
    /**
     * 删除一个节点和子节点2
     * @param key 主键
     */
    public boolean removeNodeAndChildren(K key);
    
    /**
     * 获取节点的所有父节点
     * @param id 主键
     */
    public List<V> getAllParentsNode(K id);
    
}
