
package com.mxcg.db.mybatis;



import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MybatisPlus自定义填充公共字段
 */
@Component
public class MybatisMetaObjectHandler extends MetaObjectHandler {

    //TODO: 获取当前用户,格式uid:username
    private String getOperator() {
        return "1:admin";
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createAt",getOperator(),metaObject);
        setFieldValByName("createDate",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("modifyAt",getOperator(),metaObject);
        setFieldValByName("modifyDate",new Date(),metaObject);
    }
}
