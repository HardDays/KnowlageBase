package ru.knowledgebase.exceptionmodule.searchexceptions;

/**
 * Created by Мария on 30.09.2016.
 */
public class SearchException extends Exception {
    public SearchException() {
        super("Wrong search request!");
    }
}
