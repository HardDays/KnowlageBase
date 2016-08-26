package ru.knowledgebase.articlemodule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.Image;
import ru.knowledgebase.modelsmodule.User;

import java.util.ArrayList;
import java.util.Arrays;
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
    public static void init() {
        Image img = new Image("home/path");
        img = ic.addImage(img);
        imgs.add(img.getId());

        u = new User("TestUser");
        u = dc.addUser(u);

        base = ac.addArticle("1", "2", u.getId(), 0, new LinkedList<String>());

        parentArticle = base.getId();
        updateArticle = ac.addArticle(title, body, u.getId(), parentArticle, new LinkedList<String>());
    }

    @AfterClass
    public static void clear() {
        ac.deleteArticle(base.getId());
        dc.deleteUser(u.getId());
    }

    @Transactional
    @Test
    public void addArticle() throws Exception {
        parentArticle = base.getId();
        createTest = ac.addArticle(title, body, u.getId(), parentArticle, imgs);
        createTest = ac.getArticle(createTest.getId());
        printObject(createTest);
        //ac.deleteArticle(createTest.getId());
    }

    @Transactional
    @Test
    public void deleteArticle() throws Exception {
        Article a = ac.addArticle(title, body, author, parentArticle, new ArrayList<String>());
        ac.deleteArticle(a.getId());
        assertTrue(ac.getArticle(a.getId()) == null);
    }

    @Transactional
    @Test
    public void deleteBaseArticle() throws Exception {
        Article a = ac.addArticle(title, body, u.getId(), parentArticle, new ArrayList<String>());
        Article b = ac.addArticle(title, body, u.getId(), a.getId(), new ArrayList<String>());
        printObject(a);
        printObject(b);

        ac.deleteArticle(a.getId());

        Article child = ac.getArticle(b.getId());
        assertTrue(child == null);
    }

    @Transactional
    @Test
    public void updateArticle() throws Exception {
        String newString = "new string";

        updateArticle.setTitle(newString);
        updateArticle = ac.updateArticle(updateArticle.getId(), updateArticle.getTitle(), updateArticle.getBody(),
                updateArticle.getAuthor().getId(), updateArticle.getParentArticle().getId(), new ArrayList<String>());

        updateArticle = ac.getArticle(updateArticle.getId());
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