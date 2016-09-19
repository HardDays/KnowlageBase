package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.dbmodule.dataservices.archiveservice.ArchiveArticleService;
import ru.knowledgebase.dbmodule.dataservices.articleservice.ArticleService;
import ru.knowledgebase.dbmodule.dataservices.imageservice.ImageService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.ArticleRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.GlobalRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.UserArticleRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.UserGlobalRoleService;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;

import java.util.LinkedList;
import java.util.List;

import ru.knowledgebase.dbmodule.dataservices.userservices.TokenService;
import ru.knowledgebase.dbmodule.dataservices.userservices.UserService;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;

/**
 * Created by root on 16.08.16.
 */
public class DataCollector {
    private ArticleService articleService;
    private TokenService tokenService;
    private UserService userService;
    private ImageService imageService;
    private ArticleRoleService articleRoleService;
    private GlobalRoleService globalRoleService;
    private UserArticleRoleService userArticleRoleService;
    private UserGlobalRoleService userGlobalRoleService;
    private ArchiveArticleService archiveArticleService;

    public DataCollector() {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        articleService = (ArticleService) context.getBean("articleService");
        tokenService = (TokenService) context.getBean("tokenService");
        userService = (UserService) context.getBean("userService");
        imageService = (ImageService) context.getBean("imageService");
        articleRoleService = (ArticleRoleService) context.getBean("articleRoleService");
        globalRoleService = (GlobalRoleService) context.getBean("globalRoleService");
        userArticleRoleService = (UserArticleRoleService) context.getBean("userArticleRoleService");
        userGlobalRoleService = (UserGlobalRoleService) context.getBean("userGlobalRoleService");
        archiveArticleService = (ArchiveArticleService) context.getBean("archiveArticleService");
    }

    //BEGIN ARTICLE CRUD METHODS

    public Article findArticle(int articleId) throws Exception {
        return articleService.findById(articleId);
    }

    public Article addArticle(Article article) throws Exception {
        return articleService.create(article);
    }

    public void deleteArticle(Integer id) throws Exception {
        articleService.delete(id);
    }

    public void deleteAllArticles(List<Integer> ids) {
        articleService.deleteAll(ids);
    }


    public Article updateArticle(Article article) throws Exception {
        return articleService.update(article);
    }

    public List<Article> getChildren(int articleId) throws Exception {
        return articleService.getChildren(articleId);
    }

    public Article getParentArticle(Article article) throws Exception {
        return findArticle(article.getParentArticle());
    }

    public List<Integer> getArticleHierarchyTree(int article) throws Exception {
        return articleService.getArticleHierarchyTree(article);
    }

    public Article getSectionByArticle(Article article) throws Exception {
        while (!article.isSection()) {
            article = getParentArticle(article);
        }
        return article;
    }

    public Article getSectionByArticleId(Integer id) throws Exception {
        Article article = findArticle(id);
        while (!article.isSection()) {
            article = getParentArticle(article);
        }
        return article;
    }

    public List<Article> findArticleByTitle(String title) {
        return articleService.findByTitle(title);
    }

    //END ARTICLE CRUD METHODS

    //BEGIN USER CRUD METHODS
    public User addUser(User user) throws Exception{
        return userService.create(user);
    }

    public void updateUser(User user) throws Exception{
        userService.update(user);
    }

    public User findUser(String login) throws Exception{
        return userService.find(login);
    }

    public User findUser(int id) throws Exception{
        return userService.find(id);
    }

    public List<User> getAllUsers() throws Exception{
        return userService.getAll();
    }

    public void deleteUser(User user) throws Exception{
        userService.delete(user);
    }

    public void deleteUser(int id) throws Exception {
        userService.delete(id);
    }
    //END USER CRUD METHODS

    //BEGIN TOKEN METHODS

    public void addToken(Token token) throws Exception{
        tokenService.create(token);
    }

    public void updateToken(Token token) throws Exception{
        tokenService.update(token);
    }

    public void deleteToken(Token token) throws Exception{
        tokenService.delete(token);
    }

    public Token getUserToken(User user) throws Exception{
        return tokenService.getUserToken(user);
    }
    //END TOKEN METHODS

    //BEGIN ARTICLEROLE METHODS

    public void addArticleRole(ArticleRole articleRole) throws Exception{
        articleRoleService.create(articleRole);
    }

    public void updateArticleRole(ArticleRole role) throws Exception {
        articleRoleService.update(role);
    }

    public void deleteArticleRole(ArticleRole role) throws Exception {
        articleRoleService.delete(role);
    }

    public List<ArticleRole> getArticleRoles() throws Exception{
        return articleRoleService.getAll();
    }

    public ArticleRole findArticleRole(String name) throws Exception{
        return articleRoleService.find(name);
    }

    public ArticleRole findArticleRole(int id) throws Exception{
        return articleRoleService.find(id);
    }

    //END ARTICLEROLE METHODS

    //BEGIN USERARTICLEROLE METHODS

    public void addUserArticleRole(UserArticleRole role) throws Exception{
        userArticleRoleService.create(role);
    }

    public void deleteUserArticleRole(UserArticleRole role) throws Exception{
        userArticleRoleService.delete(role);
    }

    public UserArticleRole findUserArticleRole(User user, Article article) throws Exception{
        return userArticleRoleService.find(user, article);
    }

    //END USERARTICLEROLE METHODS


    //BEGIN GLOBALROLE METHODS
    public void addGlobalRole(GlobalRole globalRole) throws Exception{
        globalRoleService.create(globalRole);
    }

    public void updateGlobalRole(GlobalRole globalRole) throws Exception{
        globalRoleService.update(globalRole);
    }

    public void deleteGlobalRole(GlobalRole globalRole) throws Exception{
        globalRoleService.delete(globalRole);
    }

    public GlobalRole findGlobalRole(String name) throws Exception{
        return globalRoleService.find(name);
    }

    public List<GlobalRole> getGlobalRoles() throws Exception{
        return globalRoleService.getAll();
    }

    public GlobalRole findGlobalRole(int id) throws Exception{
        return globalRoleService.find(id);
    }
    //END GLOBALROLE METHODS

    //BEGIN USERGLOBALROLE METHODS
    public void addUserGlobalRole(UserGlobalRole role) throws Exception{
        userGlobalRoleService.create(role);
    }

    public UserGlobalRole findUserGlobalRole(User user) throws Exception{
        return userGlobalRoleService.find(user);
    }

    public void deleteUserGlobalRole(UserGlobalRole role) throws Exception{
        userGlobalRoleService.delete(role);

    }
    //END USERGLOBALROLE METHODS

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
    public List<Image> getImages(List<String> imagesId) throws Exception {
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

    //BEGIN ARCHIVE CRUD METHODS
    public void addAllToArchive(List<ArchiveArticle> archArticles) throws Exception {
        archiveArticleService.createAll(archArticles);
    }

    public void deleteArchiveArticle(int id) {
        archiveArticleService.delete(id);
    }

    public List<ArchiveArticle> getSectionArchive(int sectionId) {
        return archiveArticleService.getSectionArchive(sectionId);
    }

    public ArchiveArticle getArchiveArticle(int archiveArticleId) {
        return archiveArticleService.findById(archiveArticleId);
    }

    //END ARCHIVE CRUD METHODS
}
