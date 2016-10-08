package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.articlemodule.NewsController;
import ru.knowledgebase.modelsmodule.articlemodels.News;
import javax.ws.rs.core.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.RoleController;
import ru.knowledgebase.usermodule.UserController;

import java.util.List;

/**
 * Created by root on 02.10.16.
 */
public class NewsWrapper {
    private NewsController               newsController = NewsController.getInstance();
    private UserController               userController = UserController.getInstance();
    private RoleController roleController = RoleController.getInstance();

    public Response addNews(String token, int userId, int authorId, int sectionId,
                            String title, String body) {
        try {
            boolean okToken = userController.checkUserToken(authorId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canAddNews(authorId, sectionId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }

            News news = newsController.addNews(title, body, authorId, sectionId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildNewsCreatedResponse();
    }

    /**
     * User can delete news if he/she can delete section of this article
     * @param token
     * @param userId
     * @param newsId
     * @param sectionId
     * @return
     * @throws Exception
     */
    public Response deleteNews(String token, int userId, int newsId, int sectionId)  {
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canDeleteArticle(userId, sectionId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }

            newsController.deleteNews(newsId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildDeleteNewsResponse();
    }

    public Response getNews(String token, int userId, int newsId, int sectionId) {
        News news = null;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canViewArticle(userId, sectionId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }

            news = newsController.findNews(newsId);

        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildGetNewsResponse(news);
    }

    public Response getNewsBySection(String token, int userId, int sectionId) {
        List<News> news = null;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canViewArticle(userId, sectionId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }

            news = newsController.getNewsBySection(sectionId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildGetSectionNewsResponse(news);
    }

    public Response updateNews(int id, String token, int userId, int authorId, int sectionId,
                               String title, String body) {
        try {
            boolean okToken = userController.checkUserToken(authorId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canAddNews(authorId, sectionId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }

            News news = newsController.updateNews(id, title, body, authorId, sectionId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildNewsUpdatedResponse();
    }
}
