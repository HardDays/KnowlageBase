package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.roleexceptions.AssignDefaultRoleException;
import ru.knowledgebase.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.UserController;

/**
 * Created by vova on 20.08.16.
 */
public class GlobalRoleController {

    private int defaultGlobalRoleId = 1;

    private DataCollector collector = new DataCollector();
    private LdapController ldapController = LdapController.getInstance();

    private static volatile GlobalRoleController instance;

    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static GlobalRoleController getInstance() {
        GlobalRoleController localInstance = instance;
        if (localInstance == null) {
            synchronized (LdapController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new GlobalRoleController();
                }
            }
        }
        return localInstance;
    }
    
    /**
     * Create new global role
     * @param globalRole global role formed object
     */
    public void create(GlobalRole globalRole) throws Exception{
        ldapController.createRole(globalRole.getName());
        try {
            collector.addGlobalRole(globalRole);
        }catch (org.springframework.dao.DataIntegrityViolationException e){
            //rollback
            ldapController.deleteRole(globalRole.getName());
            throw new RoleAlreadyExistsException();
        }catch (Exception e){
            //rollback
            ldapController.deleteRole(globalRole.getName());
            throw e;
        }
    }
    /**
     * Update global role
     * @param globalRole global role object (important: id should be specified)
     */
    public void update(GlobalRole globalRole) throws Exception{
        collector.updateGlobalRole(globalRole);
    }
    /**
     * Delete global role
     * @param globalRole global role object (important: id should be specified)
     */
    public void delete(GlobalRole globalRole) throws Exception{
        if (globalRole == null)
            throw new RoleNotFoundException();
        collector.deleteGlobalRole(globalRole);
        ldapController.deleteRole(globalRole.getName());
    }
    /**
     * Update global role
     * @param globalRoleId of global role
     */
    public void delete(int globalRoleId) throws Exception{
        GlobalRole globalRole = collector.findGlobalRole(globalRoleId);
        delete(globalRole);
    }
    /**
     * Assign global role for specified user
     * @param role formed object
     */
    public void assignUserRole(UserGlobalRole role) throws Exception{
        UserGlobalRole existRole =  collector.findUserGlobalRole(role.getUser());
        if (existRole != null)
            role.setId(existRole.getId());
        collector.addUserGlobalRole(role);
        ldapController.changeRole(role.getUser().getLogin(), role.getGlobalRole().getName());
    }
    /**
     * Assign default global role for specified user
     * @param user user object
     */
    public void assignDefaultUserRole(User user) throws Exception{
        GlobalRole globalRole = collector.findGlobalRole(defaultGlobalRoleId);
        if (globalRole == null)
            throw new AssignDefaultRoleException();
        assignUserRole(user, globalRole);
    }
    /**
     * Assign global role for specified user
     * @param user user object (important: id should be specified)
     * @param globalRole global role object (important: id should be specified)
     */
    public void assignUserRole(User user, GlobalRole globalRole) throws Exception{
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
    public void assignUserRole(int userId, int globalRoleId) throws Exception{
        User user = collector.findUser(userId);
        GlobalRole role = collector.findGlobalRole(globalRoleId);
        assignUserRole(user, role);
    }
    /**
     * Find global role for specified user
     * @param user user object (important: id should be specified)
     * @return global role for user
     */
    public GlobalRole findUserRole(User user) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        return collector.findUserGlobalRole(user).getGlobalRole();
    }
    /**
     * Find global role for specified user
     * @param userId user id
     * @return global role for user
     */
    public GlobalRole findUserRole(int userId) throws Exception{
        User user = collector.findUser(userId);
        return findUserRole(user);
    }
    /**
     * Delete user global role
     * @param role formed role object (important: id should be specified)
     */
    private void deleteUserRole(UserGlobalRole role) throws Exception{
        collector.deleteUserGlobalRole(role);
    }
    /**
     * Delete user global role
     * @param user user object (important: id should be specified)
     * @param globalRole global role object (important: id should be specified)
     */
    public void deleteUserRole(User user, GlobalRole globalRole) throws Exception{
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
    public void deleteUserRole(int userId, int globalRoleId) throws Exception{
        User user = collector.findUser(userId);
        GlobalRole role = collector.findGlobalRole(globalRoleId);
        deleteUserRole(user, role);
    }

    public int getDefaultGlobalRoleId() {
        return defaultGlobalRoleId;
    }

    public void setDefaultGlobalRoleId(int defaultGlobalRoleId) {
        this.defaultGlobalRoleId = defaultGlobalRoleId;
    }

}
