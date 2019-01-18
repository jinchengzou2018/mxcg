package com.mxcg.common.cachemap.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 构造树节点参数的接口
 * 

 */
public interface HasTreeIndex<K> extends HasPkey<K>
{
    /**
     * 获取父id
     * @return
     */
    @JSONField(serialize=false)
    K getParentId();
    
    /**
     * 设置父id
     * @param parentid
     */
    @JSONField(serialize=false)
    void setParentId(K parentid);
    
    /**
     * 获取级别
     * @return
     */
    @JSONField(serialize=false)
    int getDepth();
    
    /**
     * 设置深度
     * @param depth
     */
    @JSONField(serialize=false)
    void setDepth(int depth);
    
    /**
     * 是否有子节点
     * @return
     */
    @JSONField(serialize=false)
    boolean isHasChild();
    
    /**
     * 设置是否有子节点
     * @param haschild
     */
    @JSONField(serialize=false)
    void setHasChild(boolean haschild);
}
