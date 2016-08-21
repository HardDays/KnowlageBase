package ru.knowledgebase.ldapmodule.exceptions;

import ru.knowledgebase.ldapmodule.LdapException;

/**
 * Created by vova on 18.08.16.
 */
public class LdapConnectionException extends LdapException {
    public LdapConnectionException() {
        super("Ldap connection exception!");
    }
}
