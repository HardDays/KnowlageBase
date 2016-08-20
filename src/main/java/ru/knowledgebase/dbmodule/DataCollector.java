package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.dbmodule.dataservices.*;
import ru.knowledgebase.modelsmodule.*;

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
    public Article findArticleById(int id) throws Exception{
        return articleService.findById(id);
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

    public User findUserByLogin(String login) throws Exception{
        return userService.findByLogin(login);
    }

    public User findUserById(int id) throws Exception{
        return userService.findById(id);
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

    public List<ArticleRole> getArticleRoles() throws Exception{
        return articleRoleService.getAll();
    }

    public GlobalRole findGlobalRoleByName(String name) throws Exception{
        return globalRoleService.findByName(name);
    }

    public ArticleRole findArticleRoleByName(String name) throws Exception{
        return articleRoleService.findByName(name);
    }

    public void addGlobalRole(GlobalRole sectionRole) throws Exception{
        globalRoleService.create(sectionRole);
    }

    public List<GlobalRole> getGlobalRoles() throws Exception{
        return globalRoleService.getAll();
    }

    public void addUserArticleRole(UserArticleRole role) throws Exception{
        userArticleRoleService.create(role);
    }

    public void addUserGlobalRole(UserGlobalRole role) throws Exception{
        userGlobalRoleService.create(role);
    }


}
