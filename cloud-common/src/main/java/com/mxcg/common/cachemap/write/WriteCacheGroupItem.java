package com.mxcg.common.cachemap.write;

import java.util.ArrayList;
import java.util.List;


import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.common.cachemap.bean.Operate;
import com.mxcg.core.data.KeyValue;
import lombok.Data;

@Data
public class WriteCacheGroupItem<K, V extends HasPkey<K>>
{
    
    private Operate operate;
    
    private List<KeyValue<K, V>> kvlist;
    
    public WriteCacheGroupItem(WriteCacheItem<K, V> item)
    {
        operate = item.getOperate();
        kvlist = new ArrayList<KeyValue<K, V>>();
        kvlist.add(new KeyValue<K, V>(item.getKey(), item.getValue()));
    }
    
    public void add(WriteCacheItem<K, V> item)
    {
        kvlist.add(new KeyValue<K, V>(item.getKey(), item.getValue()));
    }

    public Operate getOperate() {
        return operate;
    }

    public void setOperate(Operate operate) {
        this.operate = operate;
    }

    public List<KeyValue<K, V>> getKvlist() {
        return kvlist;
    }

    public void setKvlist(List<KeyValue<K, V>> kvlist) {
        this.kvlist = kvlist;
    }
}
