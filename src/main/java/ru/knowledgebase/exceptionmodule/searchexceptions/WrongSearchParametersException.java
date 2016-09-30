package ru.knowledgebase.exceptionmodule.searchexceptions;

/**
 * Created by Мария on 30.09.2016.
 */
public class WrongSearchParametersException extends Exception {
    public WrongSearchParametersException() {
        super("Wrong search parameters!");
    }
}
