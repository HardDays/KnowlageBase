package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.rolemodels.UserSectionRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.responsemodule.ResponseBuilder;

import ru.knowledgebase.rolemodule.RoleController;
import ru.knowledgebase.usermodule.UserController;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

/**
 * Created by vova on 29.08.16.
 */
public class UserWrapper {

    private UserController userController = UserController.getInstance();
    private RoleController roleController = RoleController.getInstance();

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
    public Response register(int adminId, String adminToken, String login, String password, String email,
                             String firstName, String middleName, String lastName,
                             String office, String phone1, String phone2,
                             Timestamp recruitmentDate, Timestamp dismissalDate,
                             boolean hasEmailNotifications, boolean hasSiteNotifications, Integer superVisorId) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canAddUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            User user = userController.register(login, password, email,
                                                firstName, middleName, lastName,
                                                office, phone1, phone2,
                                                recruitmentDate, dismissalDate,
                                                hasEmailNotifications, hasSiteNotifications, superVisorId);
            roleController.assignDefaultUserRole(user);
            return ResponseBuilder.buildRegisteredResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }
    /**
     * Update user
     * @param userId id of user
     * @param token token of user
     * @return Response object
     */
    public Response update(int userId, String token, String login, String password, String email,
                           String firstName, String middleName, String lastName,
                           String office, String phone1, String phone2,
                           Timestamp recruitmentDate, Timestamp dismissalDate,
                           boolean hasEmailNotifications, boolean hasSiteNotifications, Integer superVisorId) {
        try {
            boolean okToken = userController.checkUserToken(userId, token);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            userController.update(userId, login, password, email,
                                  firstName, middleName, lastName,
                                  office, phone1, phone2,
                                  recruitmentDate, dismissalDate,
                                  hasEmailNotifications, hasSiteNotifications, superVisorId);
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
     * @return Response object
     */
    public Response update(int adminId, String adminToken, int userId, String token, String login, String password, String email,
                           String firstName, String middleName, String lastName,
                           String office, String phone1, String phone2,
                           Timestamp recruitmentDate, Timestamp dismissalDate,
                           boolean hasEmailNotifications, boolean hasSiteNotifications, Integer superVisorId) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canEditUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            userController.update(userId, login, password, email,
                    firstName, middleName, lastName,
                    office, phone1, phone2,
                    recruitmentDate, dismissalDate,
                    hasEmailNotifications, hasSiteNotifications, superVisorId);
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
            boolean hasRights = roleController.canDeleteUser(adminId);
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
    /**
     * Delete user from section
     * @param adminId id of admin who's assigning role
     * @param adminToken token of admin
     * @param userId id of user
     * @param sectionId id of section
     * @return Response object
     */
    public Response deleteSectionRole(int adminId, String adminToken, int userId, int sectionId) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canEditUserRole(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            roleController.deleteUserRole(userId, sectionId);
            return ResponseBuilder.buildUserRoleChangedResponse();
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }
    /**
     * Assign user to section and specify role
     * @param adminId id of admin who's assigning role
     * @param adminToken token of admin
     * @param userId id of user
     * @param sectionId id of section
     * @param roleId id of role
     * @return Response object
     */
    public Response assignSectionRole(int adminId, String adminToken, int userId, int sectionId, int roleId) {
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canEditUserRole(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            //attach super user to root
            if (roleController.isBaseRole(roleId)) {
                roleController.assignBaseUserRole(userId, roleId);
            }else{
                roleController.assignUserRole(userId, sectionId, roleId);
            }
            return ResponseBuilder.buildUserRoleChangedResponse();
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
            Role role = roleController.findUserRole(userId, sectionId);
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
            boolean hasRights = roleController.canEditUserRole(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            Role role = roleController.findUserRole(userId, sectionId);
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
            boolean hasRights = roleController.canViewUser(adminId);
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
    /**
     * Find user by login
     * @param adminId id of admin who want to look at user info
     * @param adminToken token of admin
     * @param userLogin user login
     * @return Response object
     */
    public Response findUser(int adminId, String adminToken, String userLogin){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canViewUser(adminId);
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
     * Find users by supervisor
     * @param adminId id of admin who want to look at user info
     * @param adminToken token of admin
     * @param superVisorId user supervisor
     * @param offset from
     * @param limit number
     * @return Response object
     */
    public Response findUsersBySupervisor(int adminId, String adminToken, int superVisorId,
                                          int offset, int limit){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canViewUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            List <User> users = userController.findBySuperVisor(superVisorId, offset, limit);
            return ResponseBuilder.buildUserListResponse(users);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }
    /**
     * Get list of all users
     * @param adminId id of admin
     * @param adminToken token of admin
     * @param offset from
     * @param limit number
     * @return Response object
     */
    public Response getUserList(int adminId, String adminToken,
                                int offset, int limit){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canViewUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            List<User> users = userController.getAll(offset, limit);
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
     * @param offset from
     * @param limit number
     * @return Response object
     */
    public Response getSectionUsers(int adminId, String adminToken, int sectionId,
                                    int offset, int limit){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canViewUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            List<UserSectionRole> users = userController.getSectionUsers(sectionId, offset, limit);
            return ResponseBuilder.buildSectionUserListResponse(users);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }
    /**
     * Get list of user sections
     * @param adminId id of admin
     * @param adminToken token of admin
     * @param userId id of user
     * @param offset from
     * @param limit number
     * @return Response object
     */
    public Response getUserSections(int adminId, String adminToken, int userId,
                                    int offset, int limit){
        try{
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canViewUser(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            List<Article> sections = userController.getUserSectionsObj(userId, offset, limit);
            return ResponseBuilder.buildUserSectionsResponse(sections);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }
    /**
     * Get list of user sections
     * @param userId id of user
     * @param offset from
     * @param limit number
     * @return Response object
     */
    public Response getUserSections(int userId, String userToken, int offset, int limit){
        try{
            boolean okToken = userController.checkUserToken(userId, userToken);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            List<Article> sections = userController.getUserSectionsObj(userId, offset, limit);
            return ResponseBuilder.buildUserSectionsResponse(sections);
        }catch (Exception e){
            return ResponseBuilder.buildResponse(e);
        }
    }
}
