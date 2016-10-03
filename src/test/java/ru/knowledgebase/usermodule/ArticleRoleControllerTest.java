package ru.knowledgebase.usermodule;

import org.junit.*;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleDeleteException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotAssignedException;
import ru.knowledgebase.ldapmodule.LdapWorker;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;

import java.sql.Timestamp;

import static org.junit.Assert.assertTrue;
/**
 * Created by vova on 25.08.16.
 */
public class ArticleRoleControllerTest {

    private DataCollector collector = DataCollector.getInstance();
    private ArticleRoleController c = ArticleRoleController.getInstance();

    private User user;
    private ArticleRole role;
    private ArticleRole role2;
    private Article base;
    private Article article1;
    private Article article2;
    private Article article3;

    @Before
    public void prepareAll() throws Exception{
        try{
            user = collector.findUser("test");
        }catch (Exception e){

        }
        if (user == null)
            user = collector.addUser(new User("test", "test", "t1@m",
                    "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null));
        try{
            base = ArticleController.getInstance().getBaseArticle();
        }catch (Exception e){

        }
        if (base == null)
            base = ArticleController.getInstance().addBaseArticle("s", "f", user.getId(), null, null, null);

        try{
            role = collector.addArticleRole(new ArticleRole());
            role2 = collector.addArticleRole(new ArticleRole());
            article1 = ArticleController.getInstance().addArticle("1", "f", user.getId(), base.getId(), null, null, null, true);
            article2 = ArticleController.getInstance().addArticle("2", "f", user.getId(), base.getId(), null, null, null, true);
            article3 = ArticleController.getInstance().addArticle("3", "f", user.getId(), article2.getId(), null, null, null, true);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @After
    public void deleteAll() throws Exception{
        try{
            collector.deleteArticle(article1.getId());
            collector.deleteArticle(article2.getId());
          //  collector.deleteArticle(article3.getId());
            collector.deleteArticle(base.getId());
            collector.deleteUser(user.getId());
            collector.deleteArticleRole(role.getId());
            collector.deleteArticleRole(role2.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void assign1() throws Exception{
        c.assignUserRole(user, article1, role);
        assertTrue(c.findUserRole(user, article1).getId() == role.getId());
    }

    @Test
    public void assign2() throws Exception{
        c.assignSuperUser(user.getId(), article2.getId(), role.getId());
        assertTrue(c.findUserRole(user, article1).getId() == role.getId());
    }

    @Test(expected = RoleNotAssignedException.class)
    public void assign3() throws Exception{
        c.assignUserRole(user.getId(), base.getId(), role.getId());
        assertTrue(c.findUserRole(user, article1).getId() == role.getId());
        assertTrue(c.findUserRole(user, article2).getId() == role.getId());
        c.assignUserRole(user.getId(), article3.getId(), role.getId());
        assertTrue(c.findUserRole(user, article3).getId() == role.getId());
        c.findUserRole(user, base);
    }

    @Test
    public void assign4() throws Exception{
        c.assignUserRole(user.getId(), article3.getId(), role.getId());
        assertTrue(c.findUserRole(user, article3).getId() == role.getId());
        c.assignUserRole(user.getId(), base.getId(), role.getId());
        assertTrue(c.findUserRole(user, article3).getId() == role.getId());
        assertTrue(collector.findUserArticleRole(user, article3) == null);
    }

    @Test
    public void assign5() throws Exception{
        c.assignUserRole(user.getId(), article1.getId(), role.getId());
        assertTrue(c.findUserRole(user, article1).getId() == role.getId());
        c.assignUserRole(user.getId(), article1.getId(), role2.getId());
        assertTrue(c.findUserRole(user, article1).getId() == role2.getId());
    }

    @Test
    public void delete1() throws Exception{
        c.assignUserRole(user.getId(), article1.getId(), role.getId());
        c.assignUserRole(user.getId(), article2.getId(), role.getId());
        c.deleteUserRole(user.getId(), article1.getId());
        assertTrue(collector.findUserArticleRole(user, article1) == null);
    }

    @Test(expected = RoleDeleteException.class)
    public void delete2() throws Exception{
        c.assignUserRole(user.getId(), article1.getId(), role.getId());
        c.deleteUserRole(user.getId(), article1.getId());
    }

    @Test(expected = RoleNotAssignedException.class)
    public void delete3() throws Exception{
        c.deleteUserRole(user.getId(), article1.getId());
    }

    @Test
    public void update1() throws Exception{
        int id = role.getId();
        role.setCanViewMistakes(true);
        c.update(role);
        assertTrue(collector.findArticleRole(id) != null);
        assertTrue(collector.findArticleRole(id).isCanViewMistakes() == true);
    }
}
