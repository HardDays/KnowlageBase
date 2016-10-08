package ru.knowledgebase.configmodule;

import org.json.JSONArray;

/**
 * Created by root on 01.09.16.
 */
public class Configurations {

    private static Config config = ConfigReader.getInstance().getConfig();

    private Configurations(){}

    public static String getLogFilePath() {
        return config.getLogPath();
    }

    public static String getUploadPath() {
        return config.getUploadPath();
    }

    public static JSONArray getRoles(){
        return config.getRoles();
    }

    public static JSONArray getUsers(){
        return config.getUsers();
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
