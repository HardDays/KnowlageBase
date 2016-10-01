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
       // return Response.ok().build();
    }
}

