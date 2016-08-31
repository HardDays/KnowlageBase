package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.responsemodule.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.usermodule.UserController;

import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class ArticleWrapper {

    private ArticleRoleController articleRoleController = ArticleRoleController.getInstance();
    private ArticleController     articleController     = ArticleController.getInstance();
    private UserController        userController        = UserController.getInstance();

    //BEGIN PUBLIC METHODS

    /**
     * Check token, check permissions and add article.
     * Generate response by exception or OK-answer
     * @param token
     * @param title
     * @param body
     * @param authorId
     * @param parentArticle
     * @param imagesId - list of image ids
     * @return response for web-service
     */
    public Response addArticle(String token, String title, String body,
                               int authorId, int parentArticle,
                               List<String> imagesId) {
        try {
            boolean okToken = userController.checkUserToken(authorId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = articleRoleController.canAddArticles(authorId, parentArticle);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            articleController.addArticle(title, body, authorId, parentArticle, imagesId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArticleCreatedResponse();
    }

    /**
     * Check token, check permissions and update article.
     * Generate response by exception or OK-answer
     * @param token
     * @param title
     * @param body
     * @param authorId
     * @param parentArticle
     * @param imagesId - list of image ids
     * @return response for web-service
     */
    public Response updateArticle(String token, int  id, String title, String body,
                               int authorId, int parentArticle,
                               List<String> imagesId) {
        try {
            boolean okToken = userController.checkUserToken(authorId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = articleRoleController.canEditArticle(authorId, parentArticle);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            articleController.updateArticle(id, title, body, authorId, parentArticle, imagesId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArticleUpdatedResponse();
    }

    /**
     * Check token, check permissions and delete article.
     * Generate response by exception or OK-answer
     * @param token
     * @param userId - user, who try to delete this article
     * @param articleId
     * @return response for web-service
     */
    public Response deleteArticle(String token, int userId, int articleId) {
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = articleRoleController.canDeleteArticle(userId, articleId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            articleController.deleteArticle(articleId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArticleDeletedResponse();
    }

    /**
     * Check token, check permissions and get article.
     * Generate response by exception or OK-answer
     * @param token
     * @param userId - user, who try to get this article
     * @param articleId
     * @return response for web-service
     */
    public Response getArticle(String token, int userId, int articleId) {
        String body;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = articleRoleController.canViewArticle(userId, articleId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            body = articleController.getArticle(articleId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArticleContentResponse(body);
    }

    //END PUBLIC METHODS
}
