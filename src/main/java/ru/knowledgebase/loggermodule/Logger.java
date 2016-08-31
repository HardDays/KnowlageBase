package ru.knowledgebase.loggermodule;

import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.Server.DataProvider;

/**
 * Created by root on 31.08.16.
 */
public class Logger {
    private static DataProvider dataProvider = DataProvider.getInstance();

    public static void write(ALogRecord logRecord) {
        dataProvider.sendRecord(logRecord);
    }
}
