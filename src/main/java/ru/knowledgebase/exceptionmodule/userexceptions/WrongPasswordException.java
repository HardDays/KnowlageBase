package ru.knowledgebase.exceptionmodule.userexceptions;

/**
 * Created by vova on 18.08.16.
 */
public class WrongPasswordException extends Exception {
    public WrongPasswordException(){
        super("Wrong password!");
    }
}
