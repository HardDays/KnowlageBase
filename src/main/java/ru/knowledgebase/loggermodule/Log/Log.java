package ru.knowledgebase.loggermodule.Log;

import ru.knowledgebase.loggermodule.logenums.CONSTANTS;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * Created by Мария on 21.08.2016.
 */

public class Log {
    private static Log ourInstance = new Log();

    private File file = new File("/home/vova/Project BZ/KnowledgeBase/LogTest/log.txt");
    private FileWriter writer;
    private BufferedWriter bufferedWriter;
    private FileReader reader;
    private BufferedReader bufferedReader;

    public static Log getInstance(){return ourInstance;}

    public void clearLog(){
        try {
            writer = new FileWriter(file);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareToWrite(){
        try {
            writer = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferedWriter = new BufferedWriter(writer, CONSTANTS.WRITER_BUFFER_SIZE);
    }

    public void writeBufferToLog(Queue<String> buffer) {
        prepareToWrite();
        try {
            while(!buffer.isEmpty())
            bufferedWriter.append(addRecordSeparator(buffer.poll()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopWriting();
    }

    public void stopWriting(){
        try {
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String addRecordSeparator(String record) {
        return record + CONSTANTS.RECORD_SEPARATOR;
    }

    public List<String> getAllRecordsFromLog(){
        LinkedList<String> stringRecords = new LinkedList<String>();
        prepareToRead();
        Stream<String> lines = bufferedReader.lines();
        lines.forEach(line -> addToListOfRecords(splitLine(line), stringRecords));

        try {
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringRecords;
    }

    private void prepareToRead() {
        try {
            reader = new FileReader(file);
            bufferedReader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String[] splitLine(String line) {
        return line.split(CONSTANTS.RECORD_SEPARATOR);
    }

    private void addToListOfRecords(String[] arrayOfRecords, LinkedList<String> records) {
        records.addAll(Arrays.asList(arrayOfRecords));
    }
}
