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
        DataCollector collector = new DataCollector();
        collector.addArticleRole(role);
    }

    public static void update(ArticleRole role) throws Exception{
        DataCollector collector = new DataCollector();
        collector.updateArticleRole(role);
    }

    public static void delete(ArticleRole role) throws Exception{
        DataCollector collector = new DataCollector();
        collector.deleteArticleRole(role);
    }

    public static ArticleRole findUserRole(User user, Article article) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        DataCollector collector = new DataCollector();
        return collector.findUserArticleRole(user, article).getArticleRole();
    }

    public static ArticleRole findUserRole(int userId, int articleId) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUser(userId);
        Article article = collector.findArticle(articleId);
        return findUserRole(user, article);
    }

    public static void assignUserRole(UserArticleRole role) throws Exception{
        DataCollector collector = new DataCollector();
        UserArticleRole existRole = collector.findUserArticleRole(role.getUser(),role.getArticle());
        if (existRole != null){
            role.setId(existRole.getId());
        }
        collector.addUserArticleRole(role);
    }

    public static void assignUserRole(User user, Article article, ArticleRole articleRole) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        if (articleRole == null)
            throw new RoleNotFoundException();
        assignUserRole(new UserArticleRole(user, article, articleRole));
    }

    public static void assignUserRole(int userId, int articleId, int roleId) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUser(userId);
        Article article = collector.findArticle(articleId);
        ArticleRole articleRole = collector.findArticleRole(roleId);
        assignUserRole(user, article, articleRole);
    }

    public static void deleteUserRole(UserArticleRole role) throws Exception{
        DataCollector collector = new DataCollector();
        collector.deleteUserArticleRole(role);
    }

    public static void deleteUserRole(User user, Article article, ArticleRole articleRole) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        if (articleRole == null)
            throw new RoleNotFoundException();
        deleteUserRole(new UserArticleRole(user, article, articleRole));
    }

    public static void deleteUserRole(int userId, int articleId, int roleId) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUser(userId);
        Article article = collector.findArticle(articleId);
        ArticleRole articleRole = collector.findArticleRole(roleId);
        deleteUserRole(user, article, articleRole);
    }

}
