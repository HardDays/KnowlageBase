package ru.knowladgebase.authorizemodule.ldap;

/**
 * Created by vova on 13.08.16.
 */
public class NoConnectionException extends  LdapException {

    public NoConnectionException() {
        super("No connection to LDAP!");
    }

    public NoConnectionException(String message) {
        super(message);
    }
}
