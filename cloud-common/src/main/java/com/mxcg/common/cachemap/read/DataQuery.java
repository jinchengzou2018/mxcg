package com.mxcg.common.cachemap.read;

import java.util.Collection;
import java.util.List;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.jpa.FindRequest;
import org.springframework.data.domain.Page;



/**
 * 
 * 数据查询接口
 * 
 * @author  wyw
 * @version  [版本号, 2018年9月17日]
 */
public interface DataQuery<K, V extends HasPkey<K>>
{
    
    /**
     * 从外部查询所有数据
     * @throws Exception 查询时可能发生异常
     */
    abstract List<V> synfindAll()
        throws Exception;
    
    /**
     * 从外部按条件查询数据
     * @param parameter 查询条件
     * @return 按分页的形式返回
     * @throws Exception 查询时可能发生异常
     */
    abstract Page<V> synfindAll(FindRequest<V> parameter)
        throws Exception;
    
    /**
     * 从外部查询数据
     * @param key 主键
     * @return 查询达到的数据
     * @throws Exception 查询时可能发生异常
     */
    abstract V synget(K key)
        throws Exception;
    
    /**
     * 从外部批量查询数据
     * @param keys 主键
     * @return 查询达到的数据
     * @throws Exception 查询时可能发生异常
     */
    abstract List<V> syngetAll(Collection<K> keys)
        throws Exception;
    
}
