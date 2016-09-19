package ru.knowledgebase.searchmodule;

import org.hibernate.search.exception.EmptyQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.dbmodule.dataservices.searchservices.SearchService;
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

    //TODO:should return what found
    public List<Article> searchByTitle(String searchRequest)throws Exception {
        try{
            return dataCollector.searchByTitle(searchRequest);
        }catch (Exception ex){
            //TODO: spacial exception
            System.out.println("Exeption" + ex.getStackTrace());
        }
        return null;
    }

    public List<Article> searchByBody(String searchRequest) {
        try{
            return dataCollector.searchByBody(searchRequest);
        }catch (EmptyQueryException ex){
            //TODO: spacial exception
            System.out.println("Exeption" + ex.getStackTrace());
        }catch (Exception ex){

        }
        return null;
    }
}
