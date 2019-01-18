package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasPkey;

import java.util.Collection;
import java.util.List;



/**
 * 
 * 写缓存接口
 * 
 * @author  wyw
 * @version  [版本号, 2018年9月17日]
 */
public interface CacheWriter<K, V extends HasPkey<K>>
{

    /**
     * 值不存在，就添加，值已存在，就更新
     * @author wyw
     * @param value 值
     */
    V put(V value);
    
    /**
     * 批量添加或更新值
     * @param values 值
     */
    List<V> putAll(Collection<V> values);
    
    /**
     * 通过主键删除一个值
     * @author wyw
     * @param key 主键
     */
    boolean removeById(K key);

    /**
     * 通过主键批量删除值
     * @param keys 主键
     */
    boolean removeAllById(Collection<K> keys);
    
    /**
     * 删除一个值
     * @author wyw
     * @param value 值
     */
    boolean remove(V value);

    /**
     * 批量删除值
     * @param values 值
     */
    boolean removeAll(Collection<V> values);
}
