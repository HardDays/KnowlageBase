package ru.knowledgebase.wrappermodule;

import org.hibernate.search.test.util.impl.ReflectionHelperTest;
import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;
import ru.knowledgebase.loggermodule.Server.Logger;
import javax.ws.rs.core.Response;

import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.searchmodule.SearchController;
import ru.knowledgebase.usermodule.UserController;

public class SearchWrapper {
    private SearchController    searchController    = SearchController.getInstance();
    private Logger              logger              = Logger.getInstance();
    private LogRecordFactory    logRecordFactory    = new LogRecordFactory();
    private ResponseBuilder     responseBuilder     = new ResponseBuilder();
    private UserController      userController      = new UserController();

    /**
     * Writes to log about search requests, searches in articles titles for appearances of words of
     * a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @return Response with list of {@code Article} having words from search request in title
     */
    public Response searchByTitle(String token, int userID, String searchRequest, int numArticles){
        Response r;
        try{
            boolean okToken = userController.checkUserToken(userID, token);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();

            writeToLog(userID, searchRequest);
            r = responseBuilder.buildSearchResultResponse(
                    searchController.searchByTitle(userID, searchRequest, numArticles));
        }catch (Exception ex) {
            return responseBuilder.buildResponse(ex);
        }
        return r;
    }

    /**
     * Writes to log about search requests, searches in articles bodies for appearances of words of
     * a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @return Response with list of {@code Article} having words from search request in body
     */
    public Response searchByBody(String token, int userID, String searchRequest, int numArticles){
        Response r;
        try{
            boolean okToken = userController.checkUserToken(userID, token);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();

            writeToLog(userID, searchRequest);
            r =  ResponseBuilder.buildSearchResultResponse(
                    searchController.searchByBody(userID, searchRequest, numArticles));
        }catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return r;
    }

    /**
     * Writes to log information of search requests in the system
     * @param userID
     * @param searchRequest
     */
    private void writeToLog(int userID, String searchRequest) throws Exception{
        logger.writeToLog(
                logRecordFactory.generateSearchRequestRecord(
                        userID,
                        searchRequest
                ));
    }
}