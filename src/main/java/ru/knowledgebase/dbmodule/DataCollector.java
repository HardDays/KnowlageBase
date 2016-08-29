package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.dbmodule.dataservices.ArticleService;
import ru.knowledgebase.dbmodule.dataservices.ImageService;
import ru.knowledgebase.dbmodule.dataservices.TokenService;
import ru.knowledgebase.dbmodule.dataservices.UserService;
import ru.knowledgebase.exceptionmodule.imageexceptions.ImageNotFoundException;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.Image;
import ru.knowledgebase.modelsmodule.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class DataCollector {
    private ArticleService articleService;
    private TokenService tokenService;
    private UserService userService;
    private ImageService imageService;

    public DataCollector() {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        articleService = (ArticleService) context.getBean("articleService");
        tokenService = (TokenService) context.getBean("tokenService");
        userService = (UserService) context.getBean("userService");
        imageService = (ImageService) context.getBean("imageService");
    }

    //BEGIN ARTICLE CRUD METHODS

    public Article findArticle(int articleId) {
        return articleService.findById(articleId);
    }

    public Article addArticle(Article article) {
        return articleService.create(article);
    }

    public void deleteArticle(Integer id) {
        articleService.delete(id);
    }

    public Article updateArticle(Article article) {
        return articleService.update(article);
    }
    //END ARTICLE CRUD METHODS

    //BEGIN USER CRUD METHODS
    public User findUser(int id) {
        return userService.find(id);
    }

    public User addUser(User user) {
        return userService.create(user);
    }

    public void deleteUser(int id) {
        userService.delete(id);
    }
    //END USER CRUD METHODS

    //BEGIN IMAGE CRUD METHODS
    public Image findImage(String id){
        return imageService.find(id);
    }

    public Image addImage(Image image) {
        return imageService.create(image);
    }

    public void deleteImage(String id) {
        imageService.delete(id);
    }

    /**
     * Atantione!!! Kostil' detected!
     * Return list of images by list of ids
     * @param imagesId
     * @return list of images
     */
    public List<Image> getImages(List<String> imagesId) {
        List<Image> images = new LinkedList<Image>();
        for (String id : imagesId) {
            Image img = findImage(id);
            if (img != null) {
                images.add(img);
            }
        }
        return images;
    }
    //END IMAGE CRUD METHODS
}
