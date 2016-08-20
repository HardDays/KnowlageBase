package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.GlobalRole;
import ru.knowledgebase.modelsmodule.User;
import ru.knowledgebase.modelsmodule.UserGlobalRole;

/**
 * Created by vova on 20.08.16.
 */
public class GlobalRoleController {
    public static void createRole(GlobalRole role) throws Exception{
        DataCollector coll = new DataCollector();
        coll.addGlobalRole(role);
    }

    public static void assignUserRole(User user, GlobalRole role) throws Exception{
        DataCollector coll = new DataCollector();
        coll.addUserGlobalRole(new UserGlobalRole(user, role));
    }

    public static void assignUserRole(int userId, int roleId){

    }
}
