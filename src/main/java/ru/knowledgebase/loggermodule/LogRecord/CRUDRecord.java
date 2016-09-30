package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.logenums.OPERATION;

import java.sql.Timestamp;

/**
 * Created by Мария on 21.08.2016.
 */
public class CRUDRecord extends ALogRecord {
    private final int articleID;


    public CRUDRecord(OPERATION CRUDOperation, Timestamp time, int userID, int articleID) {
        this.operationType = CRUDOperation;
        this.time = time;
		this.userID = userID;
		this.articleID = articleID;
    }

    @Override
    public String toString() {
        StringBuilder record = getRecordBase();
        record.append(articleID);
		return record.toString();
    }

    public int getArticleID() {
        return articleID;
    }
}
