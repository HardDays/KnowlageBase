package ru.knowledgebase.usermodule;

import org.junit.*;
import ru.knowledgebase.articlemodule.ArticleNotFoundException;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.exceptions.RoleAlreadyExistsException;
import ru.knowledgebase.rolemodule.exceptions.RoleNotFoundException;
import ru.knowledgebase.usermodule.exceptions.UserAlreadyExistsException;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongPasswordException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;
import ru.knowledgebase.ldapmodule.LdapController;

import javax.validation.constraints.AssertTrue;

import static org.junit.Assert.assertTrue;
/**
 * Created by vova on 25.08.16.
 */
public class ArticleRoleControllerTest {
    private final String login1 = "testlogin1";

    private final String password1 = "testpassword1";

    private final int articleId = 1;
    private final String articleName = "testarticle";

    private final String role1Name = "testrole1";
    private final String role2Name = "testrole2";

    private final int roleId = 1;
    private final String roleName = "User";

    private DataCollector collector = new DataCollector();
    private LdapController ldapController = LdapController.getInstance();

    @Before
    public void prepareUser() throws Exception{
        User user = collector.findUser(login1);
        if (user == null) {
            user = new User();
            user.setLogin(login1);
            user.setPassword(password1);
            collector.addUser(user);
        }

        if (!ldapController.isUserExists(login1))
            ldapController.createUser(login1, password1, "User");
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

    @Before
    public void prepareRoles() throws Exception{
        ArticleRole role = collector.findArticleRole(role1Name);
        if (role != null){
            collector.deleteArticleRole(role);
        }
        role = collector.findArticleRole(role2Name);
        if (role != null){
            collector.deleteArticleRole(role);
        }
    }

    @Before
    public void prepareArticle() throws Exception{
        Article article = collector.findArticle(articleId);
        if (article == null) {
            article = new Article(articleId);
            article.setTitle(articleName);
            collector.addArticle(article);
        }
    }

    @Test
    public void create1() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        assertTrue(collector.findArticleRole(role1Name) != null);
    }

    @Test(expected = RoleAlreadyExistsException.class)
    public void create2() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        ArticleRoleController.create(role);
    }

    @Test
    public void update1() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        role = collector.findArticleRole(role1Name);
        role.setName(role2Name);
        ArticleRoleController.update(role);
        assertTrue(collector.findArticleRole(role1Name) == null);
        assertTrue(collector.findArticleRole(role2Name) != null);
    }

    @Test
    public void delete1() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.delete(role);
        assertTrue(collector.findArticleRole(role1Name) == null);
    }

    @Test
    public void delete2() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.delete(role.getId());
        assertTrue(collector.findArticleRole(role1Name) == null);
    }

    @Test
    public void delete3() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        UserArticleRole userArticleRole = new UserArticleRole(user, article, role);
        ArticleRoleController.assignUserRole(userArticleRole);
        collector.deleteArticleRole(role);
        assertTrue(collector.findUser(login1) != null);
        assertTrue(collector.findArticle(articleId) != null);
        assertTrue(collector.findArticleRole(role1Name) == null);
    }

    @Test
    public void findUserRole1() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(user, article, role);
        assertTrue(ArticleRoleController.findUserRole(user, article).getName().equals(role1Name));
    }

    @Test
    public void findUserRole2() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(user, article, role);
        assertTrue(ArticleRoleController.findUserRole(user.getId(), article.getId()).getName().equals(role1Name));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserRole3() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(user, article, role);
        assertTrue(ArticleRoleController.findUserRole(10000, article.getId()).getName().equals(role1Name));
    }

    @Test(expected = ArticleNotFoundException.class)
    public void findUserRole4() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(user, article, role);
        assertTrue(ArticleRoleController.findUserRole(user.getId(), 10000).getName().equals(role1Name));
    }

    @Test
    public void assignUserRole1() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(user, article, role);
        assertTrue(collector.findUserArticleRole(user, article) != null);
    }

    @Test
    public void assignUserRole2() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(user.getId(), article.getId(), role.getId());
        assertTrue(collector.findUserArticleRole(user, article) != null);
    }

    @Test
    public void assignUserRole3() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(new UserArticleRole(user, article, role));
        assertTrue(collector.findUserArticleRole(user, article) != null);
    }

    @Test(expected = UserNotFoundException.class)
    public void assignUserRole4() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(10000, article.getId(), role.getId());
        assertTrue(collector.findUserArticleRole(user, article) != null);
    }

    @Test(expected = ArticleNotFoundException.class)
    public void assignUserRole5() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(user.getId(), 10000, role.getId());
        assertTrue(collector.findUserArticleRole(user, article) != null);
    }

    @Test(expected = RoleNotFoundException.class)
    public void assignUserRole6() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        ArticleRoleController.assignUserRole(user.getId(), article.getId(), 10000);
        assertTrue(collector.findUserArticleRole(user, article) != null);
    }

    @Test
    public void deleteUserRole1() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        collector.addUserArticleRole(new UserArticleRole(user, article, role));
        ArticleRoleController.deleteUserRole(user, article, role);
        assertTrue(collector.findUser(login1) != null);
        assertTrue(collector.findArticle(articleId) != null);
        assertTrue(collector.findArticleRole(role1Name) != null);
    }

    @Test
    public void deleteUserRole2() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        collector.addUserArticleRole(new UserArticleRole(user, article, role));
        ArticleRoleController.deleteUserRole(user.getId(), article.getId(), role.getId());
        assertTrue(collector.findUser(login1) != null);
        assertTrue(collector.findArticle(articleId) != null);
        assertTrue(collector.findArticleRole(role1Name) != null);
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUserRole3() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        collector.addUserArticleRole(new UserArticleRole(user, article, role));
        ArticleRoleController.deleteUserRole(10000, article.getId(), role.getId());
    }

    @Test(expected = ArticleNotFoundException.class)
    public void deleteUserRole4() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        collector.addUserArticleRole(new UserArticleRole(user, article, role));
        ArticleRoleController.deleteUserRole(user.getId(), 10000, role.getId());
    }

    @Test(expected = RoleNotFoundException.class)
    public void deleteUserRole5() throws Exception{
        ArticleRole role = new ArticleRole(role1Name);
        ArticleRoleController.create(role);
        User user = collector.findUser(login1);
        Article article = collector.findArticle(articleId);
        role = collector.findArticleRole(role1Name);
        collector.addUserArticleRole(new UserArticleRole(user, article, role));
        ArticleRoleController.deleteUserRole(user.getId(), article.getId(), 10000);
    }


}
