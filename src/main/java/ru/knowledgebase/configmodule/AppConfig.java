//package ru.knowledgebase.configmodule;
//
//import java.util.Properties;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
///**
// * Created by Мария on 09.10.2016.
// */
//@ComponentScan("ru.knowledgebase")
//public class AppConfig {
//    @Bean
////    <property name="minPoolSize" value="5" />
////        <property name="maxPoolSize" value="20" />
////        <property name="acquireIncrement" value="1" />
////        <property name="maxStatements" value="50" />
////        <property name="idleConnectionTestPeriod" value="3000" />
////        <property name="loginTimeout" value="300" />
//
//    public DataSource dataSource(){
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
//        dataSource.setUsername( "postgres" );
//        dataSource.setPassword( "11" );
//        return dataSource;
//    }
//}
