package ru.knowledgebase.responsemodule;

import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.articlemodels.News;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.rolemodels.UserSectionRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.json.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.HashSet;
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


    private static JsonObjectBuilder buildSecionPermissions(Role role){
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
                .add("can_view_mistakes", role.isCanViewMistakes())
                .add("can_add_user", role.isCanAddUser())
                .add("can_delete_user", role.isCanDeleteUser())
                .add("can_edit_user", role.isCanEditUser())
                .add("can_edit_user_role", role.isCanEditUserRole())
                .add("can_view_user", role.isCanViewUser());
    }

    public static Response buildSectionPermissionsResponse(Role role){
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
                .add("dismissal_date", checkNull(user.getDismissalDate()))
                .add("has_email_notifications", checkNull(user.isHasEmailNotifications()))
                .add("has_site_notifications", checkNull(user.isHasSiteNotifications()))
                .add("super_visor_id", checkNull(user.getSuperVisorId()));

    }

    public static Response buildUserInfoResponse(User user){
        return Response.ok(buildUserInfo(user).build().toString(),
                            MediaType.APPLICATION_JSON).build();
    }

    public static Response buildSuperVisorInfo(User user){
        return Response.ok(buildUserInfo(user).build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildNoSuperVisor(){
        return Response.ok("No supervisor!").build();
    }

    public static Response buildUserListResponse(List <User> users){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (User user : users){
            jarr.add(buildUserInfo(user));
        }
        return Response.ok(jarr.build().toString(),
                            MediaType.APPLICATION_JSON).build();
    }

    public static Response buildSectionUserListResponse(List <UserSectionRole> roles){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (UserSectionRole role : roles){
            JsonObject object = Json.createObjectBuilder()
                    .add("user", buildUserInfo(role.getUser()))
                    .add("role_id", role.getRole().getId())
                    .add("role_name", role.getRole().getName()).build();
            jarr.add(object);
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildUserSectionsResponse(List<Article> sections){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (Article section : sections){
            jarr.add(Json.createObjectBuilder()
                    .add("id", section.getId())
                    .add("title", section.getTitle())
                    .build());
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildSectionRoleListResponse(List <Role> roles){
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (Role role : roles){
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
        return Response.ok().entity("Article has been moved to archive.").build();
    }

    public static Response buildArchiveArticleContentResponse(ArchiveArticle arch) {
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("id", arch.getId())
                .add("title", arch.getTitle())
                .add("body", arch.getClearBody())
                .add("author", arch.getAuthor().getFullName());
        return Response.ok(job.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }


    public static Response buildArchiveArticleDeletedResponse() {
        return Response.ok().entity("Article has been deleted from archive.").build();
    }

    public static Response buildArticleCreatedResponse() {
        return Response.ok().entity("Article has been created.").build();
    }

    public static Response buildArticleUpdatedResponse() {
        return Response.ok().entity("Article has been updated.").build();
    }

    public static Response buildArticleDeletedResponse() {
        return Response.ok().entity("Article has been deleted.").build();
    }


    public static Response buildArticleContentResponse(Article article) {
      /*  return Json.createObjectBuilder()
                .add("id", article.getId())
                .add("Title", article.getTitle())
                .add("user_id", role.isCanAddArticle())
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
                .add("can_view_mistakes", role.isCanViewMistakes());*/
        return Response.ok(getArticleContents(article).build().toString(),
                MediaType.APPLICATION_JSON).build();
        //TODO: check if it is all we need to pass
    }

    public static Response buildArticleChildrenResponse(List<Article> articles) {
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (Article article : articles){
            jarr.add(getArticleContents(article));
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildSectionsResponse(List<Article> sections) {
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (Article article : sections){
            jarr.add(getArticleContents(article));
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildSectionHierarchyResponse(
            HashMap<Integer, HashMap<Article, List<Article>>> sections) {
        //TODO
        return null;
    }

    public static Response buildImageCreatedResponse(String id) {
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("id", id);
        return Response.ok(job.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildImagePathResponse(String path) {
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("path", path);
        return Response.ok(job.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildImagesDeletedResponse() {
        return Response.ok().entity("Image has been deleted.").build();
    }

    public static Response buildGetAllImagesResponse(List<Image> images) {
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (Image image : images){
            jarr.add(Json.createObjectBuilder()
                    .add("id", image.getId())
                    .add("name", image.getName())
                    .add("path", image.getPath()));
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildNewsCreatedResponse() {

        return Response.ok().entity("News have been created.").build();
    }

    public static Response buildDeleteNewsResponse() {

        return Response.ok().entity("News have been deleted.").build();
    }

    public static Response buildGetNewsResponse(News news) {
        return Response.ok(getNewsContents(news).build(), MediaType.APPLICATION_JSON).build();

    }

    public static Response buildGetSectionNewsResponse(List<News> news) {
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (News n : news){
            jarr.add(getNewsContents(n));
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }

    public static Response buildNewsUpdatedResponse() {
        return Response.ok().entity("News have been updated.").build();
    }


    public static Response buildUserNewsResponse(List<News> news) {
        JsonArrayBuilder jarr = Json.createArrayBuilder();
        for (News n : news){
            jarr.add(getNewsContents(n));
        }
        return Response.ok(jarr.build().toString(),
                MediaType.APPLICATION_JSON).build();
    }


    public Response buildReportResponse(String path, String reportName) {
        return Response.ok().entity("Report " + reportName +
                "had been created. Path to report: " + path).build();
    }


    private static JsonObjectBuilder getArticleContents(Article article) {
        return Json.createObjectBuilder()
                .add("id", article.getId())
                .add("title", article.getTitle())
                .add("body", article.getClearBody())
                .add("author", article.getAuthor().getFullName())
                .add("life time", article.getLifeTime().toString())
                .add("created time", article.getCreatedTime().toString())
                .add("updated time", article.getUpdatedTime().toString());
    }

    private static JsonObjectBuilder getNewsContents(News news) {
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("id", news.getId())
                .add("title", news.getTitle())
                .add("body", news.getClearBody())
                .add("author", news.getAuthor().getFullName())
                .add("creation date", news.getCreationDate().toString());
        return job;
    }
}
