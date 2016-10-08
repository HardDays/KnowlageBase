package ru.knowledgebase.dbmodule;

import jdk.internal.util.xml.impl.Pair;
import jdk.nashorn.internal.runtime.ECMAException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.configmodule.Configurations;
import ru.knowledgebase.dbmodule.dataservices.archiveservice.ArchiveArticleService;
import ru.knowledgebase.dbmodule.dataservices.articleservice.ArticleService;
import ru.knowledgebase.dbmodule.dataservices.commentservice.CommentService;
import ru.knowledgebase.dbmodule.dataservices.imageservice.ImageService;
import ru.knowledgebase.dbmodule.dataservices.newsservice.NewsService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.ArticleRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.GlobalRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.UserArticleRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.UserGlobalRoleService;
import ru.knowledgebase.dbmodule.dataservices.searchservices.SearchService;
import ru.knowledgebase.dbmodule.storages.LocalStorage;
import ru.knowledgebase.exceptionmodule.analyticsexceptions.ParseKeywordException;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.articlemodels.News;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.imagemodels.Image;

import java.awt.*;
import java.sql.Timestamp;
import java.util.*;
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
    private NewsService newsService;
    private CommentService commentService;
    private SearchService searchService;

    private final int BASE_ARTICLE = -1;

    private LocalStorage localStorage = null;

    private static DataCollector instance;

    /**
     * Controller as thread-safe singleton
     * @return
     */
    public static DataCollector getInstance() {
        DataCollector localInstance = instance;
        if (localInstance == null) {
            synchronized (DataCollector.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataCollector();
                }
            }
        }
        return localInstance;
    }

    //BEGIN PRIVATE METHODS

    private DataCollector(){
        ApplicationContext context = Configurations.getApplicationContext();
        articleService = (ArticleService) context.getBean("articleService");
        tokenService = (TokenService) context.getBean("tokenService");
        userService = (UserService) context.getBean("userService");
        imageService = (ImageService) context.getBean("imageService");
        articleRoleService = (ArticleRoleService) context.getBean("articleRoleService");
        globalRoleService = (GlobalRoleService) context.getBean("globalRoleService");
        userArticleRoleService = (UserArticleRoleService) context.getBean("userArticleRoleService");
        userGlobalRoleService = (UserGlobalRoleService) context.getBean("userGlobalRoleService");
        archiveArticleService = (ArchiveArticleService) context.getBean("archiveArticleService");
        newsService = (NewsService)context.getBean("newsService");
        commentService = (CommentService) context.getBean("commentService");
        searchService = (SearchService) context.getBean("searchService");

        try {
            initLocalStorage();
        }
        catch (Exception ex) {
            //TODO: handle exception
        }
    }

    //KOSTILI MODE ON (exceptions problem)
    private void initLocalStorage() throws Exception {
        localStorage = LocalStorage.getInstance();
        localStorage.initSectionMapStorage(this.initSectionStorage());
        localStorage.initSectionRoleStorage(this.initSectionRoleStorage());
    }

    private List<UserArticleRole> initSectionRoleStorage() throws Exception {
        return userArticleRoleService.getAll();
    }

    private HashMap<Integer, LinkedList<Integer>> initSectionStorage() throws Exception {
        HashMap<Integer, LinkedList<Integer>> sections = new HashMap<>();

        Article base = this.getBaseArticle();
        if (base == null) {
            return sections;
        }
        Queue<Integer> queue = new LinkedList<>();
        queue.add(base.getId());
        while(!queue.isEmpty()) {
            Integer current = queue.poll();
            List<Integer> got = this.getNextLevelSectionsIds(current);
            sections.put(current, (LinkedList) got);
            queue.addAll(got);
        }

        return sections;
    }
    //KOSTILI MODE OFF


    //END PRIVATE METHODS

    //BEGIN ARTICLE CRUD METHODS

    public Article getBaseArticle() throws Exception {
        Article base = localStorage.getBaseArticle();
        if (base == null) {
            base = articleService.getBaseArticle();
            localStorage.setBaseArticle(base);
        }
        return base;
    }

    public Article findArticle(int articleId) throws Exception {
        if (localStorage != null) {
            Article cached = localStorage.getSectionFromCache(articleId);
            if (cached != null) {
                return cached;
            }
        }
        return articleService.findById(articleId);
    }

    public Article addArticle(Article article) throws Exception {
        Article saved = articleService.create(article);
        if (saved.isSection()) {
            localStorage.addSection(saved);
        }
        return saved;
    }

    /**
     * Get tree of section by root section (@id).
     * We need to delete all sections under root section and root section
     * from list of section of its parent
     * @param id
     * @throws Exception
     */
    public void deleteArticle(Integer id) throws Exception {
        Article art = this.findArticle(id);
        if(art.isSection()) {
            List<Article> articles = this.getSectionTree(id);
            for (Article i : articles) {
                deleteAllNewsBySection(i.getId());
            }
            Integer parentId = art.getParentArticle();
            if (parentId == BASE_ARTICLE) {
                parentId = null;
            }
            localStorage.deleteSections(parentId, articles);
        }
        articleService.delete(id);
    }

    public void deleteAllArticles(List<Integer> ids) throws Exception{
        for (Integer i : ids) {
            this.deleteArticle(i);
        }
    }


    public Article updateArticle(Article article) throws Exception {
        localStorage.updateArticleInCache(article);
        return articleService.update(article);
    }

    /**
     *
     * @param articleId
     * @param from
     * @param to
     * @return
     * @throws Exception
     */
    public List<Article> getChildren(int articleId, int from, int to) throws Exception {
        List<Integer> children = articleService.getChildrenIds(articleId);
        if (to > children.size()) {
            to = children.size();
        }
        if (from < to) {
            children = children.subList(from, to);
        }
        List<Article> articles = new LinkedList<>();
        for (Integer i : children) {
            articles.add(findArticle(i));
        }
        return articles;
    }

    public List<Integer> getChildrenIds(int articleId) throws Exception {
        return articleService.getChildrenIds(articleId);
    }

    public Article getParentArticle(Article article) throws Exception {
        if (article.getParentArticle() == BASE_ARTICLE)
            return article;
        return findArticle(article.getParentArticle());
    }

    public List<Integer> getArticleHierarchyTree(int article) throws Exception {
        return articleService.getArticleHierarchyTree(article);
    }

    public Iterable<Article> findArticleByTitle(String title) throws Exception {
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

    public void deleteArticleRole(int id) throws Exception {
        articleRoleService.delete(id);
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
        localStorage.add(role.getUser().getId(), role.getArticle().getId());
    }

    public HashSet<Integer> getUserSections(int userId) throws Exception{
        return localStorage.getSections(userId);
    }

    public void deleteUserArticleRole(UserArticleRole role) throws Exception{
        userArticleRoleService.delete(role);
    }

    public void deleteUserArticleRole(int id) throws Exception{
        userArticleRoleService.delete(id);
    }

    public UserArticleRole findUserArticleRole(User user, Article article) throws Exception{
        return userArticleRoleService.find(user, article);
    }

    public List <User> findMistakeViewers(Article article) throws Exception{
        return userArticleRoleService.findMistakeViewers(article);
    }

    public List <UserArticleRole> findUserArticleRoleByArticle(int articleId){
        return userArticleRoleService.findByArticle(articleId);
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

    public void deleteGlobalRole(int id) throws Exception{
        globalRoleService.delete(id);
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

    public void deleteUserGlobalRole(int id) throws Exception{
        userGlobalRoleService.delete(id);
    }

    //END USERGLOBALROLE METHODS

    //BEGIN IMAGE CRUD METHODS
    public Image findImage(String id) throws  Exception {
        return imageService.find(id);
    }

    public Image addImage(Image image) throws Exception {
        return imageService.create(image);
    }

    public void deleteImage(String id) throws  Exception {
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

    public List<Image> addAllImages() throws Exception {
        return imageService.getAllImages();
    }
    //END IMAGE CRUD METHODS

    //BEGIN ARCHIVE CRUD METHODS
    public void addAllToArchive(List<ArchiveArticle> archArticles) throws Exception {
        archiveArticleService.createAll(archArticles);
    }

    public void deleteArchiveArticle(int id) throws Exception {
        archiveArticleService.delete(id);
    }

    public List<ArchiveArticle> getSectionArchive(int sectionId) throws Exception {
        return archiveArticleService.getSectionArchive(sectionId);
    }

    public ArchiveArticle getArchiveArticle(int archiveArticleId) throws Exception {
        return archiveArticleService.findById(archiveArticleId);
    }

    //END ARCHIVE CRUD METHODS

    //START SECTION METHODS

    @Deprecated
    public Article getSectionByArticle(Article article) throws Exception {
        while (!article.isSection()) {
            article = getParentArticle(article);
        }
        return article;
    }

    /**
     * If local storage initializwd, get info from it.
     * Otherwise search for children in DB.
     * @param section
     * @return
     * @throws Exception
     */
    public List<Integer> getNextLevelSectionsIds(Integer section) throws Exception {
        if (localStorage != null) {
            List<Integer> sectionsId = localStorage.getNextLevelSection(section);
            return sectionsId;
        }
        else {
            List<Integer> children = this.getChildrenIds(section);
            List<Integer> sections = new LinkedList<>();
            for (Integer c : children) {
                if (this.findArticle(c).isSection()) {
                    sections.add(c);
                }
            }
            return sections;

        }
    }

    /**
     * Section on the next from current section level
     * @param section
     * @return
     * @throws Exception
     */
    public List<Article> getNextLevelSections(Integer section) throws Exception {
        List<Integer> sectionsId = this.getNextLevelSectionsIds(section);
        List<Article> sections = new LinkedList<>();
        for (Integer i : sectionsId) {
            sections.add(this.findArticle(i));
        }
        return sections;
    }


    /**
     * Tree of sections. Root is first
     * @param section
     * @return
     * @throws Exception
     */
    public List<Integer> getSectionTreeIds(Integer section) throws Exception {
        List<Integer> sectionTree = new LinkedList<>();
        if (this.findArticle(section).isSection()) {
            Queue<Integer> queue = new LinkedList<>();
            sectionTree.add(section);
            queue.add(section);
            while (!queue.isEmpty()) {
                Integer current = queue.poll();
                List<Integer> children = getChildrenIds(current);
                for (Integer i : children) {
                    if (!findArticle(i).isSection()) {
                        break;
                    }
                    else{
                        queue.add(i);
                        sectionTree.add(i);
                    }
                }
            }
        }
        return sectionTree;
    }

    /**
     * Tree of sections. Root is first
     * @param section
     * @return
     * @throws Exception
     */
    public List<Article> getSectionTree(Integer section) throws Exception {
        List<Article> sectionTree = new LinkedList<>();
        Queue<Article> queue = new LinkedList<>();
        Article root = this.findArticle(section);
        sectionTree.add(root);
        queue.add(root);
        while (!queue.isEmpty()) {
            Article current = queue.poll();
            List<Integer> children = getChildrenIds(current.getId());
            for (Integer i : children) {
                Article art = findArticle(i);
                if (!art.isSection()) {
                    break;
                }
                else {
                    queue.add(art);
                    sectionTree.add(art);
                }
            }
        }
        return sectionTree;
    }

    public HashMap<Integer, HashMap<Article, List<Article>>> getSectionHierarchy() throws Exception {
        Article root = getBaseArticle();
        HashMap<Integer, HashMap<Article, List<Article>>> sectionHierarchy = new HashMap<>();
        if (root == null) {
            return sectionHierarchy;
        }

        //Kostili:(
        HashMap<Integer, Integer> parentLevels = new HashMap<>();

        Queue<Article> queue = new LinkedList<>();
        parentLevels.put(root.getId(), 0);
        Article current;
        List<Article> levelArticles = null;
        int level = 0;
        HashMap<Article, List<Article>> levelMap = new HashMap<>();
        levelArticles = getNextLevelSections(root.getId());
        queue.addAll(levelArticles);

        while(!queue.isEmpty()) {
            current = queue.poll();
            level = parentLevels.get(current.getParentArticle()) + 1;

            //Kostili:(
            levelArticles = getChildren(current.getId(), 1, 0);

            levelMap = new HashMap<>();
            if (!levelArticles.isEmpty() && levelArticles.get(0).isSection() == false) {
                levelArticles = new LinkedList<>();
            }
            levelMap.put(current, levelArticles);
            queue.addAll(levelArticles);
            parentLevels.put(current.getId(), level);
            if (sectionHierarchy.get(level) == null)
                sectionHierarchy.put(level, levelMap);
            else {
                HashMap<Article, List<Article>> newMap = new HashMap<>();
                newMap.putAll(sectionHierarchy.get(level));
                newMap.putAll(levelMap);
                sectionHierarchy.put(level, newMap);
            }
        }
        return sectionHierarchy;
    }

    public boolean isArticleSection(int articleId) throws Exception {
        return articleService.isSection(articleId);
    }


    //END SECTION METHODS

    //BEGIN NEWS METHODS

    public News findNews(int id) throws Exception {
        return newsService.findNews(id);
    }

    public void deleteNews(int id) throws Exception {
        newsService.deleteNews(id);
    }

    public News addNews(News news) throws Exception {
        return newsService.addNews(news);
    }

    public List<News> getNewsBySection(int section) throws Exception{
        return newsService.getAllNewsBySection(section);
    }

    public void deleteAllNewsBySection(int section) throws Exception {
        newsService.deleteAllBySection(section);
    }

    public News updateNews(News news) throws Exception{
        return newsService.updateNews(news);
    }

    public List<News> getSectionNewsFromDate(Integer i, Timestamp date) throws Exception {
        return newsService.getSectionNewsFromDate(i, date);
    }

    //END NEWS METHODS

    //BEGIN COMMENT CRUD METHODS
    public void addComment(Comment comment) throws Exception{
        commentService.create(comment);
    }

    public void updateComment(Comment comment) throws Exception{
        commentService.update(comment);
    }

    public Comment findComment(int id) throws Exception{
        return commentService.find(id);
    }

    public void deleteComment(int id) throws Exception{
        commentService.delete(id);
    }

    public List<Comment> findCommentsByAdmin(User admin) throws Exception{
        return commentService.findByAdmin(admin);
    }

    public void deleteComment(Comment comment) throws Exception{
        commentService.delete(comment);
    }
    //END COMMENT CRUD METHODS

    //BEGIN SEARCH METHODS
    public List<Article> searchByTitle(String searchRequest, List<Integer> sectionsAvailableToUser, int numArticles) throws ParseKeywordException {
        return searchService.searchByTitle(searchRequest, sectionsAvailableToUser, numArticles);
    }

    public List<Article> searchByBody(String searchRequest, List<Integer> sectionsAvailableToUser, int numArticles) throws ParseKeywordException {
        return searchService.searchByBody(searchRequest, sectionsAvailableToUser, numArticles);
    }

    //END SEARCH METHODS

}
