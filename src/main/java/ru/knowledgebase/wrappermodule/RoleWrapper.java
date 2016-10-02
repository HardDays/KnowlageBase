package ru.knowledgebase.wrappermodule;

import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.responsemodule.ResponseBuilder;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.usermodule.UserController;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by vova on 19.09.16.
 */
public class RoleWrapper {


    private ArticleRoleController articleRoleController = ArticleRoleController.getInstance();
    private GlobalRoleController globalRoleController = GlobalRoleController.getInstance();
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
            boolean hasRights = globalRoleController.canEditUserRole(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            List<ArticleRole> roles = articleRoleController.getAll();
            return ResponseBuilder.buildSectionRoleListResponse(roles);
        }catch (Exception e) {
            return ResponseBuilder.buildResponse(e);
        }
    }
    /**
     * Get list of all global roles
     * @param adminId id of admin
     * @param adminToken token of admin
     * @return Response object
     */
    public Response getGlobalRoles(int adminId, String adminToken){
        try {
            boolean okToken = userController.checkUserToken(adminId, adminToken);
            boolean hasRights = globalRoleController.canEditUserRole(adminId);
            if (!okToken)
                return ResponseBuilder.buildWrongTokenResponse();
            if (!hasRights)
                return ResponseBuilder.buildNoAccessResponse();
            List<GlobalRole> roles = globalRoleController.getAll();
            return ResponseBuilder.buildGlobalRoleListResponse(roles);
        }catch (Exception e) {
            return ResponseBuilder.buildResponse(e);
        }
    }
}
