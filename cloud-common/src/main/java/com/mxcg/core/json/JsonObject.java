package com.mxcg.core.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Set;

/**
 */
public class JsonObject
{
    private JSONObject jsonObject;

    public JsonObject()
    {
        jsonObject = new JSONObject();
    }

    public JsonObject(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    public JsonObject(String json) throws UnsupportedEncodingException
    {
        jsonObject = JSON.parseObject(URLDecoder.decode(json, "utf-8"));
    }

    public JsonObject(Object o)
    {
        jsonObject = (JSONObject) JSON.toJSON(o);
    }

    public Object get(String key)
    {
        return jsonObject.get(key);
    }

    public Object opt(String key, Object defaultValue)
    {
        Object o = get(key);
        return null == o ? defaultValue : o;
    }

    public Integer getInt(String key)
    {
        return jsonObject.getInteger(key);
    }

    public int optInt(String key, int defaultValue)
    {
        Integer i = getInt(key);
        return null == i ? defaultValue : i;
    }

    public String getString(String key)
    {
        return jsonObject.getString(key);
    }

    public String optString(String key, String defaultValue)
    {
        String s = getString(key);
        return null == s ? defaultValue : s;
    }

    public Boolean getBoolean(String key)
    {
        return jsonObject.getBoolean(key);
    }

    public boolean optBoolean(String key, boolean defaultValue)
    {
        Boolean b = getBoolean(key);
        return null == b ? defaultValue : b;
    }

    public Long getLong(String key)
    {
        return jsonObject.getLong(key);
    }

    public long optLong(String key, long defaultValue)
    {
        Long l = getLong(key);
        return null == l ? defaultValue : l;
    }

    public Double getDouble(String key)
    {
        return jsonObject.getDouble(key);
    }

    public double optDouble(String key, double defaultValue)
    {
        Double d = getDouble(key);
        return null == d ? defaultValue : d;
    }

    public JSONObject getJsonObject()
    {
        return jsonObject;
    }

    public JsonObject getJSONObject(String key)
    {
        JSONObject jo = jsonObject.getJSONObject(key);
        return null == jo ? null : new JsonObject(jo);
    }

    public JsonObject optJSONObject(String key)
    {
        JsonObject jo = getJSONObject(key);
        return null == jo ? new JsonObject() : jo;
    }

    public JsonArray getJSONArray(String key)
    {
        JSONArray ja = jsonObject.getJSONArray(key);
        return null == ja ? null : new JsonArray(ja);
    }

    public JsonArray optJSONArray(String key)
    {
        JsonArray ja = getJSONArray(key);
        return null == ja ? new JsonArray() : ja;
    }

    public Set<String> keys()
    {
        return jsonObject.keySet();
    }

    public Collection<Object> values()
    {
        return jsonObject.values();
    }

    public void put(String name, Object value)
    {
        jsonObject.put(name, value);
    }

    @Override
    public String toString()
    {
        return jsonObject.toString();
    }
}
