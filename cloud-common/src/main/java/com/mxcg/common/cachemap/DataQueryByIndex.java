package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasIndex;

import java.util.List;



public interface DataQueryByIndex<K extends Comparable<K>, Idx extends Comparable<Idx>, V extends HasIndex<K, Idx>>
{

    /**
     * 通过索引获取数据
     */
    List<V> synfindByIndex(Idx idx)
        throws Exception;
    
}
