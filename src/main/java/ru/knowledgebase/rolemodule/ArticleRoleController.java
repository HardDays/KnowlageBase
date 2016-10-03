package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.exceptionmodule.articleexceptions.NotASectionException;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.roleexceptions.*;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;

import java.util.List;

/**
 * Created by vova on 20.08.16.
 */
public class ArticleRoleController {

    private int defaultArticleRoleId = 1;

    private DataCollector collector = DataCollector.getInstance();

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
        //go through tree and clear previous roles
        try {
            Article temp = article;
            while (true) {
                if (temp.isSection()){
                    role = collector.findUserArticleRole(user, temp);
                    if (role != null) {
                        break;
                    }
                }
                if (temp.getParentArticle() == -1)
                    break;
                temp = collector.findArticle(temp.getSectionId());
            }
        }catch (Exception e){
            throw  new DataBaseException();
        }
        ArticleRole articleRole = role.getArticleRole();
        if (articleRole == null)
            throw new RoleNotFoundException();
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
            article = collector.getBaseArticle();
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
            article = collector.getBaseArticle();
            articleRole = collector.findArticleRole(defaultArticleRoleId);
            user = collector.findUser(userId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (article == null || articleRole == null || user == null)
            throw new AssignDefaultRoleException();
        assignUserRole(user, article, articleRole);
    }

    private void removeParentRoles(User user, Article section) throws Exception{
        try{
            Article temp = collector.findArticle(section.getSectionId());
            while (true){
                UserArticleRole role = collector.findUserArticleRole(user, temp);
                collector.deleteUserArticleRole(role);
                if (temp.getParentArticle() == -1)
                    break;
                temp = collector.findArticle(temp.getSectionId());
            }
        }catch (Exception e){
            throw new DataBaseException();
        }
    }

    /**
     * Create user role for specified article
     * @param role formed object
     */
    public void assignUserRole(UserArticleRole role) throws Exception{
        Article article = role.getArticle();
        if (!article.isSection())
            throw new NotASectionException();
        try{
            Article temp = article;
            while (true) {
                if (temp.isSection()){
                    UserArticleRole tempRole = collector.findUserArticleRole(role.getUser(), temp);
                    if (tempRole != null) {
                        deleteUserRole(tempRole);
                        break;
                    }
                }
                if (temp.getParentArticle() == -1)
                    break;
                temp = collector.findArticle(temp.getSectionId());
            }
            User user = role.getUser();
            for (Article child : collector.getUserSectionsObj(user.getId())){
                UserArticleRole tempRole = collector.findUserArticleRole(user, child);
                if (tempRole != null)
                    deleteUserRole(tempRole);
            }
            collector.addUserArticleRole(role);
        }catch (Exception e){
            e.printStackTrace();
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
     * Create user role for specified section
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
            throw new DataBaseException();
        }
        assignUserRole(user, article, articleRole);
    }

