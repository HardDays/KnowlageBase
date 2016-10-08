package ru.knowledgebase.configmodule;

import org.json.JSONArray;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by root on 01.09.16.
 */
public class Config {
    private String logPath;

    private String uploadPath;
    private String imageFolder;

    private String ldapURI;
    private String ldapContextFactory;
    private String ldapDomain;

    private JSONArray roles;

    private static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");

    public JSONArray getRoles() {
        return roles;
    }

    public void setRoles(JSONArray roles) {
        this.roles = roles;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String imagePath) {
        this.uploadPath = imagePath;
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
}
