package ru.knowledgebase.articlemodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.NewsNotFoundException;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.articlemodels.News;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.sql.Timestamp;

import static org.junit.Assert.*;

/**
 * Created by root on 02.10.16.
 */
public class NewsControllerTest {

    private static ImageController ic = ImageController.getInstance();
    private static NewsController  nc = NewsController.getInstance();

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

        img = new Image("home/path", "some2");
        img = ic.addImage(img);

        u = new User("TestUser", "123", "t1@m",
                "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null);
        u = dc.addUser(u);
        author = u.getId();

        base = ac.addBaseArticle("1", "2", u.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5));

        parentArticle = base.getId();

    }

    @AfterClass
    public static void clear() throws Exception{
        ac.deleteArticle(base.getId());
        dc.deleteUser(u.getId());
        ic.deleteImage(img.getId());
    }

    @Test
    public void findNews() throws Exception {
        News news = nc.addNews("Hot!", "Vovan loh", author, base.getId());

        News n = nc.findNews(news.getId());
        assertTrue(n.getId() == news.getId());

        nc.deleteNews(news.getId());
    }

    @Test(expected = NewsNotFoundException.class)
    public void deleteNews() throws Exception {
        News news = nc.addNews("Hot!", "Vovan loh", author, base.getId());
        nc.deleteNews(news.getId());

        News n = nc.findNews(news.getId());
    }

    @Test
    public void addNews() throws Exception {
        News news = nc.addNews("Hot!", "Vovan loh", author, base.getId());

        Exception ex = null;
        try {
            News n = nc.findNews(news.getId());
        }
        catch (Exception e) {
            ex = e;
        }

        assertEquals(null, ex);

        nc.deleteNews(news.getId());
    }

    @Test
    public void getNewsBySection() throws Exception {

    }

}