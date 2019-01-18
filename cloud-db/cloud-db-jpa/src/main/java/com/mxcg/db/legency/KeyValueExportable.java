package com.mxcg.db.legency;


import com.mxcg.core.data.KeyValue;

public interface KeyValueExportable
{
    KeyValue<?, ?> toKeyValue();
}
