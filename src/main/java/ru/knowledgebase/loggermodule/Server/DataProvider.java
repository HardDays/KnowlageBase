package ru.knowledgebase.loggermodule.Server;

import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Мария on 15.08.2016.
 */
public class DataProvider {
    private static final int port = 666;
    private static DataInputStream in;
    private static PrintWriter out;
    private static DataProvider instance = new DataProvider();
    private static Queue<ALogRecord> logRecordQueue = new LinkedList();
    private static boolean isActive = false;
    private static ServerSocket ss;

    public static DataProvider getInstance() {
        return instance;
    }

    private DataProvider(){
        new Thread(() -> {
            try {
                ss = new ServerSocket(port);
                while(true){
                    if(!isActive){
                        Socket socket = ss.accept();
                        InputStream sin = socket.getInputStream();
                        OutputStream sout = socket.getOutputStream();

                        in = new DataInputStream(sin);
                        out = new PrintWriter(sout);

                        isActive = true;
                    }
                }
            } catch(Exception x) {
                x.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            while(true) {
                synchronized(logRecordQueue) {
                    if(!logRecordQueue.isEmpty() && isActive) {
                        ALogRecord recordToSend = logRecordQueue.peek();
                        if(isRecordSent(recordToSend)){
                            logRecordQueue.poll();
                        }else{
                            isActive = false;
                        }
                    }
                }
            }
        }).start();
    }

    public static void sendRecord(ALogRecord logRecord) {
        synchronized(logRecordQueue) {
            logRecordQueue.add(logRecord);
        }
    }

    private static boolean isRecordSent(ALogRecord logRecord) {
        out.println(logRecord.toString());
        out.flush();
        if(out.checkError()){
            return false;
        }else{
            return true;
        }
    }
}
