package ru.knowledgebase.responsemodule;

import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.articlemodels.News;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.json.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
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
        ex.printStackTrace();
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

    private static JsonObjectBuilder buildGlobalPermissions(GlobalRole role) {
         return Json.createObjectBuilder()
                .add("id", role.getId())
                .add("name", role.getName())
                .add("can_add_user", role.isCanAddUser())
                .add("can_delete_user", role.isCanDeleteUser())
                .add("can_edit_user", role.isCanEditUser())
                .add("can_edit_user_role", role.isCanEditUserRole())
                .add("can_view_user", role.isCanViewUser());
    }

    public static Response buildGlobalPermissionsResponse(GlobalRole role){
        return Response.ok(buildGlobalPermissions(role).build().toString(), MediaType.APPLICATION_JSON).build();
    }

    private static JsonObjectBuilder buildSecionPermissions(ArticleRole role){
        return Json.createObjectBuilder()
                .add("id", role.getId())
                .add("name", role.getName())
                .add("can_add_article", role.isCanAddArticle())
                .add("can_add_mistakes", role.isCanAddMistakes())
                .add("can_add_news", role.isCanAddNews())
                .add("can_delete_article", role.isCanDeleteArticle())
                .add("can_edit_article", role.isCanEditArticle())
                .add("can_get_employees_actions_reports", role.isCanGetEmployeesActionsReports())
                .add("can_get_notifications", role.isCanGetNotifications())
                .add("can_get_search_operations_reports", role.isCanGetSearchOperationsReports())
                .add("can_get_system_actions_reports", role.isCanGetSystemActionsReports())
                .add("can_off_on_notifications", role.isCanOnOffNotifications())
                .add("can_search", role.isCanSearch())
                .add("can_view_articles", role.isCanViewArticle())
                .add("can_view_mistakes", role.isCanViewMistakes());
    }

    public static Response buildSectionPermissionsResponse(ArticleRole role){
        return Response.ok(buildSecionPermissions(role).build().toString(), MediaType.APPLICATION_JSON).build();
    }

    public static Response buildRoleNotAssigned(){
        return Response.status(400).entity("RoleNotAssigned").build();
    }

    public static Response buildCommentAddedResponse(){
        return Response.ok().build();
    }

    public static Response buildCommentListResponse(List<Comment> comments){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (Comment comment : comments){
            jarr.add(Json.createObjectBuilder()
                    .add("id", comment.getId())
                    .add("article_id", comment.getArticle().getId())
                    .add("commentator_id", comment.getCommentator().getId())
                    .add("comment", comment.getComment())
                    .add("article_text", comment.getArticleText())
                    .build()
                    );
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildCommentDeleted(){
        return Response.ok().build();
    }

    public static Response buildWrongTokenResponse(){
        return Response.status(401).build();
    }

    private static String checkNull(Object object){
        if (object == null){
            return "";
        }
        return object.toString();
    }

    private static JsonObjectBuilder buildUserInfo(User user){
        return Json.createObjectBuilder()
                .add("id", user.getId())
                .add("login", checkNull(user.getLogin()))
                .add("email", checkNull(user.getEmail()))
                .add("first_name", checkNull(user.getFirstName()))
                .add("middle_name", checkNull(user.getMiddleName()))
                .add("last_name", checkNull(user.getLastName()))
                .add("office", checkNull(user.getOffice()))
                .add("phone1", checkNull(user.getPhone1()))
                .add("phone2", checkNull(user.getPhone2()))
                .add("recruitment_date", checkNull(user.getRecruitmentDate()))
                .add("dismissal_date", checkNull(user.getDismissalDate()));

    }

    public static Response buildUserInfoResponse(User user){
        return Response.ok(buildUserInfo(user).build().toString(),
                            MediaType.APPLICATION_JSON).build();
    }

    public static Response buildUserListResponse(List <User> users){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (User user : users){
            jarr.add(buildUserInfo(user));
        }
        return Response.ok(jarr.build().toString(),
                            MediaType.APPLICATION_JSON).build();
    }

    public static Response buildSectionUserListResponse(List <UserArticleRole> users){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (UserArticleRole user : users){
            jarr.add(buildUserInfo(user.getUser()));
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildGlobalRoleListResponse(List <GlobalRole> roles){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (GlobalRole role : roles){
            jarr.add(buildGlobalPermissions(role));
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildSectionRoleListResponse(List <ArticleRole> roles){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (ArticleRole role : roles){
            jarr.add(buildSecionPermissions(role));
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildNoAccessResponse(){
        return Response.status(403).entity("No permissions!").build();
    }

    public static Response buildArtilceCreatedResponse() {
        return Response.ok().build();
    }

    public static Response buildArticleMovedToArchiveResponse() {
        return null;
    }

    public static Response buildArchiveArticleContentResponse(ArchiveArticle arch) {
        return null;
    }

    public static Response buildArchiveArticleDeletedResponse() {
        return null;
    }

    public static Response buildArticleCreatedResponse() {
        return null;
    }

    public static Response buildArticleUpdatedResponse() {
        return null;
    }

    public static Response buildArticleDeletedResponse() {
        return null;
    }

    public static Response buildArticleContentResponse(Article article) {
        return null;
    }

    public static Response buildArticleChildrenResponse(List<Article> articles) {
        return null;
    }

    public static Response buildSectionsResponse(List<Article> sections) {
        return null;
    }

    public static Response buildSectionHierarchyResponse(HashMap<Integer, HashMap<Article, List<Article>>> sections) {
        return null;
    }

    public static Response buildImageCreatedResponse(String id) {
        return null;
    }

    public static Response buildGetAllImagesResponse(List<Image> images) {
        return null;
    }

    public static Response buildNewsCreatedResponse() {
        return null;
    }

    public static Response buildDeleteNewsResponse() {
        return null;
    }

    public static Response buildGetNewsResponse(News news) {
        return null;
    }

    public static Response buildGetSectionNewsResponse(List<News> news) {
        return null;
    }

    public static Response buildNewsUpdatedResponse() {
        return null;
    }

    public static Response buildUserNewsResponse() {
        return null;
    }
}
