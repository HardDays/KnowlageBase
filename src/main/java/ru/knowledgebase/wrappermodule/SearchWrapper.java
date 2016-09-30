package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.exceptionmodule.searchexceptions.SearchException;
import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;
import ru.knowledgebase.loggermodule.Server.Logger;
import ru.knowledgebase.responsemodule.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.searchmodule.SearchController;


public class SearchWrapper {
    private SearchController    searchController    = SearchController.getInstance();
    private Logger              logger              = Logger.getInstance();
    private LogRecordFactory    logRecordFactory    = new LogRecordFactory();


    /**
     * Writes to log about search requests, searches in articles titles for appearances of words of
     * a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @return Response with list of {@code Article} having words from search request in title
     */
    public Response searchByTitle(int userID, String searchRequest){
        writeToLog(userID, searchRequest);
        if(isWrongRequestFormat(searchRequest))
            return ResponseBuilder.buildWrongSearchRequestResponse();
        try{
            return ResponseBuilder.buildSearchResultResponse(searchController.searchByTitle(searchRequest));
        }catch (Exception ex) {
            return ResponseBuilder.buildWrongSearchRequestResponse();
        }
    }

    /**
     * Writes to log about search requests, searches in articles bodies for appearances of words of
     * a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @return Response with list of {@code Article} having words from search request in body
     */
    public Response searchByBody(int userID, String searchRequest){
        writeToLog(userID, searchRequest);
        if(isWrongRequestFormat(searchRequest))
            return ResponseBuilder.buildWrongSearchRequestResponse();
        try{
            return ResponseBuilder.buildSearchResultResponse(searchController.searchByBody(searchRequest));
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

    /**
     * Checks if search request is not empty and contains smth other than punctuation symbols
     * @param searchRequest
     * @return {@code true} if format of request is wrong, {@code false} vise versa.
     */
    private boolean isWrongRequestFormat(String searchRequest) {
        if ((searchRequest.replaceAll("[^a-zA-Z ]", "").isEmpty()))
            return true;
        return false;
    }
}
