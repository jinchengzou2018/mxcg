package com.mxcg.common.entitydescription;

public class EntityDescriptionField implements Cloneable{

    private String id;
    
    private String name;
    
    private Integer length;

    private DataTypeEnum datatype;
    
    private boolean show = true;
    
    private boolean nullable = true;
    
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
    
    public Integer getLength() {
        return length;
    }
    
    public void setLength(Integer length) {
        this.length = length;
    }
    
    public DataTypeEnum getDatatype() {
        return datatype;
    }

    public void setDatatype(DataTypeEnum datatype) {
        this.datatype = datatype;
    }

    public boolean isShow() {
        return show;
    }
    
    public void setShow(boolean show) {
        this.show = show;
    }
    
    public boolean isNullable() {
        return nullable;
    }
    
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public EntityDescriptionField clone(){
        EntityDescriptionField entityDescriptionField = new EntityDescriptionField();
        entityDescriptionField.id = this.id;
        entityDescriptionField.name = this.name;
        entityDescriptionField.length = this.length;
        entityDescriptionField.show = this.show;
        entityDescriptionField.nullable = this.nullable;
        return entityDescriptionField;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\t [");
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
        if (length != null) {
            builder.append("length=");
            builder.append(length);
            builder.append(", ");
        }
        if (datatype != null) {
            builder.append("datatype=");
            builder.append(datatype);
            builder.append(", ");
        }
        builder.append("show=");
        builder.append(show);
        builder.append(", nullable=");
        builder.append(nullable);
        builder.append("]\n");
        return builder.toString();
    }
}
