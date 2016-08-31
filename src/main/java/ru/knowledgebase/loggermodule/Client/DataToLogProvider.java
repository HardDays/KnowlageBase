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
public class DataToLogProvider {
    private static DataToLogProvider ourInstance = new DataToLogProvider();

    private final int port = 666;
    private final String server = "localhost";

    private Queue<String> buffer = new LinkedList<>();

    public static DataToLogProvider getInstance() {
        return ourInstance;
    }


    public void startProvider() {
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

        //W THREAD
        new Thread(() -> {
            LogWriter logWriter = LogWriter.getInstance();

            while (true){
                synchronized (buffer){
                    logWriter.writeToLog((List<String>) buffer);
                }
            }
        }).start();
    }
}
