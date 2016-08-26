package ru.knowledgebase.deploymodule;

import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.rolemodule.GlobalRoleController;

/**
 * Created by vova on 26.08.16.
 */
public class RoleDeployer {
    private static GlobalRoleController globalRoleConroller = GlobalRoleController.getInstance();

    public static void deploy() throws Exception{
        //read config
        GlobalRole role = new GlobalRole();

    }
}
