package ru.knowledgebase.exceptionmodule.roleexceptions;

/**
 * Created by vova on 21.08.16.
 */
public class AssignDefaultRoleException extends Exception {
    public AssignDefaultRoleException(){
        super("Cannot create default roles!");
    }
}
