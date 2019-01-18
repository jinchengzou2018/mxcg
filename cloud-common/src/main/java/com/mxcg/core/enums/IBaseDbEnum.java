package com.mxcg.core.enums;

/**
 * 
 * 枚举接口
 * <p>
 * 用于自定义非连续索引的枚举值
 * </p>
 */
public interface IBaseDbEnum
{
    /**
     * 用于显示的枚举名
     *
     * @return
     */
    String getName();
    
    /**
     * 存储到数据库的索引值
     *
     * @return
     */
    int getIndex();
    
    /**
     * 按枚举的value获取枚举实例
     * @param enumType
     * @param index
     * @return
     */
    static <T extends IBaseDbEnum> T fromIndex(Class<T> enumType, int index)
    {
        for (T object : enumType.getEnumConstants())
        {
            if (index == object.getIndex())
            {
                return object;
            }
        }
        throw new IllegalArgumentException("No enum index " + index + " of " + enumType.getCanonicalName());
    }
}
