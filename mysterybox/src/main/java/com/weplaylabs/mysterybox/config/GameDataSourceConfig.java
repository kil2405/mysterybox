package com.weplaylabs.mysterybox.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(
    basePackages = "com.weplaylabs.mysterybox.repository.game",
    entityManagerFactoryRef = "masterEntityManager", 
    transactionManagerRef = "masterTransactionManager"
)
public class GameDataSourceConfig {
    @Autowired
    Environment env;

    @Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean masterEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(masterDataSource());
        em.setPackagesToScan(new String[] {"com.weplaylabs.mysterybox.model.game"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> prop = new HashMap<>();
        prop.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        prop.put("hibernate.use_sql_comments", env.getProperty("spring.jpa.properties.use_sql_comments"));
        prop.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        prop.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
        prop.put("hibernate.show_sql", env.getProperty("spring.jpa.properties.hibernate.show_sql"));
        em.setJpaPropertyMap(prop);

        return em;
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager masterTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(masterEntityManager().getObject());
        return transactionManager;
    }
}
