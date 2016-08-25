package ru.knowledgebase.exceptionmodule.ldapexceptions;

/**
 * Created by vova on 18.08.16.
 */
public class LdapException extends Exception {
    public LdapException() {
        super("Ldap exception!");
    }

    public LdapException(String msg) {
        super(msg);
    }
}
