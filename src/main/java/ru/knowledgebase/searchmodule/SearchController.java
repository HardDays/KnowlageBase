package ru.knowledgebase.searchmodule;

import org.springframework.beans.factory.annotation.Autowired;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.searchexceptions.SearchException;
import ru.knowledgebase.exceptionmodule.searchexceptions.WrongSearchParametersException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Мария on 07.09.2016.
 */

public class SearchController {

    @Autowired
    private DataCollector dataCollector = DataCollector.getInstance();

    private static SearchController instance;

    /**
     * Controller as thread-safe singleton
     * @return
     */
    public static SearchController getInstance() {
        SearchController localInstance = instance;
        if (localInstance == null) {
            synchronized (SearchController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SearchController();
                }
            }
        }
        return localInstance;
    }

    /**
     * Searches in articles titles for appearances of words of a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @return List of {@code Article} having words from search request in title
     * @throws SearchException
     */
    public List<Article> searchByTitle(int userID, String searchRequest, int numArticles) throws Exception {
        if(isWrongRequestFormat(searchRequest))
            throw new WrongSearchParametersException();
        try{
            return getArticlesAvailableToUser(userID, dataCollector.searchByTitle(searchRequest)).subList(0, numArticles);
        }catch (Exception ex){
            throw new SearchException();
        }
    }

    /**
     * Searches in articles bodies for appearances of words of a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @param numArticles
     * @return List of {@code Article} having words from search request in body
     * @throws SearchException
     */
    public List<Article> searchByBody(int userID, String searchRequest, int numArticles) throws Exception {
        if(isWrongRequestFormat(searchRequest))
            throw new WrongSearchParametersException();
        try{
            return getArticlesAvailableToUser(userID, dataCollector.searchByBody(searchRequest)).subList(0, numArticles);
        }catch (Exception ex){
            throw new SearchException();
        }
    }

    private List<Article> getArticlesAvailableToUser(int userID, List<Article> articles) throws Exception {
        List<Integer> sections = getSectionsAvailableToUser(userID);
        List<Article> result = new LinkedList<>();
        for (Article article : articles) {
            if (sections.contains(article.getSectionId())) {
                result.add(article);
            }
        }
        return result;
    }

    /**
     * Finds a list of sections which user as access to.
     * @param userID
     * @return list of sections available to user
     * @throws DataBaseException
     */
    private List<Integer> getSectionsAvailableToUser(int userID) throws Exception {
        try {
            List<Integer> list = new LinkedList<>();
            list.addAll(dataCollector.getUserSections(userID));
            return list;
        } catch (Exception e) {
            throw new DataBaseException();
        }
    }

    /**
     * Checks if search request is not empty and contains smth other than punctuation symbols
     * @param searchRequest
     * @return {@code true} if format of request is wrong, {@code false} vise versa.
     */
    private boolean isWrongRequestFormat(String searchRequest) throws Exception {
        if ((searchRequest.replaceAll("[^a-zA-Z ]", "").isEmpty()))
            return true;
        return false;
    }
}
