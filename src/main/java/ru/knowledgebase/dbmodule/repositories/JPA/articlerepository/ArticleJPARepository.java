package ru.knowledgebase.dbmodule.repositories.JPA.articlerepository;

/**
 * Created by root on 17.08.16.
 */
import org.elasticsearch.bootstrap.Elasticsearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.List;

public interface ArticleJPARepository extends CrudRepository<Article, Integer> {

    @Query("from Article where title like ?1")
    public List<Article> findByTitle(String title);

    public List<Article> findByParentId(int parentId);
	
	    @Query("select isSection from Article where id = ?1")
    boolean isSection(int articleId);
}
