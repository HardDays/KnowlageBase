package ru.knowledgebase.responsemodule;

import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by root on 31.08.16.
 */
public class ResponseBuilder {
    public static Response getResponse() {
        return new Response();
    }

    public static Response buildResponse(Exception ex) {
        return new Response();
    }

    public static Response buildAuthorizedResponse(Token token) {
        Response res = new Response();
        res.setBody(token.getToken());
        return res;
    }

    public static Response buildRegisteredResponse() {
        Response res = new Response();
        return res;
    }

    public static Response buildUserChangedResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildUserDeletedResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildUserRoleChangedResponse(){
        Response res = new Response();
        return res;
    }


    public static Response buildGlobalPermissionsResponse(GlobalRole role){
        Response res = new Response();
        return res;
    }

    public static Response buildSectionPermissionsResponse(ArticleRole role){
        Response res = new Response();
        return res;
    }

    public static Response buildRoleNotAssigned(){
        Response res = new Response();
        return res;
    }

    public static Response buildCommentAddedResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildCommentListResponse(List<Comment> comments){
        Response res = new Response();
        return res;
    }

    public static Response buildCommentDeleted(){
        Response res = new Response();
        return res;
    }

    public static Response buildWrongTokenResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildUserInfoResponse(User user){
        Response res = new Response();
        return res;
    }

    public static Response buildNoAccessResponse(){
        Response res = new Response();
        return res;
    }

    public static Response buildArtilceCreatedResponse() {
        return new Response();
    }
}
