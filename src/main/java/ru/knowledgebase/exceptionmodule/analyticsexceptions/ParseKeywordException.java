package ru.knowledgebase.exceptionmodule.analyticsexceptions;

/**
 * Created by vova on 11.09.16.
 */
public class ParseKeywordException extends Exception {
    public ParseKeywordException(){
        super("Cannot parse keywords");
    }

}
