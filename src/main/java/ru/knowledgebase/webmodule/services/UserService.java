package ru.knowledgebase.webmodule.services;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import ru.knowledgebase.wrappermodule.UserWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * Created by vova on 30.09.16.
 */

@Path("/users")
public class UserService {
    private UserWrapper userWrapper = new UserWrapper();

    @POST
    @Path("/authorize")
    public Response authorize(@FormParam(value = "login") String login,
                              @FormParam(value = "password") String password) {
        return userWrapper.authorize(login, password);
    }

    @POST
    @Path("/register")
    public Response register(@FormParam(value = "admin_id") int adminId,
                             @FormParam(value = "admin_token") String adminToken,
                             @FormParam(value = "login") String login,
                             @FormParam(value = "password")String password){
        return userWrapper.register(adminId, adminToken, login, password);
    }

    @POST
    @Path("/delete")
    public Response delete(@FormParam(value = "admin_id") int adminId,
                             @FormParam(value = "admin_token") String adminToken,
                             @FormParam(value = "user_id") int userId){
       return userWrapper.delete(adminId, adminToken, userId);
    }

    @POST
    @Path("/assign_section_role")
    public Response assignSecitonRole(@FormParam(value = "admin_id") int adminId,
                           @FormParam(value = "admin_token") String adminToken,
                           @FormParam(value = "user_id") int userId,
                           @FormParam(value = "section_id") int sectionId,
                          @FormParam(value = "role_id") int roleId){
        return userWrapper.assignSectionRole(adminId, adminToken, userId, sectionId, roleId);
    }

    @POST
    @Path("/assign_global_role")
    public Response assignGlobalRole(@FormParam(value = "admin_id") int adminId,
                                      @FormParam(value = "admin_token") String adminToken,
                                      @FormParam(value = "user_id") int userId,
                                      @FormParam(value = "role_id") int roleId){
        return userWrapper.assignGlobalRole(adminId, adminToken, userId, roleId);
    }

    @POST
    @Path("/get_section_permissions")
    public Response getSectionPermissions(@FormParam(value = "admin_id") int adminId,
                                     @FormParam(value = "admin_token") String adminToken,
                                     @FormParam(value = "user_id") int userId,
                                     @FormParam(value = "section_id") int sectionId){
        return userWrapper.getSectionPermissions(adminId, adminToken, userId, sectionId);
    }

    @POST
    @Path("/get_my_section_permissions")
    public Response getSectionPermissions(@FormParam(value = "user_id") int userId,
                                          @FormParam(value = "user_token") String userToken,
                                          @FormParam(value = "section_id") int sectionId){
        return userWrapper.getSectionPermissions(userId, userToken, sectionId);
    }

    @POST
    @Path("/get_global_permissions")
    public Response getGlobalPermissions(@FormParam(value = "admin_id") int adminId,
                                          @FormParam(value = "admin_token") String adminToken,
                                          @FormParam(value = "user_id") int userId){
        return userWrapper.getGlobalPermissions(adminId, adminToken, userId);
    }

    @POST
    @Path("/get_my_global_permissions")
    public Response getGlobalPermissions(@FormParam(value = "user_id") int userId,
                                         @FormParam(value = "user_token") String userToken){
        return userWrapper.getGlobalPermissions(userId, userToken);
    }

    @POST
    @Path("/find")
    public Response find(@FormParam(value = "admin_id") int adminId,
                         @FormParam(value = "admin_token") String adminToken,
                         @FormParam(value = "user_login") String userLogin){
        return userWrapper.findUser(adminId, adminToken, userLogin);
    }

    @POST
    @Path("/get_list")
    public Response getList(@FormParam(value = "admin_id") int adminId,
                         @FormParam(value = "admin_token") String adminToken){
        return userWrapper.getUserList(adminId, adminToken);
    }

    @POST
    @Path("/get_section_users")
    public Response getSecionUsers(@FormParam(value = "admin_id") int adminId,
                            @FormParam(value = "admin_token") String adminToken,
                            @FormParam(value = "section_id") int sectionId){
        return userWrapper.getSectionUsers(adminId, adminToken, sectionId);
    }

    //TODO: get_user_sections
}

