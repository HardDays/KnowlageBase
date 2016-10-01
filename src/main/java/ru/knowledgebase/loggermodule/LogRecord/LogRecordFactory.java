package ru.knowledgebase.loggermodule.LogRecord;

import ru.knowledgebase.loggermodule.enums.OPERATION;

import java.sql.Timestamp;

/**
 * Created by Мария on 21.08.2016.
 */
public class LogRecordFactory {

    public ALogRecord generateSearchResultRecord(int userID, int articleID){
        return new SearchResultRecord( userID, articleID);
    }
    public ALogRecord generateSearchRequestRecord(int userID, String searchRequest){
        return new SearchRequestRecord(userID, searchRequest);
    }
    public ALogRecord generateOnCreateRecord(int userID, int articleID){
        return new CRUDRecord(OPERATION.CREATE, userID, articleID);
    }
    public ALogRecord generateOnUpdateRecord(int userID, int articleID){
        return new CRUDRecord(OPERATION.UPDATE, userID, articleID);
    }
    public ALogRecord generateOnDeleteRecord(int userID, int articleID){
        return new CRUDRecord(OPERATION.DELETE, userID, articleID);
    }

    /**
     * Generates an object of corresponding to the input array of record's fields {@code recordParameters}
     * descender of {@code ALogRecord}.
     * @param recordParameters
     * @return {@code ALogRecord} object if input parameters correspond to any log record class and
     * {@code null} if input parameters are wrong.
     */
    public ALogRecord generateRecord(String[] recordParameters) {
        try{
            OPERATION operation = OPERATION.valueOf(recordParameters[0]);
            Timestamp time = Timestamp.valueOf(recordParameters[1]);
            int userID = Integer.valueOf(recordParameters[2]);

            if(OPERATION.SEARCH_REQUEST.equals(operation)){
                return new SearchRequestRecord(
                        userID,
                        recordParameters[3]);
            }else{
                int articleID = Integer.valueOf(recordParameters[3]);
                if(OPERATION.SEARCH_RESULT.equals(operation)){
                    return new SearchResultRecord(userID, articleID);
                }else{
                    return new CRUDRecord(operation, userID, articleID);
                }
            }
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
        return null;
    }
}
