package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.convertermodule.ArticleConverter;
import ru.knowledgebase.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.dbmodule.dataservices.ArticleService;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;
import ru.knowledgebase.rolemodule.ArticleRoleController;
import ru.knowledgebase.usermodule.UserController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Program2 {

    private static final int MAX_USER_NUMBER = 10;
    private static final int MAX_ART_NUMBER = 100;
    private static Random rand = new Random();

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        ArticleService service = (ArticleService) context.getBean("articleService");
        /*

        List<Article> list = getArticles(users);
        for (Article a : list)
            service.create(a);*/

       // List<User> users = generateUsers();
       // UserService service2 = (UserService) context.getBean("userService");
        //for (User u : users)
          //  System.out.println(service2.create(u));

     //   UserController contr = new UserController();
     //   contr.authorize("user0", "user0");
       // RegisterController.register("userNew", "userNew");
       // RegisterController.register("userNew", "userNew");
        DataCollector d = new DataCollector();
        try {
            Article a = new Article(1);
            a.setTitle("fds");
          //  service.create(a);

            ArticleRole r = new ArticleRole("User");
            r.setCanViewArticle(true);
            r.setCanSearch(true);
            r.setCanAddMistakes(true);
            r.setCanViewMistakes(true);

           // UserController.register("tt5", "tt");
           // UserController.register("tt6", "tt");
    //        LdapController.getInstance().deleteRole("User");
      //      System.out.println(LdapController.getInstance().isRoleExists("User"));
            //   UserController.changeLogin("tt", "tt4");

           // ArticleRoleController.createRole(r);

        //     ArticleRoleController.updateRole(2, r);
            //UserEditController.changePassword("testrole7", "passsss");

          //
            // UserEditController.changePassword("testrole7", "11111");
           // LdapController.getInstance().createRole("testrolesss.");


        //    service.create(a);
         //   createRoles();
         // UserController.register("testrole19", "testrole15");
       //     ArticleRoleController.assignUserRole(4, 1, 1);
       //  UserController.authorize("testrole19", "testrole15");
          // UserController.delete("testrole19");
           // ArticleRole res = ArticleRoleController.findUserRole(4, 1);
           // System.out.println(res.getName());


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void convertFolder(){
        File folder = new File("/home/vova/Project BZ/documents");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    System.out.println("File " + listOfFiles[i].getName());
                    String name = listOfFiles[i].getName();
                    InputStream is = new FileInputStream( "/home/vova/Project BZ/documents/" + name);
                    if (name.contains(".docx")) {
                        name = name.replace(".docx", "");
                        ArticleConverter.getInstance().convert(is, "/home/vova/Project BZ/documents/" + name + "/" + name + ".html");
                    }
                    if (name.contains(".doc")) {
                        name = name.replace(".doc", "");
                        File file = new File("/home/vova/Project BZ/documents/" + name);
                        if (!file.exists()){
                            file.mkdir();
                        }
                        file = new File("/home/vova/Project BZ/documents/" + name + "/" + name+  ".html");
                        if (!file.exists()){
                            file.createNewFile();
                        }
                        ArticleConverter.getInstance().convertDoc(is,  "/home/vova/Project BZ/documents/" + name + "/" + name+  ".html");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<User> generateUsers() {
        List<User> users = new LinkedList<User>();
        for (int i = 0 ; i < MAX_USER_NUMBER; i++) {
            users.add(new User("user" + new Integer(i).toString()));
        }
        return users;
    }


    public static void createRoles() throws Exception{
        DataCollector coll = new DataCollector();

        GlobalRole gr = new GlobalRole("SuperUser");
        gr.setCanAddUser(true);
        gr.setCanEditUser(true);
        gr.setCanDeleteUser(true);
        gr.setCanEditUserRoles(true);

        coll.addGlobalRole(gr);

        gr = new GlobalRole("User");
        gr.setCanAddUser(false);
        gr.setCanEditUser(false);
        gr.setCanDeleteUser(false);
        gr.setCanEditUserRoles(false);

        coll.addGlobalRole(gr);

        ArticleRole r = new ArticleRole("SuperUser");

        r.setCanAddArticles(true);
        r.setCanEditArticle(true);
        r.setCanDeleteArticle(true);
        r.setCanViewArticle(true);

        r.setCanSearch(true);
        r.setCanAddNews(true);
        r.setCanAddMistakes(true);
        r.setCanViewMistakes(true);
        r.setCanGetReports(true);
        r.setCanOnOffNotifications(true);

        coll.addArticleRole(r);

        r = new ArticleRole("User");

        r.setCanViewArticle(true);
        r.setCanSearch(true);
        r.setCanAddMistakes(true);
        r.setCanViewMistakes(true);

        coll.addArticleRole(r);

    }

    public static List<Article> getArticles(List<User> users) {
        List<Article> arts = new LinkedList<Article>();
        for (int i = 0; i < MAX_ART_NUMBER; i++) {
            arts.add(new Article(new Integer(rand.nextInt(MAX_USER_NUMBER)).toString(),
                    new Integer(rand.nextInt(MAX_USER_NUMBER)).toString(),
                    users.get(rand.nextInt(users.size()))));
        }
        return arts;
    }


}