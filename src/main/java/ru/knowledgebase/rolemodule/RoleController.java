package ru.knowledgebase.rolemodule;

import org.apache.commons.codec.digest.DigestUtils;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.exceptionmodule.articleexceptions.NotASectionException;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.roleexceptions.*;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongUserDataException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.rolemodels.UserSectionRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by vova on 07.10.16.
 */
public class RoleController {

    //ВНИМАНИЕ! ATTENTION! ACHTUNG! УВАГА!
    //KOSTILI DETECTED
    private int defaultRoleId = 5;
    private int adminRoleId = 2;
    private int superUserId = 1;

    private DataCollector collector = DataCollector.getInstance();
    private static volatile RoleController instance;

    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static RoleController getInstance() {
        RoleController localInstance = instance;
        if (localInstance == null) {
            synchronized (RoleController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RoleController();
                }
            }
        }
        return localInstance;
    }
    /**
     * Return all available section roles
     * @return list of roles
     */
    public List<Role> getAll() throws Exception{
        List <Role> roles = null;
        try{
            roles = collector.getRoles();
        }catch (Exception e){
            throw new DataBaseException();
        }
        return roles;
    }
    /**
     * Create new article role
     * @param role article role formed object
     */
    public void create(Role role) throws Exception{
        try {
            collector.addRole(role);
        }catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new RoleAlreadyExistsException();
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Update article role
     * @param role article role object (important: id should be specified)
     */
    public void update(Role role) throws Exception{
        try{
            collector.updateRole(role);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Delete article role
     * @param role article role object (important: id should be specified)
     */
    public void delete(Role role) throws Exception{
        if (role == null)
            throw new RoleNotFoundException();
        try{
            collector.deleteRole(role);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Delete article role
     * @param roleId id of article role
     */
    public void delete(int roleId) throws Exception{
        if (roleId == 0)
            throw new WrongUserDataException();
        try{
            collector.deleteRole(roleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Find user role for article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @return article role object
     */
    public Role findUserRole(User user, Article article) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        UserSectionRole userSectionRole = null;
        //go through tree and clear previous roles
        try {
            Article temp = article;
            while (true) {
                if (temp.isSection()){
                    userSectionRole = collector.findUserSectionRole(user, temp);
                    if (userSectionRole != null) {
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
        if (userSectionRole == null)
            throw new RoleNotAssignedException();
        Role role = userSectionRole.getRole();
        if (role == null)
            throw new RoleNotFoundException();
        return role;
    }
    /**
     * Find user role for article
     * @param userId id of user
     * @param articleId id of article
     * @return article role object
     */
    public Role findUserRole(int userId, int articleId) throws Exception{
        if (userId == 0 || articleId == 0)
            throw new WrongUserDataException();
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
        Role role = null;
        try {
            article = collector.getBaseArticle();
            role = collector.findRoleByRoleId(defaultRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (article == null || role == null)
            throw new AssignDefaultRoleException();
        assignUserRole(user, article, role);
    }
    /**
     * Create default user role for root article
     * @param userId user id
     */
    public void assignDefaultUserRole(int userId) throws Exception{
        if (userId == 0)
            throw new WrongUserDataException();
        Article article = null;
        Role role = null;
        User user = null;
        try {
            article = collector.getBaseArticle();
            role = collector.findRoleByRoleId(defaultRoleId);
            user = collector.findUser(userId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (article == null || role == null || user == null)
            throw new AssignDefaultRoleException();
        assignUserRole(user, article, role);
    }

    //wtf
    private void removeParentRoles(User user, Article section) throws Exception{
        try{
            Article temp = collector.findArticle(section.getSectionId());
            while (true){
                UserSectionRole role = collector.findUserSectionRole(user, temp);
                collector.deleteUserSectionRole(role);
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
    public void assignUserRole(UserSectionRole role) throws Exception{
        //kostili detected
        //if role is banned user, superuser etc - attach it to root
        try {
            if (role.getRole().isBaseRole()) {
                role.setArticle(collector.getBaseArticle());
            }
        }catch (Exception e){
            throw new DataBaseException();
        }
        Article article = role.getArticle();
        if (!article.isSection())
            throw new NotASectionException();
        try{
            Article temp = article;
            //go through all branch to root and delete roles
            while (true) {
                if (temp.isSection()){
                    UserSectionRole tempRole = collector.findUserSectionRole(role.getUser(), temp);
                    if (tempRole != null) {
                        try {
                            deleteUserRoleFromDB(role.getUser().getId(), temp.getId());
                        } catch (Exception e) {
                        }
                        break;
                    }
                }
                if (temp.getParentArticle() == -1)
                    break;
                temp = collector.findArticle(temp.getSectionId());
            }
            User user = role.getUser();
            //delete child roles
            for (Article child : collector.getSectionTree(article.getId())){
                UserSectionRole tempRole = collector.findUserSectionRole(user, child);
                if (tempRole != null)
                    deleteUserRoleFromDB(tempRole);
            }
            collector.addUserSectionRole(role);
            //needed by TZ
            if (role.getRole().getRoleId() == adminRoleId) {
                user.setSuperVisorId(user.getId());
                collector.updateUser(user);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new DataBaseException();
        }
    }
    /**
     * Create user role for specified article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @param role article role object (important: id should be specified)
     */
    public void assignUserRole(User user, Article article, Role role) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        if (role == null)
            throw new RoleNotFoundException();
        assignUserRole(new UserSectionRole(user, article, role));
    }
    /**
     * Create user role for specified section
     * @param userId user id
     * @param articleId article id
     * @param roleId article role id
     */
    public void assignUserRole(int userId, int articleId, int roleId) throws Exception{
        if (userId == 0 || articleId == 0 || roleId == 0)
            throw new WrongUserDataException();
        User user = null;
        Article article = null;
        Role role = null;
        try {
            user = collector.findUser(userId);
            article = collector.findArticle(articleId);
            role = collector.findRole(roleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        assignUserRole(user, article, role);
    }
    /**
     * Delete role from db by object
     * @param role - object
     */
    private void deleteUserRoleFromDB(UserSectionRole role) throws Exception{
        try {
            collector.deleteUserSectionRole(role);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Delete role from db
     * @param userId - id of user
     * @param sectionId - from
     */
    private void deleteUserRoleFromDB(int userId, int sectionId) throws Exception{
        if (sectionId == 0 || userId == 0)
            throw new WrongUserDataException();
        try {
            collector.deleteUserSectionRole(userId, sectionId);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }

    /**
     * Delete user role for specified article
     * @param role role formed object (important: id should be specified)
     */
    private void deleteUserRole(UserSectionRole role) throws Exception{
        int count = collector.getAttachedSectionCount(role.getUser().getId());
        if (count == 1){
            throw new RoleDeleteException();
        }
        deleteUserRoleFromDB(role);
    }
    /**
     * Create user role for specified article
     * @param user user object (important: id should be specified)
     * @param article article object (important: id should be specified)
     * @param role article role object (important: id should be specified)
     */
    public void deleteUserRole(User user, Article article, Role role) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        if (role == null)
            throw new RoleNotFoundException();
        UserSectionRole userSectionRole = null;
        try{
            userSectionRole = collector.findUserSectionRole(user, article);
        }catch (Exception e){
            throw new DataBaseException();
        }
        deleteUserRole(userSectionRole);
    }

    /**
     * Delete user role for specified article
     * @param userId        user id
     * @param articleId     article id
     */
    public void deleteUserRole(int userId, int articleId) throws Exception {
        if (articleId == 0 || userId == 0)
            throw new WrongUserDataException();
        int count = collector.getAttachedSectionCount(userId);
        if (count == 1){
            throw new RoleDeleteException();
        }
        deleteUserRoleFromDB(userId, articleId);
    }

    /**
     * Add base roles do DB
     */
    public void createBaseRoles() throws Exception{
        try {
            for (Role role : RoleImporter.getRoles()) {
                Role exist = collector.findRoleByRoleId(role.getRoleId());
                if (exist == null) {
                    collector.addRole(role);
                } else {
                    role.setId(exist.getId());
                    collector.updateRole(role);
                }
            }
            //ATTENTION! KOSTILI DETECTED
            //Create some super admin to get ability  create new users
            //delete this user after all
            User user = collector.findUser("admin_1337");
            if (user == null) {
                user = new User();
                user.setLogin("admin_1337");
                user.setPassword(DigestUtils.md5Hex("admin_1337_password"));
                user = collector.addUser(user);
            }
            assignUserRole(user.getId(), collector.getBaseArticle().getId(), superUserId);

        }catch (Exception e){
            e.printStackTrace();
        }

        /*
        Role user = new Role();
        user.setName("Пользователь");
        user.setCanViewArticle(true);
        user.setCanAddMistakes(true);
        user.setCanSearch(true);
        user.setCanAddArticle(true);
        create(user);

        Role superUser = new Role();
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

        Role admin = new Role();
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

        Role superVisor = new Role();
        superVisor.setName("Супервизор");
        superVisor.setCanViewArticle(true);
        superVisor.setCanAddMistakes(true);
        superVisor.setCanSearch(true);
        superVisor.setCanAddArticle(true);
        superVisor.setCanGetEmployeesActionsReports(true);
        create(superVisor);

        Role banned = new Role();
        banned.setName("Пользователь без прав");
        create(banned);
        */
    }

    public int getDefaultRoleId() {
        return defaultRoleId;
    }

    public void setDefaultRoleId(int defaultRoleId) {
        this.defaultRoleId = defaultRoleId;
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

    public boolean canAddUser(int userId) throws Exception{
        try {
            return findUserRole(userId, collector.getBaseArticle().getId()).isCanAddUser();
        } catch (RoleNotAssignedException e){
            return false;
        }
    }

    public boolean canEditUser(int userId) throws Exception{
        return findUserRole(userId, collector.getBaseArticle().getId()).isCanEditUser();
    }

    public boolean canDeleteUser(int userId) throws Exception{
        try {
            return findUserRole(userId, collector.getBaseArticle().getId()).isCanDeleteUser();
        } catch (RoleNotAssignedException e){
            return false;
        }
    }

    public boolean canViewUser(int userId) throws Exception{
        try {
            return findUserRole(userId, collector.getBaseArticle().getId()).isCanViewUser();
        } catch (RoleNotAssignedException e){
            return false;
        }
    }

    public boolean canEditUserRole(int userId) throws Exception {
        try {
            return findUserRole(userId, collector.getBaseArticle().getId()).isCanEditUserRole();
        } catch (RoleNotAssignedException e){
            return false;
        }
    }
    public boolean isBaseRole(int roleId) throws Exception{
        return collector.findRole(roleId).isBaseRole();
    }
}
