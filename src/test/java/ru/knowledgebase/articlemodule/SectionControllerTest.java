package ru.knowledgebase.articlemodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by root on 02.10.16.
 */
public class SectionControllerTest {
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

    @BeforeClass
    @Transactional
    public static void init() throws Exception{

        img = new Image("home/path", "some3");
        img = ic.addImage(img);

        u = new User("TestUser", "123", "t1@m",
                "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null);
        u = dc.addUser(u);
        author = u.getId();

        base = ac.addBaseArticle("1", "2", u.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5));

        parentArticle = base.getId();
        updateArticle = ac.addArticle(title, body, u.getId(), parentArticle, new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
    }

    @AfterClass
    public static void clear() throws Exception{
        ac.deleteArticle(base.getId());
        dc.deleteUser(u.getId());
        ic.deleteImage(img.getId());
    }

    @Test
    public void testHeirarchy() throws Exception {
        Article newArticle1 = ac.addArticle("1_1", body, u.getId(), base.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        Article newArticle2 = ac.addArticle("1_2", body, u.getId(), base.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        Article newArticle3 = ac.addArticle("2_1", body, u.getId(), newArticle2.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        Article newArticle4 = ac.addArticle("2_2", body, u.getId(), newArticle1.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        Article newArticle5 = ac.addArticle("3_1", body, u.getId(), newArticle4.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5), true);

        HashMap<Integer, HashMap<Article, List<Article>>> m = sc.getSectionHierarchy();
        System.out.println(m.toString());
    }
}