package ru.knowledgebase.articlemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.*;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.imageexceptions.ImageNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class ArticleController {

    private DataCollector dataCollector = new DataCollector();
    private final int BASE_ARTICLE = -1;

    private static ArticleController instance;

    /**
     * Controller as thread-safe singleton
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
     * @return
     * @throws Exception
     */
    public Article addBaseArticle(String title, String body,
                                  int authorId,
                                  Timestamp lifeTime) throws Exception {
        Article article = getFullArticleObject(title, body, authorId,
                -1, lifeTime, true);

        Article resultArticle = null;
        try {
            resultArticle = dataCollector.addArticle(article);
        }
        catch (Exception ex) {
            throw new DataBaseException();
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
     */
    public Article addArticle(String title, String body,
                           int authorId, int parentArticle,
                           Timestamp lifeTime, boolean isSection) throws Exception{

        Article article = getFullArticleObject(title, body, authorId,
                                               parentArticle, lifeTime, isSection);

        Article resultArticle = null;
        try {
            resultArticle = dataCollector.addArticle(article);
        }
        catch (Exception ex) {
            throw new DataBaseException();
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

    /**
     * Find article by id
     * @param id
     * @return article object
     */
    public Article getArticle(Integer id) throws Exception {
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
        return article;
    }

    /**
     * Update article. Search for old article, change fields and save
     * @param id
     * @param title
     * @param body
     * @param authorId
     * @param parentArticle
     * @return
     */
    public Article updateArticle(Integer id, String title, String body,
                                 int authorId, int parentArticle,
                                 Timestamp lifeTime, boolean isSection) throws Exception {
        Article article = null;
            article = getFullArticleObject(title, body, authorId,
                    parentArticle, lifeTime, isSection);

            article.setId(id);
        try {
            article = dataCollector.updateArticle(article);
        }
        catch (Exception ex) {
            throw new ArticleUpdateException();
        }
        return article;
    }

    public Article updateArticle(Article article) throws Exception {
        Article upArticle = null;
        try {
            upArticle = dataCollector.updateArticle(article);
        }
        catch (Exception ex) {
            throw new ArticleUpdateException();
        }
        return upArticle;
    }

    /**
     * Returns all first-level children of current article
     * @param articleId
     * @return List of articles
     * @throws Exception
     */
    public List<Article> getArticleChildren(int articleId) throws Exception {
        List<Article> artilces = null;
        try {
            artilces = dataCollector.getChildren(articleId);
        }
        catch (Exception ex) {
            throw new ArticleHasNoChildrenException();
        }
        return artilces;
    }

    public List<Article> findArticleByTitle(String title) throws Exception{
        return this.dataCollector.findArticleByTitle(title);
    }

    //END CRUD METHODS

    //BEGIN PRIVATE METHODS

    /**
     * Construct new Article object by data
     * @param title
     * @param body
     * @param authorId
     * @param parentArticle
     * @return
     * */
    private Article getFullArticleObject(String title, String body,
                                         int authorId, int parentArticle,
                                         Timestamp lifeTime, boolean isSection) throws Exception
    {
        Article article = new Article();

        String clearBody = ArticleProcessor.getPureBody(body);

        Article parent;
        List<Image> images;
        User author;

        try {
            parent = dataCollector.findArticle(parentArticle);
            author = dataCollector.findUser(authorId);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }

        if (parent == null && parentArticle != BASE_ARTICLE) {
            throw new ParentArticleNotFoundException();
        }
        if (author == null) {
            throw new UserNotFoundException();
        }


        article.setBody(body);
        article.setTitle(title);
        article.setClearBody(clearBody);
        article.setParentArticle(parentArticle);
        article.setAuthor(author);
        article.setLifeTime(lifeTime);
        article.setSection(isSection);

        return article;
    }
    //END PRIVATE METHODS
}
