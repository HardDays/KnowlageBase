package ru.knowledgebase.webmodule.services;

import ru.knowledgebase.wrappermodule.CommentWrapper;
import ru.knowledgebase.wrappermodule.UserWrapper;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by vova on 02.10.16.
 */
@Path("/comments")
public class CommentWebService {

    private CommentWrapper commentWrapper = new CommentWrapper();

    @POST
    @Path("/add")
    public Response add(@FormParam(value = "user_id") int userId,
                        @FormParam(value = "user_token") String userToken,
                        @FormParam(value = "article_id") int articleId,
                        @FormParam(value = "comment") String comment,
                        @FormParam(value = "article_text") String articleText) {
        return commentWrapper.add(userId, userToken, articleId, comment, articleText);
    }

    @POST
    @Path("/get_list")
    public Response add(@FormParam(value = "user_id") int userId,
                        @FormParam(value = "user_token") String userToken){
        return commentWrapper.get(userId, userToken);
    }

    @POST
    @Path("/delete")
    public Response add(@FormParam(value = "user_id") int userId,
                        @FormParam(value = "user_token") String userToken,
                        @FormParam(value = "comment_id") int commentId){
        return commentWrapper.delete(userId, userToken, commentId);
    }
}
