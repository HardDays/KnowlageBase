package ru.knowledgebase.usermodule.exceptions;

/**
 * Created by vova on 18.08.16.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User not found!");
    }
}