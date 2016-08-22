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

    public static void assignUserRole(User user, ArticleRole articleRole, Article article) throws Exception{
        DataCollector coll = new DataCollector();
        if (user == null)
            throw new UserNotFoundException();
        if (articleRole == null)
            throw new RoleNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        UserArticleRole newRole = new UserArticleRole(user, articleRole, article);
        UserArticleRole role = coll.findUserArticleRole(user, article);
        if (role != null){
            newRole.setId(role.getId());
        }
        coll.addUserArticleRole(newRole);
    }

    public static void assignUserRole(int userId, int roleId, int articleId) throws Exception{
        DataCollector coll = new DataCollector();
        User user = coll.findUserById(userId);
        ArticleRole role = coll.findArticleRoleById(roleId);
        Article article = coll.findArticleById(articleId);
        assignUserRole(user, role, article);
    }

    public static void delete(ArticleRole role){
        DataCollector coll = new DataCollector();

    }
}
