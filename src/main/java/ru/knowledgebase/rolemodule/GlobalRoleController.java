package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.rolemodule.exceptions.RoleNotFoundException;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;

/**
 * Created by vova on 20.08.16.
 */
public class GlobalRoleController {
    public static void create(GlobalRole role) throws Exception{
        DataCollector coll = new DataCollector();
        coll.addGlobalRole(role);
    }

    public static void assignUserRole(User user, GlobalRole role) throws Exception{
        DataCollector coll = new DataCollector();
        coll.addUserGlobalRole(new UserGlobalRole(user, role));
    }

    public static void assignUserRole(int userId, int roleId) throws Exception{
        DataCollector coll = new DataCollector();
        User user = coll.findUserById(userId);
        if (user == null)
            throw new UserNotFoundException();
        GlobalRole role = coll.findGlobalRoleById(roleId);
        if (role == null)
            throw new RoleNotFoundException();
        assignUserRole(user, role);
    }
}
