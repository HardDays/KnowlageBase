package ru.knowledgebase.loggermodule.logenums;

/**
 * Created by Мария on 21.08.2016.
 */
public final class CONSTANTS{
    public static final int    AVG_RECORD_SIZE     = 100;
    public static final int    MIN_BUFFER_SIZE     = 10;
    public static final int    WRITER_BUFFER_SIZE  = AVG_RECORD_SIZE * MIN_BUFFER_SIZE;

    public static final String RECORD_SEPARATOR    = "#";
    public static final String INSIDE_RECORD_SEPARATOR = "%";
}
