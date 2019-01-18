package com.mxcg.core.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by wgj on 2017/3/22.
 */
public class JsonArray
{
    private JSONArray jsonArray;

    public JsonArray()
    {
        jsonArray = new JSONArray();
    }

    public JsonArray(JSONArray jsonArray)
    {
        this.jsonArray = jsonArray;
    }

    public JsonArray(String json) throws UnsupportedEncodingException
    {
        jsonArray = JSON.parseArray(URLDecoder.decode(json, "utf-8"));
    }

    public JsonArray(Object o)
    {
        jsonArray = (JSONArray) JSON.toJSON(o);
    }

    public Object get(int index)
    {
        return jsonArray.get(index);
    }

    public String getString(int index)
    {
        return jsonArray.getString(index);
    }

    public String optString(int index, String defaultValue)
    {
        String s = getString(index);
        return s == null ? defaultValue : s;
    }

    public Integer getInt(int index)
    {
        return jsonArray.getInteger(index);
    }

    public int optInt(int index, int defaultValue)
    {
        Integer i = getInt(index);
        return i == null ? defaultValue : i;
    }

    public JsonObject getJSONObject(int index)
    {
        JSONObject jo = jsonArray.getJSONObject(index);
        return null == jo ? null : new JsonObject(jo);
    }

    public JsonObject optJSONObject(int index)
    {
        JsonObject jo = getJSONObject(index);
        return null == jo ? new JsonObject() : jo;
    }

    public JSONArray getJsonArray()
    {
        return jsonArray;
    }

    public void add(Object ele)
    {
        jsonArray.add(ele);
    }

    public int size()
    {
        return jsonArray.size();
    }

    public String join(String separator)
    {
        StringBuilder builder = new StringBuilder();
        int size = size();
        for (int i = 0; i < size; i++)
        {
            builder.append(i > 0 ? separator : "");
            builder.append(getString(i));
        }
        return builder.toString();
    }

    @Override
    public String toString()
    {
        return jsonArray.toString();
    }
}
