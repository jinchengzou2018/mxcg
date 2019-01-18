package com.mxcg.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * 集合工具集
 * @author  huhao
 * @version  [版本号, 2014-2-13]
 */
public final class CollectionUtil
{
    private CollectionUtil()
    {
    }
    
    public static boolean isEmpty(Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }
    
    public static boolean isNotEmpty(Collection<?> collection)
    {
        return !isEmpty(collection);
    }
    
    public static boolean isEmpty(Map<?, ?> map)
    {
        return map == null || map.isEmpty();
    }
    
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }
    
    public static List<?> arrayToList(Object[] source)
    {
        return Arrays.asList(source);
    }
    
    public static Object[] listToArray(Collection<?> source)
    {
        Object[] obj = new Object[source.size()];
        return source.toArray(obj);
    }
    
    /**
     * 获取子List
     * 
     * @param list
     * @param startpos 开始位置
     * @param count 数量
     * @return
     */
    public static List<?> subList(List<?> list, int startpos, int count)
    {
        if (startpos < 0)
            startpos = 0;
        List<Object> result = new ArrayList<Object>();
        for (int i = startpos; i < startpos + count; i++)
        {
            try
            {
                result.add(list.get(i));
            }
            catch (Exception e)
            {
                break;
            }
        }
        return result;
    }
    
    /**
     * 获取子List
     * 
     * @param list
     * @param beginIndex 开始位置
     * @param endIndex 结束位置 (不包含）
     * @return
     */
    public static List<?> subList2(List<?> list, int beginIndex, int endIndex)
    {
        if (beginIndex < 0)
            beginIndex = 0;
        List<Object> sublist = new ArrayList<Object>();
        for (int i = beginIndex; i < endIndex; i++)
        {
            try
            {
                sublist.add(list.get(i));
            }
            catch (Exception e)
            {
                break;
            }
        }
        return sublist;
    }
    
    public static String list2String(List<String> stringList)
    {
        if ((stringList == null) || (stringList.size() == 0))
            return "";
        String result = "";
        for (int i = 0; i < stringList.size() - 1; i++)
        {
            String s = stringList.get(i);
            result = result + s + ";";
        }
        result = result + stringList.get(stringList.size() - 1);
        return result;
    }

    public static String list2String(List<String> stringList, String split)
    {
        if ((stringList == null) || (stringList.size() == 0))
            return "";
        String result = "";
        for (int i = 0; i < stringList.size() - 1; i++)
        {
            String s = stringList.get(i);
            result = result + s + split;
        }
        result = result + stringList.get(stringList.size() - 1);
        return result;
    }
    
    public static List<String> string2List(String string)
    {
        List<String> result = new ArrayList<String>();
        if (string == null)
            return result;
        String[] list = string.split(";");
        for (String s : list)
        {
            result.add(s);
        }
        return result;
    }

    public static List<String> string2List(String string, String split)
    {
        List<String> result = new ArrayList<String>();
        if (StringUtil.isEmpty(string))
            return result;
        String[] list = string.split(split);
        for (String s : list)
        {
            result.add(s);
        }
        return result;
    }
    
    public static boolean containkey(Map<Object, Object> map, Object key)
    {
        if (map == null || key == null) return false;
        return map.containsKey(key);
    }
}
