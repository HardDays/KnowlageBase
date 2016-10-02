package ru.knowledgebase.configmodule;

/**
 * Created by root on 01.09.16.
 */
public class Configurations {

    private static Config config = ConfigReader.getInstance().getConfig();

    private Configurations(){}

    public static String getLogFilePath() {
        return config.getLogPath();
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

}
