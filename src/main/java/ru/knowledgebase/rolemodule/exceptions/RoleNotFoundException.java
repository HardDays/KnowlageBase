package ru.knowledgebase.rolemodule.exceptions;

/**
 * Created by vova on 21.08.16.
 */
public class RoleNotFoundException extends Exception {
    public RoleNotFoundException(){
        super("Role not found!");
    }
}
