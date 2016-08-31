package ru.knowledgebase.articlemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.*;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.imageexceptions.ImageNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class ArticleController {

    private DataCollector dataCollector = new DataCollector();
    private final int BASE_ARTICLE = -1;

    private static ArticleController instance;

    /**
     * Controller as thread-safe singeleton
     * @return
     */
    public static ArticleController getInstance() {
        ArticleController localInstance = instance;
        if (localInstance == null) {
            synchronized (ArticleController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArticleController();
                }
            }
        }
        return localInstance;
    }

    //BEGIN CRUD METHODS

    /**
     * Add base (without parent) article
     * @param title
     * @param body
     * @param authorId
     * @param imagesId
     * @return
     * @throws Exception
     */
    public Article addBaseArticle(String title, String body,
                                  int authorId,
                                  List<String> imagesId) throws Exception {
        Article article = getFullArticleObject(title, body, authorId,
                -1, imagesId);

        Article resultArticle = null;
        try {
            resultArticle = dataCollector.addArticle(article);
        }
        catch (Exception ex) {
            //TODO: throw new DBException();
            throw new Exception();
        }
        if (resultArticle == null) {
            throw new ArticleAddException();
        }

        return resultArticle;
    }


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
                           List<String> imagesId) throws Exception{

        Article article = getFullArticleObject(title, body, authorId,
                                               parentArticle, imagesId);

        Article resultArticle = null;
        try {
            resultArticle = dataCollector.addArticle(article);
        }
        catch (Exception ex) {
            //TODO: throw new DBException();
            throw new Exception();
        }
        if (resultArticle == null) {
            throw new ArticleAddException();
        }

        return resultArticle;
    }

    /**
     * Delete article by id
     * @param id
     */
    public void deleteArticle(Integer id) throws Exception{
        try {
            dataCollector.deleteArticle(id);
        }
        catch (Exception ex) {
           throw new ArticleDeleteException();
        }
    }

    public Article getArticleObject(Integer id) throws Exception{
        Article article = null;
        try {
            article = dataCollector.findArticle(id);
        }
        catch (Exception ex) {
            //TODO: throw new DBException();
            throw new Exception();
        }
        if (article == null) {
            throw new ArticleNotFoundException();
        }
        return article;
    }

    /**
     * Find article by id
     * @param id
     * @return article object
     */
    public String getArticle(Integer id) throws Exception{
        Article article = null;
        try {
            article = dataCollector.findArticle(id);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        if (article == null) {
            throw new ArticleNotFoundException();
        }
        return article.getBody();
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
                                 List<String> imagesId) throws Exception {
        Article article = null;
            article = getFullArticleObject(title, body, authorId,
                    parentArticle, imagesId);

            article.setId(id);
        try {
            article = dataCollector.updateArticle(article);
        }
        catch (Exception ex) {
            throw new ArticleUpdateException();
        }
        return article;
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
                                         List<String> imagesId) throws Exception
    {
        Article article = new Article();

        String clearBody = ArticleProcessor.getPureBody(body);

        Article parent;
        List<Image> images;
        User author;

        try {
            parent = dataCollector.findArticle(parentArticle);
            images = dataCollector.getImages(imagesId);
            author = dataCollector.findUser(authorId);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }

        if (parent == null && parentArticle != BASE_ARTICLE) {
            throw new ParentArticleNotFoundException();
        }
        if (imagesId.size() != images.size()) {
            throw new ImageNotFoundException();
        }
        if (author == null) {
            throw new UserNotFoundException();
        }


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
