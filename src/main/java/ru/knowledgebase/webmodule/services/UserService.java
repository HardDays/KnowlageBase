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

    @GET
    @Path("/authorize")
    public Response methodImCalling(@QueryParam(value = "login") String login,
                                    @QueryParam(value = "password") String password) {

        return userWrapper.authorize(login, password);
       // return Response.ok().entity(login + password).build();
    }
}
