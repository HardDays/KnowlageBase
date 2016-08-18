package ru.knowledgebase.usermodule.exceptions;

/**
 * Created by vova on 18.08.16.
 */
public class WrongPasswordException extends Exception {
    public WrongPasswordException(){
        super("Wrong password!");
    }
}
