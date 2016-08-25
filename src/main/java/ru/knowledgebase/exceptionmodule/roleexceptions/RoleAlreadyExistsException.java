package ru.knowledgebase.exceptionmodule.roleexceptions;

/**
 * Created by vova on 21.08.16.
 */
public class RoleAlreadyExistsException extends Exception {
    public RoleAlreadyExistsException(){
        super("Cannot create new role!");
    }
}
