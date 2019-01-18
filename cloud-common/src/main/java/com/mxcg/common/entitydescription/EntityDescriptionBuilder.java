package com.mxcg.common.entitydescription;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.mxcg.core.exception.TofocusException;
import com.mxcg.core.log.SimpleLog;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class EntityDescriptionBuilder {
    
    private Map<String, EntityDescription> map = new HashMap<>();
    
    private Map<String, EntityDescription> baseindex = new HashMap<>();
    
    private Map<String, Map<String, EntityDescription>> viewindex = new HashMap<>();
    
    public EntityDescriptionBuilder() {
        //扫描配置文件并分组
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath*:entitydescription/*description.xml");
            
            List<Resource> basedescriptions = new ArrayList<Resource>();
            List<Resource> viewdescriptions = new ArrayList<Resource>();
            for (Resource res : resources) {
                if (res.exists()) {
                    if (res.getFilename().endsWith("-description.xml")) {
                        basedescriptions.add(res);
                    } else if (res.getFilename().endsWith("-viewdescription.xml")) {
                        viewdescriptions.add(res);
                    }
                }
            }
            for (Resource res : basedescriptions) {
                loadcontent(map, baseindex, viewindex, res, true);
            }
            for (Resource res : viewdescriptions) {
                loadcontent(map, baseindex, viewindex, res, false);
            }
        } catch (Exception e) {
            throw new TofocusException("扫描实体描述配置文件发生错误", e);
        }
    }
    
    private void loadcontent(Map<String, EntityDescription> map, Map<String, EntityDescription> baseindex, Map<String, Map<String, EntityDescription>> viewindex, Resource res, boolean base) {
        DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
        InputStream is = null;
        try {
            DocumentBuilder dombuilder = domfac.newDocumentBuilder();
            is = res.getInputStream();
            Document doc = dombuilder.parse(is);
            //读取entitydescriptions
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    //读取entitydescription
                    Node node = nodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE && "entitydescription".equals(node.getNodeName())) {
                        String id = node.getAttributes().getNamedItem("id").getNodeValue().trim();
                        String entity = node.getAttributes().getNamedItem("entity").getNodeValue().trim();
                        if(base){
                            EntityDescription entityDescription = map.get(id);
                            if(entityDescription == null){
                                entityDescription = new EntityDescription();
                                entityDescription.setId(id);
                                entityDescription.setEntity(entity);
                            }
                            loadcontent(entityDescription, node);
                            map.put(id, entityDescription);
                            baseindex.put(entity, entityDescription);
                        }else{
                            EntityDescription entityDescription = map.get(id);
                            if(entityDescription == null){
                                //读取基础配置
                                EntityDescription baseentityDescription = baseindex.get(entity);
                                if(baseentityDescription == null){
                                    entityDescription = new EntityDescription();
                                    entityDescription.setId(id);
                                    entityDescription.setEntity(entity);
                                }else{
                                    entityDescription = baseentityDescription.clone();
                                    entityDescription.setId(id);
                                }
                            }
                            loadcontent(entityDescription, node);
                            map.put(id, entityDescription);
                            Map<String, EntityDescription> m = viewindex.get(entity);
                            if(m == null){
                                m = new HashMap<>();
                            }
                            m.put(id, entityDescription);
                            viewindex.put(entity, m);
                        }
                    }
                }
            }
        } catch (Exception e) {
            SimpleLog.outException("初始化系统参数配置失败", e);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    SimpleLog.outException("初始化系统参数配置失败", e);
                }
        }
        
    }
    
    private void loadcontent(EntityDescription entityDescription, Node node) {
        Node name = node.getAttributes().getNamedItem("name");
        if (name == null) {
            entityDescription.setName(entityDescription.getId());
        } else {
            entityDescription.setName(name.getNodeValue().trim());
        }
        
        for (Node field = node.getFirstChild(); field != null; field = field.getNextSibling()) {
            if (field.getNodeType() == Node.ELEMENT_NODE && "field".equals(field.getNodeName())) {
                EntityDescriptionField entityDescriptionField = new EntityDescriptionField();
                String fieldid = field.getAttributes().getNamedItem("id").getNodeValue().trim();
                Node fieldname = field.getAttributes().getNamedItem("name");
                Node length = field.getAttributes().getNamedItem("length");
                Node nullable = field.getAttributes().getNamedItem("nullable");
                Node isshow = field.getAttributes().getNamedItem("isshow");
                Node datatype = field.getAttributes().getNamedItem("datatype");
                entityDescriptionField.setId(fieldid);
                if (fieldname == null) {
                    entityDescriptionField.setName(fieldid);
                } else {
                    entityDescriptionField.setName(fieldname.getNodeValue().trim());
                }
                if (length != null)
                    entityDescriptionField.setLength(Integer.parseInt(length.getNodeValue().trim()));
                if (nullable != null)
                    entityDescriptionField.setNullable("true".equals(nullable.getNodeValue().trim()));
                if (isshow != null)
                    entityDescriptionField.setShow("true".equals(isshow.getNodeValue().trim()));
                if (datatype != null)
                    entityDescriptionField.setDatatype(DataTypeEnum.valueOf(datatype.getNodeValue().trim()));
                entityDescription.getFields().put(fieldid, entityDescriptionField);
            }
        }
        
    }

    public EntityDescription build(String id) {
        return map.get(id);
    }

    public EntityDescription buildbase(String entity) {
        return baseindex.get(entity);
    }

    public List<EntityDescription> buildview(String entity) {
        List<EntityDescription> list = new ArrayList<>();
        if(viewindex.containsKey(entity)){
            list.addAll(viewindex.get(entity).values());
        }
        return list;
    }

    public List<EntityDescription> buildall(String entity) {
        List<EntityDescription> list = new ArrayList<>();
        if(baseindex.containsKey(entity)){
            list.add(baseindex.get(entity));
        }
        if(viewindex.containsKey(entity)){
            list.addAll(viewindex.get(entity).values());
        }
        return list;
    }
}
