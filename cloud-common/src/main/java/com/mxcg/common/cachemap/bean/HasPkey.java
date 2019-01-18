package com.mxcg.common.cachemap.bean;

/**
 * 
 * 获取主键接口
 * 

 */
public interface HasPkey<K>// extends Comparable<K>>
{
    /**
     * 获取主键
     * @return
     */
    K getPkey();
    
}
