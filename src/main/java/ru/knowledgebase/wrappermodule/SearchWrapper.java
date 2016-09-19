package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;
import ru.knowledgebase.loggermodule.Server.Logger;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.searchmodule.SearchController;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Мария on 07.09.2016.
 */
public class SearchWrapper {
    private SearchController    searchController    = SearchController.getInstance();
    private Logger              logger              = Logger.getInstance();
    private LogRecordFactory    logRecordFactory    = new LogRecordFactory();


    public List<Article> searchByTitle(int userID, String searchRequest){
        writeToLog(userID, searchRequest);
        try{
            return searchController.searchByTitle(searchRequest);
        }catch (Exception ex){
            //TODO:
        }
        return null;
    }

    public List<Article> searchByBody(int userID, String searchRequest){
        writeToLog(userID, searchRequest);
        try{
            return searchController.searchByBody(searchRequest);
        }catch (Exception ex){
            //TODO:
        }
        return null;
    }

    private void writeToLog(int userID, String searchRequest) {
        logger.writeToLog(
                logRecordFactory.generateSearchRequestRecord(
                        new Timestamp(System.currentTimeMillis()),
                        userID,
                        searchRequest
                ));
    }
}
