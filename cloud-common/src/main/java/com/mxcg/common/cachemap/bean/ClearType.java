package com.mxcg.common.cachemap.bean;

/**
 * 
 * 缓存队列清除规则
 * 

 */
public enum ClearType {
    /**
     * 先进先出
     */
    FIFO, 
    /**
     * 最近最常使用
     */
    LRU
}
