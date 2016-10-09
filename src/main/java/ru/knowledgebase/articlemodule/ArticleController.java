package ru.knowledgebase.articlemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.*;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.imageexceptions.ImageNotFoundException;
import ru.knowledgebase.exceptionmodule.sectionexceptions.ArticleCanNotBeSectionException;
import ru.knowledgebase.exceptionmodule.sectionexceptions.NoSectionsException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class ArticleController {

    private DataCollector dataCollector = DataCollector.getInstance();
    private final int BASE_ARTICLE = -1;
    private int baseArticleId = -1;

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
                                  int authorId, Timestamp createdTime, Timestamp updatedTime,
                                  Timestamp lifeTime) throws Exception {
        Article article = getFullArticleObject(title, body, authorId,
                -1, createdTime, updatedTime, lifeTime, true);

        if (baseArticleId != BASE_ARTICLE) {
            throw new BaseArticleWasAlreadyCreatedException();
        }
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

        baseArticleId = resultArticle.getId();

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
                           int authorId, int parentArticle, Timestamp createdTime, Timestamp updatedTime,
                           Timestamp lifeTime, boolean isSection) throws Exception{

        Article article = getFullArticleObject(title, body, authorId,
                                               parentArticle, createdTime, updatedTime, lifeTime, isSection);

        if (isSection == true) {
            if (!this.checkIfSectionIsWellOrganized(parentArticle)) {
                throw new ArticleCanNotBeSectionException();
            }
        }

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
        if (id == baseArticleId) {
            baseArticleId = BASE_ARTICLE;
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
                                 int authorId, int parentArticle, Timestamp createdTime, Timestamp updatedTime,
                                 Timestamp lifeTime) throws Exception {
        Article originArticle = null;
        try {
            originArticle = dataCollector.findArticle(id);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }

        Article article = null;
        article = getFullArticleObject(title, body, authorId,
                parentArticle, createdTime, updatedTime, lifeTime, originArticle.isSection());

        article.setId(id);

        if (originArticle.isSection() == true) {
            if (!this.checkIfSectionIsWellOrganized(parentArticle)) {
                throw new ArticleCanNotBeSectionException();
            }
        }

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
    public List<Article> getArticleChildren(int articleId, int from, int to) throws Exception {
        List<Article> artilces = null;
        try {
            artilces = dataCollector.getChildren(articleId, from, to);
        }
        catch (Exception ex) {
            throw new ArticleHasNoChildrenException();
        }
        return artilces;
    }

    public List<Article> findArticleByTitle(String title) throws Exception{
        return this.dataCollector.findArticleByTitle(title);
    }

    /**
     * Returns all first-level children of current article
     * @param articleId
     * @return List of articles id
     * @throws Exception
     */
    public List<Integer> getArticleChildrenIds(int articleId) throws Exception {
        List<Integer> artilces = null;
        try {
            artilces = dataCollector.getChildrenIds(articleId);
        }
        catch (Exception ex) {
            throw new ArticleHasNoChildrenException();
        }
        return artilces;
    }

    public Article getBaseArticle() throws Exception{
        try {
            return dataCollector.getBaseArticle();
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
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
                                         int authorId, int parentArticle, Timestamp createdTime, Timestamp updatedTime,
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
        article.setCreatedTime(createdTime);
        article.setUpdatedTime(updatedTime);

        article.setSectionId(getArticleSection(parentArticle));

        return article;
    }

    /**
     * Calc section by next logic:
     * 1) If it is base article, section id will be BASE_ARTICLE id
     * 2) If this article if node under section
     * article - it will have section = parent id (nearest section)
     * 3) Otherwise section will be the same as parent section
     * @param parentId
     * @return
     * @throws Exception
     */
    private int getArticleSection(int parentId) throws Exception {
        if (parentId == BASE_ARTICLE) {
            return BASE_ARTICLE;
        }
        else {
            try {
                Article parent = dataCollector.findArticle(parentId);
                if (parent.isSection()){
                    return parent.getId();
                }
                else {
                    return parent.getSectionId();
                }
            }
            catch (Exception ex) {
                throw new DataBaseException();
            }
        }
    }

    //Call if we create a section under parent articles
    private boolean checkIfSectionIsWellOrganized(int parentArticle) throws Exception{
        //Section not not be created if parent article is not a section!
        //***
        Article parent = null;
        List<Integer> children = new LinkedList<Integer>();
        try {
            parent = dataCollector.findArticle(parentArticle);
            if (parent.isSection()) {
                children = dataCollector.getChildrenIds(parentArticle);
            }
        } catch (Exception ex) {
            throw new DataBaseException();
        }

        for (Integer i : children) {
            Article a = null;
            try {
                a = dataCollector.findArticle(i);
            }
            catch (Exception ex) {
                throw new DataBaseException();
            }
            if (a == null) {
                throw new ArticleNotFoundException();
            }
            if (!a.isSection()) {
                return false;
            }
        }

        if (parent.isSection()) {
            return true;
        }
        return false;
        //***
    }
    //END PRIVATE METHODS
}
