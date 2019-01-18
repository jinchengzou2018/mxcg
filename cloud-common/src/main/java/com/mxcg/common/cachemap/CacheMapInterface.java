package com.mxcg.common.cachemap;


import com.mxcg.common.cachemap.bean.HasPkey;

/**
 * 
 * 缓存Map接口
 * 
 * @author  wyw
 * @version  [版本号, 2018年3月22日]
 */
public interface CacheMapInterface<K, V extends HasPkey<K>> extends BaseCacheMapInterface<K, V>
{
    
    /**
     * 重载缓存
     */
    void reload();
}
