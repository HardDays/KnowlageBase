package ru.knowledgebase.exceptionmodule.converterexceptions;

/**
 * Created by vova on 05.09.16.
 */
public class ConvertException extends Exception {
    public ConvertException(){
        super("Cannot convert article!");
    }
}
