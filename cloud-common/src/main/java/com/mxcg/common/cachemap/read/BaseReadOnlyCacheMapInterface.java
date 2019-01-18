package com.mxcg.common.cachemap.read;


import com.mxcg.common.cachemap.bean.ClearType;
import com.mxcg.common.cachemap.bean.HasPkey;

/**
 * 
 * 只读缓存Map接口
 * 
 * @author  wyw
 * @version  [版本号, 2018年3月22日]
 */
public interface BaseReadOnlyCacheMapInterface<K, V extends HasPkey<K>> extends CacheReader<K, V>
{

    /**
     * 是否启用读缓存
     */
    default boolean isReadCacheEnabled()
    {
        return true;
    }

    /**
     * 是否缓存Null
     */
    default boolean isCacheNull()
    {
        return true;
    }
    
    /**
     * 缓存超时时间
     */
    default long getCacheTimeout()
    {
        return 3600000L;
    }

    /**
     * 结果集缓存超时时间
     */
    default long getResultCacheTimeout()
    {
        return 600000L;
    }


    /**
     * 缓存更新策略
     */
    default ClearType getClearType()
    {
        return ClearType.LRU;
    }

    /**
     * 结果集缓存更新策略
     */
    default ClearType getResultClearRype()
    {
        return ClearType.LRU;
    }
    
    /**
     * 缓存队列大小下限
     */
    default int getMinsize()
    {
        return 500;
    }

    /**
     * 缓存队列大小上限
     */
    default int getMaxsize()
    {
        return 1000;
    }

    /**
     * 结果集缓存队列大小下限
     */
    default int getResultMinsize()
    {
        return 20;
    }

    /**
     * 结果集缓存队列大小上限
     */
    default int getResultMaxsize()
    {
        return 50;
    }
    
    /**
     * 获取集合大小
     */
    int size();

    /**
     * 是否空集合
     */
    boolean isEmpty();

    /**
     * 主键是否存在
     * @param key 主键
     */
    boolean containsKey(K key);
    
    
}
