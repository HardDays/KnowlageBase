package ru.knowledgebase.imagemodule;

import ru.knowledgebase.configmodule.Configurations;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        String fileLocation = Configurations.getUploadPath() + filename;
        Files.copy(uploadedInputStream, Paths.get(fileLocation));
        return fileLocation;
    }
}
