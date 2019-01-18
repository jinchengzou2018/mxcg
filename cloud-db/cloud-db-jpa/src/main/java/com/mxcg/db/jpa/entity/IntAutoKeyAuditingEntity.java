package com.mxcg.db.jpa.entity;


import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.core.data.KeyValue;
import com.mxcg.db.legency.KeyValueExportable;
import com.mxcg.jpa.AutoKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedSuperclass
@Getter
@Setter
public abstract class IntAutoKeyAuditingEntity extends AuditingEntity implements KeyValueExportable, AutoKey, HasPkey<Integer>
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1360732391226373751L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer pkey;
    
    protected void mapKeyBaseEntity(ResultSet rs) throws SQLException
    {
        pkey = rs.getInt("pkey");
    }

    @Override
    public KeyValue<Integer, ?> toKeyValue()
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
        IntAutoKeyAuditingEntity other = (IntAutoKeyAuditingEntity)obj;
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
