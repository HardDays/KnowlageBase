package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.dbmodule.dataservices.ArticleService;
import ru.knowledgebase.dbmodule.dataservices.ImageService;
import ru.knowledgebase.dbmodule.dataservices.UserService;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.Image;
import ru.knowledgebase.modelsmodule.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Program {

    private static final int MAX_USER_NUMBER = 10;
    private static final int MAX_ART_NUMBER = 100;
    private static Random rand = new Random();
    private static Article article;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        ArticleService service = (ArticleService) context.getBean("articleService");
        ImageService is = (ImageService) context.getBean("imageService");
        UserService us = (UserService) context.getBean("userService");

        User au = new User("Anton");
        au = us.create(au);
        //au = us.find(3);


        List<Image> img = new LinkedList<Image>();
        //img.add(is.find("d162d837-f0a7-42aa-a7fc-9d1e93b0dfd0"));
        img.add(new Image("hui"));

        img.set(0, is.create(img.get(0)));

        article = new Article("New book", "Very interesting book!", "New",
                au, null, img);

        article = service.create(article);

        List<Article> arts = service.getAll();

        for (Article a : arts) {
            System.out.println("============");
            System.out.println(a.getTitle());
            System.out.println(a.getAuthor().getLogin());
            for (Image i : a.getImages()) {
                System.out.println(i.getPath());
            }
            //service.delete(a.getId());
        }

        //service.delete(article.getId());



        /*
        List<User> users = generateUsers();
        List<Article> list = getArticles(users);
        for (Article a : list)
            service.create(a);

        List<Article> arts = service.getAll();
        System.out.println(arts.size());
        */

    }

    public static List<User> generateUsers() {
        List<User> users = new LinkedList<User>();
        for (int i = 0 ; i < MAX_USER_NUMBER; i++)
            users.add(new User(new Integer(rand.nextInt(MAX_USER_NUMBER)).toString()));
        return users;
    }


    public static List<Article> getArticles(List<User> users) {
        List<Article> arts = new LinkedList<Article>();
        for (int i = 0; i < MAX_ART_NUMBER; i++) {
            arts.add(new Article(new Integer(rand.nextInt(MAX_USER_NUMBER)).toString(),
                    new Integer(rand.nextInt(MAX_USER_NUMBER)).toString(),
                    new Integer(rand.nextInt(MAX_USER_NUMBER)).toString(),
                    users.get(rand.nextInt(users.size())), article, null));
        }
        return arts;
    }


}