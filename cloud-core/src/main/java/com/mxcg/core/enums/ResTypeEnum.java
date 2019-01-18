/*
 * 文 件 名:  ResTypeEnum.java
 * 版    权:  XXX Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  63413
 * 修改时间:  2018年5月18日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.mxcg.core.enums;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 权限类型

 */
public enum ResTypeEnum implements IBaseDbEnum
{
//    // @formatter:off
//    // 无法查看
//    no(),
//    // user:1,本人
//    user,
//    // dept:11,本部门
//    dept,
//    // dept_ext:12,本级及下级部门
//    dept_ext,
//    // org:31,本机构
//    org,
//    // org_ext:32,本级及下级机构
//    org_ext,
//    // all:99,不限制
//    all;
//    // @formatter:on
    
    NO("无法查看",0),USER("本人",1),DEPT("本部门",11),DEPT_EXT("本级及下级部门",12),MARKET("本市场",21),MARKET_EXT("本级及下级市场",22)
    ,ORG("本机构",31),ORG_EXT("本级及下级机构",32),ALL("不限制",99);
    
    private final int index;
    
    private final String name;
 
    private ResTypeEnum(String name, int index)
    {
        this.name = name;
        this.index = index;
    }

    @Override
    public int getIndex()
    {
        return index;
    }

    @Override
    public String getName()
    {
        return name;
    }
    
    //获取枚举实例
    public static ResTypeEnum fromIndex(int index) {
        for (ResTypeEnum e : ResTypeEnum.values()) {
            if (index == e.getIndex()) {
                return e;
            }
        }
        throw new IllegalArgumentException();
    }
    
}
