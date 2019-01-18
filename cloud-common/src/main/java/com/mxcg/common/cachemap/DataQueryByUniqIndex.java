package com.mxcg.common.cachemap;


import com.mxcg.common.cachemap.bean.HasUniqIndex;

public interface DataQueryByUniqIndex<K extends Comparable<K>, Idx extends Comparable<Idx>, V extends HasUniqIndex<K, Idx>>
{
    /**
     * 通过唯一索引获取数据
     */
    V synfindOneByIndex(Idx idx)
        throws Exception;
}
