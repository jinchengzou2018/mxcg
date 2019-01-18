package com.mxcg.db.jpa.entity;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.core.data.KeyValue;
import com.mxcg.db.legency.KeyValueExportable;
import lombok.Getter;
import lombok.Setter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;



@MappedSuperclass
@Getter
@Setter
public abstract class IntKeyAuditingEntity extends AuditingEntity implements KeyValueExportable, HasPkey<Integer>
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2701139034873964075L;
    /**
     * 注释内容
     */
    @Id
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
        IntKeyAuditingEntity other = (IntKeyAuditingEntity)obj;
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
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
