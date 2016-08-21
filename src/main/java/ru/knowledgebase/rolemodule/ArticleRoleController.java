package ru.knowledgebase.rolemodule;

import ru.knowledgebase.articlemodule.ArticleNotFoundException;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.rolemodule.exceptions.RoleNotFoundException;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;

/**
 * Created by vova on 20.08.16.
 */
public class ArticleRoleController {
    public static void create(ArticleRole role) throws Exception{
        DataCollector coll = new DataCollector();
        coll.addArticleRole(role);
    }

    public static void assignUserRole(User user, ArticleRole role, Article article) throws Exception{
        DataCollector coll = new DataCollector();
        coll.addUserArticleRole(new UserArticleRole(user, role, article));
    }

    public static void assignUserRole(int userId, int roleId, int articleId) throws Exception{
        DataCollector coll = new DataCollector();
        User user = coll.findUserById(userId);
        if (user == null)
            throw new UserNotFoundException();
        ArticleRole role = coll.findArticleRoleById(roleId);
        if (role == null)
            throw new RoleNotFoundException();
        Article article = coll.findArticleById(articleId);
        if (article == null)
            throw new ArticleNotFoundException();
        assignUserRole(user, role, article);
    }

    public static void delete(ArticleRole role){
        DataCollector coll = new DataCollector();

    }
}
