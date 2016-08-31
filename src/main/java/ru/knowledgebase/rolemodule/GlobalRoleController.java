package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.roleexceptions.AssignDefaultRoleException;
import ru.knowledgebase.ldapmodule.LdapWorker;
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

    private int defaultGlobalRoleId = 1;

    private DataCollector collector = new DataCollector();
    private LdapWorker ldapWorker = LdapWorker.getInstance();

    private static volatile GlobalRoleController instance;

    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static GlobalRoleController getInstance() {
        GlobalRoleController localInstance = instance;
        if (localInstance == null) {
            synchronized (GlobalRoleController.class) {
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
        ldapWorker.createRole(globalRole.getName());
        try {
            collector.addGlobalRole(globalRole);
        }catch (org.springframework.dao.DataIntegrityViolationException e){
            //rollback
            ldapWorker.deleteRole(globalRole.getName());
            throw new RoleAlreadyExistsException();
        }catch (Exception e){
            //rollback
            ldapWorker.deleteRole(globalRole.getName());
            throw new DataBaseException();
        }
    }
    /**
     * Update global role
     * @param globalRole global role object (important: id should be specified)
     */
    public void update(GlobalRole globalRole) throws Exception{
        try {
            collector.updateGlobalRole(globalRole);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Delete global role
     * @param globalRole global role object (important: id should be specified)
     */
    public void delete(GlobalRole globalRole) throws Exception{
        if (globalRole == null)
            throw new RoleNotFoundException();
        ldapWorker.deleteRole(globalRole.getName());
        try {
            collector.deleteGlobalRole(globalRole);
        }catch (Exception e){
            //rollback ldap
            ldapWorker.createRole(globalRole.getName());
            throw new DataBaseException();
        }
    }
    /**
     * Update global role
     * @param globalRoleId of global role
     */
    public void delete(int globalRoleId) throws Exception{
        GlobalRole globalRole = null;
        try {
            globalRole = collector.findGlobalRole(globalRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        delete(globalRole);
    }
    /**
     * Finds global role
     * @param id id of global role
     */
    public GlobalRole findGlobalRole(int id) throws Exception{
        GlobalRole role = null;
        try {
            collector.findGlobalRole(id);
        } catch (Exception e){
            throw new DataBaseException();
        }
        return role;
    }
    /**
     * Assign global role for specified user
     * @param role formed object
     */
    public void assignUserRole(UserGlobalRole role) throws Exception{
        UserGlobalRole existRole = null;
        try {
            collector.findUserGlobalRole(role.getUser());
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (existRole != null)
            role.setId(existRole.getId());
        try {
            collector.addUserGlobalRole(role);
        }catch (Exception e){
            throw new DataBaseException();
        }
        ldapWorker.changeRole(role.getUser().getLogin(), role.getGlobalRole().getName());
    }
    /**
     * Assign default global role for specified user
     * @param user user object
     */
    public void assignDefaultUserRole(User user) throws Exception{
        GlobalRole globalRole = null;
        try {
            collector.findGlobalRole(defaultGlobalRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
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
        User user = null;
        GlobalRole role = null;
        try {
            user = collector.findUser(userId);
            role = collector.findGlobalRole(globalRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
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
        GlobalRole globalRole = null;
        try {
            globalRole = collector.findUserGlobalRole(user).getGlobalRole();
        }catch (Exception e){
            throw new DataBaseException();
        }
        return globalRole;
    }
    /**
     * Find global role for specified user
     * @param userId user id
     * @return global role for user
     */
    public GlobalRole findUserRole(int userId) throws Exception{
        User user = null;
        try {
            user = collector.findUser(userId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        return findUserRole(user);
    }
    /**
     * Delete user global role
     * @param role formed role object (important: id should be specified)
     */
    private void deleteUserRole(UserGlobalRole role) throws Exception{
        try {
            collector.deleteUserGlobalRole(role);
        }catch (Exception e){
            throw new DataBaseException();
        }
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
        User user = null;
        GlobalRole role = null;
        try {
            user = collector.findUser(userId);
            role = collector.findGlobalRole(globalRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        deleteUserRole(user, role);
    }

    public int getDefaultGlobalRoleId() {
        return defaultGlobalRoleId;
    }

    public void setDefaultGlobalRoleId(int defaultGlobalRoleId) {
        this.defaultGlobalRoleId = defaultGlobalRoleId;
    }

}