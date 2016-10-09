package ru.knowledgebase.dbmodule.repositories.articlerepositories;

/**
 * Created by root on 17.08.16.
 */
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    @Query("from Article")
    public List<Article> getAll();

    @Query("from Article where title = ?1")
    public List<Article> findByTitle(String title);

    @Query("from Article where title LIKE ?1")
    public Iterable<Article> findByClearBody(String title);

    @Query("from Article where parentId=-1")
    public Article getBaseArticle();

    @Query("select isSection from Article where id = ?1")
    boolean isSection(int articleId);
}
