package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;

/**
 * Created by vova on 20.08.16.
 */
public class GlobalRoleController {

    private static DataCollector collector = new DataCollector();
    private static LdapController ldapController = LdapController.getInstance();

    /**
     * Create new global role
     * @param globalRole global role formed object
     */
    public static void create(GlobalRole globalRole) throws Exception{
        try {
            collector.addGlobalRole(globalRole);
        }catch (org.springframework.dao.DataIntegrityViolationException e){
            throw new RoleAlreadyExistsException();
        }
        ldapController.createRole(globalRole.getName());
    }
    /**
     * Update global role
     * @param globalRole global role object (important: id should be specified)
     */
    public static void update(GlobalRole globalRole) throws Exception{
        collector.updateGlobalRole(globalRole);
    }
    /**
     * Delete global role
     * @param globalRole global role object (important: id should be specified)
     */
    public static void delete(GlobalRole globalRole) throws Exception{
        if (globalRole == null)
            throw new RoleNotFoundException();
        collector.deleteGlobalRole(globalRole);
        ldapController.deleteRole(globalRole.getName());
    }
    /**
     * Update global role
     * @param globalRoleId of global role
     */
    public static void delete(int globalRoleId) throws Exception{
        GlobalRole globalRole = collector.findGlobalRole(globalRoleId);
        delete(globalRole);
    }
    /**
     * Assign global role for specified user
     * @param role formed object
     */
    public static void assignUserRole(UserGlobalRole role) throws Exception{
        UserGlobalRole existRole =  collector.findUserGlobalRole(role.getUser());
        if (existRole != null)
            role.setId(existRole.getId());
        collector.addUserGlobalRole(role);
        ldapController.changeRole(role.getUser().getLogin(), role.getGlobalRole().getName());
    }
    /**
     * Assign global role for specified user
     * @param user user object (important: id should be specified)
     * @param globalRole global role object (important: id should be specified)
     */
    public static void assignUserRole(User user, GlobalRole globalRole) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (globalRole == null)
            throw new RoleNotFoundException();
        assignUserRole(new UserGlobalRole(user, globalRole));
    }
    /**
     * Assign global role for specified user
     * @param userId user id
     * @param globalRoleId global role id
     */
    public static void assignUserRole(int userId, int globalRoleId) throws Exception{
        User user = collector.findUser(userId);
        GlobalRole role = collector.findGlobalRole(globalRoleId);
        assignUserRole(user, role);
    }
    /**
     * Find global role for specified user
     * @param user user object (important: id should be specified)
     * @return global role for user
     */
    public static GlobalRole findUserRole(User user) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        return collector.findUserGlobalRole(user).getGlobalRole();
    }
    /**
     * Find global role for specified user
     * @param userId user id
     * @return global role for user
     */
    public static GlobalRole findUserRole(int userId) throws Exception{
        User user = collector.findUser(userId);
        return findUserRole(user);
    }
    /**
     * Delete user global role
     * @param role formed role object (important: id should be specified)
     */
    private static void deleteUserRole(UserGlobalRole role) throws Exception{
        collector.deleteUserGlobalRole(role);
    }
    /**
     * Delete user global role
     * @param user user object (important: id should be specified)
     * @param globalRole global role object (important: id should be specified)
     */
    public static void deleteUserRole(User user, GlobalRole globalRole) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        if (globalRole == null)
            throw new RoleNotFoundException();
        deleteUserRole(new UserGlobalRole(user, globalRole));
    }
    /**
     * Delete user global role
     * @param userId user id
     * @param globalRoleId global role id
     */
    public static void deleteUserRole(int userId, int globalRoleId) throws Exception{
        User user = collector.findUser(userId);
        GlobalRole role = collector.findGlobalRole(globalRoleId);
        deleteUserRole(user, role);
    }

}
