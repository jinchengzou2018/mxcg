package com.mxcg.common.cachemap.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 获取索引接口
 * 

 */
public interface HasUniqIndex<K extends Comparable<K>, Idx extends Comparable<Idx>> extends HasPkey<K>
{
    
    /**
     * 获取索引值
     * @return
     */
    @JSONField(serialize=false)
    Idx getUniqIndex();
}
