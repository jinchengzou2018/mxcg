

package com.mxcg.entity.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**

 */
public enum UserStatus implements IEnum {

    //用户状态(0启用,1禁用)
    ENABLED(0,"启用"),
    LOCKED(1,"禁用");

    private int code;
    private String name;
    UserStatus(int code,String name) {
        this.code = code;
        this.name = name;
    }
    public int getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    @Override
    public Serializable getValue() {
        return this.code;
    }
}
