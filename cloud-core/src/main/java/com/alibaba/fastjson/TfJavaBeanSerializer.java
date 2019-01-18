package com.alibaba.fastjson;

import java.util.Map;

import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;

public class TfJavaBeanSerializer extends JavaBeanSerializer
{

    public TfJavaBeanSerializer(Class<?> beanType, Map<String, String> aliasMap)
    {
        super(beanType, aliasMap);
    }

    public TfJavaBeanSerializer(Class<?> beanType, String... aliasList)
    {
        super(beanType, aliasList);
    }

    public TfJavaBeanSerializer(Class<?> beanType)
    {
        super(beanType);
    }

    public TfJavaBeanSerializer(SerializeBeanInfo beanInfo)
    {
        super(beanInfo);
    }
    
//    public FieldInfo[] getBeanFields()
//    {
//        return beanInfo.fields;
//    }
}
