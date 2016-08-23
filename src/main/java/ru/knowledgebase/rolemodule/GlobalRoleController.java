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
        DataCollector collector = new DataCollector();
        collector.addGlobalRole(role);
    }

    public static void update(GlobalRole role) throws Exception{
        DataCollector collector = new DataCollector();
        collector.updateGlobalRole(role);
    }

    public static void delete(GlobalRole role) throws Exception{
        if (role == null)
            throw new RoleNotFoundException();
        DataCollector collector = new DataCollector();
        collector.deleteGlobalRole(role);
    }

    public static void delete(int id) throws Exception{
        DataCollector collector = new DataCollector();
        GlobalRole role = collector.findGlobalRole(id);
        collector.deleteGlobalRole(role);
    }

    public static void assignUserRole(UserGlobalRole role) throws Exception{
        DataCollector collector = new DataCollector();
        UserGlobalRole existRole =  collector.findUserGlobalRole(role.getUser());
        if (existRole != null)
            role.setId(existRole.getId());
        collector.addUserGlobalRole(role);
    }

    public static void assignUserRole(User user, GlobalRole globalRole) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (globalRole == null)
            throw new RoleNotFoundException();
        DataCollector collector = new DataCollector();
        collector.addUserGlobalRole(new UserGlobalRole(user, globalRole));
    }

    public static void assignUserRole(int userId, int roleId) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUser(userId);
        GlobalRole role = collector.findGlobalRole(roleId);
        assignUserRole(user, role);
    }

    public static void deleteUserRole(UserGlobalRole role) throws Exception{
        DataCollector collector = new DataCollector();
        collector.deleteUserGlobalRole(role);
    }

    public static void deleteUserRole(User user, GlobalRole globalRole) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (globalRole == null)
            throw new RoleNotFoundException();
        deleteUserRole(new UserGlobalRole(user, globalRole));
    }

    public static void deleteGlobalRole(int userId, int roleId) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUser(userId);
        GlobalRole role = collector.findGlobalRole(roleId);
        deleteUserRole(user, role);
    }

}
