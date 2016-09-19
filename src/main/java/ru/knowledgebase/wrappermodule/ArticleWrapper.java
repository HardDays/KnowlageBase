package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.archivemodule.ArchiveArticleController;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.responsemodule.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.usermodule.UserController;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class ArticleWrapper {

    private ArticleRoleController    articleRoleController = ArticleRoleController.getInstance();
    private ArticleController        articleController     = ArticleController.getInstance();
    private UserController           userController        = UserController.getInstance();
    private ArchiveArticleController archArticleController = ArchiveArticleController.getInstance();

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
                               List<String> imagesId, Timestamp lifeTime, boolean isSection) {
        try {
            boolean okToken = userController.checkUserToken(authorId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = articleRoleController.canAddArticles(authorId, parentArticle);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            Article art = articleController.addArticle(title, body, authorId, parentArticle, lifeTime, isSection);
            archArticleController.addArchivationTime(art);
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
     * @return response for web-service
     */
    public Response updateArticle(String token, int  id, String title, String body,
                               int authorId, int parentArticle,
                               Timestamp lifeTime, boolean isSection) {
        try {
            boolean okToken = userController.checkUserToken(authorId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = articleRoleController.canEditArticle(authorId, parentArticle);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            Article art = articleController.updateArticle(id, title, body, authorId, parentArticle, lifeTime, isSection);
            archArticleController.changeArchivationTime(art);
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
            Article art = articleController.getArticle(articleId);
            articleController.deleteArticle(articleId);
            archArticleController.deleteArchivationTime(articleId);
            if (art.isSection()) {
                archArticleController.clearSectionArchive();
            }
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
        Article article;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = articleRoleController.canViewArticle(userId, articleId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            article = articleController.getArticle(articleId);
            archArticleController.archiveNext();
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArticleContentResponse(article);
    }

    //END PUBLIC METHODS
}
