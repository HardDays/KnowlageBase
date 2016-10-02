package ru.knowledgebase.imagemodule;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by root on 31.08.16.
 */
public class ImageProcessor {


    /**
     * Save file from InputStream to real file at hard drive
     * @param uploadedInputStream - stream of byte, that we should save as image
     * @param filename
     * @return path to file in current file system
     * @throws Exception
     */
    public static String saveImage(InputStream uploadedInputStream, String filename)
                                    throws Exception {
        String fileLocation = "/home/pisatel/Documents/KBuploads/" + filename;
        //saving to file
        File yourFile = new File(fileLocation);
        if (!yourFile.exists()) {
            yourFile.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(yourFile, false);
        int read = 0;
        byte[] bytes = new byte[1024];//WHY??
        while ((read = uploadedInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();

        return fileLocation;
    }
}
