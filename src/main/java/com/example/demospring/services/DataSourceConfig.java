package com.example.demospring.services;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() throws Exception {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        var config = AwsConfig.getInstance();
        
        dataSourceBuilder.url(config.postgresqlModel.dsn);
        dataSourceBuilder.username(config.postgresqlModel.username);
        dataSourceBuilder.password(config.postgresqlModel.password);
        return dataSourceBuilder.build();
    }
}