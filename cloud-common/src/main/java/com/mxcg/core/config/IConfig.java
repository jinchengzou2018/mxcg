package com.mxcg.core.config;

import java.util.List;
import java.util.Map;


public interface IConfig
{
    public Object get(String id);
    
    public Boolean getBoolean(String id);
    
    public Integer getInteger(String id);
    
    public String getString(String id);
    
    public List<String> getList(String id);
    
    public String getList(String id, int index, String defaultvalue);
    
    public Map<String, String> getMap(String id);
    
    public String getMap(String id, String key, String defaultvalue);
    
    public Map<String, Map<String, String>> getKVMap(String id);

    public String getKVMap(String id, String key1, String key2, String defaultvalue);
    
    public List<Map<String, String>> getKVList(String id);

    public String getKVList(String id, int index, String key, String defaultvalue);

    public Object get(String id, Object defaultvalue);
    
    public Boolean getBoolean(String id, boolean defaultvalue);
    
    public Integer getInteger(String id, int defaultvalue);
    
    public String getString(String id, String defaultvalue);
    
    public List<String> getList(String id, List<String> defaultvalue);
    
    public Map<String, String> getMap(String id, Map<String, String> defaultvalue);
    
    public Map<String, Map<String, String>> getKVMap(String id, Map<String, Map<String, String>> defaultvalue);
    
    public List<Map<String, String>> getKVList(String id, List<Map<String, String>> defaultvalue);

}
