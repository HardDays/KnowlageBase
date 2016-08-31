package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.responsemodule.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.usermodule.UserController;

/**
 * Created by vova on 29.08.16.
 */
public class UserWrapper {

    private UserController userController = UserController.getInstance();
    private ArticleRoleController articleRoleController = ArticleRoleController.getInstance();
    private GlobalRoleController globalRoleController = GlobalRoleController.getInstance();

    public Response authorize(String login, String password) {
        try {
            String token = userController.authorize(login, password);
            return ResponseBuilder.buildAuthorizedResponse(token);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response register(int adminId, String adminToken, String login, String password) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canAddUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            userController.register(login, password);
            return ResponseBuilder.buildRegisteredResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response changePassword(int adminId, String adminToken, int userId, String newPassword) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canEditUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            userController.changePassword(userId, newPassword);
            return ResponseBuilder.buildUserChangedResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response changeLogin(int adminId, String adminToken, int userId, String newLogin) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canEditUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            userController.changeLogin(userId, newLogin);
            return ResponseBuilder.buildUserChangedResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response delete(int adminId, String adminToken, int userId) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canDeleteUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            userController.delete(userId);
            return ResponseBuilder.buildUserDeletedResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response assignArticleUserRole(int adminId, String adminToken, int userId, int articleId, int roleId) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canEditUserRoles(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            articleRoleController.assignUserRole(userId, articleId, roleId);
            return ResponseBuilder.buildUserRoleChangedResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response assignGlobalUserRole(int adminId, String adminToken, int userId, int roleId) {
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canEditUserRoles(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            globalRoleController.assignUserRole(userId, roleId);
            return ResponseBuilder.buildUserRoleChangedResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

}
