package ru.knowledgebase.loggermodule.Server;

import ru.knowledgebase.loggermodule.Client.DataToLogProvider;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;

import java.sql.Timestamp;
import java.util.Random;

/**
 * Created by Мария on 15.08.2016.
 */
public class ServerMain {

    public static void main(String[] args) {

        DataToLogProvider log = DataToLogProvider.getInstance();
        log.startProvider();
        DataProvider dataProvider = DataProvider.getInstance();

        while (true){
            for (int i = 0; i < 5; i++){

                Timestamp param1 = new Timestamp(System.currentTimeMillis());
                int     param2 = 2;
                String  param3 = "Search request";

                ALogRecord record = new LogRecordFactory().generateSearchRequestRecord(
                        param1, param2, param3);

                dataProvider.sendRecord(record);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
