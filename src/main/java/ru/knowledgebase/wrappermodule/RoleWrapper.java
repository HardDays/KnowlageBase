package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.RoleController;
import ru.knowledgebase.usermodule.UserController;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by vova on 19.09.16.
 */
public class RoleWrapper {


    private RoleController roleController = RoleController.getInstance();
    private UserController userController = UserController.getInstance();

    /**
     * Get list of all section roles
     * @param adminId id of admin
     * @param adminToken token of admin
     * @return Response object
     */
    public Response getSectionRoles(int adminId, String adminToken){
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = roleController.canEditUserRole(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            List<Role> roles = roleController.getAll();
            return ResponseBuilder.buildSectionRoleListResponse(roles);
        }catch (Exception e) {
            return ResponseBuilder.buildResponse(e);
        }
    }

}
