package ru.knowledgebase.configmodule;

import org.springframework.context.ApplicationContext;

/**
 * Created by root on 01.09.16.
 */
public class Configurations {

    private static Config config;

    private Configurations(){}

    public static String getLogFilePath() {
        return config.getLogPath();
    }

    public static String getReportFolder() {
        return config.getReportFolder();
    }

    public static String getImageFilePath(){
        return config.getImagePath();
    }

    public static String getImageFolderPath(){
        return config.getImageFolder();
    }

    public static String getLdapURI() {
        return config.getLdapURI();
    }

    public static String getLdapContextFactory() {
        return config.getLdapContextFactory();
    }

    public static String getLdapDomain() {
        return config.getLdapDomain();
    }

    public static ApplicationContext getApplicationContext() {
        if (config == null) {
            config = ConfigReader.getInstance().getConfig();
        }
        return config.getContext();
    }

}
