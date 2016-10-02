package ru.knowledgebase.webmodule.services;

import ru.knowledgebase.wrappermodule.ArticleWrapper;
import ru.knowledgebase.wrappermodule.UserWrapper;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 * Created by vova on 02.10.16.
 */
@Path("/articles")
public class ArticleWebService {

    private ArticleWrapper articleWrapper = new ArticleWrapper();

    @POST
    @Path("/add_article")
    public Response addArticle(@FormParam(value = "author_id") int authorId,
                        @FormParam(value = "token") String userToken,
                        @FormParam(value = "title") String title,
                        @FormParam(value = "body") String body,
                        @FormParam(value = "parent_article_id") int parentArticleId,
                        @FormParam(value = "created_time") Timestamp createdTime,
                        @FormParam(value = "updated_time") Timestamp updatedTime,
                        @FormParam(value = "life_time") Timestamp lifeTime,
                        @FormParam(value = "is_section") boolean isSection) {
           // return articleWrapper.addArticle(token, title, body, authorId,
           //         parentArticleId, createdTime, updatedTime, lifeTime
           //         isSection, new LinkedList<String>());
        return Response.ok().build();
    }

    @POST
    @Path("/update_article")
    public Response updateArticle(@FormParam(value = "author_id") int authorId,
                        @FormParam(value = "token") String userToken,
                        @FormParam(value = "article_id") int articleId,
                        @FormParam(value = "title") String title,
                        @FormParam(value = "body") String body,
                        @FormParam(value = "parent_article_id") int parentArticleId,
                        @FormParam(value = "created_time") Timestamp createdTime,
                        @FormParam(value = "updated_time") Timestamp updatedTime,
                        @FormParam(value = "life_time") Timestamp lifeTime,
                        @FormParam(value = "is_section") boolean isSection) {
        // return articleWrapper.updateArticle(token, articleId, title, body, authorId,
        //         parentArticleId, createdTime, updatedTime, lifeTime
        //         isSection, new LinkedList<String>());
        return Response.ok().build();
    }

    @POST
    @Path("/delete_article")
    public Response deleteArticle(@FormParam(value = "user_id") int authorId,
                        @FormParam(value = "token") String userToken,
                        @FormParam(value = "article_id") int articleId) {
        // return articleWrapper.deleteArticle(token, userId, articleId);
        return Response.ok().build();
    }

    @POST
    @Path("/get_article")
    public Response getArticle(@FormParam(value = "user_id") int authorId,
                           @FormParam(value = "token") String userToken,
                           @FormParam(value = "article_id") int articleId) {
        // return articleWrapper.getArticle(token, userId, articleId);
        return Response.ok().build();
    }

    @POST
    @Path("/get_next_level_articles")
    public Response getNextLevelArticles(@FormParam(value = "user_id") int authorId,
                               @FormParam(value = "token") String userToken,
                               @FormParam(value = "article_id") int articleId,
                               @FormParam(value = "from") int from,
                               @FormParam(value = "to") int to) {
        // return articleWrapper.getNextLevelArticles(token, userId, articleId, from, to);
        return Response.ok().build();
    }

    @POST
    @Path("/get_next_level_sections")
    public Response getNextLevelSections(@FormParam(value = "user_id") int authorId,
                                         @FormParam(value = "token") String userToken,
                                         @FormParam(value = "sectionId") int sectionId){
        // return articleWrapper.getNextLevelSections(token, userId, sectionId);
        return Response.ok().build();
    }

    @POST
    @Path("/get_first_level_articles")
    public Response getFirstLevelArticles(@FormParam(value = "user_id") int authorId,
                                         @FormParam(value = "token") String userToken){
        // return articleWrapper.getFirstLevelArticles(token, userId);
        return Response.ok().build();
    }

    @POST
    @Path("/get_section_hierarchy")
    public Response getSectionHierarchy(@FormParam(value = "user_id") int authorId,
                                          @FormParam(value = "token") String userToken){
        // return articleWrapper.getSectionHierarchy(token, userId);
        return Response.ok().build();
    }

}
