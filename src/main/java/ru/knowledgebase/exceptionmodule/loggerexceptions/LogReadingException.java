package ru.knowledgebase.exceptionmodule.loggerexceptions;

/**
 * Created by Мария on 02.10.2016.
 */
public class LogReadingException extends Exception {
    public LogReadingException() {
        super("Error happened while reading the log.");
    }
}
