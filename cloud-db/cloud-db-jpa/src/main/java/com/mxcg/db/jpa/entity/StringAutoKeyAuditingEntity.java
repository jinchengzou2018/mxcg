package com.mxcg.db.jpa.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.core.data.KeyValue;
import com.mxcg.db.legency.KeyValueExportable;
import com.mxcg.jpa.AutoKey;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;



@MappedSuperclass
@Getter
@Setter
public abstract class StringAutoKeyAuditingEntity extends AuditingEntity implements KeyValueExportable, AutoKey, HasPkey<String>
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5834237491931938067L;
    @Id
    @GeneratedValue(generator="idGenerator")
    @GenericGenerator(name="idGenerator", strategy="uuid2")
    @Column(length = 40)
    private String pkey;
    
    protected void mapKeyBaseEntity(ResultSet rs) throws SQLException
    {
        pkey = rs.getString("pkey");
    }

    @Override
    public KeyValue<String, ?> toKeyValue()
    {
        return new KeyValue<>(pkey, null);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((pkey == null) ? 0 : pkey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        StringAutoKeyAuditingEntity other = (StringAutoKeyAuditingEntity)obj;
        if (pkey == null)
        {
            if (other.pkey != null) return false;
        }
        else if (!pkey.equals(other.pkey)) return false;
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(pkey);
        builder.append(":[");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
    
