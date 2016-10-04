package ru.knowledgebase.usermodule;

import jdk.nashorn.internal.objects.Global;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleDeleteException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotAssignedException;
import ru.knowledgebase.ldapmodule.LdapWorker;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.rolemodule.GlobalRoleController;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;

import java.sql.Timestamp;

import static org.junit.Assert.assertTrue;

/**
 * Created by vova on 25.08.16.
 */
public class GlobalRoleControllerTest {
    private DataCollector collector = DataCollector.getInstance();
    private GlobalRoleController c = GlobalRoleController.getInstance();

    private User user;
    private GlobalRole role;
    private GlobalRole role2;
    private Article base;
    private Article article1;
    private Article article2;
    private Article article3;

    @Before
    public void prepareAll() throws Exception{
        try{
            user = collector.findUser("testeeee");
        }catch (Exception e){

        }
        if (user == null)
            user = UserController.getInstance().register("testeeee", "test", "t1@m",
                    "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null);
        try{
            base = ArticleController.getInstance().getBaseArticle();
        }catch (Exception e){

        }
        if (base == null)
            base = ArticleController.getInstance().addBaseArticle("s", "f", user.getId(), null, null, null);

        try{
            role = collector.addGlobalRole(new GlobalRole());
            role2 = collector.addGlobalRole(new GlobalRole());
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
        }catch (Exception e){
        }
        try{
            collector.deleteGlobalRole(role.getId());
        }catch (Exception e){
        }
        try{
            collector.deleteGlobalRole(role2.getId());

        }catch (Exception e){
        }
        try{
            collector.deleteUser(user.getId());
        }catch (Exception e){
        }
        try{
            collector.deleteArticle(article2.getId());
        }catch (Exception e){
        }
        try{
            collector.deleteArticle(article3.getId());
        }catch (Exception e){
        }
        try{
            collector.deleteArticle(base.getId());
        }catch (Exception e){
        }
    }

    @Test
    public void assign1() throws Exception{
        c.assignUserRole(user, role);
        assertTrue(c.findUserRole(user).getId() == role.getId());
    }

    @Test
    public void assign2() throws Exception{
        c.assignUserRole(user, role);
        c.assignUserRole(user, role2);
        assertTrue(c.findUserRole(user).getId() == role2.getId());
    }

    @Test
    public void delete1() throws Exception{
        c.assignUserRole(user.getId(), role.getId());
        c.deleteUserRole(user.getId());
        assertTrue(collector.findUserGlobalRole(user) == null);
    }

    @Test(expected = RoleNotAssignedException.class)
    public void delete2() throws Exception{
        c.deleteUserRole(user.getId());
    }

    @Test
    public void update1() throws Exception{
        int id = role.getId();
        role.setCanAddUser(true);
        c.update(role);
        assertTrue(collector.findGlobalRole(id) != null);
        assertTrue(collector.findGlobalRole(id).isCanAddUser() == true);
    }
}
