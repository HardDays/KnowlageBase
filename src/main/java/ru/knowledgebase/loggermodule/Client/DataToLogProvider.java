package ru.knowledgebase.loggermodule.Client;

import ru.knowledgebase.loggermodule.Log.Log;
import ru.knowledgebase.loggermodule.Log.LogWriter;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Created by Мария on 15.08.2016.
 */

/**
 * A service that collects records about actions in the system and writes them to {@code Log}.
 */
public class DataToLogProvider {
    private static DataToLogProvider ourInstance = new DataToLogProvider();

    private final int port = 666;
    private final String server = "localhost";

    private Queue<String> buffer = new LinkedList<String>();

    public static DataToLogProvider getInstance() {
        return ourInstance;
    }


    /**
     * Starts two threads:
     * Reader thread (reads records from {@code socket} and puts them into queue{@code buffer})
     * Writer thread (takes records from {@code buffer} and sends to {@code LogWriter} to
     * be written to {@code Log}).
     */
    public void startProvider() {
        /**
         * Reader thread
         */
        new  Thread(() -> {
            try {
                InetAddress ipAddress = InetAddress.getByName(server);
                Socket socket = new Socket(ipAddress, port);

                InputStream sin = socket.getInputStream();
                DataInputStream in = new DataInputStream(sin);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while (true) {
                    String line = reader.readLine();
                    synchronized (buffer){
                        buffer.add(line);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }).start();

        /**
         * Writer thread
         */
        new Thread(() -> {
            LogWriter logWriter = LogWriter.getInstance();

            while (true){
                synchronized (buffer){
                    if(!buffer.isEmpty()){
                        logWriter.writeToLog((List<String>) buffer);
                        buffer.clear();
                    }
                }
            }
        }).start();
    }
}
