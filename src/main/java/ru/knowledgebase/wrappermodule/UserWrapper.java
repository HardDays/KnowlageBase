package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.usermodule.UserController;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by vova on 29.08.16.
 */
public class UserWrapper {

    private UserController userController = UserController.getInstance();
    private ArticleRoleController articleRoleController = ArticleRoleController.getInstance();
    private GlobalRoleController globalRoleController = GlobalRoleController.getInstance();

    /**
     * Authorize user
     * @param login user login
     * @param password user password
     * @return Response object
     */
    public Response authorize(String login, String password) {
        try {
            Token token = userController.authorize(login, password);
            return ResponseBuilder.buildAuthorizedResponse(token);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }
    /**
     * Create new user
     * @param adminId id of admin who's registering user
     * @param adminToken token of admin
     * @param login user login
     * @param password user password
     * @return Response object
     */
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
    /**
     * Update user
     * @param userId id of user
     * @param token token of user
     * @param newLogin new login
     * @param newPassword new password
     * @return Response object
     */
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
    /**
     * Update user
     * @param adminId id of admin who's updating user
     * @param adminToken token of admin
     * @param userId id of user
     * @param newLogin new user login
     * @param newPassword new user password
     * @return Response object
     */
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

    /**
     * Delete user
     * @param adminId id of admin who's deleting user
     * @param adminToken token of admin
     * @param userId id of user
     * @return Response object
     */
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
            e.printStackTrace();
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
    /**
     * Select global role of user
     * @param adminId id of admin who's selecting user role
     * @param adminToken token of admin
     * @param userId id of user
     * @param roleId id of role
     * @return Response object
     */
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
    /**
     * Get global permissions of user
     * @param userId id of user
     * @param token user token
     * @return Response object
     */
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
    /**
     * Get global permissions of user by admin
     * @param adminId id of admin who want to view user permissions
     * @param adminToken token of admin
     * @param userId id of user
     * @return Response object
     */
    public Response getGlobalPermissions(int adminId, String adminToken, int userId){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
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
    /**
     * Get user section permissions
     * @param userId id of user
     * @param token token of user
     * @param sectionId id of section
     * @return Response object
     */
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
    /**
     * Get user section permissions
     * @param adminId id of admin who want to look at user permissions
     * @param adminToken token of admin
     * @param userId id of user
     * @param sectionId id of section
     * @return Response object
     */
    public Response getSectionPermissions(int adminId, String adminToken, int userId, int sectionId){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
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
    /**
     * Get user info
     * @param adminId id of admin who want to look at user info
     * @param adminToken token of admin
     * @param userId id of user
     * @return Response object
     */
    public Response getUserInfo(int adminId, String adminToken, int userId){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
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
    /**
     * Get user info
     * @param userId id of user
     * @param token token of user
     * @return Response object
     */
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
    /**
     * Get list of all users
     * @param adminId id of admin
     * @param adminToken token of admin
     * @return Response object
     */
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
    /**
     * Get list of all section users
     * @param adminId id of admin
     * @param adminToken token of admin
     * @param sectionId id of section
     * @return Response object
     */
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