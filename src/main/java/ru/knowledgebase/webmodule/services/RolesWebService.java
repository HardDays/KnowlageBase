package ru.knowledgebase.webmodule.services;

import ru.knowledgebase.wrappermodule.RoleWrapper;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by vova on 02.10.16.
 */

@Path("/roles")
public class RolesWebService {

    private RoleWrapper roleWrapper = new RoleWrapper();

    @POST
    @Path("/get_section_roles")
    public Response getSectionRoles(@FormParam(value = "admin_id") int adminId,
                        @FormParam(value = "admin_token") String adminToken) {
        return roleWrapper.getSectionRoles(adminId, adminToken);
    }
}
