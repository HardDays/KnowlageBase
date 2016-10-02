package ru.knowledgebase.dbmodule.repositories.articlerepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.articlemodels.ArticleConnection;

import java.util.List;

/**
 * Created by root on 11.09.16.
 */
public interface ArticleConnectionRepository extends CrudRepository<ArticleConnection, Integer> {
    @Query("from ArticleConnection where parentId = ?1")
    List<ArticleConnection> findByParentId(int articleId);

    @Query("from ArticleConnection where childId = ?1")
    List<ArticleConnection> findConnectionsById(int articleId);
}
