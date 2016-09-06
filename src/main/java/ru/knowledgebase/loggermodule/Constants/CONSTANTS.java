package ru.knowledgebase.loggermodule.Constants;

/**
 * Created by Мария on 21.08.2016.
 */

/**
 * Class {@code CONSTANTS} contains all constants used for Logging
 */
public final class CONSTANTS{
    //Average size of records (number of symbols in record)
    public static final int    AVG_RECORD_SIZE     = 100;
    //Min size of buffer which is enough to write it to log
    public static final int    MIN_BUFFER_SIZE     = 1;
    //Size of buffer of BufferWriter created to write to log
    public static final int    WRITER_BUFFER_SIZE  = AVG_RECORD_SIZE * MIN_BUFFER_SIZE;

    //Separates parts of the record
    public static final String RECORD_SEPARATOR    = "#";
    //Separates records in log from each other
    public static final String INSIDE_RECORD_SEPARATOR = "%";
}
