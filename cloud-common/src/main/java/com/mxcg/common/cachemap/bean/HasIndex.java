package com.mxcg.common.cachemap.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 获取索引接口
 *
 */
public interface HasIndex<K, Idx extends Comparable<Idx>> extends HasPkey<K>
{
    
    /**
     * 获取索引值
     * @return
     */
    @JSONField(serialize=false)
    List<Idx> getIndex();
}
