package ru.knowledgebase.rolemodule;

import ru.knowledgebase.articlemodule.ArticleNotFoundException;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;

/**
 * Created by vova on 20.08.16.
 */
public class ArticleRoleController {

    private static DataCollector collector = new DataCollector();

    /**
     * Create new article role
     * @param articleRole article role formed object
     */
    public static void create(ArticleRole articleRole) throws Exception{
        try {
            collector.addArticleRole(articleRole);
        }catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new RoleAlreadyExistsException();
        }
    }
    /**
     * Update article role
     * @param articleRole article role object (important: id should be specified)
     */
    public static void update(ArticleRole articleRole) throws Exception{
        collector.updateArticleRole(articleRole);
    }
    /**
     * Delete article role
     * @param articleRole article role object (important: id should be specified)
     */
    public static void delete(ArticleRole articleRole) throws Exception{
        if (articleRole == null)
            throw new RoleNotFoundException();
        collector.deleteArticleRole(articleRole);
    }
    /**
     * Update article role
     * @param articleRoleId id of article role
     */
    public static void delete(int articleRoleId) throws Exception{
        ArticleRole role = collector.findArticleRole(articleRoleId);
        collector.deleteArticleRole(role);
    }
    /**
     * Find user role for article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @return article role object
     */
    public static ArticleRole findUserRole(User user, Article article) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        return collector.findUserArticleRole(user, article).getArticleRole();
    }
    /**
     * Find user role for article
     * @param userId id of user
     * @param articleId id of article
     * @return article role object
     */
    public static ArticleRole findUserRole(int userId, int articleId) throws Exception{
        User user = collector.findUser(userId);
        Article article = collector.findArticle(articleId);
        return findUserRole(user, article);
    }
    /**
     * Create user role for specified article
     * @param role formed object
     */
    public static void assignUserRole(UserArticleRole role) throws Exception{
        UserArticleRole existRole = collector.findUserArticleRole(role.getUser(),role.getArticle());
        if (existRole != null)
            role.setId(existRole.getId());
        collector.addUserArticleRole(role);
    }
    /**
     * Create user role for specified article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @param articleRole article role object (important: id should be specified)
     */
    public static void assignUserRole(User user, Article article, ArticleRole articleRole) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        if (articleRole == null)
            throw new RoleNotFoundException();
        assignUserRole(new UserArticleRole(user, article, articleRole));
    }
    /**
     * Create user role for specified article
     * @param userId user id
     * @param articleId article id
     * @param articleRoleId article role id
     */
    public static void assignUserRole(int userId, int articleId, int articleRoleId) throws Exception{
        User user = collector.findUser(userId);
        Article article = collector.findArticle(articleId);
        ArticleRole articleRole = collector.findArticleRole(articleRoleId);
        assignUserRole(user, article, articleRole);
    }
    /**
     * Delete user role for specified article
     * @param role role formed object (important: id should be specified)
     */
    private static void deleteUserRole(UserArticleRole role) throws Exception{
        collector.deleteUserArticleRole(role);
    }
    /**
     * Create user role for specified article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @param articleRole article role object (important: id should be specified)
     */
    public static void deleteUserRole(User user, Article article, ArticleRole articleRole) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        if (articleRole == null)
            throw new RoleNotFoundException();
        deleteUserRole(new UserArticleRole(user, article, articleRole));
    }
    /**
     * Delete user role for specified article
     * @param userId user id
     * @param articleId article id
     * @param articleRoleId article role id
     */
    public static void deleteUserRole(int userId, int articleId, int articleRoleId) throws Exception{
        User user = collector.findUser(userId);
        Article article = collector.findArticle(articleId);
        ArticleRole articleRole = collector.findArticleRole(articleRoleId);
        deleteUserRole(user, article, articleRole);
    }

}
