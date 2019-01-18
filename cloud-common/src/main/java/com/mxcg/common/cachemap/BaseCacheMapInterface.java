package com.mxcg.common.cachemap;


import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.common.cachemap.read.BaseReadOnlyCacheMapInterface;

/**
 * 
 * 可写缓存Map接口
 * 

 */
public interface BaseCacheMapInterface<K, V extends HasPkey<K>>
    extends BaseReadOnlyCacheMapInterface<K, V>, CacheWriter<K, V>
{
    
    /**
     * 刷新内存数据到持久存储
     */
    void flush();
    
    /**
     * 读取写缓存的大小
     * @author wyw
     * @return
     */
    int getWriteCacheSize();
    
    
}
