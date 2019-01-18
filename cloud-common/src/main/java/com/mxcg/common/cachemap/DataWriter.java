package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.common.cachemap.write.WriteCacheItem;

import java.util.List;


public interface DataWriter<K extends Comparable<K>, V extends HasPkey<K>>
{
    /**
     * 批量处理数据
     */
    void synBatchWrite(List<WriteCacheItem<K, V>> values)
        throws Exception;
}
