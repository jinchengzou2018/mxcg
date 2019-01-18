package com.mxcg.common.cachemap.write;

public interface WriteCacheInterface<V>
{
    /**
     * 写缓存大小
     * @return
     */
    default int getMaxWriteCacheSize()
    {
        return 1000;
    }

    int getWriteCacheSize();

    /**
     * 把item放到写缓存，如果缓存满，则等待
     * @param item
     */
    void putIntoWriteCache(V item);
    
    public void flush();
}
