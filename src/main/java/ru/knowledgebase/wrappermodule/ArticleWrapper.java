package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.ArticleRoleController;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class ArticleWrapper {

    private ArticleRoleController articleRoleController = ArticleRoleController.getInstance();
    private ArticleController articleController = ArticleController.getInstance();
    private ResponseBuilder responseBuilder = new ResponseBuilder();

    //BEGIN PRIVATE METHODS
    public Response addArticle(String title, String body,
                               int authorId, int parentArticle, boolean isSection,
                               List<String> imagesId) {
        Response response = null;
        try {
            articleRoleController.canAddArticle(authorId, parentArticle);
            articleController.addArticle(title, body, authorId, parentArticle, isSection, imagesId);
        }
        catch (Exception ex) {
            return ResponseBuilder.buildResponse(ex);
        }
        if (response == null) {
            response = ResponseBuilder.buildArtilceCreatedResponse();
        }
        return response;
    }
    //END PRIVATE MATHODS
}
