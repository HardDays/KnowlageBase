package ru.knowledgebase.loggermodule.Log;

import ru.knowledgebase.loggermodule.Constants.CONSTANTS;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;

import java.util.LinkedList;

/**
 * Class {@code LogReader} works with records from Log. It reads from log all the records
 * and then crates objects of corresponding LogRecord class.
 * Those objects are available through {@code getRecordsFromLog} method.
 */
public class LogReader {
    private static LogReader ourInstance = new LogReader();

    private Log log;
    private LinkedList<ALogRecord> logRecords;

    public LogReader getInstance(){ return ourInstance;}

    public LogReader(){
        log = Log.getInstance();
    }

    /**
     * Takes a list of string records from log and transforms them into objects of
     * {@code ALogRecord} class.
     * @return a list of records from log
     */
    public LinkedList<ALogRecord> getRecordsFromLog() throws Exception {
        logRecords = new LinkedList<ALogRecord>();

        LogRecordFactory logRecordFactory = new LogRecordFactory();
        LinkedList<String> listOfStringRecords = new LinkedList<>(log.getAllRecordsFromLog());
        for (String record : listOfStringRecords) {
            createCorrespondingLogRecord(logRecordFactory, record);
        }
        return logRecords;
    }

    /**
     * For a given {@code record} creates an object of corresponding LogRecord class.
     * @param logRecordFactory
     * @param record
     */
    private void createCorrespondingLogRecord(LogRecordFactory logRecordFactory, String record) {
        String[] recordParameters = record.split(CONSTANTS.INSIDE_RECORD_SEPARATOR);
        ALogRecord logRecord = logRecordFactory.generateRecord(recordParameters);
        if(logRecord != null){
            logRecords.add(logRecord);
        }else{
            System.out.println("ALogRecord for " + record + " can not be created");
        }
    }
}
