package ru.knowledgebase.configmodule;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by root on 01.09.16.
 */
public class Config {
    private String logPath;
    private static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");


    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
    
    public static ApplicationContext getContext() {
        return context;
    }
}
