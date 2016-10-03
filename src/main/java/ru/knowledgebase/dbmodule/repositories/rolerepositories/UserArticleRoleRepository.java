package ru.knowledgebase.dbmodule.repositories.rolerepositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by vova on 19.08.16.
 */
public interface UserArticleRoleRepository extends CrudRepository<UserArticleRole, Integer> {
    @Query("select r from UserArticleRole r where  r.user =?1 and r.article = ?2")
    public List<UserArticleRole> find(User user, Article article) throws Exception;

    @Query("select r from UserArticleRole r where  r.user.id =?1 and r.article.id = ?2")
    public List<UserArticleRole> find(int userId, int articleId) throws Exception;

    @Query("select r.user from UserArticleRole r where r.article = ?1 and r.articleRole.canViewMistakes = true")
    public List <User> findMistakeViewers(Article article);

    @Query("select r.user from UserArticleRole r where r.article.id = ?1 and r.articleRole.canViewMistakes = true")
    public List <User> findMistakeViewers(int articleId);

    @Query("select r from UserArticleRole r where r.article.id = ?1")
    public List<UserArticleRole> findByArticle(int articleId);

    @Query("from UserArticleRole")
    public List<UserArticleRole> getAll() throws Exception;

    @Modifying
    @Transactional
    @Query("delete from UserArticleRole where user.id = ?1 and article.id = ?2")
    void delete(int userId, int articleId);

}
