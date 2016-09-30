package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.logenums.OPERATION;

import java.sql.Timestamp;

public class SearchRequestRecord extends ALogRecord {
    private String searchRequest;

    public SearchRequestRecord(Timestamp time, int userID, String searchRequest) {
        this.operationType = OPERATION.SEARCH_REQUEST;
        this.time = time;
        this.userID = userID;
        this.searchRequest = searchRequest;
    }

    @Override
    public String toString() {
        StringBuilder record = getRecordBase();
        record.append(searchRequest);
        return record.toString();
    }

    public String getSearchRequest() {
        return searchRequest;
    }
}
