package ru.knowledgebase.exceptionmodule.loggerexceptions;

/**
 * Created by Мария on 08.10.2016.
 */
public class LogAccessException extends Exception {
    public LogAccessException() {
        super("Error happened while trying to access the log.");
    }
}
