package ru.knowledgebase.authorizemodule.ldap;

/**
 * Created by vova on 13.08.16.
 */
public class UserAlreadyExistsException extends LdapException{

    public UserAlreadyExistsException() {
        super("Username already taken!");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
