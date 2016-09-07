package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.logenums.OPERATION;

import java.sql.Timestamp;

/**
 * Created by Мария on 21.08.2016.
 */
public class SearchResultRecord extends ALogRecord {
    private int articleID;

    public SearchResultRecord(Timestamp time, int userID, int articleID) {
        this.time = time;
        this.articleID = articleID;
        this.operationType = OPERATION.SEARCH_RESULT;
        this.userID = userID;
    }

    @Override
    public String toString() {
        StringBuilder record = getRecordBase();
        record.append(Integer.toString(articleID));
        return record.toString();
    }

    public int getArticleID() {
        return articleID;
    }
}
