package ru.knowledgebase.usermodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.*;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.rolemodule.exceptions.AssignDefaultRoleException;
import ru.knowledgebase.usermodule.exceptions.UserAlreadyExistsException;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongPasswordException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;
import ru.knowledgebase.ldapmodule.exceptions.LdapController;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Date;

/**
 * Created by vova on 17.08.16.
 */
public class UserController {
    private final static int defaultGlobalRoleId = 2;
    private final static int defaultArticleRoleId = 2;
    private final static int defaultRootArticleId = 1;

    public static Token authorize(String login, String password) throws Exception{
        User user = null;
        DataCollector collector = new DataCollector();
        password = DigestUtils.md5Hex(password);
        user = collector.findUserByLogin(login);
        if (user == null){
            throw new UserNotFoundException();
        }else if (!user.getPassword().equals(password)){
            throw new WrongPasswordException();
        }
        Date date = new Date(new java.util.Date().getTime());
        String tokenStr = DigestUtils.md5Hex(user.getLogin() + date.toString());
        Token token = new Token(user, tokenStr, date);
        Token oldToken = collector.getUserToken(user);
        if (oldToken == null){
            collector.addToken(token);
        }else{
            token.setId(oldToken.getId());
            collector.updateToken(token);
        }
        return token;
    }

    public static Token authorizeLdap(String login, String password) throws Exception{
        LdapController.getInstance().authorize(login, DigestUtils.md5Hex(password));
        return authorize(login, password);
    }

    public static void register(String login, String password) throws Exception{
        if (login.length() == 0 || password.length() == 0){
            throw new WrongUserDataException();
        }
        DataCollector collector = new DataCollector();
        password = DigestUtils.md5Hex(password);

        User resUser = null;

        Article article = collector.findArticleById(defaultRootArticleId);
        ArticleRole articleRole = collector.findArticleRoleById(defaultArticleRoleId);
        GlobalRole globalRole = collector.findGlobalRoleById(defaultGlobalRoleId);

        if (article == null || articleRole == null || globalRole == null){
            throw new AssignDefaultRoleException();
        }
        try {
            resUser = collector.addUser(new User(login, password));
            GlobalRoleController.assignUserRole(resUser, globalRole);
            ArticleRoleController.assignUserRole(resUser, articleRole, article);
        }catch(org.springframework.dao.DataIntegrityViolationException e){
            throw new UserAlreadyExistsException();
        }
       // LdapController.getInstance().createUser(login, password, "User");
    }

    public static void delete(User user) throws Exception{
        DataCollector collector = new DataCollector();
        if (user == null)
            throw new UserNotFoundException();
        collector.deleteUser(user);
   //     LdapController.getInstance().deleteUser(user.getLogin());
    }

    public static void delete(int id) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUserById(id);
        delete(user);
    }

    public static void delete(String login) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUserByLogin(login);
        delete(user);
    }

    public static void changePassword(User user, String newPass) throws Exception {
        if (newPass.length() == 0)
            throw new WrongUserDataException();
        newPass = DigestUtils.md5Hex(newPass);
        DataCollector collector = new DataCollector();
        if (user == null)
            throw new UserNotFoundException();
        user.setPassword(newPass);
        collector.updateUser(user);
        LdapController.getInstance().changePass(user.getLogin(), newPass);
    }

    public static void changePassword(int id, String newPass) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUserById(id);
        changePassword(user, newPass);
    }

    public static void changePassword(String login, String newPass) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUserByLogin(login);
        changePassword(user, newPass);
    }
}
