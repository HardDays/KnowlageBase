package ru.knowledgebase.usermodule.exceptions;

/**
 * Created by vova on 18.08.16.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(){
        super("User already exists!");
    }
}
