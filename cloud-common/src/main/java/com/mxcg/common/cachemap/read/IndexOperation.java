package com.mxcg.common.cachemap.read;


import com.mxcg.common.cachemap.bean.HasPkey;

public interface IndexOperation<K, V extends HasPkey<K>>
{
    
     default void clearIndex()
    {
        
    }
    
    default void removeFromIndex(K key)
    {
        
    }

    default void removeFromIndex(V v)
    {
        
    }
    
    default void addIntoIndex(V v)
    {
        
    }
}
