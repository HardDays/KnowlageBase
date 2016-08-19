package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.modelsmodule.GlobalRole;
import ru.knowledgebase.modelsmodule.SectionRole;
import ru.knowledgebase.dbmodule.dataservices.ArticleService;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Program {

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


     //   AuthorizeController contr = new AuthorizeController();
     //   contr.authorize("user0", "user0");
       // RegisterController.register("userNew", "userNew");
       // RegisterController.register("userNew", "userNew");
        DataCollector d = new DataCollector();
        try {
            createRoles();
       //     d.addRole(new SectionRole("AddUser"));
        //    d.addRole(new SectionRole("DeleteUser"));
        }catch (Exception e){
            e.printStackTrace();
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

        SectionRole r = new SectionRole("SuperUser");

        r.setCanAddSection(true);
        r.setCanEditSection(true);
        r.setCanViewSection(true);
        r.setCanDeleteSection(true);

        r.setCanAddArticle(true);
        r.setCanEditArticle(true);
        r.setCanDeleteArticle(true);
        r.setCanViewArticle(true);

        r.setCanSearch(true);
        r.setCanAddNews(true);
        r.setCanAddMistakes(true);
        r.setCanViewMistakes(true);
        r.setCanGetReports(true);
        r.setCanOnOffNotifications(true);

        coll.addSectionRole(r);

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