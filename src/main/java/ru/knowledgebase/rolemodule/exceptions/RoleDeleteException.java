package ru.knowledgebase.rolemodule.exceptions;

/**
 * Created by vova on 25.08.16.
 */
public class RoleDeleteException extends Exception {
    public RoleDeleteException(){
        super("Cannot delete role!");
    }
}
