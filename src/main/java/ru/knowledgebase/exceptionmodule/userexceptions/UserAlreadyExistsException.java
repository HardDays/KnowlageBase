package ru.knowledgebase.exceptionmodule.userexceptions;

/**
 * Created by vova on 18.08.16.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(){
        super("User already exists!");
    }
}
