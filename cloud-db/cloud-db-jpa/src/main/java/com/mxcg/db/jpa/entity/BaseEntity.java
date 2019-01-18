package com.mxcg.db.jpa.entity;


import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.EqualsAndHashCode;
import com.mxcg.common.util.StringUtil;
import com.mxcg.common.util.date.DateUtil;
import com.mxcg.db.legency.KeyValueExportable;
import com.mxcg.db.legency.LegencyJsonExportable;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 实体基类
 * <功能详细描述>
 * 

 */
@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
public abstract class BaseEntity implements Serializable, LegencyJsonExportable {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3905246085769235171L;
    
    @Override
    public Map<String, String> toLegencyMap() {
        JavaBeanSerializer javaBeanSerializer = new JavaBeanSerializer(this.getClass());
        Map<String, String> map = new HashMap<>();
        try {
            Map<String, Object> values = javaBeanSerializer.getFieldValuesMap(this);
            
            for (FieldInfo field : TypeUtils.computeGetters(this.getClass(), null)) {
                String key = field.name;
                Object value = values.get(key);
                if (value != null) {
                    if (value instanceof Date) {
                        map.put(key, formatDate((Date)value));
                    } else if (value instanceof KeyValueExportable) {
                        Object v = ((KeyValueExportable)value).toKeyValue().getValue();
                        if (v != null)
                            map.put(key, v.toString());
                    } else if (value instanceof BigDecimal) {
                        map.put(key, String.format("%.2f", (BigDecimal)value));
                    } else if (value instanceof List) {
                        List<String> l = new ArrayList<>();
                        for (Object v : (List<?>)value) {
                            if (v instanceof KeyValueExportable) {
                                Object name = ((KeyValueExportable)v).toKeyValue().getValue();
                                l.add(name.toString());
                            }
                        }
                        map.put(key, StringUtil.arrayToString(l, ","));
                    } else if (value instanceof BaseEntity) {
                    } else {
                        map.put(key, value.toString());
                    }
                } else {
                    map.put(key, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    
    protected String formatDate(Date date) {
        return DateUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    protected String formatLong(Long value) {
        if (value == null)
            return null;
        else
            return value.toString();
    }
    
}
