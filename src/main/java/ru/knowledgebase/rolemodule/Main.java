package ru.knowledgebase.rolemodule;

import org.apache.commons.codec.digest.DigestUtils;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.commentmodule.CommentController;
import ru.knowledgebase.configmodule.Configurations;
import ru.knowledgebase.convertermodule.ArticleConverter;

import ru.knowledgebase.ldapmodule.LdapWorker;
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
       // UserController.getInstance().register("ttttt", "fffff");
        //UserController.getInstance().authorize("ttttt", "fffff");
        //ArticleController c = ArticleController.getInstance();
      //  c.addBaseArticle("sd", "ds", 1, new LinkedList <String>());
       // createTest();
      //  System.out.println(LdapWorker.getInstance().getUserInfo("tttttt").getEmail());
        //LdapWorker.getInstance().createUser("cc3c", DigestUtils.md5Hex("cc3c"));
//        System.out.println(Configurations.getLogFilePath());
     //   GlobalRoleController.getInstance().createBaseRoles();
     //   ArticleRoleController.getInstance().createBaseRoles();
    //    LdapWorker.getInstance().getUserInfo("dssddssdsa");
        UserController.getInstance().register("228user", "1pass", "t1@m",
                                                            "vov", "vas", "fam", "off", "222", "333", null, null);
        UserController.getInstance().authorize("228user", "1pass");
        UserController.getInstance().delete(1);
    }


    public static void createTest() throws Exception{
        //UserController.getInstance().register("1user", "1pass", "t1@m",
    //                                            "vov", "vas", "fam", "off", "222", "333", null, null);
    //    UserController.getInstance().register("2user", "2pass", "t1@m",
       //         "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null);
      //  ArticleRole role = new ArticleRole();
      //  role.setName("SectionAdmin");
      //  role.setCanViewMistakes(true);
      //  ArticleRoleController.getInstance().create(role);
      //  ArticleController.getInstance().addBaseArticle("1", "1", 1, new LinkedList<String>());
     //   ArticleController.getInstance().addArticle("2", "2", 1, 1,false, new LinkedList<String>());
     //   ArticleController.getInstance().addArticle("3", "3", 1, 2, true, new LinkedList<String>());
    //    ArticleController.getInstance().addArticle("4", "4", 1, 3, false, new LinkedList<String>());
     //   ArticleRoleController.getInstance().assignUserRole(2, 2, 1);

    }
}
