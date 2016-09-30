package ru.knowledgebase.loggermodule.Log;

import ru.knowledgebase.loggermodule.logenums.CONSTANTS;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;

import java.util.LinkedList;

public class LogReader {
    private static LogReader ourInstance = new LogReader();

    private Log log;
    private LinkedList<ALogRecord> logRecords;

    public LogReader getInstance(){ return ourInstance;}

    public LinkedList<ALogRecord> getRecordsFromLog(){
        log = Log.getInstance();
        logRecords = new LinkedList<ALogRecord>();
        LogRecordFactory logRecordFactory = new LogRecordFactory();

        LinkedList<String> listOfStringRecords = new LinkedList<String>(log.getAllRecordsFromLog());
        for (String record : listOfStringRecords) {
            createCorrespondingLogRecord(logRecordFactory, record);
        }
        return logRecords;
    }

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
