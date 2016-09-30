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

}
