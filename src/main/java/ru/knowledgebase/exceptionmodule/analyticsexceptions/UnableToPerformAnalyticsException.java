package ru.knowledgebase.exceptionmodule.analyticsexceptions;

/**
 * Created by Мария on 02.10.2016.
 */
public class UnableToPerformAnalyticsException extends Exception {
    public UnableToPerformAnalyticsException(){
        super(" Error happened while performing analytics");
    }
}
