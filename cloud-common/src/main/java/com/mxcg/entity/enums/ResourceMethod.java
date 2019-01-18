

package com.mxcg.entity.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**

 */
public enum ResourceMethod implements IEnum {
    //请求方式,GET/POST/DELETE/PUT,NONE(无)
    GET,POST,DELETE,PUT,NONE;

    @Override
    public Serializable getValue() {
        return this.toString();
    }
}
