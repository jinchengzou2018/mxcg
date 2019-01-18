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
public abstract class LongKeyEntity extends BaseEntity implements KeyValueExportable, HasPkey<Long>
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2082247026184735999L;
    /**
     * 注释内容
     */
    @Id
    private Long pkey;
    
    protected void mapKeyBaseEntity(ResultSet rs) throws SQLException
    {
        pkey = rs.getLong("pkey");
    }

    @Override
    public KeyValue<Long, ?> toKeyValue()
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
        LongKeyEntity other = (LongKeyEntity)obj;
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
        return String.valueOf(pkey);
    }
}
