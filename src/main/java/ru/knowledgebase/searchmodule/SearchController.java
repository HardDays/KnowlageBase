package ru.knowledgebase.searchmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.searchexceptions.SearchException;
import ru.knowledgebase.exceptionmodule.searchexceptions.WrongSearchParametersException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Мария on 07.09.2016.
 */

@Controller
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
    public List<Article> searchByTitle(int userID, String searchRequest) throws Exception {
        List<Article> result;
        if(isWrongRequestFormat(searchRequest))
            throw new WrongSearchParametersException();
        try{
            result = dataCollector.searchByTitle(searchRequest);
        }catch (Exception ex){
            throw new SearchException();
        }

        return getArticlesAvailableToUser(result, userID);
    }

    /**
     * Searches in articles bodies for appearances of words of a given string {@code searchRequest}
     * @param userID
     * @param searchRequest
     * @return List of {@code Article} having words from search request in body
     * @throws SearchException
     */
    public List<Article> searchByBody(int userID, String searchRequest) throws Exception {
        List<Article> result;
        if(isWrongRequestFormat(searchRequest))
            throw new WrongSearchParametersException();
        try{
            result =  dataCollector.searchByBody(searchRequest);
        }catch (Exception ex){
            throw new SearchException();
        }
        return getArticlesAvailableToUser(result, userID);
    }

    /**
     * Traverse through given list of articles and finds those to which user {@code userID} has access to
     * @param articles
     * @param userID
     * @return
     * @throws DataBaseException
     */
    private List<Article> getArticlesAvailableToUser(List<Article> articles, Integer userID)
            throws DataBaseException {
        if(articles.isEmpty()) return articles;
        List<Article> articlesUserHasAccessTo = new LinkedList<>();
        try {
            HashSet<Integer> usersSections = dataCollector.getUserSections(userID);
            for(Article article : articles){
                if(usersSections.contains(article.getId()))
                    articlesUserHasAccessTo.add(article);
            }
        } catch (Exception e) {
            throw new DataBaseException();
        }
        return articlesUserHasAccessTo;
    }

    /**
     * Checks if search request is not empty and contains smth other than punctuation symbols
     * @param searchRequest
     * @return {@code true} if format of request is wrong, {@code false} vise versa.
     */
    private boolean isWrongRequestFormat(String searchRequest) {
        if ((searchRequest.replaceAll("[^a-zA-Z ]", "").isEmpty()))
            return true;
        return false;
    }
}
