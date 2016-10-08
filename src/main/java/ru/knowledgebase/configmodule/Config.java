package ru.knowledgebase.configmodule;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManagerFactory;

/**
 * Created by root on 01.09.16.
 */
public class Config {
    private String logPath;

    private String imagePath;
    private String imageFolder;

    private String reportFolder;

    private String ldapURI;
    private String ldapContextFactory;
    private String ldapDomain;

    private static ApplicationContext context;



    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        }
        return context;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageFolder() {
        return imageFolder;
    }

    public void setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
    }

    public String getLdapURI() {
        return ldapURI;
    }

    public void setLdapURI(String ldapURI) {
        this.ldapURI = ldapURI;
    }

    public String getLdapContextFactory() {
        return ldapContextFactory;
    }

    public void setLdapContextFactory(String ldapContextFactory) {
        this.ldapContextFactory = ldapContextFactory;
    }

    public String getLdapDomain() {
        return ldapDomain;
    }

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
    }

    public String getReportFolder() {
        return reportFolder;
    }
}
