package com.mxcg.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * XML格式配置文件
 * 
 * 构造函数的参数：
 * "/" 开头的，用file方式去读取。如：/file.xml 当前目录 //file.xml当前磁盘的根目录下。/D:/1/file.xml
 * 其他的从classpath里定义的顺序读取遇到的第一个文件
 * 

 */
public class XmlConfig implements IConfig
{
    private Map<String, Object> constantMap = new LinkedHashMap<String, Object>();
    
    public XmlConfig(Resource res)
    {
        constantMap = loadContent(res);
    }
    
    private Map<String, Object> loadContent(Resource res)
    {
        Map<String, Object> constantMap = new HashMap<String, Object>();
        if (res == null)
            return constantMap;
        DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
        InputStream is = null;
        try
        {
            DocumentBuilder dombuilder = domfac.newDocumentBuilder();
            is = res.getInputStream();
            Document doc = dombuilder.parse(is);
            Element root = doc.getDocumentElement();
            NodeList mapps = root.getChildNodes();
            Map<String, List<String>> beanpropertys = new HashMap<String, List<String>>();
            if (mapps != null)
            {
                for (int i = 0; i < mapps.getLength(); i++)
                {
                    Node mapp = mapps.item(i);
                    if (mapp.getNodeType() == Node.ELEMENT_NODE)
                    {
                        if ("bean".equals(mapp.getNodeName()))
                        {
                            String id = mapp.getAttributes().getNamedItem("id").getNodeValue();
                            List<String> propertys = new ArrayList<String>();
                            for (Node node = mapp.getFirstChild(); node != null; node = node.getNextSibling())
                            {
                                if (node.getNodeType() == Node.ELEMENT_NODE)
                                {
                                    if (node.getNodeName().equals("property"))
                                    {
                                        String property =
                                            node.getAttributes().getNamedItem("name").getNodeValue().trim();
                                        propertys.add(property);
                                    }
                                }
                            }
                            beanpropertys.put(id, propertys);
                        }
                        else if ("map".equals(mapp.getNodeName()))
                        {
                            String id = mapp.getAttributes().getNamedItem("id").getNodeValue();
                            Map<String, Object> map = new HashMap<String, Object>();
                            for (Node node = mapp.getFirstChild(); node != null; node = node.getNextSibling())
                            {
                                if (node.getNodeType() == Node.ELEMENT_NODE)
                                {
                                    if (node.getNodeName().equals("entry"))
                                    {
                                        String key = node.getAttributes().getNamedItem("key").getNodeValue().trim();
                                        if (node.getAttributes().getNamedItem("bean") == null)
                                        {
                                            String value = node.getTextContent().trim();
                                            map.put(key, value);
                                        }
                                        else
                                        {
                                            String beanid =
                                                node.getAttributes().getNamedItem("bean").getNodeValue().trim();
                                            List<String> propertys = beanpropertys.get(beanid);
                                            Map<String, String> value = new HashMap<String, String>();
                                            if (propertys != null)
                                            {
                                                for (String property : propertys)
                                                {
                                                    Node n = node.getAttributes().getNamedItem(property);
                                                    if (n != null)
                                                    {
                                                        String v = n.getNodeValue().trim();
                                                        value.put(property, v);
                                                    }
                                                }
                                            }
                                            map.put(key, value);
                                        }
                                    }
                                }
                            }
                            constantMap.put(id, map);
                        }
                        else if ("list".equals(mapp.getNodeName()))
                        {
                            String id = mapp.getAttributes().getNamedItem("id").getNodeValue();
                            List<Object> list = new ArrayList<Object>();
                            for (Node node = mapp.getFirstChild(); node != null; node = node.getNextSibling())
                            {
                                if (node.getNodeType() == Node.ELEMENT_NODE)
                                {
                                    if (node.getNodeName().equals("item"))
                                    {
                                        if (node.getAttributes().getNamedItem("bean") == null)
                                        {
                                            String value = node.getTextContent().trim();
                                            list.add(value);
                                        }
                                        else
                                        {
                                            String beanid =
                                                node.getAttributes().getNamedItem("bean").getNodeValue().trim();
                                            List<String> propertys = beanpropertys.get(beanid);
                                            Map<String, String> value = new HashMap<String, String>();
                                            if (propertys != null)
                                            {
                                                for (String property : propertys)
                                                {
                                                    Node n = node.getAttributes().getNamedItem(property);
                                                    if (n != null)
                                                    {
                                                        String v = n.getNodeValue().trim();
                                                        value.put(property, v);
                                                    }
                                                }
                                            }
                                            list.add(value);
                                        }
                                    }
                                }
                            }
                            constantMap.put(id, list);
                        }
                        else if ("string".equals(mapp.getNodeName()))
                        {
                            String id = mapp.getAttributes().getNamedItem("id").getNodeValue();
                            Node node = mapp.getFirstChild();
                            if (node != null && node.getNodeType() == Node.TEXT_NODE)
                            {
                                String value = node.getTextContent().trim();
                                constantMap.put(id, value);
                            }
                        }
                        else if ("number".equals(mapp.getNodeName()))
                        {
                            String id = mapp.getAttributes().getNamedItem("id").getNodeValue();
                            Node node = mapp.getFirstChild();
                            if (node != null && node.getNodeType() == Node.TEXT_NODE)
                            {
                                String value = node.getTextContent().trim();
                                Integer v = null;
                                try
                                {
                                    v = Integer.valueOf(value);
                                }
                                catch (Exception e)
                                {
                                    
                                }
                                constantMap.put(id, v);
                            }
                        }
                        else if ("boolean".equals(mapp.getNodeName()))
                        {
                            String id = mapp.getAttributes().getNamedItem("id").getNodeValue();
                            Node node = mapp.getFirstChild();
                            if (node != null && node.getNodeType() == Node.TEXT_NODE)
                            {
                                String value = node.getTextContent().trim();
                                constantMap.put(id, Boolean.valueOf(value));
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (is != null)
                try
                {
                is.close();
                }
                catch (IOException e)
                {
                e.printStackTrace();
                }
        }
        return constantMap;
    }
    
    public void merger(XmlConfig tmp)
    {
        if (tmp != null)
        {
            for (Entry<String, Object> entry : tmp.constantMap.entrySet())
            {
                this.constantMap.put(entry.getKey(), entry.getValue());
            }
        }
        
    }
    
    @Override
    public Object get(String id)
    {
        return constantMap.get(id);
    }
    
    @Override
    public Boolean getBoolean(String id)
    {
        Object v = constantMap.get(id);
        if (v != null && v instanceof Boolean)
            return (Boolean)v;
        else if (v != null && v instanceof String)
        {
            return Boolean.valueOf((String)v);
        }
        else
            return null;
    }
    
    @Override
    public Integer getInteger(String id)
    {
        Object v = constantMap.get(id);
        if (v != null && v instanceof Integer)
            return (Integer)v;
        else if (v != null && v instanceof String)
        {
            try
            {
                return Integer.valueOf((String)v);
            }
            catch (Exception e)
            {
                return null;
            }
        }
        else
            return null;
    }
    
    @Override
    public String getString(String id)
    {
        Object v = constantMap.get(id);
        if (v != null && v instanceof String)
            return (String)v;
        else if (v != null && v instanceof Boolean)
            return String.valueOf((Boolean)v);
        else if (v != null && v instanceof Integer)
            return String.valueOf((Integer)v);
        else
            return null;
    }
    
    @Override
    public List<String> getList(String id)
    {
        Object v = constantMap.get(id);
        if (v != null && v instanceof List)
        {
            List list = (List)v;
            if (list.size() > 0)
            {
                if (list.get(0) instanceof String)
                    return (List<String>)v;
                else
                    return null;
            }
            return (List<String>)v;
        }
        else
            return null;
    }
    
    @Override
    public Map<String, String> getMap(String id)
    {
        Object v = constantMap.get(id);
        if (v != null && v instanceof Map)
        {
            Map map = (Map)v;
            if (map.size() > 0)
            {
                Object o = map.values().iterator().next();
                if (o instanceof String)
                    return (Map<String, String>)v;
                else
                    return null;
            }
            return (Map<String, String>)v;
        }
        else
            return null;
        
    }
    
    @Override
    public Map<String, Map<String, String>> getKVMap(String id)
    {
        Object v = constantMap.get(id);
        if (v != null && v instanceof Map)
        {
            Map map = (Map)v;
            if (map.size() > 0)
            {
                Object o = map.values().iterator().next();
                if (o instanceof Map)
                    return (Map<String, Map<String, String>>)v;
                else
                    return null;
            }
            return (Map<String, Map<String, String>>)v;
        }
        else
            return null;
    }
    
    @Override
    public List<Map<String, String>> getKVList(String id)
    {
        Object v = constantMap.get(id);
        if (v != null && v instanceof List)
        {
            List list = (List)v;
            if (list.size() > 0)
            {
                if (list.get(0) instanceof Map)
                    return (List<Map<String, String>>)v;
                else
                    return null;
            }
            return (List<Map<String, String>>)v;
        }
        else
            return null;
    }
    
    @Override
    public Object get(String id, Object defaultvalue)
    {
        Object v = get(id);
        if (v != null)
            return v;
        else
            return defaultvalue;
    }
    
    @Override
    public Boolean getBoolean(String id, boolean defaultvalue)
    {
        Boolean v = getBoolean(id);
        if (v != null)
            return v;
        else
            return defaultvalue;
    }
    
    @Override
    public Integer getInteger(String id, int defaultvalue)
    {
        Integer v = getInteger(id);
        if (v != null)
            return v;
        else
            return defaultvalue;
    }
    
    @Override
    public String getString(String id, String defaultvalue)
    {
        String v = getString(id);
        if (v != null)
            return v;
        else
            return defaultvalue;
    }
    
    @Override
    public List<String> getList(String id, List<String> defaultvalue)
    {
        List<String> v = getList(id);
        if (v != null)
            return v;
        else
            return defaultvalue;
    }
    
    @Override
    public Map<String, String> getMap(String id, Map<String, String> defaultvalue)
    {
        Map<String, String> v = getMap(id);
        if (v != null)
            return v;
        else
            return defaultvalue;
    }
    
    @Override
    public Map<String, Map<String, String>> getKVMap(String id, Map<String, Map<String, String>> defaultvalue)
    {
        Map<String, Map<String, String>> v = getKVMap(id);
        if (v != null)
            return v;
        else
            return defaultvalue;
    }
    
    @Override
    public List<Map<String, String>> getKVList(String id, List<Map<String, String>> defaultvalue)
    {
        List<Map<String, String>> v = getKVList(id);
        if (v != null)
            return v;
        else
            return defaultvalue;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Config [constantMap=").append(constantMap).append("]");
        return builder.toString();
    }
    
    @Override
    public String getList(String id, int index, String defaultvalue)
    {
        List<String> v = getList(id);
        if (v != null && v.size() > index)
            return v.get(index);
        else
            return defaultvalue;
    }
    
    @Override
    public String getMap(String id, String key, String defaultvalue)
    {
        Map<String, String> v = getMap(id);
        if (v != null && v.containsKey(key))
            return v.get(key);
        else
            return defaultvalue;
    }
    
    @Override
    public String getKVMap(String id, String key1, String key2, String defaultvalue)
    {
        Map<String, Map<String, String>> v = getKVMap(id);
        if (v != null && v.containsKey(key1))
        {
            Map<String, String> map = v.get(key1);
            if (map != null && map.containsKey(key2))
                return map.get(key2);
        }
        return defaultvalue;
    }
    
    @Override
    public String getKVList(String id, int index, String key, String defaultvalue)
    {
        List<Map<String, String>> v = getKVList(id);
        if (v != null && v.size() > index)
        {
            Map<String, String> map = v.get(index);
            if (map.containsKey(key))
                return map.get(key);
        }
        return defaultvalue;
    }
}
