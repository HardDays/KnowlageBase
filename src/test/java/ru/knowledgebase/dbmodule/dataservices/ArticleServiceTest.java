package ru.knowledgebase.dbmodule.dataservices;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.Image;
import ru.knowledgebase.modelsmodule.User;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by root on 17.08.16.
 */
public class ArticleServiceTest {

    static final ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
    static final ArticleService as = (ArticleService) context.getBean("articleService");;
    static final UserService us = (UserService) context.getBean("userService");

    @Transactional
    @Test
    public void create() throws Exception {


        User au = new User("Anon");
        au = us.create(au);

        List<Image> img = new LinkedList<Image>();
        img.add(new Image("home/pisatel"));
        Article article = new Article("New book", "Very interestinf book!", "New",
                au, null, img);

        article = as.create(article);
        boolean res = as.exists(article.getId());
        assertTrue(res);
        as.delete(article.getId());
    }

    @Transactional
    @Test
    public void findById() throws Exception {
        User au = new User("Anon");
        au = us.create(au);
        Article article = new Article("New book", "Very interestinf book!", "New",
                au, null, null);
        article = as.create(article);
        assertTrue(as.exists(article.getId()));
    }

    @Transactional
    @Test
    public void update() throws Exception {
        User au = new User("Anon");
        au = us.create(au);
        Article article = new Article("New book", "Very interestinf book!", "New",
                au, null, null);
        article = as.create(article);
        String newTitle = "New new book";
        article.setTitle(newTitle);
        as.update(article);
        assertTrue(as.findById(article.getId()).getTitle().equals(newTitle));
        as.delete(article.getId());
    }

    @Transactional
    @Test
    public void delete() throws Exception {
        User au = new User("Anon");
        au = us.create(au);
        Article article = new Article("New book", "Very interestinf book!", "New",
                au, null, null);
        as.create(article);
        article = as.findByTitle(article.getTitle()).get(0);
        as.delete(article.getId());
        assertTrue(as.findById(article.getId()) == null);
    }

    @Transactional
    @Test
    public void hasNoChanges() throws Exception {
        User au = new User("Anon");
        au = us.create(au);
        Article article = new Article("New book", "Very interestinf book!", "New",
                au, null, null);
        article = as.create(article);

        Article newArticle = new Article("New book2", "Very interesting book!", "New",
                au, article, null);
        newArticle = as.create(newArticle);

        assertTrue(newArticle.getParentArticle().equals(article));

        as.delete(article.getId());
        as.delete(newArticle.getId());


    }

}
