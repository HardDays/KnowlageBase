package ru.knowledgebase.loggermodule.Server;

import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;

/**
 * Created by root on 06.09.16.
 */

/**
 * Class {@code Logger} provides interface for working with log.
 */
public class Logger {
    Logger ourInstance = new Logger();
    private Logger(){}
    public Logger getInstance(){
        return ourInstance;
    }

    DataProvider dataProvider = DataProvider.getInstance();

    public void writeToLog(ALogRecord record){
        dataProvider.sendRecord(record);
    }
}
