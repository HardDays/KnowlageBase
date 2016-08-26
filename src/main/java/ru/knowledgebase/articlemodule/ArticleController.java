package ru.knowledgebase.articlemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.Image;
import ru.knowledgebase.modelsmodule.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class ArticleController {

    private DataCollector dataCollector = new DataCollector();
    private ImageController imageController = new ImageController();


    //BEGIN CRUD METHODS
    /**
     *
     * @param title - article title
     * @param body - article body with tags
     * @param authorId - author id
     * @param parentArticle - parent article id
     * @param imagesId - list of images id
     */
    public Article addArticle(String title, String body,
                           int authorId, int parentArticle,
                           List<String> imagesId) {

        Article article = getFullArticleObject(title, body, authorId,
                                               parentArticle, imagesId);

        return dataCollector.addArticle(article);
    }

    /**
     * Delete article by id
     * @param id
     */
    public void deleteArticle(Integer id) {
        dataCollector.deleteArticle(id);
    }

    /**
     * Find article by id
     * @param id
     * @return article object
     */
    public Article getArticle(Integer id) {
        return dataCollector.findArticle(id);
    }

    /**
     * Update article. Search for old article, change fields and save
     * @param id
     * @param title
     * @param body
     * @param authorId
     * @param parentArticle
     * @param imagesId
     * @return
     */
    public Article updateArticle(Integer id, String title, String body,
                                 int authorId, int parentArticle,
                                 List<String> imagesId) {
        Article article = getFullArticleObject(title, body, authorId,
                                                parentArticle, imagesId);

        article.setId(id);
        return dataCollector.updateArticle(article);
    }

    public Article updateArticle(Article article) {
        return dataCollector.updateArticle(article);
    }

    //END CRUD METHODS

    //BEGIN PRIVATE METHODS

    /**
     * Construct new Article object by data
     * @param title
     * @param body
     * @param authorId
     * @param parentArticle
     * @param imagesId
     * @return
     * */
    private Article getFullArticleObject(String title, String body,
                                         int authorId, int parentArticle,
                                         List<String> imagesId)
    {
        Article article = new Article();

        String clearBody = ArticleProcessor.getPureBody(body);
        Article parent = dataCollector.findArticle(parentArticle);
        List<Image> images = imageController.getImages(imagesId);

        //TODO: change to userController
        User author = dataCollector.findUser(authorId);

        article.setBody(body);
        article.setTitle(title);
        article.setClearBody(clearBody);
        article.setParentArticle(parent);
        article.setImages(images);
        article.setAuthor(author);

        return article;
    }
    //END PRIVATE MATHODS
}
