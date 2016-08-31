package ru.knowledgebase.exceptionmodule.databaseexceptions;

/**
 * Created by vova on 29.08.16.
 */
public class DataBaseException extends Exception {
    public DataBaseException(){
        super("Database exception!");
    }
    public DataBaseException(String msg){
        super(msg);
    }
}
