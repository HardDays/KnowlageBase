package ru.knowledgebase.dbmodule.dataservices.searchservices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.dbmodule.repositories.ES.articlerepositories.ArticleESRepository;
import ru.knowledgebase.dbmodule.repositories.JPA.articlerepository.ArticleJPARepository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.List;

/**
 * Created by Мария on 13.09.2016.
 */
@Service
public class SearchService {

    @Autowired
    ArticleJPARepository articleRepository;

    @Autowired
    ArticleESRepository articleESRepository;

    public List<Article> searchByTitle(String searchRequest) throws Exception {
        return articleESRepository.findByTitle(searchRequest);
    }

    public List<Article> searchByBody(String searchRequest) throws Exception{
        return articleESRepository.findByClearBody(searchRequest);
    }
}
