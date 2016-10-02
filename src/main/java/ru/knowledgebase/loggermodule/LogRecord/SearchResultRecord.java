package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.enums.OPERATION;

import java.sql.Timestamp;

/**
 * Created by Мария on 21.08.2016.
 */
/**
 * Class {@code SearchResultRecord} represents a record which contains information about search results
 * chosen by user.
 */
public class SearchResultRecord extends ALogRecord {
    private int articleID;

    public SearchResultRecord(int userID, int articleID) {
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

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }
}
