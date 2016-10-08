package ru.knowledgebase.webmodule.services;

import ru.knowledgebase.wrappermodule.NewsWrapper;
import ru.knowledgebase.wrappermodule.UserWrapper;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by vova on 08.10.16.
 */

@Path("/news")
public class NewsWebService {
    private NewsWrapper newsWrapper = new NewsWrapper();

    @POST
    @Path("/add_news")
    public Response authorize(@FormParam(value = "user_id") int userId,
                              @FormParam(value = "token") String token,
                              @FormParam(value = "author_id") int authorId,
                              @FormParam(value = "section_id") int sectionId,
                              @FormParam(value = "title") String title,
                              @FormParam(value = "body") String body) {
        return newsWrapper.addNews(token, userId, authorId, sectionId, title, body);
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
                               @FormParam(value = "body") String body) {
        return newsWrapper.updateNews(newsId, token, userId, authorId, sectionId, title, body);
    }

    @POST
    @Path("/get_news_by_section")
    public Response getBySections(@FormParam(value = "user_id") int userId,
                               @FormParam(value = "token") String token,
                               @FormParam(value = "section_id") int sectionId) {
        return newsWrapper.getNewsBySection(token, userId, sectionId);
    }
}
