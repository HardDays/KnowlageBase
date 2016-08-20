package ru.knowledgebase.dbmodule.repositories;

/**
 * Created by root on 17.08.16.
 */
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.Article;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    @Query("from Article")
    public List<Article> getAll() throws Exception;
}
