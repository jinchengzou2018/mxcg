package com.mxcg.Repository;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/17 13:21
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Repository
public class ComDao extends SqlSessionDaoSupport {
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
}
