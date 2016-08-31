package ru.knowledgebase.exceptionmodule.ldapexceptions;

/**
 * Created by vova on 18.08.16.
 */
public class LdapConnectionException extends Exception{
    public LdapConnectionException() {
        super("Ldap connection exception!");
    }
}
