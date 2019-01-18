package com.mxcg.common.cachemap.bean;

import java.io.Serializable;

/**
 * 通用操作
 *
 */
public enum Operate implements Serializable {
    /**
     * 删除
     */
    del,
    /**
     * 增加或修改
     */
    put
}
