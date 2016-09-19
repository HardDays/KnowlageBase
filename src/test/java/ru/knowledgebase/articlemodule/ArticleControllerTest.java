package ru.knowledgebase.articlemodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by root on 25.08.16.
 */
public class ArticleControllerTest {

    private static ImageController ic = new ImageController();
    private static DataCollector dc = new DataCollector();
    private static String title = "Title";
    private static String body = "Body";
    private static Integer author = 1;
    private static Integer parentArticle = 1;
    private static List<String> imgs = new LinkedList<String>();
    private static ArticleController ac = new ArticleController();

    private static User u;
    private static Article base;

    private static Article createTest;
    private static Article updateArticle;

    @BeforeClass
    public static void init() throws Exception{
        Image img = new Image("home/path");
        img = ic.addImage(img);
        imgs.add(img.getId());

        u = new User("TestUser", "123");
        u = dc.addUser(u);
        author = u.getId();

        base = ac.addBaseArticle("1", "2", u.getId(), new LinkedList<String>());

        parentArticle = base.getId();
        updateArticle = ac.addArticle(title, body, u.getId(), parentArticle, new LinkedList<String>());
    }

    @AfterClass
    public static void clear() throws Exception{
        ac.deleteArticle(base.getId());
        dc.deleteUser(u.getId());
    }

    @Transactional
    @Test
    public void addArticle() throws Exception {
        parentArticle = base.getId();
        createTest = ac.addArticle(title, body, u.getId(), parentArticle, imgs);
        createTest = ac.getArticleObject(createTest.getId());
        printObject(createTest);
        //ac.deleteArticle(createTest.getId());
    }

    @Transactional
    @Test(expected = ArticleNotFoundException.class)
    public void deleteArticle() throws Exception {
        Article a = ac.addArticle(title, body, author, parentArticle, new ArrayList<String>());
        ac.deleteArticle(a.getId());
        ac.getArticle(a.getId());
    }

    @Transactional
    @Test(expected = ArticleNotFoundException.class)
    public void deleteBaseArticle() throws Exception {
        Article a = ac.addArticle(title, body, u.getId(), parentArticle, new ArrayList<String>());
        Article b = ac.addArticle(title, body, u.getId(), a.getId(), new ArrayList<String>());
        printObject(a);
        printObject(b);

        ac.deleteArticle(a.getId());

        Article child = ac.getArticleObject(b.getId());
    }

    @Transactional
    @Test
    public void updateArticle() throws Exception {
        String newString = "new string";

        updateArticle.setTitle(newString);
        updateArticle = ac.updateArticle(updateArticle.getId(), updateArticle.getTitle(), updateArticle.getBody(),
                updateArticle.getAuthor().getId(), updateArticle.getParentArticle().getId(), new ArrayList<String>());

        updateArticle = ac.getArticleObject(updateArticle.getId());
        assertTrue(updateArticle.getTitle().equals(newString));

        updateArticle.setBody(newString);
        updateArticle = ac.updateArticle(updateArticle);
        assertTrue(updateArticle.getBody().equals(newString));

        //ac.deleteArticle(updateArticle.getId());
    }

    private void printObject(Article a) {
        System.out.println("============");
        System.out.println(a.getTitle());
        System.out.println(a.getAuthor().getLogin());
        for (Image i : a.getImages()) {
            System.out.println(i.getPath());
        }
    }

}