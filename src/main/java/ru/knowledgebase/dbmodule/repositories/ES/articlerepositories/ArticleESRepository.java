package ru.knowledgebase.dbmodule.repositories.ES.articlerepositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.List;

/**
 * Created by Мария on 08.10.2016.
 */
public interface ArticleESRepository extends ElasticsearchRepository<Article, Integer>, ArticleRepoFacade {

    List<Article> findByTitle(String title);

    List<Article> findByClearBody(String clearBody);
}
