package com.mxcg.core.data;


import com.mxcg.common.cachemap.bean.HasPkey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 
 * 键值对
 * <p/>
 * <功能详细描述>
 * 

 */
public class KeyValue<K, V> implements HasPkey<K>
{

    private K key;
    
    private V value;

    public KeyValue()
    {
    }

    public KeyValue(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KeyValue<?,?> other = (KeyValue<?,?>)obj;
        if (key == null)
        {
            if (other.key != null)
                return false;
        }
        else if (!key.equals(other.key))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<");
        builder.append(key);
        builder.append(" : ");
        builder.append(value);
        builder.append(">");
        return builder.toString();
    }

    @Override
    public K getPkey()
    {
        return key;
    }

}
