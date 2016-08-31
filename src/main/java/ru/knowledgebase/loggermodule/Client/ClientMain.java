package ru.knowledgebase.loggermodule.Client;

/**
 * Created by Мария on 15.08.2016.
 */
public class ClientMain {
    public static void main(String[] args) {
        DataToLogProvider log = DataToLogProvider.getInstance();
        log.startProvider();
    }

}
