package ru.knowledgebase.webmodule.services;

import org.json.JSONObject;
import ru.knowledgebase.wrappermodule.RoleWrapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by vova on 02.10.16.
 */

@Path("/roles")
public class RolesWebService {

    private RoleWrapper roleWrapper = new RoleWrapper();

    @POST
    @Path("/get_section_roles")
        @Consumes(MediaType.APPLICATION_JSON)
    public Response getSectionRoles(String param) {
        try {
            JSONObject obj = new JSONObject(param);
            return roleWrapper.getSectionRoles(obj.getInt("admin_id"),
                    obj.getString("admin_token"));
        }catch (org.json.JSONException e){
            return Response.status(400).entity("Wrong parameters!").build();
        }
    }
}
