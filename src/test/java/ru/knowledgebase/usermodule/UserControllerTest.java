package ru.knowledgebase.usermodule;

import org.junit.*;
import static org.junit.Assert.*;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.exceptionmodule.userexceptions.UserAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongUserDataException;

/**
 * Created by vova on 18.08.16.
 */
public class UserControllerTest {

    private final String login1 = "testlogin1";
    private final String login2 = "testlogin2";

    private final String password1 = "testpassword1";
    private final String password2 = "testpassword2";

    private final int articleId = 1;
    private final String articleName = "testarticle";

    private final int roleId = 1;
    private final String roleName = "User";
    private DataCollector collector = new DataCollector();
    private LdapController ldapController = LdapController.getInstance();

    @Before
    public void prepareArticle() throws Exception{
        Article article = collector.findArticle(articleId);
        if (article == null) {
            article = new Article(articleId);
            article.setTitle(articleName);
            collector.addArticle(article);
        }
    }

    @Before
    public void prepareUser() throws Exception{
        User user = collector.findUser(login1);
        if (user != null)
            collector.deleteUser(user);
        user = collector.findUser(login2);
        if (user != null)
            collector.deleteUser(user);

        if (ldapController.isUserExists(login1))
            ldapController.deleteUser(login1);

        if (ldapController.isUserExists(login2))
            ldapController.deleteUser(login2);
    }

    @Before
    public void prepareGlobalRole() throws Exception{
        GlobalRole role = collector.findGlobalRole(roleId);
        if (role == null){
            role = new GlobalRole(roleId);
            role.setName(roleName);
            collector.addGlobalRole(role);
        }
    }

    @Before
    public void prepareArticleRole() throws Exception{
        ArticleRole role = collector.findArticleRole(roleId);
        if (role == null){
            role = new ArticleRole(roleId);
            role.setName(roleName);
            collector.addArticleRole(role);
        }
    }

    @Test
    public void register1() throws Exception{
        UserController.getInstance().register(login1, password1);
        User user = collector.findUser(login1);
        assertTrue(user != null);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void register2() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().register(login1, password1);
    }

    @Test(expected = WrongUserDataException.class)
    public void register3() throws Exception{
        UserController.getInstance().register("", password1);
    }

    @Test(expected = WrongUserDataException.class)
    public void register4() throws Exception{
        UserController.getInstance().register(login1, "");
    }

    @Test
    public void authorize1() throws Exception{
        UserController.getInstance().register(login1, password1);
        Token token = UserController.getInstance().authorize(login1, password1);
        assertTrue(token != null);
    }

    @Test
    public void authorize2() throws Exception{
        UserController.getInstance().register(login1, password1);
        Token token = UserController.getInstance().authorize(login1, password1);
        assertTrue(token != null);
        token = UserController.getInstance().authorize(login1, password1);
        assertTrue(token != null);
    }

    @Test
    public void authorize3() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().authorizeLdap(login1, password1);
    }

    @Test(expected = UserNotFoundException.class)
    public void authorize4() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().authorize(login2, password1);
    }
    @Test(expected = UserNotFoundException.class)
    public void authorize5() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().authorizeLdap(login2, password1);
    }

    @Test(expected = WrongPasswordException.class)
    public void authorize6() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().authorize(login1, password2);
    }

    @Test(expected = WrongPasswordException.class)
    public void authorize7() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().authorizeLdap(login1, password2);
    }

    @Test
    public void changePassword1() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().changePassword(login1, password2);
        UserController.getInstance().authorizeLdap(login1, password2);
    }

    @Test
    public void changePassword2() throws Exception{
        UserController.getInstance().register(login1, password1);
        User user = collector.findUser(login1);
        UserController.getInstance().changePassword(user.getId(), password2);
        UserController.getInstance().authorizeLdap(login1, password2);
    }

    @Test(expected = WrongUserDataException.class)
    public void changePassword3() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().changePassword(login1, "");
    }

    @Test(expected = UserNotFoundException.class)
    public void changePassword4() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().changePassword(login2, password1);
    }

    @Test(expected = UserNotFoundException.class)
    public void changePassword5() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().changePassword(login2, password1);
    }

    @Test
    public void changeLogin1() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().changeLogin(login1, login2);
        UserController.getInstance().authorizeLdap(login2, password1);
    }

    @Test
    public void changeLogin2() throws Exception{
        UserController.getInstance().register(login1, password1);
        User user = collector.findUser(login1);
        UserController.getInstance().changeLogin(user.getId(), login2);
        UserController.getInstance().authorizeLdap(login2, password1);
    }

    @Test(expected = WrongUserDataException.class)
    public void changeLogin3() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().changeLogin(login1, "");
    }

    @Test(expected = UserNotFoundException.class)
    public void changeLogin4() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().changeLogin(login2, login1);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void changeLogin5() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().register(login2, password2);
        UserController.getInstance().changeLogin(login1, login2);
    }

    @Test
    public void delete1() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().delete(login1);
        User user = collector.findUser(login1);
        assertTrue(user == null);
        GlobalRole globalRole = collector.findGlobalRole(roleId);
        assertTrue(globalRole != null);
        assertTrue(globalRole.getName().equals(roleName));
        Article article = collector.findArticle(articleId);
        assertTrue(article != null);
        assertTrue(article.getTitle().equals(articleName));
        ArticleRole articleRole = collector.findArticleRole(roleId);
        assertTrue(articleRole != null);
        assertTrue(articleRole.getName().equals(roleName));
    }

    @Test
    public void delete2() throws Exception{
        UserController.getInstance().register(login1, password1);
        User user = collector.findUser(login1);
        UserController.getInstance().delete(user);
        user = collector.findUser(login1);
        assertTrue(user == null);
        GlobalRole globalRole = collector.findGlobalRole(roleId);
        assertTrue(globalRole != null);
        assertTrue(globalRole.getName().equals(roleName));
        Article article = collector.findArticle(articleId);
        assertTrue(article != null);
        assertTrue(article.getTitle().equals(articleName));
        ArticleRole articleRole = collector.findArticleRole(roleId);
        assertTrue(articleRole != null);
        assertTrue(articleRole.getName().equals(roleName));
    }

    @Test
    public void delete3() throws Exception{
        UserController.getInstance().register(login1, password1);
        User user = collector.findUser(login1);
        UserController.getInstance().delete(user.getId());
        user = collector.findUser(login1);
        assertTrue(user == null);
        GlobalRole globalRole = collector.findGlobalRole(roleId);
        assertTrue(globalRole != null);
        assertTrue(globalRole.getName().equals(roleName));
        Article article = collector.findArticle(articleId);
        assertTrue(article != null);
        assertTrue(article.getTitle().equals(articleName));
        ArticleRole articleRole = collector.findArticleRole(roleId);
        assertTrue(articleRole != null);
        assertTrue(articleRole.getName().equals(roleName));
    }

    @Test(expected = UserNotFoundException.class)
    public void delete4() throws Exception{
        UserController.getInstance().register(login1, password1);
        UserController.getInstance().delete(login2);
    }
}