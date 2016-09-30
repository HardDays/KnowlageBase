package ru.knowledgebase.loggermodule.Log;

import ru.knowledgebase.loggermodule.logenums.CONSTANTS;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Мария on 22.08.2016.
 */
public class LogWriter {
    private static LogWriter ourInstance = new LogWriter();

    public static LogWriter getInstance() {
        return ourInstance;
    }

    private LogWriter() {
    }

    private Log log = Log.getInstance();

    Queue<String> buffer = new LinkedList<String>();

    public void writeToLog(List<String> inputBuffer) {
        buffer.addAll(inputBuffer);
        if(buffer.size() > CONSTANTS.MIN_BUFFER_SIZE){
            log.writeBufferToLog(buffer);
        }
    }

    public void writeToLog(String input) {
        buffer.add(input);
        log.writeBufferToLog(buffer);
    }
}
