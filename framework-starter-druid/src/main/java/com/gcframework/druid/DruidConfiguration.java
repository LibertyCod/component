package com.gcframework.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class DruidConfiguration {

    @Autowired
    private DatasourceProperties datasourceProperties;

    @Bean
    public DruidDataSource druidDataSource() throws SQLException {
        DruidDataSource druidDataSource = new ClosableDruidDataSource();
        druidDataSource.setUrl(datasourceProperties.getUrl());
        druidDataSource.setUsername(datasourceProperties.getUsername());
        druidDataSource.setPassword(datasourceProperties.getPassword());
        druidDataSource.setInitialSize(datasourceProperties.getInitialSize());
        druidDataSource.setMinIdle(datasourceProperties.getMinIdle());
        druidDataSource.setMaxActive(datasourceProperties.getMaxActive());
        druidDataSource.setMaxWait(datasourceProperties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(datasourceProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(datasourceProperties.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(datasourceProperties.getValidationQuery());
        druidDataSource.setTestWhileIdle(datasourceProperties.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(datasourceProperties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(datasourceProperties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(datasourceProperties.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(datasourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setFilters(datasourceProperties.getFilters());
        druidDataSource.setName("hellodruid");
        druidDataSource.setKeepAlive(datasourceProperties.isKeepAlive());
        druidDataSource.init();
        return druidDataSource;
    }
}
