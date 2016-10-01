package ru.knowledgebase.responsemodule;

import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;



/**
 * Created by root on 31.08.16.
 */
public class ResponseBuilder {

    public static Response buildResponse(Exception ex){
        int code = 400;
        if (ex instanceof WrongPasswordException){
            code = 401;
        }else if (ex instanceof UserNotFoundException){
            code = 401;
        }
        return Response.status(code).entity(ex.getClass().getName()).build();
    }

    public static Response buildAuthorizedResponse(Token token) {
        JsonObject json = Json.createObjectBuilder()
                        .add("token", token.getToken())
                        .build();
        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();

    }

    public static Response buildRegisteredResponse() {
        return Response.ok().build();
    }

    public static Response buildUserChangedResponse(){
        return Response.ok().build();
    }

    public static Response buildUserDeletedResponse(){
         return Response.ok().build();
    }

    public static Response buildUserRoleChangedResponse(){
        return Response.ok().build();
    }


    public static Response buildGlobalPermissionsResponse(GlobalRole role){
        return Response.ok().build();
    }

    public static Response buildSectionPermissionsResponse(ArticleRole role){
        return Response.ok().build();
    }

    public static Response buildRoleNotAssigned(){
        return Response.ok().build();
    }

    public static Response buildCommentAddedResponse(){
        return Response.ok().build();
    }

    public static Response buildCommentListResponse(List<Comment> comments){
        return Response.ok().build();
    }

    public static Response buildCommentDeleted(){
        return Response.ok().build();
    }

    public static Response buildWrongTokenResponse(){
        return Response.status(401).build();
    }

    public static Response buildUserInfoResponse(User user){
        return Response.ok().build();
    }

    public static Response buildUserListResponse(List <User> users){
        return Response.ok().build();
    }

    public static Response buildSectionUserListResponse(List <UserArticleRole> users){
        return Response.ok().build();
    }

    public static Response buildGlobalRoleListResponse(List <GlobalRole> roles){
        return Response.ok().build();
    }

    public static Response buildSectionRoleListResponse(List <ArticleRole> roles){
        return Response.ok().build();
    }

    public static Response buildNoAccessResponse(){
        return Response.ok().build();
    }

    public static Response buildArtilceCreatedResponse() {
        return Response.ok().build();
    }
}
