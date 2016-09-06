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

/**
 * Class {@code DataProvider} is used to transmit records which are supposed to
 * be written to log to special service {@code DataToLogProvider} which will
 * print it to log. It maintains a queue of records {@code buffer} and keeps
 * them in queue when it's not possible to connect to {@code DataToLogProvider}.
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
        /**
         * Thread checks if connection to service is active. If service is not
         * connected or the connection was lost (field {@code isActive} is false)
         * it would wait for connection.
         */
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

        /**
         * When queue of records is not empty thread tries to send them to
         * the service. If connection was lost it would set field
         * {@code isActive} false.
         */
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

    /**
     * Adds input record {@code logRecord} to queue of records.
     * @param logRecord
     */
    public static void sendRecord(ALogRecord logRecord) {
        synchronized(logRecordQueue) {
            logRecordQueue.add(logRecord);
        }
    }

    /**
     * Sends input record to {@code DataToLogProvider} and checks weather there
     * was an error.
     * @param logRecord record to be sent.
     * @return {@code true} if record was sent and {@code false} in an error
     * occurred.
     */
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
