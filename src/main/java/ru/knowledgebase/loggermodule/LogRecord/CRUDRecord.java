package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.enums.OPERATION;

import java.sql.Timestamp;

/**
 * Created by Мария on 21.08.2016.
 */

/**
 * Class {@code CRUDRecord} represents a record which contains information about CRUD operations
 * made in the system.
 */
public class CRUDRecord extends ALogRecord {
    private final int articleID;

    public int getArticleID() {
        return articleID;
    }

    public CRUDRecord(OPERATION CRUDOperation, int userID, int articleID) {
        this.operationType = CRUDOperation;
        this.time = new Timestamp(System.currentTimeMillis());
		this.userID = userID;
		this.articleID = articleID;
    }

    @Override
    public String toString() {
        StringBuilder record = getRecordBase();
        record.append(articleID);
		return record.toString();
    }
}
