package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.dbmodule.dataservices.*;
import ru.knowledgebase.dbmodule.dataservices.roleservices.ArticleRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.GlobalRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.UserArticleRoleService;
import ru.knowledgebase.dbmodule.dataservices.roleservices.UserGlobalRoleService;
import ru.knowledgebase.dbmodule.dataservices.userservices.TokenService;
import ru.knowledgebase.dbmodule.dataservices.userservices.UserService;
import ru.knowledgebase.modelsmodule.*;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by root on 16.08.16.
 */
public class DataCollector {
    private ArticleService articleService;
    private TokenService tokenService;
    private UserService userService;
    private ArticleRoleService articleRoleService;
    private GlobalRoleService globalRoleService;
    private UserArticleRoleService userArticleRoleService;
    private UserGlobalRoleService userGlobalRoleService;

    public DataCollector() {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        articleService = (ArticleService) context.getBean("articleService");
        tokenService = (TokenService) context.getBean("tokenService");
        userService = (UserService) context.getBean("userService");
        articleRoleService = (ArticleRoleService) context.getBean("articleRoleService");
        globalRoleService = (GlobalRoleService) context.getBean("globalRoleService");
        userArticleRoleService = (UserArticleRoleService) context.getBean("userArticleRoleService");
        userGlobalRoleService = (UserGlobalRoleService) context.getBean("userGlobalRoleService");

    }

    // to add

    public void addArticle(Article article) throws Exception{
        articleService.create(article);
    }

    public Article findArticle(int id) throws Exception{
        return articleService.find(id);
    }

    public void deleteArticle(Article article) throws Exception{
        articleService.delete(article);
    }

    public User addUser(User user) throws Exception{
        return userService.create(user);
    }

    public void addToken(Token token) throws Exception{
        tokenService.create(token);
    }

    public void updateToken(Token token) throws Exception{
        tokenService.update(token);
    }

    public void deleteToken(Token token) throws Exception{
        tokenService.delete(token);
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

    public Token getUserToken(User user) throws Exception{
        return tokenService.getUserToken(user);
    }

    public void deleteUser(User user) throws Exception{
        userService.delete(user);
    }

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

    public void addUserArticleRole(UserArticleRole role) throws Exception{
        userArticleRoleService.create(role);
    }

    public void deleteUserArticleRole(UserArticleRole role) throws Exception{
        userArticleRoleService.delete(role);
    }

    public UserArticleRole findUserArticleRole(User user, Article article) throws Exception{
        return userArticleRoleService.find(user, article);
    }

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

    public void addUserGlobalRole(UserGlobalRole role) throws Exception{
        userGlobalRoleService.create(role);
    }

    public UserGlobalRole findUserGlobalRole(User user) throws Exception{
        return userGlobalRoleService.find(user);
    }

    public void deleteUserGlobalRole(UserGlobalRole role) throws Exception{
        userGlobalRoleService.delete(role);

    }

}
