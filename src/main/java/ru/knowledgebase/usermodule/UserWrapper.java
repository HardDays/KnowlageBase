package ru.knowledgebase.usermodule;

import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;

/**
 * Created by vova on 29.08.16.
 */
public class UserWrapper {

    private UserController userController = UserController.getInstance();
    private ArticleRoleController articleRoleController = ArticleRoleController.getInstance();
    private GlobalRoleController globalRoleController = GlobalRoleController.getInstance();

    public String authorize(String login, String password) throws Exception {
        return userController.authorize(login, password).getToken();
    }

    public void register(int adminId, String adminToken, String login, String password) throws Exception {
        boolean okToken = userController.checkUserToken(adminId, adminToken);
        boolean hasRights = globalRoleController.findGlobalRole(adminId).isCanAddUser();
        if (okToken && hasRights) {
            int userId = userController.register(login, password);
            globalRoleController.assignDefaultUserRole(userId);
            articleRoleController.assignDefaultUserRole(userId);
        }
    }

    public void changePassword(int adminId, String adminToken, int userId, String newPassword) throws Exception {
        boolean okToken = userController.checkUserToken(adminId, adminToken);
        boolean hasRights = globalRoleController.findGlobalRole(adminId).isCanEditUser();
        if (okToken && hasRights)
            userController.changePassword(userId, newPassword);
    }

    public void changeLogin(int adminId, String adminToken, int userId, String newLogin) throws Exception {
        boolean okToken = userController.checkUserToken(adminId, adminToken);
        boolean hasRights = globalRoleController.findGlobalRole(adminId).isCanEditUser();
        if (okToken && hasRights)
            userController.changeLogin(userId, newLogin);
    }

    public void delete(int adminId, String adminToken, int userId) throws Exception {
        boolean okToken = userController.checkUserToken(adminId, adminToken);
        boolean hasRights = globalRoleController.findGlobalRole(adminId).isCanEditUser();
        if (okToken && hasRights)
            userController.delete(userId);
    }

    public void assignArticleUserRole(int adminId, String adminToken, int userId, int articleId, int roleId) throws Exception{
        boolean okToken = userController.checkUserToken(adminId, adminToken);
        boolean hasRights = globalRoleController.findGlobalRole(adminId).isCanEditUserRoles();
        if (okToken && hasRights)
            articleRoleController.assignUserRole(userId, articleId, roleId);
    }

    public void assignGlobalUserRole(int adminId, String adminToken, int userId, int roleId) throws Exception{
        boolean okToken = userController.checkUserToken(adminId, adminToken);
        boolean hasRights = globalRoleController.findGlobalRole(adminId).isCanEditUserRoles();
        if (okToken && hasRights)
            globalRoleController.assignUserRole(userId, roleId);
    }

}
