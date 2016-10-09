package ru.knowledgebase.articlemodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.dbmodule.dataservices.searchservices.SearchService;
import ru.knowledgebase.dbmodule.storages.SectionStorage;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.exceptionmodule.sectionexceptions.ArticleCanNotBeSectionException;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by root on 25.08.16.
 */
public class ArticleControllerTest {

    private static ImageController ic = ImageController.getInstance();
    private static DataCollector dc = DataCollector.getInstance();
    private static String title = "Title";
    private static String body = "Body";
    private static Integer author = 1;
    private static Integer parentArticle = 1;
    private static ArticleController ac = ArticleController.getInstance();
    private static SectionController sc = SectionController.getInstance();

    private static User u;
    private static Article base;

    private static Article createTest;
    private static Article updateArticle;

    private static Image img;

    //t
    private static String[] titles = {
            "Summary form only given, as follows right left no yes",
            "Simple tokenizer that splits the text stream on whitespace ",
            "Внимание! С 7 сентября Агентством транспорта Финляндии были введены ограничения скорости ",
            "Ориентировочное опоздание скоростных поездов Аллегро в указанный период отправления "};

    private static Article addArticle;
    //t

    @BeforeClass
    public static void init() throws Exception{

        img = new Image("home/path", "some");
        img = ic.addImage(img);

        u = new User("TestUser", "123", "t1@m",
                "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null);
        u = dc.addUser(u);
        author = u.getId();

        base = ac.addBaseArticle("1", "2", u.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5));

        parentArticle = base.getId();
        updateArticle = ac.addArticle(title, body, u.getId(), parentArticle, new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        addArticle = updateArticle;
    }

    @AfterClass
    public static void clear() throws Exception{
        ac.deleteArticle(base.getId());
        dc.deleteUser(u.getId());
        ic.deleteImage(img.getId());
    }


    @Test
    public void addArticle() throws Exception {
        parentArticle = base.getId();
        createTest = ac.addArticle(title, body, u.getId(), parentArticle, new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        createTest = ac.getArticle(createTest.getId());
        assertTrue(createTest != null);

        ac.deleteArticle(createTest.getId());
    }

    @Test(expected = ArticleNotFoundException.class)
    public void deleteArticle() throws Exception {
        Article a = ac.addArticle(title, body, author, parentArticle, new Timestamp(5), new Timestamp(5), new Timestamp(5), false);
        ac.deleteArticle(a.getId());
        ac.getArticle(a.getId());
    }

    @Test
    public void childrenArticle() throws Exception {
        Article a = ac.addArticle(title, body, author, parentArticle, new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        assertTrue(ac.getArticleChildrenIds(base.getId()).size() == 2);
    }

    @Test
    public void deleteBaseArticle() throws Exception {
        Article a = ac.addArticle(title, body, u.getId(), parentArticle, new Timestamp(5), new Timestamp(5), new Timestamp(5), false);
        Article b = ac.addArticle(title, body, u.getId(), a.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), false);
        //printObject(a);
        //printObject(b);

        ac.deleteArticle(a.getId());

        try {
            Article child = ac.getArticle(b.getId());
        }
        catch (Exception ex) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
        return;
    }

    @Test
    public void updateArticle() throws Exception {
        String newString = "new string";

        updateArticle.setTitle(newString);
        updateArticle = ac.updateArticle(updateArticle.getId(), updateArticle.getTitle(), updateArticle.getBody(),
                updateArticle.getAuthor().getId(), updateArticle.getParentArticle(), new Timestamp(5), new Timestamp(5), new Timestamp(5));

        updateArticle = ac.getArticle(updateArticle.getId());
        assertTrue(updateArticle.getTitle().equals(newString));

        updateArticle.setBody(newString);
        int id1 = updateArticle.getId();
        updateArticle = ac.updateArticle(updateArticle);
        int id2 = updateArticle.getId();
        assertTrue(updateArticle.getBody().equals(newString));

        //ac.deleteArticle(updateArticle.getId());
    }

    @Test(expected = ArticleCanNotBeSectionException.class)
    public void sectionOrganization() throws Exception{
        Article newArticle1 = ac.addArticle(title, body, u.getId(), updateArticle.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), false);
        Article newArticle2 = ac.addArticle(title, body, u.getId(), newArticle1.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), false);
        Article newArticle3 = ac.addArticle(title, body, u.getId(), newArticle2.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);

    }

    @Test
    public void getNextLevelSections() throws Exception {
        Article newArticle1 = ac.addArticle("A1", body, u.getId(), base.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        Article newArticle2 = ac.addArticle("A2", body, u.getId(), newArticle1.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        Article newArticle3 = ac.addArticle("A3", body, u.getId(), newArticle1.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        List<Article> arts = sc.getNextLevelSections(newArticle1.getId());
        for (Article a : arts) {
            //printObject(a);
        }
        assertTrue(arts.size() == 2);
        //ac.deleteArticle(newArticle1.getId());
    }

    @Test
    public void getSectionTree() throws Exception {
        Article newArticle1 = ac.addArticle("A1", body, u.getId(), base.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        Article newArticle2 = ac.addArticle("A2", body, u.getId(), newArticle1.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        Article newArticle3 = ac.addArticle("A3", body, u.getId(), newArticle2.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        List<Article> arts = sc.getSectionTree(newArticle1.getId());
        for (Article a : arts) {
            //printObject(a);
        }
        assertTrue(arts.size() == 3);
        //ac.deleteArticle(newArticle1.getId());
    }

    private void printObject(Article a) {
        System.out.println("============");
        System.out.println(a.getTitle());
        System.out.println(a.getAuthor().getLogin());
    }

}