    public void assignSuperUser(int userId, int articleId, int articleRoleId) throws Exception{
        User user = null;
        Article article = null;
        ArticleRole articleRole = null;
        try {
            user = collector.findUser(userId);
            article = collector.getBaseArticle();
            articleRole = collector.findArticleRole(articleRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        assignUserRole(user, article, articleRole);
    }

    /**
     * Delete user role for specified article
     * @param role role formed object (important: id should be specified)
     */
    private void deleteUserRole(UserArticleRole role) throws Exception{
        int count = collector.getAttachedSectionCount(role.getUser().getId());
        if (count == 1){
            throw new RoleDeleteException();
        }
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
        UserArticleRole role = null;
        try{
            role = collector.findUserArticleRole(user, article);
        }catch (Exception e){
            throw new DataBaseException();
        }
        deleteUserRole(role);
    }

    /**
     * Delete user role for specified article
     *
     * @param userId        user id
     * @param articleId     article id
     */
    public void deleteUserRole(int userId, int articleId) throws Exception {
        User user = null;
        Article article = null;
        ArticleRole articleRole = null;
        try {
            user = collector.findUser(userId);
            article = collector.findArticle(articleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        try{
            articleRole = collector.findUserArticleRole(user, article).getArticleRole();
        }catch (Exception e){
            throw new RoleNotAssignedException();
        }
        deleteUserRole(user, article, articleRole);
    }

    public void createBaseRoles() throws Exception{
        ArticleRole user = new ArticleRole();
        user.setName("Пользователь");
        user.setCanViewArticle(true);
        user.setCanAddMistakes(true);
        user.setCanSearch(true);
        user.setCanAddArticle(true);
        create(user);

        try{
            defaultArticleRoleId = collector.findArticleRole("Пользователь").getId();
        }catch (Exception e){
            throw new DataBaseException();
        }

        ArticleRole superUser = new ArticleRole();
        superUser.setName("Суперпользователь");
        superUser.setCanViewArticle(true);
        superUser.setCanAddMistakes(true);
        superUser.setCanSearch(true);
        superUser.setCanAddArticle(true);
        superUser.setCanEditArticle(true);
        superUser.setCanViewArticle(true);
        superUser.setCanDeleteArticle(true);
        superUser.setCanAddNews(true);
        superUser.setCanGetEmployeesActionsReports(true);
        superUser.setCanGetNotifications(true);
        superUser.setCanGetSearchOperationsReports(true);
        superUser.setCanGetSystemActionsReports(true);
        superUser.setCanOnOffNotifications(true);
        superUser.setCanSearch(true);
        superUser.setCanViewArticle(true);
        superUser.setCanViewMistakes(true);
        create(superUser);

        ArticleRole admin = new ArticleRole();
        admin.setName("Администратор раздела");
        admin.setCanViewArticle(true);
        admin.setCanAddMistakes(true);
        admin.setCanSearch(true);
        admin.setCanAddArticle(true);
        admin.setCanEditArticle(true);
        admin.setCanViewArticle(true);
        admin.setCanDeleteArticle(true);
        admin.setCanAddNews(true);
        admin.setCanGetEmployeesActionsReports(true);
        admin.setCanGetNotifications(true);
        admin.setCanGetSearchOperationsReports(true);
        admin.setCanGetSystemActionsReports(true);
        admin.setCanOnOffNotifications(true);
        admin.setCanSearch(true);
        admin.setCanViewArticle(true);
        admin.setCanViewMistakes(true);
        create(admin);

        ArticleRole superVisor = new ArticleRole();
        superVisor.setName("Супервизор");
        superVisor.setCanViewArticle(true);
        superVisor.setCanAddMistakes(true);
        superVisor.setCanSearch(true);
        superVisor.setCanAddArticle(true);
        superVisor.setCanGetEmployeesActionsReports(true);
        create(superVisor);

    }

    public int getDefaultArticleRoleId() {
        return defaultArticleRoleId;
    }

    public void setDefaultArticleRoleId(int defaultArticleRoleId) {
        this.defaultArticleRoleId = defaultArticleRoleId;
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
        return findUserRole(userId, articleId).isCanGetSystemActionsReports();
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

    public boolean canGetSearchOperationsReports(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanGetSearchOperationsReports();
    }

    public boolean canGetEmployeesActionsReports(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanGetEmployeesActionsReports();
    }

    public boolean canGetSystemActionsReports(int userId, int articleId) throws Exception {
        return findUserRole(userId, articleId).isCanGetSystemActionsReports();
    }

    /** Indicates if user has an access to all those sections
    @param userID @param sections
    @return */
    public boolean hasAccessToSections(int userID, List<Integer> sections) throws Exception{
        try {
            boolean res = true;
            for (Integer sectionId : sections) {
                res = res && findUserRole(userID, sectionId).isCanViewArticle();
            }
            return res;
        }catch (Exception e){
            throw new DataBaseException();
        }
    }

}
