package ru.knowledgebase.dbmodule.repositories.rolerepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by vova on 19.08.16.
 */
public interface UserArticleRoleRepository extends CrudRepository<UserArticleRole, Integer> {
    @Query("select r from UserArticleRole r where  r.user =?1 and r.article = ?2")
    public List<UserArticleRole> find(User user, Article article) throws Exception;
}
