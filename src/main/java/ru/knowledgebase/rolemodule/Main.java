package ru.knowledgebase.rolemodule;

import ru.knowledgebase.articlemodule.ArticleController;

import ru.knowledgebase.configmodule.ConfigReader;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.dbmodule.repositories.userrepositories.UserRepository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.usermodule.UserController;
import ru.knowledgebase.wrappermodule.UserWrapper;

/**
 * Created by vova on 31.08.16.
 */
public class Main {
    public static void main(String [] argv) throws Exception {
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
        //ArticleController.getInstance().addArticle("a", "a", 1, 1, null, null, null, true);
        // ArticleController.getInstance().addArticle("b", "b", 1, 1, null, null, null, true);
        //   ArticleController.getInstance().addArticle("c", "c", 1, 33, null, null, null, true);
        //   ArticleController.getInstance().addArticle("e", "e", 1, 33, null, null, null, true);

        //UserController.getInstance().register("228user", "2pass", "t1@m",
        //               "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null);
        //  UserController.getInstance().authorize("228user", "2pass");
        //UserController.getInstance().delete("228user");
        //UserController c = UserController.getInstance();
        //c.authorize("testeeee2", "2");
        // c.delete(user2);
        //  GlobalRoleController.getInstance().createBaseRoles();
        //createTest();
        RoleController.getInstance().createBaseRoles();

    }

    public static void createTest() throws Exception{
        User user = UserController.getInstance().register("user337", "pass", "t1@m",
                                                "vov", "vas", "fam", "off", "222", "333", null, null, true, true, null);
        UserController.getInstance().register("user339", "pass", "t1@m",
                "rrr", "ttt", "aaaa", "ssss", "111", "444", null, null, true, true, null);
        RoleController.getInstance().createBaseRoles();
     //  User user = new User();
       // user.setId(116);
       Article b = ArticleController.getInstance().addBaseArticle("1", "1", user.getId(), null, null, null);
       ArticleController.getInstance().addArticle("2", "2", user.getId(), b.getId(), null, null, null, false);
        ArticleController.getInstance().addArticle("3", "3", user.getId(), b.getId(), null, null, null, false);
        ArticleController.getInstance().addArticle("4", "4", user.getId(), b.getId(), null, null, null, false);

    }
}
