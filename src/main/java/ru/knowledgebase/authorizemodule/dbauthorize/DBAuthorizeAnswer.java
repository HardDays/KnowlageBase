package ru.knowledgebase.authorizemodule.dbauthorize;

/**
 * Created by vova on 16.08.16.
 */
public enum DBAuthorizeAnswer {
    OK, CONNECTION_ERROR, UNKNOWN_ERROR, WRONG_PASSWORD, EMPTY_PASSWORD, WRONG_UID, EMPTY_UID, USER_ALREADY_EXISTS;
}
