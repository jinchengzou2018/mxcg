package com.mxcg.common.cachemap.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public interface HasMultiIndex<K> extends HasPkey<K>
{

    /**
     * 获取索引值
     * @return
     */
    @JSONField(serialize=false)
    default Object getUniqIndex(String index)
    {
        return null;
    }

    default String[] uniqIndexes()
    {
        return new String[0];
    }
    
    /**
     * 获取索引值
     * @return
     */
    @JSONField(serialize=false)
    default List<?> getIndex(String index)
    {
        return null;
    }
    
    default String[] indexes()
    {
        return new String[0];
    }

}
