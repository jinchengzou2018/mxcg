package com.mxcg.common.cachemap.read;

import java.util.Collection;
import java.util.List;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.jpa.FindRequest;
import org.springframework.data.domain.Page;



/**
 * 
 * 读缓存接口
 * 
 * @author  wyw
 * @version  [版本号, 2018年9月17日]
 */
public interface CacheReader<K, V extends HasPkey<K>>
{

    /**
     * 批量获取
     * @param keys 主键列表
     */
    List<V> get(Collection<K> keys);
    
    /**
     * 获取
     * @param key 主键
     */
    V get(K key);
    
    
    List<V> findAll();
    
    
    Page<V> findAll(FindRequest<V> parameter);
    
    
}
