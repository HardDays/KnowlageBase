package ru.knowledgebase.searchmodule;

import org.hibernate.search.exception.EmptyQueryException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.dbmodule.dataservices.searchservices.SearchService;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Мария on 15.09.2016.
 */
public class SearchServiceTest {
    private static ImageController ic = new ImageController();
    private static DataCollector dc = new DataCollector();
    private static Integer parentArticle = 1;
    private static List<String> imgs = new LinkedList<String>();
    private static ArticleController ac = new ArticleController();
    private static SearchService searchService = SearchService.getInstance();

    private static User u;
    private static Article base;
    private static String body = "Body";
    private static Integer author = 1;
    private static String[] titles = {
            "Summary form only given, as follows right left no yes",
            "Simple tokenizer that splits the text stream on whitespace ",
            "Внимание! С 7 сентября Агентством транспорта Финляндии были введены ограничения скорости ",
            "Ориентировочное опоздание скоростных поездов Аллегро в указанный период отправления "};

    private static Article addArticle;

    @BeforeClass
    public static void init() throws Exception {

        u = new User("TestUser2", "8555");
        u = dc.addUser(u);
        author = u.getId();
        base = ac.addBaseArticle("Base title", "Base body", u.getId(), new LinkedList<String>());
        parentArticle = base.getId();
        createArticles();
    }

    @AfterClass
    public static void clear() throws Exception {
        ac.deleteArticle(base.getId());
        dc.deleteUser(u.getId());
    }

    private static void createArticles() throws Exception {
        for (String title : titles) {
            addArticle = ac.addArticle(title, body, u.getId(), parentArticle, false,new LinkedList<String>());
        }
    }

    @Test
    public void searchByTitleInRussian() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "Агентством транспорта Финляндии";
        List<Article> result = searchService.searchByTitle(searchString);
        assertFalse("Resulting list is null", result == null);
        String rightArticle = titles[2];
        boolean foundRightArticle = false;
        for (Article a : result
                ) {
            if(rightArticle.equals(a.getTitle())) foundRightArticle = true;
        }
        assertFalse("Right article was not found", foundRightArticle);
    }

    @Test
    public void searchByTitleInEnglish() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "tokenizer that splits";
        List<Article> result = searchService.searchByTitle(searchString);
        assertFalse("Resulting list is null", result == null);
        String rightArticle = titles[1];
        boolean foundRightArticle = false;
        for (Article a : result
                ) {
            if(rightArticle.equals(a.getTitle())) foundRightArticle = true;
        }
        assertFalse("Right article was not found", foundRightArticle);
    }

    @Test
    public void searchByTitleWithSpellingMistakes() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "tokкenizer tt sуыits";
        List<Article> result = searchService.searchByTitle(searchString);
        assertFalse("Resulting list is null", result == null);
        String rightArticle = titles[1];
        boolean foundRightArticle = false;
        for (Article a : result
                ) {
            if(rightArticle.equals(a.getTitle())) foundRightArticle = true;
        }
        assertFalse("Right article was not found", foundRightArticle);
    }

    @Test
    public void searchByTitleWithRightWordsSeparatedWithOthers() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "Ориентировочное аооаоао отаот лььо тоо лльо Аллегро";
        List<Article> result = searchService.searchByTitle(searchString);
        assertFalse("Resulting list is null", result == null);
        String rightArticle = titles[2];
        boolean foundRightArticle = false;
        for (Article a : result
                ) {
            if(rightArticle.equals(a.getTitle())) foundRightArticle = true;
        }
        assertFalse("Right article was not found", foundRightArticle);
    }

    @Test(expected = EmptyQueryException.class)
    public void searchByTitleWithWrongRequest(){
        System.out.println("Fuzzy search");
        String searchString = "!@#$%%^&*(\\){}:\"~";
        List<Article> result = searchService.searchByTitle(searchString);
        assertFalse("Resulting list is null", result != null);
    }

    @Test
    public void searchByBody() throws Exception {
        System.out.println("searchByBody");
        String searchString = "ИКТ";
        List<Article> result = searchService.searchByBody(searchString);
        assertFalse("Resulting list is null", result == null);
        String rightArticle = titles[2];
        boolean foundRightArticle = false;
        for (Article a : result
                ) {
            if(rightArticle.equals(a.getTitle())) foundRightArticle = true;
        }
        assertFalse("Right article was not found", foundRightArticle);
    }
}
