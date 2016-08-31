package ru.knowledgebase.responsemodule;

/**
 * Created by root on 31.08.16.
 */
public class ResponseBuilder {
    public static Response getResponse() {
        return new Response();
    }

    public static Response getResponse(Exception ex) {
        return new Response();
    }

    public static Response getArtilceCreatedResponse() {
        return new Response();
    }
}
