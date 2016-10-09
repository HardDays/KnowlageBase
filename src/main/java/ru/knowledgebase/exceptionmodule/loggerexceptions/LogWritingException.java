package ru.knowledgebase.exceptionmodule.loggerexceptions;

/**
 * Created by Мария on 02.10.2016.
 */
public class LogWritingException extends Exception {
    public LogWritingException(){
        super("Error happened while writing to the log");
    }
}
