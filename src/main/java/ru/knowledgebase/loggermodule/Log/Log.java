package ru.knowledgebase.loggermodule.Log;

import ru.knowledgebase.exceptionmodule.loggerexceptions.LogReadingException;
import ru.knowledgebase.exceptionmodule.loggerexceptions.LogWritingException;
import ru.knowledgebase.exceptionmodule.loggerexceptions.UnableToFindLogException;
import ru.knowledgebase.loggermodule.Constants.CONSTANTS;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * Created by Мария on 21.08.2016.
 */

/**
 * Works with log file: {@code logFile}.
 */
public class Log {
    private static Log ourInstance = new Log();

    private File logFile = new File("Log.txt");
    private FileWriter writer;
    private BufferedWriter bufferedWriter;
    private FileReader reader;
    private BufferedReader bufferedReader;

    public static Log getInstance(){return ourInstance;}

    /**
     * Deletes all records from {@code logFile}.
     */
    public void clearLog(){
        try {
            writer = new FileWriter(logFile);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all elements from input {@code buffer} to the {@code logFile}.
     * @param buffer
     */
    public void writeBufferToLog(Queue<String> buffer) throws Exception {

        try {
            writer = new FileWriter(logFile, true);
        } catch (IOException e) {
            throw new UnableToFindLogException();
        }
        bufferedWriter = new BufferedWriter(writer, CONSTANTS.WRITER_BUFFER_SIZE);

        try {
            while(!buffer.isEmpty())
                bufferedWriter.append(addRecordSeparator(buffer.poll()));
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            throw new LogWritingException();
        }
    }

    /**
     * Adda spacial symbol {@code RECORD_SEPARATOR} to record to separate it from other records.
     * @param record
     * @return record with {@code RECORD_SEPARATOR} in the end.
     */
    private static String addRecordSeparator(String record) {
        return record + CONSTANTS.RECORD_SEPARATOR;
    }

    /**
     * Reads all records from {@code logFile}.
     * @return a list of all records.
     */
    public List<String> getAllRecordsFromLog()
            throws UnableToFindLogException, LogReadingException {
        LinkedList<String> stringRecords = new LinkedList<>();
        try {
            reader = new FileReader(logFile);
            bufferedReader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            throw new UnableToFindLogException();
        }

        Stream<String> lines = bufferedReader.lines();
        lines.forEach(line -> addToListOfRecords(splitLine(line), stringRecords));

        try {
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            throw new LogReadingException();
        }
        return stringRecords;
    }

    /**
     * Splits input line {@code line} around matches of {@code RECORD_SEPARATOR}.
     * @param line
     * @return an array of records
     */
    private String[] splitLine(String line) {
        return line.split(CONSTANTS.RECORD_SEPARATOR);
    }

    /**
     * Adds all records from input array {@code arrayOfRecords} to the list records {@code records}.
     * @param arrayOfRecords
     * @param records
     */
    private void addToListOfRecords(String[] arrayOfRecords, LinkedList<String> records) {
        records.addAll(Arrays.asList(arrayOfRecords));
    }
}
