package ru.knowledgebase.authorizemodule.ldap;

/**
 * Created by vova on 13.08.16.
 */
public class WrongUsernameException extends LdapException {

    public WrongUsernameException() {
        super("Wrong LDAP username!");
    }

    public WrongUsernameException(String message) {
        super(message);
    }
}
