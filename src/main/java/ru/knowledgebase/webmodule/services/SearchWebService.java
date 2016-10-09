package ru.knowledgebase.webmodule.services;

import ru.knowledgebase.wrappermodule.SearchWrapper;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by Мария on 08.10.2016.
 */
@Path("/search")
public class SearchWebService {
    SearchWrapper searchWrapper = new SearchWrapper();

    @POST
    @Path("/search_by_title")
    public Response searchByTitle(
            @FormParam(value = "token") String token,
            @FormParam(value = "user_id") int userID,
            @FormParam(value = "search_request") String searchRequest,
            @FormParam(value = "num_articles") int numArticles) {
        return searchWrapper.searchByTitle(token, userID, searchRequest, numArticles);
    }

    @POST
    @Path("/search_by_body")
    public Response searchByBody(
            @FormParam(value = "token") String token,
            @FormParam(value = "user_id") int userID,
            @FormParam(value = "search_request") String searchRequest,
            @FormParam(value = "num_articles") int numArticles) {
        return searchWrapper.searchByBody(token, userID, searchRequest, numArticles);
    }
}
