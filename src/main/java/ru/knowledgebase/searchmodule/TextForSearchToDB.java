package ru.knowledgebase.searchmodule;

import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.dbmodule.dataservices.searchservices.SearchService;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Мария on 18.09.2016.
 */
public class TextForSearchToDB {

    private static ImageController ic = new ImageController();
    private static DataCollector dc = new DataCollector();
    private static Integer parentArticle = 1;
    private static List<String> imgs = new LinkedList<String>();
    private static ArticleController ac = new ArticleController();

    private static User u;
    private static Article base;
    private static String body = "Body";
    private static Integer author = 1;
    private static String[] titles = {"First  title", "Second  title", "Third  title", "Fourth  title"};

    private static Article addArticle;

    public void createDB(){
        try {
            List<Article> articles = readArticles();
            init();
            createArticles(articles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws Exception{
        Image img = new Image("home/path");
        img = ic.addImage(img);
        imgs.add(img.getId());
        u = new User("TestUser", "123");
        u = dc.addUser(u);
        author = u.getId();
        base = ac.addBaseArticle("Base title", "Base body", u.getId(), new LinkedList<String>());
        parentArticle = base.getId();
}

    private void createArticles(List<Article> articles) {
        int count = 0;
        for (Article article: articles
             ) {
            try {
                createArticle(article);
                count++;
                System.out.println("Article " + article.getTitle() + " was created");
            } catch (Exception e) {
                System.out.println("Unable to crate article " + article.getTitle());
//                e.printStackTrace();
            }
        }
        System.out.println(count + " articles were added to DB");
    }

    private List<Article> readArticles() {
        File file = new File("text.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Article> articles = new LinkedList<>();
        while (sc.hasNextLine()){
            Article a = new Article();
            String line = sc.nextLine();
            if(line.isEmpty())
                line = sc.nextLine();
            a.setTitle(line);
            a.setBody(sc.nextLine()) ;
            articles.add(a);
        }
        System.out.println(articles.size() + " articles were read");
        return articles;
    }

    private void createArticle(Article a) throws Exception {
        addArticle = ac.addArticle(a.getTitle(), a.getBody(), u.getId(), parentArticle, new LinkedList<String>());
    }

    public static void main(String[] args) {
        TextForSearchToDB textForSearchToDB = new TextForSearchToDB();
        textForSearchToDB.createDB();
    }
}
