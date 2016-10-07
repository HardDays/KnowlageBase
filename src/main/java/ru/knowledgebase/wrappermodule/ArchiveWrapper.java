package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.archivemodule.ArchiveArticleController;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.articlemodule.SectionController;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import javax.ws.rs.core.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.RoleController;
import ru.knowledgebase.usermodule.UserController;

/**
 * Created by root on 02.10.16.
 */
public class ArchiveWrapper {

    private RoleController roleController = RoleController.getInstance();
    private ArticleController articleController     = ArticleController.getInstance();
    private UserController userController        = UserController.getInstance();
    private ArchiveArticleController archArticleController = ArchiveArticleController.getInstance();
    private SectionController sectionController     = SectionController.getInstance();

    public Response moveToArchive(String token, int userId, int articleId) {
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canEditArticle(userId, articleId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }

            Article art = articleController.getArticle(articleId);
            archArticleController.moveToArchive(art);

        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArticleMovedToArchiveResponse();
    }

    public Response getArchiveArticle(String token, int userId, int articleId) {
        ArchiveArticle arch = null;
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canEditArticle(userId, articleId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            arch = archArticleController.getArchiveArticle(articleId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArchiveArticleContentResponse(arch);
    }

    public Response deleteArchiveArticle(String token, int userId, int articleId) {
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (okToken != true) {
                return ResponseBuilder.buildWrongTokenResponse();
            }
            boolean hasRights = roleController.canEditArticle(userId, articleId);
            if (hasRights != true) {
                return ResponseBuilder.buildNoAccessResponse();
            }
            archArticleController.deleteArchiveArticle(articleId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        return ResponseBuilder.buildArchiveArticleDeletedResponse();
    }
}
