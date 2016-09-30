package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.logenums.OPERATION;

import java.sql.Timestamp;

/**
 * Created by Мария on 21.08.2016.
 */
public class LogRecordFactory {

    public ALogRecord generateSearchResultRecord(Timestamp time, int userID, int articleID){
        return new SearchResultRecord(time, userID, articleID);
    }
    public ALogRecord generateSearchRequestRecord(Timestamp time, int userID, String searchRequest){
        return new SearchRequestRecord(time, userID, searchRequest);
    }
    public ALogRecord generateOnCreateRecord(Timestamp time, int userID, int articleID){
        return new CRUDRecord(OPERATION.CREATE, time, userID, articleID);
    }
    public ALogRecord generateOnUpdateRecord(Timestamp time, int userID, int articleID){
        return new CRUDRecord(OPERATION.UPDATE, time, userID, articleID);
    }
    public ALogRecord generateOnDeleteRecord(Timestamp time, int userID, int articleID){
        return new CRUDRecord(OPERATION.DELETE, time, userID, articleID);
    }

    public ALogRecord generateRecord(String[] recordParameters) {
        try{
            OPERATION operation = OPERATION.valueOf(recordParameters[0]);
            Timestamp time = Timestamp.valueOf(recordParameters[1]);
            int userID = Integer.valueOf(recordParameters[2]);

            if(OPERATION.SEARCH_REQUEST.equals(operation)){
                return new SearchRequestRecord(
                        time,
                        userID,
                        recordParameters[3]);
            }else{
                int articleID = Integer.valueOf(recordParameters[3]);
                if(OPERATION.SEARCH_RESULT.equals(operation)){
                    return new SearchResultRecord(time, userID, articleID);
                }else{
                    return new CRUDRecord(operation, time, userID, articleID);
                }
            }
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
        return null;
    }
}
