package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.archivemodule.ArchiveArticleController;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.articlemodule.SectionController;
import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;
import ru.knowledgebase.loggermodule.Server.Logger;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import javax.ws.rs.core.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.RoleController;
import ru.knowledgebase.usermodule.UserController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class ArticleWrapper {

    private RoleController roleController = RoleController.getInstance();
    private ArticleController        articleController     = ArticleController.getInstance();
    private UserController           userController        = UserController.getInstance();
    private ArchiveArticleController archArticleController = ArchiveArticleController.getInstance();
    private SectionController        sectionController     = SectionController.getInstance();
    private Logger                   logger                = Logger.getInstance();
    private LogRecordFactory         logRecordFactory      = new LogRecordFactory();

    //BEGIN PUBLIC METHODS

    /**
     * Check token, check permissions and add article.
     * Generate response by exception or OK-answer
     * @param token
     * @param title
     * @param body
     * @param authorId
     * @param parentArticle
     * @return response for web-service
     */
    public Response addArticle(String token, String title, String body,
                               int authorId, int parentArticle, Timestamp createdTime, Timestamp updatedTime,
                               Timestamp lifeTime, boolean isSection) {
        Article art = null;
        try {
            boolean okToken = userController.checkUserToken(authorId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canAddArticle(authorId, parentArticle);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }

            art = articleController.addArticle(title, body, authorId, parentArticle, createdTime, updatedTime, lifeTime, isSection);
            archArticleController.addArchivationTime(art);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        logger.writeToLog(
                logRecordFactory.generateOnCreateRecord(authorId, art.getId())
        );
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
                               int authorId, int parentArticle, Timestamp createdTime, Timestamp updatedTime,
                               Timestamp lifeTime) {
        Article art = null;
        try {
            boolean okToken = userController.checkUserToken(authorId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canEditArticle(authorId, parentArticle);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            art = articleController.updateArticle(id, title, body, authorId, parentArticle, createdTime, updatedTime, lifeTime);
            archArticleController.changeArchivationTime(art);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        logger.writeToLog(
                logRecordFactory.generateOnUpdateRecord(authorId, art.getId())
        );
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
        Article art = null;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canDeleteArticle(userId, articleId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            art = articleController.getArticle(articleId);
            articleController.deleteArticle(articleId);
            archArticleController.deleteArchivationTime(articleId);
            if (art.isSection()) {
                archArticleController.clearSectionArchive();
            }
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        logger.writeToLog(
                logRecordFactory.generateOnDeleteRecord(userId, art.getId())
        );
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
            boolean hasRights = roleController.canViewArticle(userId, articleId);
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

    /**
     * Get articles that has @articleId as parent article
     * @param token
     * @param userId
     * @param articleId
     * @return
     */
    public Response getNextLevelArticles(String token, int userId, int articleId, int from ,int to) {
        List<Article> articles = null;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canViewArticle(userId, articleId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            articles = articleController.getArticleChildren(articleId, from, to);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArticleChildrenResponse(articles);
    }

    /**
     * Get sections under current article
     * @param token
     * @param userId
     * @param sectionId article, that is section
     * @return
     */
    public Response getNextLevelSections(String token, int userId, int sectionId) {
        List<Article> sections = null;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            sections = sectionController.getNextLevelSections(sectionId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildSectionsResponse(sections);
    }

    public Response getFirstLevelArticles(String token, int userId) {
        List<Article> sections = null;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            Article base = articleController.getBaseArticle();
            sections = sectionController.getNextLevelSections(base.getId());
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildSectionsResponse(sections);
    }

    public Response getSectionHierarchy(String token, int userId) {
        HashMap<Integer, HashMap<Article, List<Article>>> sections = null;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            sections = sectionController.getSectionHierarchy();
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildSectionHierarchyResponse(sections);
    }


    //END PUBLIC METHODS

    //BEGIN PRIVATE METHODS
    /**
     * Writes to log information of article requests in the system
     * @param userID
     * @param searchRequest
     */
    private void writeToLog(int userID, String searchRequest) {
        logger.writeToLog(
                logRecordFactory.generateSearchRequestRecord(
                        userID,
                        searchRequest
                ));
    }
    //END PRIVATE MATHODS
}
