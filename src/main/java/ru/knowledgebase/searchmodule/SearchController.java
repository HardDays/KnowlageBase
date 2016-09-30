package ru.knowledgebase.searchmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.searchexceptions.SearchException;
import ru.knowledgebase.exceptionmodule.searchexceptions.WrongSearchParametersException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Мария on 07.09.2016.
 */

@Controller
public class SearchController {

    @Autowired
    private DataCollector dataCollector = new DataCollector();

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

        return getArticlesUserHasAcessTo(userID, result);
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
        return getArticlesUserHasAcessTo(userID, result);
    }

    /**
     * Traverse through given list of articles and finds those to which user {@code userID} has access to
     * @param userID
     * @param articles
     * @return list of articles user has access to
     */
    private List<Article> getArticlesUserHasAcessTo(int userID, List<Article> articles) {
        if(articles.isEmpty())
            return articles;
        List<Article>  avalableArticles = new LinkedList<>();
        for (Article article:articles) {
            if(hasAcess(userID, article)){
                avalableArticles.add(article);
            }
        }
        return avalableArticles;
    }

    /**
     * Indicates wheather user has access to a given article
     * @param userID
     * @param article
     * @return
     */
    private boolean hasAcess(int userID, Article article) {
        //TODO: check if user has access to given article
        return false;
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
