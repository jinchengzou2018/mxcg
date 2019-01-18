
package com.mxcg.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**

 */
@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public abstract class MpEntity<T extends Model> extends Model<T>
        implements Serializable {

    // 公用字段,插入和更新时将自动填充值
    // 详见:com.fastjee.db.mybatis.config.MybatisMetaObjectHandler
    // http://baomidou.oschina.io/mybatis-plus-doc/#/auto-fill

    @TableField(value = "createAt",fill = FieldFill.INSERT_UPDATE)
    private String createAt;

    @TableField(value = "createDate",fill = FieldFill.INSERT_UPDATE)
    private Date createDate;

    @TableField(value = "modifyAt",fill = FieldFill.INSERT_UPDATE)
    private String modifyAt;

    @TableField(value = "modifyDate",fill = FieldFill.INSERT_UPDATE)
    private Date modifyDate;

    @Override
    protected abstract Serializable pkVal();
}
