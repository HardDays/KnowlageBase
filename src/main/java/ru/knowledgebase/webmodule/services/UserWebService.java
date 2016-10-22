package ru.knowledgebase.webmodule.services;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.knowledgebase.wrappermodule.UserWrapper;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by vova on 30.09.16.
 */

@Path("/users")
public class UserWebService {
    private UserWrapper userWrapper = new UserWrapper();

    @POST
    @Path("/authorize")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authorize(String param) {
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.authorize(obj.getString("login"),
                                         obj.getString("password"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(String param){
        try {
            JSONObject obj = new JSONObject(param);
            List <Integer> sections = new LinkedList<>();
            List <Integer> roles = new LinkedList<>();
            JSONArray secArr = obj.getJSONArray("sections");
            for (int i = 0; i < secArr.length(); i++){
                sections.add(((JSONObject)secArr.get(i)).getInt("section_id"));
                sections.add(((JSONObject)secArr.get(i)).getInt("role_id"));
            }
            return userWrapper.register(obj.getInt("admin_id"),
                                        obj.getString("admin_token"),
                                        obj.getString("login"),
                                        obj.getString("password"),
                                        obj.getString("email"),
                                        obj.getString("first_name"),
                                        obj.getString("middle_name"),
                                        obj.getString("last_name"),
                                        obj.getString("office"),
                                        obj.getString("phone1"),
                                        obj.getString("phone2"),
                                        new Timestamp(obj.getLong("recruitment_date")),
                                        new Timestamp(obj.getLong("dismissal_date")),
                                        obj.getBoolean("has_email_notifications"),
                                        obj.getBoolean("has_site_notifications"),
                                        obj.getInt("super_visor_id"),
                                        sections,
                                        roles);
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/update_me")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMe(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.update(obj.getInt("user_id"),
                    obj.getString("user_token"),
                    obj.getString("login"),
                    obj.getString("password"),
                    obj.getString("email"),
                    obj.getString("first_name"),
                    obj.getString("middle_name"),
                    obj.getString("last_name"),
                    obj.getString("office"),
                    obj.getString("phone1"),
                    obj.getString("phone2"),
                    new Timestamp(obj.getLong("recruitment_date")),
                    new Timestamp(obj.getLong("dismissal_date")),
                    obj.getBoolean("has_email_notifications"),
                    obj.getBoolean("has_site_notifications"),
                    obj.getInt("super_visor_id"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(String param){
        try {
            JSONObject obj = new JSONObject(param);
            List <Integer> sections = new LinkedList<>();
            List <Integer> roles = new LinkedList<>();
            JSONArray secArr = obj.getJSONArray("sections");
            for (int i = 0; i < secArr.length(); i++){
                sections.add(((JSONObject)secArr.get(i)).getInt("section_id"));
                sections.add(((JSONObject)secArr.get(i)).getInt("role_id"));
            }
            return userWrapper.update(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("user_id"),
                    obj.getString("login"),
                    obj.getString("password"),
                    obj.getString("email"),
                    obj.getString("first_name"),
                    obj.getString("middle_name"),
                    obj.getString("last_name"),
                    obj.getString("office"),
                    obj.getString("phone1"),
                    obj.getString("phone2"),
                    new Timestamp(obj.getLong("recruitment_date")),
                    new Timestamp(obj.getLong("dismissal_date")),
                    obj.getBoolean("has_email_notifications"),
                    obj.getBoolean("has_site_notifications"),
                    obj.getInt("super_visor_id"),
                    sections,
                    roles);
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.delete(obj.getInt("admin_id"),
                                      obj.getString("admin_token"),
                                      obj.getInt("user_id"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/assign_user_role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response assignSecitonRole(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.assignSectionRole(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("user_id"),
                    obj.getInt("section_id"),
                    obj.getInt("role_id"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/delete_user_role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteSectionRole(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.deleteSectionRole(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("user_id"),
                    obj.getInt("section_id"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/get_section_permissions")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSectionPermissions(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getSectionPermissions(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("user_id"),
                    obj.getInt("section_id"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/get_my_section_permissions")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMySectionPermissions(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getSectionPermissions(obj.getInt("user_id"),
                    obj.getString("user_token"),
                    obj.getInt("section_id"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }    }

    @POST
    @Path("/get_my_supervisor")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSuperVisor(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getSuperVisor(obj.getInt("user_id"),
                    obj.getString("user_token"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/find")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response find(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.findUser(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getString("user_login"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/get_all_users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getList(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getUserList(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("offset"),
                    obj.getInt("limit"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/get_section_users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSecionUsers(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getSectionUsers(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("section_id"),
                    obj.getInt("offset"),
                    obj.getInt("limit"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/get_user_sections")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserSections(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getUserSections(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("user_id"),
                    obj.getInt("offset"),
                    obj.getInt("limit"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }

    }

    @POST
    @Path("/get_my_sections")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMySections(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getUserSections(obj.getInt("user_id"),
                    obj.getString("user_token"),
                    obj.getInt("offset"),
                    obj.getInt("limit"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/get_my_info")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMyInfo(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getUserInfo(obj.getInt("user_id"),
                    obj.getString("user_token"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/get_user_info")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserInfo(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.getUserInfo(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("user_Id"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }

    @POST
    @Path("/find_users_by_supervisor")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findBySuperVisor(String param){
        try {
            JSONObject obj = new JSONObject(param);
            return userWrapper.findUsersBySupervisor(obj.getInt("admin_id"),
                    obj.getString("admin_token"),
                    obj.getInt("super_visor_id"),
                    obj.getInt("offset"),
                    obj.getInt("limit"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }
}

