package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.roleexceptions.AssignDefaultRoleException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;

import java.util.List;

/**
 * Created by vova on 20.08.16.
 */
public class ArticleRoleController {

    private int defaultArticleRoleId = 1;
    private int defaultRootArticleId = 1;

    private DataCollector collector = new DataCollector();

    private static volatile ArticleRoleController instance;

    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static ArticleRoleController getInstance() {
        ArticleRoleController localInstance = instance;
        if (localInstance == null) {
            synchronized (ArticleRoleController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArticleRoleController();
                }
            }
        }
        return localInstance;
    }
    /**
     * Return all available section roles
     * @return list of roles
     */
    public List<ArticleRole> getAll() throws Exception{
        List <ArticleRole> roles = null;
        try{
            roles = collector.getArticleRoles();
        }catch (Exception e){
            throw new DataBaseException();
        }
        return roles;
    }
    /**
     * Create new article role
     * @param articleRole article role formed object
     */
    public void create(ArticleRole articleRole) throws Exception{
        try {
            collector.addArticleRole(articleRole);
        }catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new RoleAlreadyExistsException();
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Update article role
     * @param articleRole article role object (important: id should be specified)
     */
    public void update(ArticleRole articleRole) throws Exception{
        try{
            collector.updateArticleRole(articleRole);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Delete article role
     * @param articleRole article role object (important: id should be specified)
     */
    public void delete(ArticleRole articleRole) throws Exception{
        if (articleRole == null)
            throw new RoleNotFoundException();
        try{
            collector.deleteArticleRole(articleRole);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Delete article role
     * @param articleRoleId id of article role
     */
    public void delete(int articleRoleId) throws Exception{
        ArticleRole role = null;
        try{
            role = collector.findArticleRole(articleRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        delete(role);
    }
    /**
     * Find user role for article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @return article role object
     */
    public ArticleRole findUserRole(User user, Article article) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        UserArticleRole role = null;
        //go through article tree to root
        try {
            role = collector.findUserArticleRole(user, article);
            while (role == null && article.getParentArticle() != null) {
                article = article.getParentArticle();
                role = collector.findUserArticleRole(user, article);
            }
        }catch (Exception e){
            throw  new DataBaseException();
        }
        ArticleRole articleRole = role.getArticleRole();
        if (articleRole == null)
            throw new DataBaseException();
        return articleRole;
    }
    /**
     * Find user role for article
     * @param userId id of user
     * @param articleId id of article
     * @return article role object
     */
    public ArticleRole findUserRole(int userId, int articleId) throws Exception{
        User user = null;
        Article article = null;
        try{
            user = collector.findUser(userId);
            article = collector.findArticle(articleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        return findUserRole(user, article);
    }
    /**
     * Create default user role for root article
     * @param user user object
     */
    public void assignDefaultUserRole(User user) throws Exception{
        Article article = null;
        ArticleRole articleRole = null;
        try {
            article = collector.findArticle(defaultRootArticleId);
            articleRole = collector.findArticleRole(defaultArticleRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (article == null || articleRole == null)
            throw new AssignDefaultRoleException();
        assignUserRole(user, article, articleRole);
    }
    /**
     * Create default user role for root article
     * @param userId user id
     */
    public void assignDefaultUserRole(int userId) throws Exception{
        Article article = null;
        ArticleRole articleRole = null;
        User user = null;
        try {
            article = collector.findArticle(defaultRootArticleId);
            articleRole = collector.findArticleRole(defaultArticleRoleId);
            user = collector.findUser(userId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (article == null || articleRole == null || user == null)
            throw new AssignDefaultRoleException();
        assignUserRole(user, article, articleRole);
    }
    /**
     * Create user role for specified article
     * @param role formed object
     */
    public void assignUserRole(UserArticleRole role) throws Exception{
        try{
            UserArticleRole existRole = collector.findUserArticleRole(role.getUser(),role.getArticle());
            if (existRole != null)
                role.setId(existRole.getId());
         //   Article article = role.getArticle();
         //   article.setIsSection(true);
         //   collector.updateArticle(article);
            collector.addUserArticleRole(role);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Create user role for specified article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @param articleRole article role object (important: id should be specified)
     */
    public void assignUserRole(User user, Article article, ArticleRole articleRole) throws Exception{
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
    public void assignUserRole(int userId, int articleId, int articleRoleId) throws Exception{
        User user = null;
        Article article = null;
        ArticleRole articleRole = null;
        try {
            user = collector.findUser(userId);
            article = collector.findArticle(articleId);
            articleRole = collector.findArticleRole(articleRoleId);
        }catch (Exception e){
            //e.printStackTrace();
            throw new DataBaseException();
        }
        assignUserRole(user, article, articleRole);
    }
    /**
     * Delete user role for specified article
     * @param role role formed object (important: id should be specified)
     */
    private void deleteUserRole(UserArticleRole role) throws Exception{
        try {
            collector.deleteUserArticleRole(role);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Create user role for specified article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @param articleRole article role object (important: id should be specified)
     */
    public void deleteUserRole(User user, Article article, ArticleRole articleRole) throws Exception{
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
     *
     * @param userId        user id
     * @param articleId     article id
     * @param articleRoleId article role id
     */
    public void deleteUserRole(int userId, int articleId, int articleRoleId) throws Exception {
        User user = null;
        Article article = null;
        ArticleRole articleRole = null;
        try {
            user = collector.findUser(userId);
            article = collector.findArticle(articleId);
            articleRole = collector.findArticleRole(articleRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        deleteUserRole(user, article, articleRole);
    }

    public int getDefaultArticleRoleId() {
        return defaultArticleRoleId;
    }

    public void setDefaultArticleRoleId(int defaultArticleRoleId) {
        this.defaultArticleRoleId = defaultArticleRoleId;
    }

    public int getDefaultRootArticleId() {
        return defaultRootArticleId;
    }

    public void setDefaultRootArticleId(int defaultRootArticleId) {
        this.defaultRootArticleId = defaultRootArticleId;
    }

    public boolean canAddArticle(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanAddArticle();
    }

    public boolean canEditArticle(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanEditArticle();
    }

    public boolean canDeleteArticle(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanDeleteArticle();
    }

    public boolean canViewArticle(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanViewArticle();
    }

    public boolean canAddNews(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanAddNews();
    }

    public boolean canOnOffNotifications(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanOnOffNotifications();
    }

    public boolean canGetReports(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanGetReports();
    }

    public boolean canViewMistakes(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanViewMistakes();
    }

    public boolean canAddMistakes(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanAddMistakes();
    }

    public boolean canSearch(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanSearch();
    }

    public boolean canGetNotifications(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanGetNotifications();
    }
}
