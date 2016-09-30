package ru.knowledgebase.rolemodule;

import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.commentmodule.CommentController;
import ru.knowledgebase.convertermodule.ArticleConverter;
import ru.knowledgebase.loggermodule.Client.DataToLogProvider;
import ru.knowledgebase.loggermodule.Log.LogWriter;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.LogRecord.CRUDRecord;
import ru.knowledgebase.loggermodule.LogRecord.LogRecordFactory;
import ru.knowledgebase.loggermodule.Logger;
import ru.knowledgebase.loggermodule.Server.DataProvider;
import ru.knowledgebase.loggermodule.logenums.OPERATION;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.usermodule.UserController;
import ru.knowledgebase.wrappermodule.CommentWrapper;
import ru.knowledgebase.wrappermodule.UserWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vova on 31.08.16.
 */
public class Main {
    public static void main(String [] argv) throws Exception{
       // createTest();
       // CommentController.getInstance().add(1, 2, "test", "test");
        //ArticleRoleController.getInstance().assignUserRole(2, 2, 1);
     //   DataToLogProvider log = DataToLogProvider.getInstance();
      //  log.startProvider();
       // InputStream in = new FileInputStream(new File("/home/vova/Project BZ/trash/docx/e.doc"));
       // ArticleConverter.getInstance().convertDoc(in, "testdddfffe", 1, 1, true);
       // UserWrapper w = new UserWrapper();
       // w.authorize("dssdsd", "asas");
      //  UserController.getInstance().register("ttttt", "fffff");
        //UserController.getInstance().authorize("ttttt", "fffff");
    }


    public static void createTest() throws Exception{
        UserController.getInstance().register("test27", "test25");
        UserController.getInstance().register("test28", "test26");
        ArticleRole role = new ArticleRole();
        role.setName("SectionAdmin");
        role.setCanViewMistakes(true);
        ArticleRoleController.getInstance().create(role);
        ArticleController.getInstance().addBaseArticle("1", "1", 1, new LinkedList<String>());
        ArticleController.getInstance().addArticle("2", "2", 1, 1,false, new LinkedList<String>());
        ArticleController.getInstance().addArticle("3", "3", 1, 2, true, new LinkedList<String>());
        ArticleController.getInstance().addArticle("4", "4", 1, 3, false, new LinkedList<String>());
        ArticleRoleController.getInstance().assignUserRole(2, 2, 1);

    }
}
