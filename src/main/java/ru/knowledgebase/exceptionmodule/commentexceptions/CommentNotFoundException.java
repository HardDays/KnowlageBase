package ru.knowledgebase.exceptionmodule.commentexceptions;

/**
 * Created by vova on 01.09.16.
 */
public class CommentNotFoundException extends Exception {
    public CommentNotFoundException(){
        super("Comment not found!");
    }
}
