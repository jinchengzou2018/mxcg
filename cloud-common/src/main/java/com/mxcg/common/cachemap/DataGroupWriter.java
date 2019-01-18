package com.mxcg.common.cachemap;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.common.cachemap.write.WriteCacheGroupItem;

import java.util.List;



public interface DataGroupWriter<K, V extends HasPkey<K>>
{
    /**
     * 批量处理数据
     */
    List<V> synBatchWrite(List<WriteCacheGroupItem<K, V>> values)
        throws Exception;
}
