package ru.knowledgebase.exceptionmodule.loggerexceptions;

import org.docx4j.wml.U;

/**
 * Created by Мария on 02.10.2016.
 */
public class UnableToFindLogException extends Exception {
    public UnableToFindLogException(){
        super("Log file not found.");
    }
}
