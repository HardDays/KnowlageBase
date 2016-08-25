package ru.knowledgebase.exceptionmodule.userexceptions;

/**
 * Created by vova on 18.08.16.
 */
public class WrongUserDataException extends Exception {
    public WrongUserDataException(){
        super("Wrong user data!");
    }
}
