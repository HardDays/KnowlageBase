package ru.knowledgebase.webmodule.services;

import ru.knowledgebase.wrappermodule.NewsWrapper;
import ru.knowledgebase.wrappermodule.UserWrapper;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;

/**
 * Created by vova on 08.10.16.
 */

@Path("/news")
public class NewsWebService {
    private NewsWrapper newsWrapper = new NewsWrapper();

    @POST
    @Path("/add_news")
    public Response addNews(@FormParam(value = "user_id") int userId,
                            @FormParam(value = "token") String token,
                            @FormParam(value = "author_id") int authorId,
                            @FormParam(value = "section_id") int sectionId,
                            @FormParam(value = "title") String title,
                            @FormParam(value = "body") String body,
                            @FormParam(value = "creation_time") Timestamp creationTime) {
        return newsWrapper.addNews(token, userId, authorId, sectionId, title, body, creationTime);
    }

    @POST
    @Path("/get_news")
    public Response getNews(@FormParam(value = "user_id") int userId,
                              @FormParam(value = "token") String token,
                              @FormParam(value = "news_id") int newsId,
                              @FormParam(value = "section_id") int sectionId) {
        return newsWrapper.getNews(token, userId, newsId, sectionId);
    }

    @POST
    @Path("/delete_news")
    public Response deleteNews(@FormParam(value = "user_id") int userId,
                              @FormParam(value = "token") String token,
                              @FormParam(value = "news_id") int newsId,
                              @FormParam(value = "section_id") int sectionId) {
        return newsWrapper.deleteNews(token, userId, newsId, sectionId);
    }

    @POST
    @Path("/update_news")
    public Response updateNews(@FormParam(value = "news_id") int newsId,
                               @FormParam(value = "user_id") int userId,
                               @FormParam(value = "token") String token,
                               @FormParam(value = "author_id") int authorId,
                               @FormParam(value = "section_id") int sectionId,
                               @FormParam(value = "title") String title,
                               @FormParam(value = "body") String body,
                               @FormParam(value = "creation_time") Timestamp creationTime) {
        return newsWrapper.updateNews(newsId, token, userId, authorId, sectionId, title, body, creationTime);
    }

    @POST
    @Path("/get_news_by_section")
    public Response getBySections(@FormParam(value = "user_id") int userId,
                               @FormParam(value = "token") String token,
                               @FormParam(value = "section_id") int sectionId) {
        return newsWrapper.getNewsBySection(token, userId, sectionId);
    }

    @POST
    @Path("/get_user_news")
    public Response getUserNews(@FormParam(value = "user_id") int userId,
                                  @FormParam(value = "token") String token,
                                  @FormParam(value = "day") Timestamp day) {
        return newsWrapper.getUserNews(userId, token, day);
    }
}
