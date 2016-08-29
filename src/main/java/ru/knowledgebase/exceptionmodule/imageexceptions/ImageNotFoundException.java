package ru.knowledgebase.exceptionmodule.imageexceptions;

/**
 * Created by root on 29.08.16.
 */
public class ImageNotFoundException extends Exception {
    public ImageNotFoundException(String id) {
        super(id);
    }

    public ImageNotFoundException(){}
}
