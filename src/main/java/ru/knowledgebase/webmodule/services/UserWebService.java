package ru.knowledgebase.webmodule.services;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import ru.knowledgebase.wrappermodule.UserWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;


/**
 * Created by vova on 30.09.16.
 */

@Path("/users")
public class UserWebService {
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
                             @FormParam(value = "password")String password,
                            @FormParam(value = "email")String email,
                            @FormParam(value = "first_name")String firstName,
                            @FormParam(value = "middle_name")String middleName,
                            @FormParam(value = "last_name")String lastName,
                            @FormParam(value = "office")String office,
                            @FormParam(value = "phone1")String phone1,
                            @FormParam(value = "phone2")String phone2,
                            @FormParam(value = "recruitment_date")Timestamp recruitmentDate,
                            @FormParam(value = "dismissal_date")Timestamp dismissalDate,
                            @FormParam(value = "has_email_notifications")boolean hasEmailNotifications,
                            @FormParam(value = "has_site_notifications")boolean hasSiteNotifications,
                            @FormParam(value = "super_visor_id")Integer superVisorId){

        return userWrapper.register(adminId, adminToken, login, password, email,
                firstName, middleName, lastName,
                office, phone1, phone2,
                recruitmentDate, dismissalDate,
                hasEmailNotifications, hasSiteNotifications, superVisorId);
    }

    @POST
    @Path("/update_me")
    public Response updateMe(@FormParam(value = "user_id") int userId,
                             @FormParam(value = "user_token") String userToken,
                             @FormParam(value = "login") String login,
                             @FormParam(value = "password")String password,
                             @FormParam(value = "email")String email,
                             @FormParam(value = "first_name")String firstName,
                             @FormParam(value = "middle_name")String middleName,
                             @FormParam(value = "last_name")String lastName,
                             @FormParam(value = "office")String office,
                             @FormParam(value = "phone1")String phone1,
                             @FormParam(value = "phone2")String phone2,
                             @FormParam(value = "recruitment_date")Timestamp recruitmentDate,
                             @FormParam(value = "dismissal_date")Timestamp dismissalDate,
                             @FormParam(value = "has_email_notifications")boolean hasEmailNotifications,
                             @FormParam(value = "has_site_notifications")boolean hasSiteNotifications,
                             @FormParam(value = "super_visor_id")Integer superVisorId){
        return userWrapper.update(userId, userToken, login, password, email,
                firstName, middleName, lastName,
                office, phone1, phone2,
                recruitmentDate, dismissalDate,
                hasEmailNotifications, hasSiteNotifications, superVisorId);
    }

    @POST
    @Path("/update")
    public Response update(@FormParam(value = "admin_id") int adminId,
                           @FormParam(value = "admin_token") String adminToken,
                           @FormParam(value = "user_id") int userId,
                           @FormParam(value = "user_token") String userToken,
                           @FormParam(value = "login") String login,
                           @FormParam(value = "password")String password,
                           @FormParam(value = "email")String email,
                           @FormParam(value = "first_name")String firstName,
                           @FormParam(value = "middle_name")String middleName,
                           @FormParam(value = "last_name")String lastName,
                           @FormParam(value = "office")String office,
                           @FormParam(value = "phone1")String phone1,
                           @FormParam(value = "phone2")String phone2,
                           @FormParam(value = "recruitment_date")Timestamp recruitmentDate,
                           @FormParam(value = "dismissal_date")Timestamp dismissalDate,
                           @FormParam(value = "has_email_notifications")boolean hasEmailNotifications,
                           @FormParam(value = "has_site_notifications")boolean hasSiteNotifications,
                           @FormParam(value = "super_visor_id")Integer superVisorId){
        return userWrapper.update(adminId, adminToken, userId, userToken, login, password, email,
                firstName, middleName, lastName, office, phone1, phone2, recruitmentDate, dismissalDate,
                hasEmailNotifications, hasSiteNotifications, superVisorId);
    }

    @POST
    @Path("/delete")
    public Response delete(@FormParam(value = "admin_id") int adminId,
                             @FormParam(value = "admin_token") String adminToken,
                             @FormParam(value = "user_id") int userId){
       return userWrapper.delete(adminId, adminToken, userId);
    }

    @POST
    @Path("/assign_user_role")
    public Response assignSecitonRole(@FormParam(value = "admin_id") int adminId,
                                      @FormParam(value = "admin_token") String adminToken,
                                      @FormParam(value = "user_id") int userId,
                                      @FormParam(value = "section_id") int sectionId,
                                      @FormParam(value = "role_id") int roleId){
        return userWrapper.assignSectionRole(adminId, adminToken, userId, sectionId, roleId);
    }

    @POST
    @Path("/delete_user_role")
    public Response deleteSectionRole(@FormParam(value = "admin_id") int adminId,
                                      @FormParam(value = "admin_token") String adminToken,
                                      @FormParam(value = "user_id") int userId,
                                      @FormParam(value = "section_id") int sectionId){
        return userWrapper.deleteSectionRole(adminId, adminToken, userId, sectionId);
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
    @Path("/find")
    public Response find(@FormParam(value = "admin_id") int adminId,
                         @FormParam(value = "admin_token") String adminToken,
                         @FormParam(value = "user_login") String userLogin){
        return userWrapper.findUser(adminId, adminToken, userLogin);
    }

    @POST
    @Path("/get_all_users")
    public Response getList(@FormParam(value = "admin_id") int adminId,
                            @FormParam(value = "admin_token") String adminToken,
                            @FormParam(value = "offset") int offset,
                            @FormParam(value = "limit") int limit){
        return userWrapper.getUserList(adminId, adminToken, offset, limit);
    }

    @POST
    @Path("/get_section_users")
    public Response getSecionUsers(@FormParam(value = "admin_id") int adminId,
                                   @FormParam(value = "admin_token") String adminToken,
                                   @FormParam(value = "section_id") int sectionId,
                                   @FormParam(value = "offset") int offset,
                                   @FormParam(value = "limit") int limit){
        return userWrapper.getSectionUsers(adminId, adminToken, sectionId, offset, limit);
    }

    @POST
        @Path("/get_user_sections")
    public Response getUserSecions(@FormParam(value = "admin_id") int adminId,
                                   @FormParam(value = "admin_token") String adminToken,
                                   @FormParam(value = "user_id") int userId,
                                   @FormParam(value = "offset") int offset,
                                   @FormParam(value = "limit") int limit){
        return userWrapper.getUserSections(adminId, adminToken, userId, offset, limit);
    }

    @POST
    @Path("/get_my_sections")
    public Response getUserSecions(@FormParam(value = "user_id") int userId,
                                   @FormParam(value = "user_token") String userToken,
                                   @FormParam(value = "offset") int offset,
                                   @FormParam(value = "limit") int limit){
        return userWrapper.getUserSections(userId, userToken, offset, limit);
    }

    @POST
    @Path("/get_my_info")
    public Response getMyInfo(@FormParam(value = "user_id") int userId,
                                   @FormParam(value = "user_token") String userToken){
        return userWrapper.getUserInfo(userId, userToken);
    }

    @POST
    @Path("/get_user_info")
    public Response getMyInfo(@FormParam(value = "admin_id") int adminId,
                              @FormParam(value = "admin_token") String adminToken,
                              @FormParam(value = "user_id") int userId){
        return userWrapper.getUserInfo(adminId, adminToken, userId);
    }

    @POST
    @Path("/find_users_by_supervisor")
    public Response findBySuperVisor(@FormParam(value = "admin_id") int adminId,
                                     @FormParam(value = "admin_token") String adminToken,
                                     @FormParam(value = "super_visor_id") int superVisorId,
                                     @FormParam(value = "offset") int offset,
                                     @FormParam(value = "limit") int limit){
        return userWrapper.findUsersBySupervisor(adminId, adminToken, superVisorId, offset, limit);
    }
    @POST
    @Path("/test")
    public Response test(@FormParam(value = "a")List<Integer> a){
       System.out.println("sdffssss" + a.get(0));
       return  Response.ok(a.get(0)).build();
    }
}

