package com.mxcg.db.jpa.entity;


import com.mxcg.common.util.date.DateUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public abstract class AuditingEntity extends BaseEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7330636449591829848L;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private Date createdTime;
    
    @LastModifiedDate
    @Column(name = "updated_time")
    private Date updatedTime;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 40)
    private Long createdBy;
    
    @LastModifiedBy
    @Column(name = "updated_by", updatable = false, length = 40)
    private Long updatedBy;
    
    protected void mapBaseEntity(ResultSet rs)
        throws SQLException {
        if (rs.getObject("created_by") != null)
            createdBy = rs.getLong("created_by");
        createdTime = rs.getDate("created_time");
        if (rs.getObject("updated_by") != null)
            updatedBy = rs.getLong("updated_by");
        updatedTime = rs.getDate("updated_time");
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if(getCreatedBy() != null || getCreatedTime() !=null)
        {
            builder.append(getCreatedBy());
            builder.append(" created on ");
            builder.append(DateUtil.formatDate(getCreatedTime()));
        }
        builder.append(",");
        if(getUpdatedBy() != null || getUpdatedTime() !=null)
        {
            builder.append(getUpdatedBy());
            builder.append(" updated on ");
            builder.append(DateUtil.formatDate(getUpdatedTime()));
        }
        return builder.toString();
    }
}
