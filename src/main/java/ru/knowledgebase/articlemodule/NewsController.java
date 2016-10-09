package ru.knowledgebase.articlemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleProcessingException;
import ru.knowledgebase.exceptionmodule.articleexceptions.NewsAddException;
import ru.knowledgebase.exceptionmodule.articleexceptions.NewsNotFoundException;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.sectionexceptions.SectionNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.articlemodels.News;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 02.10.16.
 */
public class NewsController {

    private static NewsController instance;

    /**
     * Controller as thread-safe singleton
     * @return
     */
    public static NewsController getInstance() {
        NewsController localInstance = instance;
        if (localInstance == null) {
            synchronized (NewsController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new NewsController();
                }
            }
        }
        return localInstance;
    }


    private DataCollector dataCollector = DataCollector.getInstance();

    public News findNews(int id) throws Exception {
        News news = null;
        try {
            news = dataCollector.findNews(id);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        if (news == null) {
            throw new NewsNotFoundException();
        }
        return news;
    }

    public void deleteNews(int id) throws Exception {
        try {
            dataCollector.deleteNews(id);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
    }

    public News addNews(News news) throws Exception {
        try {
            return dataCollector.addNews(news);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
    }

    public News addNews(String title, String body, int authorId,
                        int sectionId, Timestamp date) throws Exception {
        News news = getFullNewsObject(title, body, authorId, sectionId, date);

        try {
            news = dataCollector.addNews(news);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }

        if(news == null) {
            throw new NewsAddException();
        }

        return news;
    }

    public List<News> getNewsBySection(int section) throws Exception{
        try {
            return dataCollector.getNewsBySection(section);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
    }

    public List<News> getNewsBySections(List<Integer> sections) throws Exception{
        List<News> news = new LinkedList<>();
        try {
            for (Integer i : sections) {
                news.addAll(dataCollector.getNewsBySection(i));
            }
            return news;
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
    }


    public News updateNews(int id, String title, String body,
                           int authorId, int sectionId, Timestamp date) throws Exception{
        News news = getFullNewsObject(title, body, authorId, sectionId, date);
        news.setId(id);

        try {
            news = dataCollector.updateNews(news);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }

        if(news == null) {
            throw new NewsAddException();
        }

        return news;
    }

    public List<News> getUserNewsFromDate(int userId, Timestamp date) throws Exception {
        Set<Integer> userSections = new HashSet<>();

        try {
            userSections = dataCollector.getUserSections(userId);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        List<News> news = new LinkedList<>();
        for (Integer i : userSections) {
            news.addAll(dataCollector.getSectionNewsFromDate(i, date));
        }

        return news;
    }

    //BEGIN PRIVATE METHODS
    private News getFullNewsObject(String title, String body, int authorId,
                                   int sectionId, Timestamp date) throws Exception {
        User user = null;
        try {
            user = dataCollector.findUser(authorId);
        }
        catch (Exception ex) {
            throw new DataBaseException();
        }
        if (user == null) {
            throw new UserNotFoundException();
        }

        String clearBody;
        try {
            clearBody = ArticleProcessor.getPureBody(body);
        }
        catch (Exception ex) {
            throw  new ArticleProcessingException();
        }

        Article section = null;
        try {
            section = dataCollector.findArticle(sectionId);
        }
        catch (Exception ex) {
            throw  new DataBaseException();
        }
        if (section == null) {
            throw new SectionNotFoundException();
        }

        News news = new News(title, body, clearBody, user, sectionId, date);
        return news;
    }

    //END PRIVATE METHODS

}
