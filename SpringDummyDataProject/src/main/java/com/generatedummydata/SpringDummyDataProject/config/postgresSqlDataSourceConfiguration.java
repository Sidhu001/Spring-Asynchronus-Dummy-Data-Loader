package com.generatedummydata.SpringDummyDataProject.config;

import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgres;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager",
        basePackages = {"com.generatedummydata.SpringDummyDataProject.repository.postgresrepo"})
public class postgresSqlDataSourceConfiguration {

    @Bean(name = "postgresProperties")
    @ConfigurationProperties(prefix = "postgresql.spring.datasource")
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean(name = "postgresDataSource")
    @ConfigurationProperties(prefix = "postgresql.spring.datasource")
    public DataSource datasource(@Qualifier("postgresProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
            (EntityManagerFactoryBuilder builder,
             @Qualifier("postgresDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource)
                .packages("com/generatedummydata/SpringDummyDataProject/entity/postgres")
                .persistenceUnit("MerchantBankPostgres").build();
    }

    @Bean(name = "postgresTransactionManager")
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager transactionManager(
            @Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "postgresJdbcTemplate")
    public JdbcTemplate mysqlJdbcTemplate(
            @Qualifier("postgresDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
