package ru.knowledgebase.usermodule.exceptions;

/**
 * Created by vova on 18.08.16.
 */
public class WrongUserDataException extends Exception {
    public WrongUserDataException(){
        super("Wrong user data!");
    }
}
