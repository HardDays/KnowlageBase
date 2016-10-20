package ru.knowledgebase.analyticsmodule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.LogRecord.SearchRequestRecord;
import ru.knowledgebase.loggermodule.LogRecord.SearchResultRecord;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.RoleController;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by vova on 20.10.16.
 */
public class AnalyticsTest {
    private DataCollector collector = DataCollector.getInstance();
    private RoleController c = RoleController.getInstance();

    private User user;
    private Role role;
    private Role role2;
    private Article base;
    private Article article1;
    private Article article2;
    private Article article3;
    private Article article4;

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
            article3 = ArticleController.getInstance().addArticle("3", "f", user.getId(), article1.getId(), null, null, null, false);
            article4 = ArticleController.getInstance().addArticle("4", "f", user.getId(), article2.getId(), null, null, null, false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @After
    public void deleteAll() throws Exception{

        try{
            collector.deleteRole(role.getId());
        }catch (Exception e){

        }
        try{
            collector.deleteRole(role2.getId());
        }catch (Exception e){

        }
        try{
            ArticleController.getInstance().deleteArticle(article1.getId());
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
            ArticleController.getInstance().deleteArticle(article4.getId());
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
    public void testPopular1() throws Exception{
        List<ALogRecord> log = new LinkedList<ALogRecord>();
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(1, article4.getId()));
        log.add(new SearchResultRecord(1, article4.getId()));
        assertTrue(Analyser.getInstance().getPopularArticles(log, base.getId()).get(0).getId() == article3.getId());
    }

    @Test
    public void testPopular2() throws Exception{
        List<ALogRecord> log = new LinkedList<ALogRecord>();
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(1, article4.getId()));
        log.add(new SearchResultRecord(1, article4.getId()));
        assertTrue(Analyser.getInstance().getPopularArticles(log, article2.getId()).get(0).getId() == article4.getId());
    }

    @Test
    public void testRelevant1() throws Exception{
        List<ALogRecord> log = new LinkedList<ALogRecord>();
        log.add(new SearchRequestRecord(1, "bank azaza"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchRequestRecord(2, "bank sdds"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(2, article3.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchResultRecord(1, article4.getId()));
        log.add(new SearchRequestRecord(2, "bank sfdsd"));
        log.add(new SearchResultRecord(2, article4.getId()));
        assertTrue(Analyser.getInstance().getRelevantArticles(log).get("bank").get(0).getId() == article3.getId());
    }

    @Test
    public void testPopularKeywrods1() throws Exception{
        List<ALogRecord> log = new LinkedList<ALogRecord>();
        log.add(new SearchRequestRecord(1, "bank azaza"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchRequestRecord(2, "bank sdds"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(2, article3.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchResultRecord(1, article4.getId()));
        log.add(new SearchRequestRecord(2, "bank sfdsd"));
        log.add(new SearchResultRecord(2, article4.getId()));
        log.add(new SearchRequestRecord(2, "guns"));
        log.add(new SearchRequestRecord(2, "guns"));
        log.add(new SearchRequestRecord(2, "guns"));
        assertTrue(Analyser.getInstance().getPopularRequests(log).get(0).getRequest().equals("bank"));
    }

    @Test
    public void testSearchUsage1() throws Exception{
        List<ALogRecord> log = new LinkedList<ALogRecord>();
        log.add(new SearchRequestRecord(1, "bank azaza"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchRequestRecord(2, "bank sdds"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(2, article3.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchResultRecord(1, article4.getId()));
        log.add(new SearchRequestRecord(2, "bank sfdsd"));
        log.add(new SearchResultRecord(2, article4.getId()));
        assertTrue(Analyser.getInstance().getSearchUsage(log, article1.getId()) == 3);
        assertTrue(Analyser.getInstance().getSearchUsage(log, base.getId()) == 5);
    }

    @Test
    public void testGetUserViews() throws Exception{
        List<ALogRecord> log = new LinkedList<ALogRecord>();
        log.add(new SearchRequestRecord(1, "bank azaza"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchRequestRecord(2, "bank sdds"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(2, article4.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchResultRecord(1, article4.getId()));
        log.add(new SearchRequestRecord(2, "bank sfdsd"));
        log.add(new SearchResultRecord(2, article4.getId()));
        assertTrue(Analyser.getInstance().getUserViews(log, 1).get(0).getId() == article3.getId());
        assertTrue(Analyser.getInstance().getUserViews(log, 2).get(0).getId() == article4.getId());
    }

    @Test
    public void testGetUserRequests() throws Exception{
        List<ALogRecord> log = new LinkedList<ALogRecord>();
        log.add(new SearchRequestRecord(1, "bank azaza"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchRequestRecord(2, "bank sdds"));
        log.add(new SearchResultRecord(1, article3.getId()));
        log.add(new SearchResultRecord(2, article4.getId()));
        log.add(new SearchRequestRecord(1, "bank"));
        log.add(new SearchResultRecord(1, article4.getId()));
        log.add(new SearchRequestRecord(2, "bank sfdsd"));
        log.add(new SearchResultRecord(2, article4.getId()));
        assertTrue(Analyser.getInstance().getUserRequests(log).get(1).contains("bank"));
        assertTrue(Analyser.getInstance().getUserRequests(log).get(2).contains("bank sfdsd"));
    }
}