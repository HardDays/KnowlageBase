package ru.knowledgebase.usermodule;

import org.junit.*;
import static org.junit.Assert.*;

import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.exceptionmodule.userexceptions.UserAlreadyExistsException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongPasswordException;

import ru.knowledgebase.rolemodule.RoleController;

/**
 * Created by vova on 18.08.16.
 */
public class UserControllerTest {
    private DataCollector collector = DataCollector.getInstance();
    private UserController c = UserController.getInstance();

    private User user;
    private User user2;
    private Role role;
    private Role role2;
    private Article base;
    private Article article1;
    private Article article2;
    private Article article3;

    @Before
    public void prepareAll() throws Exception{
        try{
            user = collector.findUser("testeeee1");
            user2 = collector.findUser("testeeee2");

        }catch (Exception e){

        }
        if (user == null)
            user = UserController.getInstance().register("testeeee1", "1", "t1@m",
                    "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null);
        if (user2 == null)
            user2 = UserController.getInstance().register("testeeee2", "2", "t1@m",
                    "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null);
        try{
            base = ArticleController.getInstance().getBaseArticle();
        }catch (Exception e){

        }
        if (base == null)
            base = ArticleController.getInstance().addBaseArticle("s", "f", user.getId(), null, null, null);
        try{
            Role r = new Role();
            r.setRoleId(228);
            role = collector.addRole(r);
            r = new Role();
            r.setRoleId(229);
            role2 = collector.addRole(r);
        }catch (Exception e){

        }
        try{
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
            collector.deleteRole(role2.getId());
        }catch (Exception e){

        }
        try{
            collector.deleteRole(role.getId());
        }catch (Exception e){
        }
        try{
            collector.deleteUser(user.getId());
        }catch (Exception e){
        }
        try{
            collector.deleteUser(user2.getId());
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
        collector.deleteAllUserSections(user.getId());
        collector.deleteAllUserSections(user2.getId());
    }

    @Test
    public void authorize1() throws Exception{
        Token t = c.authorize("testeeee1", "1");
        assertTrue(c.checkUserToken(user.getId(), t.getToken()));
    }

    @Test(expected = WrongPasswordException.class)
    public void authorize2() throws Exception{
        c.authorize("testeeee1", "dssda");
    }

    @Test(expected = UserNotFoundException.class)
    public void authorize3() throws Exception{
        c.authorize("testeeee1222222222", "dssda");
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void register1() throws Exception{
        c.register("testeeee1", "1", "t1@m",
                "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null);
    }

    @Test
    public void update1() throws Exception{
        c.update(user.getId(), "testeeee3", "1", "t1@m",
                "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null);
        Token t = c.authorize("testeeee3", "1");
        assertTrue(c.checkUserToken(user.getId(), t.getToken()));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void update2() throws Exception{
        c.update(user.getId(), "testeeee2", "sdds", "t1@m",
                "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null);
    }

    @Test
    public void delete1() throws Exception {
        RoleController.getInstance().assignUserRole(user2.getId(), article1.getId(), role2.getId());
        c.delete(user2.getId());
        assertTrue(collector.findUserSectionRole(user2, article1) == null);
    }


    @Test
    public void delete3() throws Exception {
        c.authorize("testeeee2", "2");
        c.delete(user2.getId());
        assertTrue(collector.getUserToken(user2) == null);
        //collector.deleteToken(token);
    }
}