package ru.knowledgebase.exceptionmodule.reportexception;

/**
 * Created by Мария on 02.10.2016.
 */
public class UnableToCreateReportExeption extends Exception {
    public UnableToCreateReportExeption(){
        super("Unable to create a report. Error happened while saving report.");
    }

}
