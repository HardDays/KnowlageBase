package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.enums.OPERATION;

import java.sql.Timestamp;
/**
 * Class {@code SearchRequestRecord} represents a record which contains information about search operations
 * made in the system.
 */
public class SearchRequestRecord extends ALogRecord {

    private String searchRequest;

    public SearchRequestRecord(int userID, String searchRequest) {
        this.operationType = OPERATION.SEARCH_REQUEST;
        this.userID = userID;
        this.searchRequest = searchRequest;
        this.time = new Timestamp(System.currentTimeMillis());
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
