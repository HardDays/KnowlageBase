package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.dbmodule.dataservices.*;
import ru.knowledgebase.dbmodule.models.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Program {

    private static final int MAX_USER_NUMBER = 10;
    private static final int MAX_ART_NUMBER = 100;
    private static Random rand = new Random();

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        MediaService service = (MediaService) context.getBean("storageService");
        List<Users> users = generateUsers();
        for (Users u : users) {
            service.save(u);
        }
        List<Article> list = getArticles(users);
        long b = System.nanoTime();
        for (Article a : list)
            service.save(a);
        long e = System.nanoTime();
        System.out.println(e-b * 10e9);

    }

    public static List<Users> generateUsers() {
        List<Users> users = new LinkedList<Users>();
        for (int i = 0 ; i < MAX_USER_NUMBER; i++) {
            users.add(new Users(new Integer(rand.nextInt(MAX_USER_NUMBER)).toString()));
        }
        return users;
    }


    public static List<Article> getArticles(List<Users> users) {
        List<Article> arts = new LinkedList<Article>();
        for (int i = 0; i < MAX_ART_NUMBER; i++) {
            arts.add(new Article(new Integer(rand.nextInt(MAX_USER_NUMBER)).toString(),
                    new Integer(rand.nextInt(MAX_USER_NUMBER)).toString(),
                    users.get(rand.nextInt(users.size()))));
        }
        return arts;
    }


}