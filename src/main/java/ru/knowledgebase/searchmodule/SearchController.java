package ru.knowledgebase.searchmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.searchexceptions.SearchException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

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
     * @param searchRequest
     * @return List of {@code Article} having words from search request in title
     * @throws SearchException
     */
    public List<Article> searchByTitle(String searchRequest) throws Exception {
        try{
            return dataCollector.searchByTitle(searchRequest);
        }catch (Exception ex){
            throw new SearchException();
        }
    }

    /**
     * Searches in articles bodies for appearances of words of a given string {@code searchRequest}
     * @param searchRequest
     * @return List of {@code Article} having words from search request in body
     * @throws SearchException
     */
    public List<Article> searchByBody(String searchRequest) throws Exception {
        try{
            return dataCollector.searchByBody(searchRequest);
        }catch (Exception ex){
            throw new SearchException();
        }
    }
}
