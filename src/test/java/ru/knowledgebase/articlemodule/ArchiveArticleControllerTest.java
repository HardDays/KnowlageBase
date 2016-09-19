package ru.knowledgebase.articlemodule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.knowledgebase.archivemodule.ArchiveArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.sql.Timestamp;

import static org.junit.Assert.assertTrue;

/**
 * Created by root on 18.09.16.
 */
public class ArchiveArticleControllerTest {

    private static DataCollector dc = new DataCollector();
    private static String title = "Title";
    private static String body = "Body";
    private static Integer author = 1;
    private static Integer parentArticle = 1;

    private static ArticleController ac = new ArticleController();
    private static ArchiveArticleController archiveArticleController = new ArchiveArticleController();

    private static User u;
    private static Article base;

    private static Article createTest;
    private static Article updateArticle;

    @BeforeClass
    public static void init() throws Exception{
        u = new User("TestUser", "123");
        u = dc.addUser(u);
        author = u.getId();

        base = ac.addBaseArticle("1", "2", u.getId(), new Timestamp(5));

        parentArticle = base.getId();
        updateArticle = ac.addArticle(title, body, u.getId(), parentArticle, new Timestamp(5), false);
    }

    @AfterClass
    public static void clear() throws Exception{
        ac.deleteArticle(base.getId());

        dc.deleteUser(u.getId());
    }

    @Test
    public void moveToArchive() throws Exception {
        Article uArticle1 = ac.addArticle(title, body, u.getId(), updateArticle.getId(), new Timestamp(5), false);
        Article uArticle2 = ac.addArticle(title, body, u.getId(), updateArticle.getId(), new Timestamp(5), false);

        archiveArticleController.moveToArchive(updateArticle);

        for (ArchiveArticle aa : archiveArticleController.getSectionArchive(base.getId())){
            archiveArticleController.deleteArchiveArticle(aa.getId());
        }
    }

    @Test
    public void movingListTest() throws Exception {
        Article newArticle1 = ac.addArticle("TimeListTest", body, u.getId(), base.getId(), new Timestamp(5), false);

        archiveArticleController.addArchivationTime(newArticle1);
        archiveArticleController.archiveNext();

        assertTrue(ac.findArticleByTitle("TimeListTest").size() == 0);

        for (ArchiveArticle aa : archiveArticleController.getSectionArchive(base.getId())){
            archiveArticleController.deleteArchiveArticle(aa.getId());
        }
    }

}