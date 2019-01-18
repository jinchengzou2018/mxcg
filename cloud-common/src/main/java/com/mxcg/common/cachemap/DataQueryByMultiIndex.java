package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasMultiIndex;

import java.util.List;



public interface DataQueryByMultiIndex<K, V extends HasMultiIndex<K>>
{

    V synfindOneByIndex(String indexname, Object value);

    List<V> synfindByIndex(String indexname, Object value);
    
}
