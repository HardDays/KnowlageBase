package ru.knowledgebase.responsemodule;

/**
 * Created by root on 31.08.16.
 */
public class Response {

    private int answerCode;
    private String headers;
    private String body;

    public int getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(int answerCode) {
        this.answerCode = answerCode;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
