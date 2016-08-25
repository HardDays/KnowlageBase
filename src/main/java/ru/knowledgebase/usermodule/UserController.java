package ru.knowledgebase.usermodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.*;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.exceptionmodule.roleexceptions.AssignDefaultRoleException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongUserDataException;
import ru.knowledgebase.ldapmodule.LdapController;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Date;

/**
 * Created by vova on 17.08.16.
 */
public class UserController {

    private static int defaultGlobalRoleId = 1;
    private static int defaultArticleRoleId = 1;
    private static int defaultRootArticleId = 1;

    private static DataCollector collector = new DataCollector();
    private static LdapController ldapController = LdapController.getInstance();

    /**
     * Update token for authorized user
     * @param user user object
     * @return return updated token
     */
    private static Token updateToken(User user) throws Exception{
        Date date = new Date(new java.util.Date().getTime());
        //token = user login + current date
        String tokenStr = DigestUtils.md5Hex(user.getLogin() + date.toString());
        Token token = new Token(user, tokenStr, date);
        Token oldToken = collector.getUserToken(user);
        //add token, if not exists, else = update with current date
        if (oldToken == null){
            collector.addToken(token);
        }else{
            token.setId(oldToken.getId());
            collector.updateToken(token);
        }
        return token;
    }
    /**
     * Authorize user in database
     * @param login user login
     * @param password user password
     * @return return user token
     */
    public static Token authorize(String login, String password) throws Exception{
        User user = null;
        //MD5 of password
        password = DigestUtils.md5Hex(password);
        user = collector.findUser(login);
        if (user == null){
            throw new UserNotFoundException();
        }else if (!user.getPassword().equals(password)){
            throw new WrongPasswordException();
        }
        return updateToken(user);
    }
    /**
     * Authorize user in database and LDAP
     * @param login user login
     * @param password user password
     * @return return user token
     */
    public static Token authorizeLdap(String login, String password) throws Exception{
        ldapController.authorize(login, DigestUtils.md5Hex(password));
        return authorize(login, password);
    }
    /**
     * Register new user in database and LDAP
     * @param user formed user object
     */
    public static void register(User user) throws Exception{
        Article article = collector.findArticle(defaultRootArticleId);
        ArticleRole articleRole = collector.findArticleRole(defaultArticleRoleId);
        GlobalRole globalRole = collector.findGlobalRole(defaultGlobalRoleId);

        if (article == null || articleRole == null || globalRole == null){
            throw new AssignDefaultRoleException();
        }
        try {
            User resUser = collector.addUser(user);
            GlobalRoleController.assignUserRole(resUser, globalRole);
            ArticleRoleController.assignUserRole(resUser, article, articleRole);
        }catch(org.springframework.dao.DataIntegrityViolationException e){
            throw new UserAlreadyExistsException();
        }
       ldapController.createUser(user.getLogin(), user.getPassword(), globalRole.getName());
    }
    /**
     * Register new user in database and LDAP
     * @param login user login
     * @param password user password
     */
    public static void register(String login, String password) throws Exception{
        if (login.length() == 0 || password.length() == 0){
            throw new WrongUserDataException();
        }
        password = DigestUtils.md5Hex(password);
        register(new User(login, password));
    }
    /**
     * Delete user from database and LDAP
     * @param user user object (important: id should be specified)
     */
    public static void delete(User user) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        collector.deleteUser(user);
       ldapController.deleteUser(user.getLogin());
    }
    /**
     * Delete user from database and LDAP
     * @param id user id
     */
    public static void delete(int id) throws Exception{
        User user = collector.findUser(id);
        delete(user);
    }
    /**
     * Delete userfrom database and LDAP
     * @param login user login
     */
    public static void delete(String login) throws Exception{
        User user = collector.findUser(login);
        delete(user);
    }
    /**
     * Change user password in database and LDAP
     * @param user user object (important: id should be specified)
     * @param newPassword new password
     */
    private static void changePassword(User user, String newPassword) throws Exception {
        if (newPassword.length() == 0)
            throw new WrongUserDataException();
        newPassword = DigestUtils.md5Hex(newPassword);
        if (user == null)
            throw new UserNotFoundException();
        user.setPassword(newPassword);
        collector.updateUser(user);
        ldapController.changePassword(user.getLogin(), newPassword);
    }
    /**
     * Change user password in database and LDAP
     * @param id user id
     * @param newPassword new password
     */
    public static void changePassword(int id, String newPassword) throws Exception{
        User user = collector.findUser(id);
        changePassword(user, newPassword);
    }
    /**
     * Change user password in database and LDAP
     * @param login user login
     * @param newPassword new password
     */
    public static void changePassword(String login, String newPassword) throws Exception{
        User user = collector.findUser(login);
        changePassword(user, newPassword);
    }
    /**
     * Change user login in database and LDAP
     * @param user user object (important: id should be specified)
     * @param newLogin new login
     */
    public static void changeLogin(User user, String newLogin) throws Exception{
        if (newLogin.length() == 0)
            throw new WrongUserDataException();
        if (user == null)
            throw new UserNotFoundException();
        if (collector.findUser(newLogin) != null)
            throw new UserAlreadyExistsException();
        String oldLogin = user.getLogin();
        user.setLogin(newLogin);
        collector.updateUser(user);
       ldapController.changeLogin(oldLogin, newLogin);
    }
    /**
     * Change user login in database and LDAP
     * @param login user login
     * @param newLogin new login
     */
    public static void changeLogin(String login, String newLogin) throws Exception{
        User user = collector.findUser(login);
        changeLogin(user, newLogin);
    }
    /**
     * Change user login in database and LDAP
     * @param id user id
     * @param newLogin new login
     */
    public static void changeLogin(int id, String newLogin) throws Exception{
        User user = collector.findUser(id);
        changeLogin(user, newLogin);
    }

    public static int getDefaultGlobalRoleId() {
        return defaultGlobalRoleId;
    }

    public static void setDefaultGlobalRoleId(int defaultGlobalRoleId) {
        UserController.defaultGlobalRoleId = defaultGlobalRoleId;
    }

    public static int getDefaultArticleRoleId() {
        return defaultArticleRoleId;
    }

    public static void setDefaultArticleRoleId(int defaultArticleRoleId) {
        UserController.defaultArticleRoleId = defaultArticleRoleId;
    }

    public static int getDefaultRootArticleId() {
        return defaultRootArticleId;
    }

    public static void setDefaultRootArticleId(int defaultRootArticleId) {
        UserController.defaultRootArticleId = defaultRootArticleId;
    }
}
