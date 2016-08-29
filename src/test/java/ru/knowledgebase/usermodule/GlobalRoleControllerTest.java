package ru.knowledgebase.usermodule;

import org.junit.Before;
import org.junit.Test;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.ldapmodule.LdapWorker;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;

import static org.junit.Assert.assertTrue;

/**
 * Created by vova on 25.08.16.
 */
public class GlobalRoleControllerTest {
    private final String login1 = "testlogin1";

    private final String password1 = "testpassword1";

    private final int articleId = 1;
    private final String articleName = "testarticle";

    private final String role1Name = "testrole1";
    private final String role2Name = "testrole2";

    private final int roleId = 1;
    private final String roleName = "User";

    private DataCollector collector = new DataCollector();
    private LdapWorker ldapWorker = LdapWorker.getInstance();

    public void prepareUser() throws Exception{
        User user = collector.findUser(login1);
        if (user == null) {
            user = new User();
            user.setLogin(login1);
            user.setPassword(password1);
            collector.addUser(user);
        }
        if (!ldapWorker.isUserExists(login1))
            ldapWorker.createUser(login1, password1);
    }

    public void afterUser() throws Exception{
        User user = collector.findUser(login1);
        if (user != null)
            collector.deleteUser(user);
        if (ldapWorker.isUserExists(login1))
            ldapWorker.deleteUser(login1);
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
        afterUser();
        GlobalRole role = collector.findGlobalRole(role1Name);
        if (role != null){
            collector.deleteGlobalRole(role);
        }
        role = collector.findGlobalRole(role2Name);
        if (role != null){
            collector.deleteGlobalRole(role);
        }
        if (ldapWorker.isRoleExists(role1Name)) {
            ldapWorker.deleteRole(role1Name);
        }
        if (ldapWorker.isRoleExists(role2Name))
            ldapWorker.deleteRole(role2Name);
        prepareUser();
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
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        assertTrue(collector.findGlobalRole(role1Name) != null);
    }

    @Test(expected = RoleAlreadyExistsException.class)
    public void create2() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        GlobalRoleController.getInstance().create(role);
    }

    @Test
    public void update1() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        role = collector.findGlobalRole(role1Name);
        role.setName(role2Name);
        GlobalRoleController.getInstance().update(role);
        assertTrue(collector.findGlobalRole(role1Name) == null);
        assertTrue(collector.findGlobalRole(role2Name) != null);
    }

    @Test
    public void delete1() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().delete(role);
        assertTrue(collector.findGlobalRole(role1Name) == null);
    }

    @Test
    public void delete2() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().delete(role.getId());
        assertTrue(collector.findGlobalRole(role1Name) == null);
    }
    /*
    @Test
    public void delete3() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        UserGlobalRole userGlobalRole = new UserGlobalRole(user, role);
        GlobalRoleController.getInstance().assignUserRole(userGlobalRole);
        GlobalRoleController.getInstance().delete(role);
        assertTrue(collector.findUser(login1) != null);
        assertTrue(collector.findGlobalRole(role1Name) == null);
    }*/

    @Test(expected = RoleNotFoundException.class)
    public void delete4() throws Exception{
        GlobalRoleController.getInstance().delete(10000);
    }

    @Test
    public void findUserRole1() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().assignUserRole(user, role);
        assertTrue(GlobalRoleController.getInstance().findUserRole(user).getName().equals(role1Name));
    }

    @Test
    public void findUserRole2() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().assignUserRole(user, role);
        assertTrue(GlobalRoleController.getInstance().findUserRole(user.getId()).getName().equals(role1Name));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserRole3() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().assignUserRole(user, role);
        assertTrue(GlobalRoleController.getInstance().findUserRole(10000).getName().equals(role1Name));
    }

    @Test
    public void assignUserRole1() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().assignUserRole(user, role);
        assertTrue(collector.findUserGlobalRole(user) != null);
    }

    @Test
    public void assignUserRole2() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().assignUserRole(user.getId(), role.getId());
        assertTrue(collector.findUserGlobalRole(user) != null);
    }

    @Test
    public void assignUserRole3() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().assignUserRole(new UserGlobalRole(user, role));
        assertTrue(collector.findUserGlobalRole(user) != null);
    }

    @Test(expected = UserNotFoundException.class)
    public void assignUserRole4() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().assignUserRole(10000, role.getId());
        assertTrue(collector.findUserGlobalRole(user) != null);
    }

    @Test(expected = RoleNotFoundException.class)
    public void assignUserRole6() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        GlobalRoleController.getInstance().assignUserRole(user.getId(), 10000);
        assertTrue(collector.findUserGlobalRole(user) != null);
    }

    @Test
    public void deleteUserRole1() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        collector.addUserGlobalRole(new UserGlobalRole(user, role));
        GlobalRoleController.getInstance().deleteUserRole(user, role);
        assertTrue(collector.findUser(login1) != null);
        assertTrue(collector.findGlobalRole(role1Name) != null);
    }

    @Test
    public void deleteUserRole2() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        collector.addUserGlobalRole(new UserGlobalRole(user, role));
        GlobalRoleController.getInstance().deleteUserRole(user.getId(), role.getId());
        assertTrue(collector.findUser(login1) != null);
        assertTrue(collector.findGlobalRole(role1Name) != null);
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUserRole3() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        collector.addUserGlobalRole(new UserGlobalRole(user, role));
        GlobalRoleController.getInstance().deleteUserRole(10000, role.getId());
    }

    @Test(expected = RoleNotFoundException.class)
    public void deleteUserRole5() throws Exception{
        GlobalRole role = new GlobalRole(role1Name);
        GlobalRoleController.getInstance().create(role);
        User user = collector.findUser(login1);
        role = collector.findGlobalRole(role1Name);
        collector.addUserGlobalRole(new UserGlobalRole(user, role));
        GlobalRoleController.getInstance().deleteUserRole(user.getId(), 10000);
    }


}
