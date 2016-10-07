package ru.knowledgebase.usermodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.UserSectionRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.exceptionmodule.userexceptions.UserAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongUserDataException;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
 * Created by vova on 17.08.16.
 */
public class UserController {

    private DataCollector collector = DataCollector.getInstance();
  //  private LdapWorker ldapWorker = LdapWorker.getInstance();

    private static volatile UserController instance;

    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static UserController getInstance() {
        UserController localInstance = instance;
        if (localInstance == null) {
            synchronized (UserController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserController();
                }
            }
        }
        return localInstance;
    }

    /**
     * Update token for authorized user
     * @param user user object
     * @return return updated token
     */
    private Token updateToken(User user) throws Exception{
        Date date = new Date(new java.util.Date().getTime());
        //token = user login + current date
        String tokenStr = DigestUtils.md5Hex(user.getLogin() + date.toString());
        Token token = new Token(user, tokenStr, date);
        try {
            Token oldToken = collector.getUserToken(user);
            //add token, if not exists, else = update with current date
            if (oldToken == null) {
                collector.addToken(token);
            } else {
                token.setId(oldToken.getId());
                collector.updateToken(token);
            }
        }catch (Exception e){
            throw new DataBaseException();
        }
        return token;
    }
    /**
     * Authorize user in database
     * @param login user login
     * @param password user password
     * @return return user token
     */
    public Token authorize(String login, String password) throws Exception{
        if (login == null || password == null)
            throw new WrongUserDataException();
        User user = null;
        //MD5 of password
        password = DigestUtils.md5Hex(password);
        try {
            user = collector.findUser(login);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (user == null) {
            throw new UserNotFoundException();
        } else if (!user.getPassword().equals(password)) {
            throw new WrongPasswordException();
        }
        return updateToken(user);
    }

    /**
     * Register new user in database and LDAP
     * @param user formed user object
     */
    public User register(User user) throws Exception{
        try {
            return collector.addUser(user);
        }catch(org.springframework.dao.DataIntegrityViolationException e){
            throw new UserAlreadyExistsException();
        }catch(Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Register new user in database and LDAP
     * @param login user login
     * @param password user password
     */
    public User register(String login, String password, String email,
                         String firstName, String middleName, String lastName,
                         String office, String phone1, String phone2,
                         Timestamp recruitmentDate, Timestamp dismissalDate,
                         boolean hasEmailNotifications, boolean hasSiteNotifications, Integer superVisorId) throws Exception{
        if (login == null || password == null)
            throw new WrongUserDataException();
        if (login.length() == 0 || password.length() == 0){
            throw new WrongUserDataException();
        }
        password = DigestUtils.md5Hex(password);
        return register(new User(login, password, email,
                                firstName, middleName, lastName,
                                office, phone1, phone2,
                                recruitmentDate, dismissalDate,
                                hasEmailNotifications, hasSiteNotifications, superVisorId));
    }

    /**
     * Find user object
     * @param login user login
     * @return user object
     */
    public User find(String login) throws Exception {
        if (login == null)
            throw new WrongUserDataException();
        User user = null;
        try {
            user = collector.findUser(login);
        } catch (Exception e) {
            throw new DataBaseException();
        }
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }
    /**
     * Find user object
     * @param id user id
     * @return user object
     */
    public User find(int id) throws Exception {
        if (id == 0)
            throw new WrongUserDataException();
        User user = null;
        try {
            user = collector.findUser(id);
        } catch (Exception e) {
            throw new DataBaseException();
        }
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }
    /**
     * Find users by supervisor
     * @param superVisorId user id
     * @return users object list
     */
    public List<User> findBySuperVisor(int superVisorId) throws Exception {
        if (superVisorId == 0)
            throw new WrongUserDataException();
        List<User> users = null;
        try {
            users = collector.findUserBySuperVisor(superVisorId);
        } catch (Exception e) {
            throw new DataBaseException();
        }
        return users;
    }
    /**
     * Find users by supervisor
     * @param offset from
     * @param limit number
     * @param superVisorId user id
     * @return users object list
     */
    public List<User> findBySuperVisor(int superVisorId, int offset, int limit) throws Exception {
        if (superVisorId == 0 || limit == 0)
            throw new WrongUserDataException();
        List<User> users = null;
        try {
            users = collector.findUserBySuperVisor(superVisorId, offset, limit);
        } catch (Exception e) {
            throw new DataBaseException();
        }
        return users;
    }
    /**
     * Delete user from database and LDAP
     * @param user user object (important: id should be specified)
     */
    public void delete(User user) throws Exception{
        if (user == null)
            throw new UserNotFoundException();
        try {
            collector.deleteUser(user);
        }catch (Exception e){
            throw new DataBaseException();
        }
        try{
            collector.updateSuperVisor(user.getId(), null);
        }catch (Exception e){

        }
    }

    /**
     * Delete user from database and LDAP
     * @param id user id
     */
    public void delete(int id) throws Exception{
        if (id == 0)
            throw new WrongUserDataException();
        try {
            collector.deleteUser(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new DataBaseException();
        }
        try{
            collector.updateSuperVisor(id, null);
        }catch (Exception e){
            e.printStackTrace();
            throw new DataBaseException();
        }
    }
    /**
     * Delete userfrom database and LDAP
     * @param login user login
     */
    public void delete(String login) throws Exception{
        if (login == null)
            throw new WrongUserDataException();
        User user = null;
        try {
            user = collector.findUser(login);
        }catch (Exception e){
            throw new DataBaseException();
        }
        delete(user);
    }

    public void update(int userId, String login, String password, String email,
                         String firstName, String middleName, String lastName,
                         String office, String phone1, String phone2,
                         Timestamp recruitmentDate, Timestamp dismissalDate,
                         boolean hasEmailNotifications, boolean hasSiteNotifications, Integer superVisorId) throws Exception{
        if (userId == 0)
            throw new WrongUserDataException();
        if (login == null || password == null)
            throw new WrongUserDataException();
        if (login.length() == 0 || password.length() == 0)
            throw new WrongUserDataException();
        User user = null, exist = null;
        try{
            user = collector.findUser(userId);
            if (!user.getLogin().equals(login))
                exist = collector.findUser(login);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (user == null)
            throw new UserNotFoundException();
        if (exist != null)
            throw new UserAlreadyExistsException();

        password = DigestUtils.md5Hex(password);
        user = new User(login, password, email,
                firstName, middleName, lastName,
                office, phone1, phone2,
                recruitmentDate, dismissalDate,
                hasEmailNotifications, hasSiteNotifications, superVisorId);
        user.setId(userId);
        collector.updateUser(user);
    }

    //convert date from date to milliseconds
    private long getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    /**
     * Checks is user token valid and actual
     * @param id user id
     * @param token user token
     * @return true if valid and actual
     */
    public boolean checkUserToken(int id, String token) throws Exception{
        if (id == 0)
            throw new WrongUserDataException();
        if (token == null)
            throw new WrongUserDataException();
        Token tokenObj = null;
        try{
            tokenObj = collector.getUserToken(id);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (tokenObj == null)
            return false;
        Date tokenDate = tokenObj.getEndDate();
        Date curDate = new Date(new java.util.Date().getTime());
        boolean notInspired = getDatePart(tokenDate) >=  getDatePart(curDate);
        return tokenObj.getToken().equals(token) && notInspired;
    }
    /**
     * Return list of all users
     * @return list with user objects
     */
    public List<User> getAll() throws Exception{
        List <User> users = null;
        try{
           users = collector.getAllUsers();
        }catch (Exception e){
            throw new DataBaseException();
        }
        return users;
    }
    /**
     * Return users with offset and limit
     * @param offset from
     * @param limit number of users
     * Return list of users
     * @return list with user objects
     */
    public List<User> getAll(int offset, int limit) throws Exception{
        if (limit == 0)
            throw new WrongUserDataException();
        List <User> users = null;
        try{
            users = collector.getAllUsers(offset, limit);
        }catch (Exception e){
            throw new DataBaseException();
        }
        return users;
    }
    /**
     * @param sectionId id of section
     * Return list of all users attached to section
     * @return list with user list with their roles
     */
    public List <UserSectionRole> getSectionUsers(int sectionId) throws Exception{
        if (sectionId == 0)
            throw new WrongUserDataException();
        List <UserSectionRole> roles = null;
        try{
            Article article = collector.findArticle(sectionId);
            while (true) {
                if (article.isSection()){
                    roles = collector.findUserSectionRoleBySection(article.getId());
                    if (roles.size() > 0)
                        break;
                }
                if (article.getParentArticle() == -1)
                    break;
                article = collector.findArticle(article.getSectionId());
            }
        }catch (Exception e){
            throw new DataBaseException();
        }
        return roles;
    }
    /**
     * @param offset from
     * @param limit number of users
     * @param sectionId id of section
     * Return list of all users attached to section
     * @return list with user list with their roles
     */
    public List <UserSectionRole> getSectionUsers(int sectionId, int offset, int limit) throws Exception{
        if (sectionId == 0 || limit == 0)
            throw new WrongUserDataException();
        List <UserSectionRole> roles = null;
        try{
            Article article = collector.findArticle(sectionId);
            while (true) {
                if (article.isSection()){
                    roles = collector.findUserSectionRoleBySection(article.getId(), offset, limit);
                    if (roles.size() > 0)
                        break;
                }
                if (article.getParentArticle() == -1)
                    break;
                article = collector.findArticle(article.getSectionId());
            }
        }catch (Exception e){
            throw new DataBaseException();
        }
        return roles;
    }
    /**
     * @param userId id of user
     * Return list of all sections where user is attached
     * @return list with user id's
     */
    public HashSet<Integer> getUserSections(int userId) throws Exception{
        if (userId == 0)
            throw new WrongUserDataException();
        try {
            return collector.getUserSections(userId);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * @param userId id of user
     * Return list of all sections where user is attached
     * @return list with user id's
     */
    public List<Integer> getUserSections(int userId, int offset, int limit) throws Exception{
        if (userId == 0 || limit == 0)
            throw new WrongUserDataException();
        try {
            return collector.getUserSections(userId, offset, limit);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * @param userId id of user
     * Return list of all sections where user is attached
     * @return list with user objects
     */
    public HashSet<Article> getUserSectionsObj(int userId) throws Exception{
        if (userId == 0)
            throw new WrongUserDataException();
        try {
            return collector.getUserSectionsObj(userId);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * @param userId id of user
     * Return list of all sections where user is attached
     * @return list with user objects
     */
    public List<Article> getUserSectionsObj(int userId, int offset, int limit) throws Exception{
        if (userId == 0 || limit == 0)
            throw new WrongUserDataException();
        try {
            return collector.getUserSectionsObj(userId, offset, limit);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
}
