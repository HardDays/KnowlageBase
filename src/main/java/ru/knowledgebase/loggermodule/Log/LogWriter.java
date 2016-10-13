package ru.knowledgebase.loggermodule.Log;

import ru.knowledgebase.exceptionmodule.loggerexceptions.LogWritingException;
import ru.knowledgebase.loggermodule.Constants.CONSTANTS;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Мария on 22.08.2016.
 */
/**
 * Class {@code LogWriter} works with {@code Log}. It takes input records and puts them
 * into it's internal queue {@code buffer} and when {@code buffer} becomes large enough,
 * it writes a list of records to {@code Log}.
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

    /**
     * Takes a list of records {@code inputBuffer} and puts them into {@code buffer}. If
     * the {@code buffer} becomes bigger then {@code MIN_BUFFER_SIZE}, it writes a list
     * of records to {@code Log}.
     * @param inputBuffer
     */
    public void writeToLog(List<String> inputBuffer) throws Exception {
        buffer.addAll(inputBuffer);
        try {
            if (buffer.size() >= CONSTANTS.MIN_BUFFER_SIZE) {
                log.writeBufferToLog(buffer);
            }
        }
        catch (Exception ex) {
            throw new LogWritingException();
        }
    }
}
