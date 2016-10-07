package ru.knowledgebase.usermodule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.commentmodule.CommentController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.RoleController;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by vova on 04.10.16.
 */
public class CommentControllerTest {
    private DataCollector collector = DataCollector.getInstance();
    private CommentController c = CommentController.getInstance();

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
                    "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null);
        if (user2 == null)
            user2 = UserController.getInstance().register("testeeee2", "2", "t1@m",
                    "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null);
        try{
            base = ArticleController.getInstance().getBaseArticle();
        }catch (Exception e){

        }
        if (base == null)
            base = ArticleController.getInstance().addBaseArticle("s", "f", user.getId(), null, null, null);

        try{
            Role r = new Role();
            r.setCanViewMistakes(true);
            role = collector.addRole(r);
            r = new Role();
            r.setCanAddMistakes(true);
            role2 = collector.addRole(r);
            article1 = ArticleController.getInstance().addArticle("1", "f", user.getId(), base.getId(), null, null, null, false);
            article2 = ArticleController.getInstance().addArticle("2", "f", user.getId(), base.getId(), null, null, null, false);
            article3 = ArticleController.getInstance().addArticle("3", "f", user.getId(), article2.getId(), null, null, null, false);
            RoleController.getInstance().assignBaseUserRole(user.getId(), role.getId());
            RoleController.getInstance().assignBaseUserRole(user2.getId(), role2.getId());

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
    public void add1() throws Exception{
        Comment com = c.add(user2.getId(), article1.getId(), "dsffds", "fdsds");
        c.delete(com.getId());
        assertTrue(collector.findComment(com.getId()) == null);
    }

    @Test
    public void add2() throws Exception{
        Comment com = c.add(user2.getId(), article1.getId(), "dsffds", "fdsds");
        assertTrue(c.canDeleteComment(user.getId(), com.getId()));
        List<Comment> l = c.findByAdmin(user.getId());
        assertTrue(l.size() == 1);
        assertTrue(l.get(0).getId() == com.getId());
        c.delete(com.getId());
        assertTrue(collector.findComment(com.getId()) == null);
    }



}
