package com.mxcg.db.jpa.entity;

import com.mxcg.core.data.KeyValue;
import lombok.Getter;
import lombok.Setter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;



@MappedSuperclass
@Getter
@Setter
public abstract class SimpleKVEntity extends StringKeyEntity
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -766921105660372298L;
    
    @Column(length = 100, nullable=false)
    private String name;

    protected void mapKeyBaseEntity(ResultSet rs) throws SQLException
    {
        name = rs.getString("name");
    }
    
    @Override
    public KeyValue<String, String> toKeyValue()
    {
        return new KeyValue<String, String>(getPkey(), name);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(getPkey());
        builder.append("=");
        builder.append(name);
        builder.append("}");
        return builder.toString();
    }
}
