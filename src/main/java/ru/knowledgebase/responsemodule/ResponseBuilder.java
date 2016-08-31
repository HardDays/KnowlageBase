package ru.knowledgebase.responsemodule;

/**
 * Created by root on 31.08.16.
 */
public class ResponseBuilder {
    public static Response getResponse() {
        return new Response();
    }

    public static Response buildResponse(Exception ex) {
        return new Response();
    }

    public static Response buildAuthorizedResponse(String token) {
        Response res = new Response();
        res.setBody(token);
        return res;
    }

    public static Response buildRegisteredResponse() {
        Response res = new Response();
        return res;
    }

    public static Response buildUserChangedResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildUserDeletedResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildUserRoleChangedResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildWrongTokenResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildNoAccessResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildArticleCreatedResponse() {
        return new Response();
    }

    public static Response buildArticleUpdatedResponse() {
        return new Response();
    }

    public static Response buildArticleDeletedResponse() {
        return new Response();
    }

    public static Response buildArticleContentResponse(String body) {
        return new Response();
    }

    public static Response buildImageCreatedResponse(String path) {
        return new Response();
    }
}
