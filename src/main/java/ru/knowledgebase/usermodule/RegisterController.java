package ru.knowledgebase.usermodule;

/**
 * Created by vova on 17.08.16.
 */

import org.apache.commons.codec.digest.DigestUtils;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.ArticleRole;
import ru.knowledgebase.modelsmodule.GlobalRole;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.usermodule.exceptions.UserAlreadyExistsException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;
import ru.knowledgebase.usermodule.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.User;

/**
 * Created by vova on 17.08.16.
 */
public class RegisterController {
    public static void register(String login, String password) throws Exception{
        if (login.length() == 0 || password.length() == 0){
            throw new WrongUserDataException();
        }
        DataCollector collector = new DataCollector();
        password = DigestUtils.md5Hex(password);
        GlobalRole globalRole = collector.findGlobalRoleByName("User");
        ArticleRole articleRole = collector.findArticleRoleByName("User");
        Article article = collector.findArticleById(1);

        if (articleRole == null || globalRole == null){
            throw new Exception("role not found!");
        }
        try {

            User resUser = collector.addUser(new User(login, password));
            GlobalRoleController.assignUserRole(resUser, new GlobalRole(2));
            ArticleRoleController.assignUserRole(resUser, new ArticleRole(2), new Article(1));
        }catch(org.springframework.dao.DataIntegrityViolationException e){
            throw new UserAlreadyExistsException();
        }catch (Exception e){
            throw e;
        }
        LdapController.getInstance().createUser(login, password, "User");
    }
}
