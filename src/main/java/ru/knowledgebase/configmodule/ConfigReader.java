package ru.knowledgebase.configmodule;

import ru.knowledgebase.exceptionmodule.configexceptions.ConfigFileNotFoundError;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by root on 01.09.16.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private String filePath = "/resources/config.cnf";
    private List<String> fileContent;
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
            fileContent = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        }
        catch (Exception ex) {
            throw new ConfigFileNotFoundError();
        }
    }

    private void initConfig() {
        config.setLogPath(fileContent.get(0));
    }

    //END PRIVATE METHODS

}
