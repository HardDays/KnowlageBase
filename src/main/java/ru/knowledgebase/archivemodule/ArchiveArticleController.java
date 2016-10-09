package ru.knowledgebase.archivemodule;

import ru.knowledgebase.archivemodule.heplers.ArchTimeNode;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArchiveArticleNotFoundException;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleDeleteException;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 17.09.16.
 */
public class ArchiveArticleController {
    private DataCollector dataCollector = DataCollector.getInstance();

    private List<ArchTimeNode> times = new LinkedList<>();

    private static ArchiveArticleController instance;

    /**
     * Controller as thread-safe singleton
     * @return
     */
    public static ArchiveArticleController getInstance() {
        ArchiveArticleController localInstance = instance;
        if (localInstance == null) {
            synchronized (ArticleController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArchiveArticleController();
                }
            }
        }
        return localInstance;
    }

    //BEGIN CRUD METHODS

    /**
     * Get full tree of articles for adding all of them to archive.
     * Delete all articles from standard storage
     * Delete and insert are transactional operations. In case of error all
     * changes will be rolled back
     * @param article
     * @throws Exception
     */
    public void moveToArchive(Article article) throws Exception{

        List<Integer> tree = this.getArticleHierarchyTree(article);
        Collections.reverse(tree);

        List<ArchiveArticle> archArticles = new LinkedList<>();
        Article art = null;

        for (Integer id : tree) {
            art = this.getArticle(id);
            archArticles.add(makeArchiveObject(art));
        }

        this.addAllToArchive(archArticles);
        this.deleteAllArticles(tree);
    }

    public void deleteArchiveArticle(int id) throws Exception {
        try {
            dataCollector.deleteArchiveArticle(id);
        }
        catch (Exception ex) {
            throw new ArticleDeleteException();
        }
    }

    public List<ArchiveArticle> getSectionArchive(int sectionId) throws Exception{
        List<ArchiveArticle> sectionArchive = null;
        try {
            sectionArchive = dataCollector.getSectionArchive(sectionId);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        return sectionArchive;
    }

    public ArchiveArticle getArchiveArticle(int archiveArticleId) throws Exception{
        ArchiveArticle archArticle = null;
        try {
            archArticle = dataCollector.getArchiveArticle(archiveArticleId);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        if (archArticle == null) {
            throw new ArchiveArticleNotFoundException();
        }
        return archArticle;
    }
    //END CRUD METHODS

    //BEGIN MOVING METHODS

    /**
     * We will maintain a data structure in which all times are ordered
     * O(n)
     * @param art
     */
    public void addArchivationTime(Article art) {
        ArchTimeNode time = new ArchTimeNode(art.getId(), art.getLifeTime());
        int i = 0;
        for (; i < this.times.size(); i++) {
            if (this.times.get(i).getTime().compareTo(time.getTime()) >= 0) {
                break;
            }
        }
        this.times.add(i, time);
    }

    /**
     * O(2n)
     * @param art
     */
    public void changeArchivationTime(Article art) {
        this.deleteArchivationTime(art.getId());
        this.addArchivationTime(art);
    }

    /**
     * Check if current time is more, then minimal.
     * If yes - move article with minimal time to archive
     */
    public void archiveNext() throws Exception{
        Timestamp time = new Timestamp(new Date().getTime());
        if (this.times.get(0).getTime().compareTo(time) < 0) {
            this.moveToArchive(getArticle(this.times.get(0).getArticleId()));
        }
    }

    /**
     * O(n)
     * @param articleId
     */
    public void deleteArchivationTime(int articleId) {
        int i = 0;
        for (; i < this.times.size(); i++) {
            if (articleId == this.times.get(i).getArticleId()) {
                break;
            }
        }
        this.times.remove(i);
    }

    public void clearSectionArchive() {

    }

    //END MOVING METHODS

    //BEGIN SUPPORT METHODS

    public ArchiveArticle makeArchiveObject(Article article) throws Exception {
        ArchiveArticle archive = new ArchiveArticle();
        archive.setClearBody(article.getClearBody());
        archive.setBody(article.getBody());
        archive.setTitle(article.getTitle());
        archive.setAuthor(article.getAuthor());

        try {
            archive.setSectionId(article.getSectionId());
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }

        return archive;
    }

    private List<Integer> getArticleHierarchyTree(Article article) throws Exception {
        List<Integer> tree = null;
        try {
            tree = dataCollector.getArticleHierarchyTree(article.getId());
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        if (tree == null) {
            throw new ArticleNotFoundException();
        }

        return tree;
    }


    private void addAllToArchive(List<ArchiveArticle> archiveArticle) throws Exception {
        try {
            dataCollector.addAllToArchive(archiveArticle);
        } catch (Exception ex) {
            throw new DataBaseException();
        }
    }

    private void deleteAllArticles(List<Integer> ids) throws Exception{
        try {
            dataCollector.deleteAllArticles(ids);
        }
        catch (Exception ex) {
            throw new ArticleDeleteException();
        }
    }

    private Article getArticle(Integer id) throws Exception {
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
    //END SUPPORT METHODS
}
