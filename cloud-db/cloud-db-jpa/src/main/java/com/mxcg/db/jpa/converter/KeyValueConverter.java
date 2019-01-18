package com.mxcg.db.jpa.converter;


import com.mxcg.core.data.KeyValue;
import com.mxcg.core.json.JsonUtil;

import javax.persistence.AttributeConverter;

public class KeyValueConverter implements AttributeConverter<KeyValue<?,?>, String>
{

    @Override
    public String convertToDatabaseColumn(KeyValue<?,?> attribute)
    {
        return JsonUtil.toString(attribute);
    }

    @SuppressWarnings("unchecked")
    @Override
    public KeyValue<?,?> convertToEntityAttribute(String dbData)
    {
        return JsonUtil.getBean(dbData, KeyValue.class);
    }
    
}
