package com.mxcg.db.jpa.entity;

import com.mxcg.common.cachemap.bean.HasPkey;
import com.mxcg.core.data.KeyValue;
import com.mxcg.db.legency.KeyValueExportable;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;




@MappedSuperclass
@Getter
@Setter
public abstract class StringKeyEntity extends BaseEntity implements KeyValueExportable, HasPkey<String>
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 895989577099117967L;
    @Id
    @Column(length = 40)
    private String pkey;
    
    protected void mapKeyBaseEntity(ResultSet rs) throws SQLException
    {
        pkey = rs.getString("pkey");
    }

    @Override
    public KeyValue<String, ?> toKeyValue()
    {
        return new KeyValue<>(getPkey(), null);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        StringKeyEntity other = (StringKeyEntity)obj;
        if (pkey == null)
        {
            if (other.pkey != null) return false;
        }
        else if (!pkey.equals(other.pkey)) return false;
        return true;
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
    public String toString()
    {
        return pkey;
    }
}
    
