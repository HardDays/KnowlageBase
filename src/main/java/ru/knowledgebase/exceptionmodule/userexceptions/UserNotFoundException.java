package ru.knowledgebase.exceptionmodule.userexceptions;

/**
 * Created by vova on 18.08.16.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User not found!");
    }
}