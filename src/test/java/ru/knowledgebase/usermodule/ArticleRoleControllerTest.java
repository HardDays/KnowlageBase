package ru.knowledgebase.usermodule;

import org.junit.*;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleDeleteException;
import ru.knowledgebase.exceptionmodule.roleexceptions.RoleNotAssignedException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.RoleController;

import static org.junit.Assert.assertTrue;
/**
 * Created by vova on 25.08.16.
 */
public class ArticleRoleControllerTest {

    private DataCollector collector = DataCollector.getInstance();
    private RoleController c = RoleController.getInstance();

    private User user;
    private Role role;
    private Role role2;
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
                    "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null));
        try{
            base = ArticleController.getInstance().getBaseArticle();
        }catch (Exception e){

        }
        try {
            if (base == null)
                base = ArticleController.getInstance().addBaseArticle("s", "f", user.getId(), null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
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
            ArticleController.getInstance().deleteArticle(article1.getId());
        }catch (Exception e){
        }
        try{
            collector.deleteRole(role.getId());
        }catch (Exception e){

        }
        try{
            collector.deleteRole(role2.getId());
        }catch (Exception e){

        }
        try{
            ArticleController.getInstance().deleteArticle(article2.getId());
        }catch (Exception e){
        }
        try{
            ArticleController.getInstance().deleteArticle(article3.getId());
        }catch (Exception e){
        }
        try{
            ArticleController.getInstance().deleteArticle(base.getId());
        }catch (Exception e){
        }
        collector.deleteAllUserSections(user.getId());
        try{
            collector.deleteUser(user.getId());
        }catch (Exception e){
        }

    }

    @Test
    public void assign1() throws Exception{
        c.assignUserRole(user, article1, role);
        assertTrue(c.findUserRole(user, article1).getId() == role.getId());
    }

    @Test
    public void assign2() throws Exception{
        c.assignUserRole(user.getId(), article1.getId(), role.getId());
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
        assertTrue(collector.findUserSectionRole(user, article3) == null);
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
        assertTrue(collector.findUserSectionRole(user, article1) == null);
    }

    @Test(expected = RoleDeleteException.class)
    public void delete2() throws Exception{
        c.assignUserRole(user.getId(), article1.getId(), role.getId());
        c.deleteUserRole(user.getId(), article1.getId());
    }

    @Test
    public void update1() throws Exception{
        int id = role.getId();
        role.setCanViewMistakes(true);
        c.update(role);
        assertTrue(collector.findRole(id) != null);
        assertTrue(collector.findRole(id).isCanViewMistakes() == true);
    }
}
