package ru.knowledgebase.configmodule;

import org.json.JSONObject;
import ru.knowledgebase.exceptionmodule.configexceptions.ConfigFileNotFoundError;
import ru.knowledgebase.exceptionmodule.configexceptions.ConfigParseException;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by root on 01.09.16.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private String filePath = "/home/pisatel/Documents/Work/KnowlageBase/config.cnf";
    private String fileContent;
    private Config config = new Config();

    /**
     * ConfigReader as thread-safe singleton
     * @return
     */
    public static ConfigReader getInstance(){
        ConfigReader localInstance = instance;
        if (localInstance == null) {
            synchronized (ConfigReader.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ConfigReader();
                }
            }
        }
        return localInstance;
    }

    public Config getConfig() {
        return config;
    }

    //BEGIN PRIVATE METHODS

    private ConfigReader() {
        readFile();
        initConfig();
    }

    private void readFile(){
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        }
        catch (Exception ex) {
            throw new ConfigFileNotFoundError();
        }
    }

    private void initConfig() {
        try {
            JSONObject obj = new JSONObject(fileContent);
            config.setLogPath(obj.getString("logPath"));
            config.setImagePath(obj.getString("imagePath"));
            config.setImageFolder(obj.getString("imageFolder"));
            config.setLdapURI(obj.getString("ldapURI"));
            config.setLdapContextFactory(obj.getString("ldapContextFactory"));
            config.setLdapDomain(obj.getString("ldapDomain"));
        }catch (Exception e){
            throw new ConfigParseException();
        }
    }

    //END PRIVATE METHODS

}
