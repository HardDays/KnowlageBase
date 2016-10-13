package ru.knowledgebase.searchmodule;

import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.hibernate.search.exception.EmptyQueryException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.configmodule.Config;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.dbmodule.dataservices.searchservices.SearchService;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Мария on 15.09.2016.
 */
public class SearchServiceTest {
    private static ImageController ic = new ImageController();
    private static DataCollector dc = DataCollector.getInstance();
    private static Integer parentArticle = 1;
    private static List<String> imgs = new LinkedList<String>();
    private static ArticleController ac = new ArticleController();
    private static SearchService searchService = (SearchService) Config.getContext().getBean("searchService");;

    private static User u;
    private static Article base;
    private static String body = "Body";
    private static Integer author = 1;
    private static String[] titles = {
            "Summary form only given, as follows right left no yes",
            "Simple tokenizer that splits the text stream on whitespace ",
            "Внимание! С 7 сентября Агентством транспорта Финляндии были введены ограничения скорости ",
            "Ориентировочное опоздание скоростных поездов Аллегро в указанный период отправления "
    };

    private static Article addArticle;


    @BeforeClass
    public static void init() throws Exception {

        u = new User("TestUser", "123", "t1@m",
                "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null);
        u = dc.addUser(u);
        author = u.getId();
        base = ac.addBaseArticle("1", "2", u.getId(), new Timestamp(5), new Timestamp(5), new Timestamp(5));
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
            addArticle = ac.addArticle(title, body, u.getId(), parentArticle, new Timestamp(5), new Timestamp(5), new Timestamp(5), true);
        }
    }

    @Test
    public void searchByPartOfTitleInRussian() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "опоздание скоростных поездов Аллегро";
        List<Article> result = searchService.searchByTitle(searchString);
        checkResult(result, titles[3]);
    }

    @Test
    public void searchByPartOfTitleInRussianWithMistake() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "опозfание";
        List<Article> result = searchService.searchByTitle(searchString);
        checkResult(result, titles[3]);
    }

    @Test
    public void searchByPartOfTitleInRussianWithMissingWordsInPhrase() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "опоздание Аллегро";
        List<Article> result = searchService.searchByTitle(searchString);
        checkResult(result, titles[3]);
    }

    @Test
    public void searchByPartOfTitleInRussianWithPhraseSeparatedWithOtherWords() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "опоздание бла бла бла Аллегро";
        List<Article> result = searchService.searchByTitle(searchString);
        checkResult(result, titles[3]);
    }

    @Test
    public void searchByTitleInEnglish() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "tokenizer bla bla  splits";
        List<Article> result = searchService.searchByTitle(searchString);
        checkResult(result, titles[1]);
    }

    @Test
    public void searchByBody() throws Exception {
        System.out.println("searchByBody");
        String searchString = body;
        List<Article> result = searchService.searchByBody(searchString);
        checkResult(result, titles[0]);
    }

    @Test
    public void searchByTitleWith1SpellingMistake() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "torenizer";
        List<Article> result = searchService.searchByTitle(searchString);
        checkResult(result, titles[1]);
    }

    @Test
    public void searchByTitleWith2SpellingMistakes() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "toеeniнer";
        List<Article> result = searchService.searchByTitle(searchString);
        checkResult(result, titles[1]);
    }

    @Test
    public void searchByPartOfTitleInEnglishWithPhraseSeparatedWithOtherWords() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "Summary kjfbkj ksjdkjd kjbdksj follows";
        List<Article> result = searchService.searchByTitle(searchString);
        checkResult(result, titles[0]);
    }

    @Test(expected = SearchPhaseExecutionException.class)
    public void searchByTitleWithWrongRequest() throws Exception {
        System.out.println("Fuzzy search");
        String searchString = "!@#$%%^&*(\\){}:\"~";
        List<Article> rYesult = searchService.searchByTitle(searchString);
    }

    private void checkResult(List<Article> result, String rightArticle) {
        assertFalse("Resulting list is null", result.isEmpty());
        boolean foundRightArticle = false;
        for (Article a : result) {
            if(rightArticle.equals(a.getTitle())) foundRightArticle = true;
        }
        assertTrue("Right article was not found", foundRightArticle);
    }
}
