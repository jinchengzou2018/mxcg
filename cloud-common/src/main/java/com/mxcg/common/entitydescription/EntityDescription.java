package com.mxcg.common.entitydescription;

import java.util.HashMap;
import java.util.Map;

public class EntityDescription implements Cloneable {
    private String id;
    
    private String name;
    
    private String entity;
    
    private Map<String, EntityDescriptionField> fields = new HashMap<>();
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEntity() {
        return entity;
    }
    
    public void setEntity(String entity) {
        this.entity = entity;
    }
    
    public Map<String, EntityDescriptionField> getFields() {
        return fields;
    }
    
    public void setFields(Map<String, EntityDescriptionField> fields) {
        this.fields = fields;
    }
    
    @Override
    public EntityDescription clone() {
        EntityDescription entityDescription = new EntityDescription();
        entityDescription.id = this.id;
        entityDescription.name = this.name;
        entityDescription.entity = this.entity;
        entityDescription.fields = new HashMap<String, EntityDescriptionField>();
        for (EntityDescriptionField f : this.fields.values()) {
            entityDescription.fields.put(f.getId(), f.clone());
        }
        return entityDescription;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityDescription [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }
        if (entity != null) {
            builder.append("entity=");
            builder.append(entity);
            builder.append(", ");
        }
        if (fields != null) {
            builder.append("fields=\n");
            builder.append(fields);
        }
        builder.append("]\n");
        return builder.toString();
    }
}
