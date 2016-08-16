package ru.knowladgebase.authorizemodule.ldap;

/**
 * Created by vova on 13.08.16.
 */
public class WrongPasswordException extends LdapException {

    public WrongPasswordException() {
        super("Wrong LDAP password!");
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
