package com.mxcg.common.cachemap.write;


import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.common.cachemap.bean.Operate;
import lombok.Data;

/**
 * 
 * 写缓存队列的对象
 * 
 * @author  wyw
 * @version  [版本号, 2018年3月22日]
 */
@Data
public class WriteCacheItem<K, V extends HasPkey<K>>
{

    private K key;
    
    private Operate operate;
    
    private V value;

    public WriteCacheItem(K key, Operate operate, V value)
    {
        super();
        this.key = key;
        this.operate = operate;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public Operate getOperate() {
        return operate;
    }

    public void setOperate(Operate operate) {
        this.operate = operate;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
