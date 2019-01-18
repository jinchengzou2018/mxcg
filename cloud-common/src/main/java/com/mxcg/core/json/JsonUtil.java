package com.mxcg.core.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * JSON工具
 */
public class JsonUtil
{
    /**
     * 对象转Json
     * @param o
     * @return
     */
    public static String toString(Object o)
    {
        if (o == null)
            return null;
        return JSON.toJSONString(o);
    }
    
    /**
     * 对象转Json，并格式化
     * @param o
     * @param beautifull
     * @return
     */
    public static String toString(Object o, boolean beautifull)
    {
        if (o == null)
            return null;
        return JSON.toJSONString(o, beautifull);
    }

    /**
     * Json转对象
     * @param text
     * @param clazz
     * @return
     */
    public static <T> T getBean(String text, Class<T> clazz)
    {
        if (text == null)
            return null;
        return JSONObject.parseObject(text, clazz);
    }
    
    /**
     * Json转List
     * @param text
     * @param clazz
     * @return
     */
    public static <T> List<T> getBeanList(String text, Class<T> clazz)
    {
        if (text == null)
            return null;
        return JSONObject.parseArray(text, clazz);
    }
    
    /**
     * {@link JsonObject}转对象
     * @param jsonObject
     * @param clazz
     * @return
     */
    public static <T> T getBean(JsonObject jsonObject, Class<T> clazz)
    {
        return getBean(jsonObject.toString(), clazz);
    }
}
