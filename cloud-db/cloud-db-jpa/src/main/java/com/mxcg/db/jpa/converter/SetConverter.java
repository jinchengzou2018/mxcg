package com.mxcg.db.jpa.converter;

import com.mxcg.core.json.JsonUtil;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeConverter;



public class SetConverter implements AttributeConverter<Set<?>, String>
{

    @Override
    public String convertToDatabaseColumn(Set<?> attribute)
    {
        return JsonUtil.toString(attribute);
    }

    @Override
    public Set<?> convertToEntityAttribute(String dbData)
    {
        Set<?> set = JsonUtil.getBean(dbData, Set.class);
        if(set == null)
            set = new HashSet<>();
        return set;
    }
    
}
