package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;
import ru.knowledgebase.loggermodule.Server.Logger;
import javax.ws.rs.core.Response;

import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.searchmodule.SearchController;

import java.util.List;


public class SearchWrapper {
    private SearchController    searchController    = SearchController.getInstance();
    private Logger              logger              = Logger.getInstance();
    private LogRecordFactory    logRecordFactory    = new LogRecordFactory();
    private ResponseBuilder     responseBuilder     = new ResponseBuilder();

    /**
     * Writes to log about search requests, searches in articles titles for appearances of words of
     * a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @return Response with list of {@code Article} having words from search request in title
     */
    public Response searchByTitle(int userID, String searchRequest, int numArticles){
        writeToLog(userID, searchRequest);
        try{
            return responseBuilder.buildSearchResultResponse(
                    searchController.searchByTitle(userID, searchRequest, numArticles));
        }catch (Exception ex) {
            return responseBuilder.buildResponse(ex);
        }
    }

    /**
     * Writes to log about search requests, searches in articles bodies for appearances of words of
     * a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @return Response with list of {@code Article} having words from search request in body
     */
    public Response searchByBody(int userID, String searchRequest, int numArticles){
        writeToLog(userID, searchRequest);
        try{
            return ResponseBuilder.buildSearchResultResponse(
                    searchController.searchByBody(userID, searchRequest, numArticles));
        }catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
    }

    /**
     * Writes to log information of search requests in the system
     * @param userID
     * @param searchRequest
     */
    private void writeToLog(int userID, String searchRequest) {
        logger.writeToLog(
                logRecordFactory.generateSearchRequestRecord(
                        userID,
                        searchRequest
                ));
    }
}