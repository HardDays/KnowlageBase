package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.roleexceptions.AssignDefaultRoleException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotAssignedException;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;

import java.util.List;

/**
 * Created by vova on 20.08.16.
 */
public class GlobalRoleController {

    private int defaultGlobalRoleId = 1;

    private DataCollector collector = DataCollector.getInstance();
   // private LdapWorker ldapWorker = LdapWorker.getInstance();

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
     * Return all available global roles
     * @return list of roles
     */
    public List<GlobalRole> getAll() throws Exception{
        List <GlobalRole> roles = null;
        try{
            roles = collector.getGlobalRoles();
        }catch (Exception e){
            throw new DataBaseException();
        }
        return roles;
    }
    /**
     * Create new global role
     * @param globalRole global role formed object
     */
    public void create(GlobalRole globalRole) throws Exception{
 //       ldapWorker.createRole(globalRole.getName());
        try {
            collector.addGlobalRole(globalRole);
        }catch (org.springframework.dao.DataIntegrityViolationException e){
            //rollback
        //    ldapWorker.deleteRole(globalRole.getName());
            throw new RoleAlreadyExistsException();
        }catch (Exception e){
            //rollback
          //  ldapWorker.deleteRole(globalRole.getName());
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
     //   ldapWorker.deleteRole(globalRole.getName());
        try {
            collector.deleteGlobalRole(globalRole);
        }catch (Exception e){
            //rollback ldap
       //     ldapWorker.createRole(globalRole.getName());
            throw new DataBaseException();
        }
    }
    /**
     * Update global role
     * @param globalRoleId of global role
     */
    public void delete(int globalRoleId) throws Exception{
        try {
            collector.deleteGlobalRole(globalRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Finds global role
     * @param id id of global role
     */
    public GlobalRole findGlobalRole(int id) throws Exception{
        GlobalRole role = null;
        try {
            role = collector.findGlobalRole(id);
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
            existRole = collector.findUserGlobalRole(role.getUser());
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
       // ldapWorker.changeRole(role.getUser().getLogin(), role.getGlobalRole().getName());
    }
    /**
     * Assign default global role for specified user
     * @param user user object
     */
    public void assignDefaultUserRole(User user) throws Exception{
        GlobalRole globalRole = null;
        try {
            globalRole = collector.findGlobalRole(defaultGlobalRoleId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (globalRole == null)
            throw new AssignDefaultRoleException();
        assignUserRole(user, globalRole);
    }
    /**
     * Assign default global role for specified user
     * @param userId user id
     */
    public void assignDefaultUserRole(int userId) throws Exception{
        GlobalRole globalRole = null;
        User user = null;
        try {
            globalRole = collector.findGlobalRole(defaultGlobalRoleId);
            user = collector.findUser(userId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (globalRole == null || user == null)
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
        if (globalRole == null)
            throw new RoleNotAssignedException();
        return globalRole;
    }
    /**
     * Find global role for specified user
     * @param userId user id
     * @return global role for user
     */
    public GlobalRole findUserRole(int userId) throws Exception{
        try {
           return collector.findUserGlobalRole(userId).getGlobalRole();
        }catch (Exception e){
            throw new DataBaseException();
        }
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
        deleteUserRole(collector.findUserGlobalRole(user));
    }
    /**
     * Delete user global role
     * @param userId user id
     */
    public void deleteUserRole(int userId) throws Exception{
        try {
            collector.deleteUserGlobalRoleByUser(userId);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }

    public void createBaseRoles() throws Exception{
        GlobalRole user = new GlobalRole();
        user.setName("Пользователь");
        create(user);

        try {
            defaultGlobalRoleId = collector.findGlobalRole("Пользователь").getId();
        }catch (Exception e){
            throw new DataBaseException();
        }

        GlobalRole superUser = new GlobalRole();
        superUser.setName("Суперпользователь");
        superUser.setCanAddUser(true);
        superUser.setCanDeleteUser(true);
        superUser.setCanEditUser(true);
        superUser.setCanEditUserRole(true);
        superUser.setCanViewUser(true);
        create(superUser);

        GlobalRole admin = new GlobalRole();
        admin.setName("Администратор раздела");
        admin.setCanViewUser(true);
        create(admin);

        GlobalRole superVisor = new GlobalRole();
        superVisor.setName("Супервизор");
        create(superVisor);

        GlobalRole banned = new GlobalRole();
        banned.setName("Пользователь без прав");
        banned.setBaseUser(true);
        create(banned);
    }

    public boolean canAddUser(int userId) throws Exception{
        return findUserRole(userId).isCanAddUser();
    }

    public boolean canEditUser(int userId) throws Exception{
        return findUserRole(userId).isCanEditUser();
    }

    public boolean canDeleteUser(int userId) throws Exception{
        return findUserRole(userId).isCanDeleteUser();
    }

    public boolean canViewUser(int userId) throws Exception{
        return findUserRole(userId).isCanViewUser();
    }

    public boolean canEditUserRole(int userId) throws Exception{
        return findUserRole(userId).isCanEditUserRole();
    }

    public boolean isBaseUser(int userId) throws Exception{
        return findUserRole(userId).isBaseUser();
    }
}
