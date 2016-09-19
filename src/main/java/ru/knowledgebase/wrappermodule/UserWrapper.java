package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.responsemodule.Response;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.usermodule.UserController;

import java.util.List;

/**
 * Created by vova on 29.08.16.
 */
public class UserWrapper {

    private UserController userController = UserController.getInstance();
    private ArticleRoleController articleRoleController = ArticleRoleController.getInstance();
    private GlobalRoleController globalRoleController = GlobalRoleController.getInstance();

    public Response authorize(String login, String password) {
        try {
            Token token = userController.authorize(login, password);
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
            User user = userController.register(login, password);
            globalRoleController.assignDefaultUserRole(user);
            articleRoleController.assignDefaultUserRole(user);
            return ResponseBuilder.buildRegisteredResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response update(int userId, String token, String newLogin, String newPassword) {
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            userController.update(userId, newLogin, newPassword);
            return ResponseBuilder.buildUserChangedResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response update(int adminId, String adminToken, int userId, String newLogin, String newPassword) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canEditUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            userController.update(userId, newLogin, newPassword);
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

    public Response assignSectionRole(int adminId, String adminToken, int userId, int articleId, int roleId) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canEditUserRole(adminId);
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

    public Response assignGlobalRole(int adminId, String adminToken, int userId, int roleId) {
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canEditUserRole(adminId);
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

    public Response getGlobalPermissions(int userId, String token){
        try{
            boolean okToken = userController.checkUserToken(userId, token);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            GlobalRole role = globalRoleController.findUserRole(userId);
            if (role == null)
                return ResponseBuilder.buildRoleNotAssigned();
            return ResponseBuilder.buildGlobalPermissionsResponse(role);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response getGlobalPermissions(int adminId, String token, int userId){
        try{
            boolean okToken = userController.checkUserToken(adminId, token);
            boolean hasRights = globalRoleController.canEditUserRole(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            GlobalRole role = globalRoleController.findUserRole(userId);
            if (role == null)
                return ResponseBuilder.buildRoleNotAssigned();
            return ResponseBuilder.buildGlobalPermissionsResponse(role);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response getSectionPermissions(int userId, String token, int sectionId){
        try{
            boolean okToken = userController.checkUserToken(userId, token);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            ArticleRole role = articleRoleController.findUserRole(userId, sectionId);
            if (role == null)
                return ResponseBuilder.buildRoleNotAssigned();
            return ResponseBuilder.buildSectionPermissionsResponse(role);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response getSectionPermissions(int adminId, String token, int userId, int sectionId){
        try{
            boolean okToken = userController.checkUserToken(adminId, token);
            boolean hasRights = globalRoleController.canEditUserRole(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            ArticleRole role = articleRoleController.findUserRole(userId, sectionId);
            if (role == null)
                return ResponseBuilder.buildRoleNotAssigned();
            return ResponseBuilder.buildSectionPermissionsResponse(role);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response getUserInfo(int adminId, String token, int userId){
        try{
            boolean okToken = userController.checkUserToken(adminId, token);
            boolean hasRights = globalRoleController.canViewUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            User user = userController.find(userId);
            return ResponseBuilder.buildUserInfoResponse(user);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response getUserInfo(int userId, String token){
        try{
            boolean okToken = userController.checkUserToken(userId, token);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            User user = userController.find(userId);
            return ResponseBuilder.buildUserInfoResponse(user);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response findUser(int adminId, String adminToken, String userLogin){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canViewUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            User user = userController.find(userLogin);
            return ResponseBuilder.buildUserInfoResponse(user);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response getUserList(int adminId, String adminToken){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            List<User> users = userController.getAll();
            return ResponseBuilder.buildUserListResponse(users);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }

    public Response getSectionUsers(int adminId, String adminToken, int sectionId){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            List<UserArticleRole> users = userController.getSectionUsers(sectionId);
            return ResponseBuilder.buildSectionUserListResponse(users);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }
}
