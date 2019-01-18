

package com.mxcg.db.mybatis;


import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.sql.SQLException;

/**
 */

@Configuration
public class MyBatisPlusConfig {
    static final String MB_CONFIG = "classpath:mybatis-config.xml";
    static final String MB_MAPPER_XML = "classpath:mapper/*.xml";
    static final String MB_MAPPER_PACKAGE = "com.mxcg.Repository";
    static final String MB_MODEL_PACKAGE = "com.mxcg.entity";
    static final String MB_ENUMS_PACKAGE = "com.mxcg.entity.enums";

    @Bean(name = "defaultDataSource", initMethod = "init", destroyMethod = "close")
    @Primary
    public DruidDataSource defaultDataSource(
            @Value("${persistence.mybatis.jdbc.driver}") String driver,
            @Value("${persistence.mybatis.jdbc.url}") String url,
            @Value("${persistence.mybatis.jdbc.username}") String username,
            @Value("${persistence.mybatis.jdbc.password}") String password,
            @Value("0") Integer initialSize,
            @Value("6") Integer minIdle,
            @Value("50") Integer maxActive,
            @Value("60000") Integer maxWait,
            @Value("60000") Integer timeBetweenEvictionRunsMillis,
            @Value("300000") Integer minEvictableIdleTimeMillis,
            @Value("SELECT 'x'") String validationQuery,
            @Value("true") Boolean testWhileIdle,
            @Value("false") Boolean testOnBorrow,
            @Value("false") Boolean testOnReturn,
            @Value("false") Boolean poolPreparedStatements,
            @Value("20") Integer maxPoolPreparedStatementPerConnectionSize,
            @Value("wall,stat") String filters
            ) {

        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setInitialSize(initialSize);
            dataSource.setMinIdle(minIdle);
            dataSource.setMaxActive(maxActive);
            dataSource.setMaxWait(maxWait);
            dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            dataSource.setValidationQuery(validationQuery);
            dataSource.setTestWhileIdle(testWhileIdle);
            dataSource.setTestOnBorrow(testOnBorrow);
            dataSource.setTestOnReturn(testOnReturn);
            dataSource.setPoolPreparedStatements(poolPreparedStatements);
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
            dataSource.setFilters(filters);
            return dataSource;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }


    @Bean(name = "mybatisGlobalConfiguration")
    public GlobalConfiguration mybatisGlobalConfiguration(
            @Value("0") Integer idType,
            @Value("true") Boolean dbColumnUnderline,
            @Value("true") Boolean isCapitalMode) {



        GlobalConfiguration globalConfig = new GlobalConfiguration();
        // 主键类型
        if (idType != null) {
            globalConfig.setIdType(idType);
        }
        // 驼峰下划线转换
        if (dbColumnUnderline != null) {
            globalConfig.setDbColumnUnderline(dbColumnUnderline);
        }
        // 数据库大写下划线转换
        if (isCapitalMode != null) {
            globalConfig.setCapitalMode(isCapitalMode);
        }
        // 自动填充公用字段值
        globalConfig.setMetaObjectHandler(new MybatisMetaObjectHandler());
        return globalConfig;

    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("mybatisGlobalConfiguration") GlobalConfiguration globalConfig,
            @Qualifier("defaultDataSource") DruidDataSource dataSource) throws Exception {

        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        // 数据源
        sqlSessionFactory.setDataSource(dataSource);
        // 全局配置
        sqlSessionFactory.setGlobalConfig(globalConfig);
        Interceptor[] interceptor = {new PaginationInterceptor()};
        // 分页插件
        sqlSessionFactory.setPlugins(interceptor);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // mybatis原生配置文件
            sqlSessionFactory.setConfigLocation(resolver.getResource(MB_CONFIG));
            // 扫描Mapper文件
            sqlSessionFactory.setMapperLocations(resolver.getResources(MB_MAPPER_XML));
            // 扫描实体包
            sqlSessionFactory.setTypeAliasesPackage(MB_MODEL_PACKAGE);
            // 扫描枚举包
            sqlSessionFactory.setTypeEnumsPackage(MB_ENUMS_PACKAGE);
            return sqlSessionFactory.getObject();
        } catch (Exception e) {

        }
        return null;
    }

   @Bean(name = "mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // 扫描Mapper接口
        mapperScannerConfigurer.setBasePackage(MB_MAPPER_PACKAGE);
        return mapperScannerConfigurer;
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("defaultDataSource") DruidDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }








}
