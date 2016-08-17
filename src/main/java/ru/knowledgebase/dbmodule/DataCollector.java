package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.dbmodule.dataservices.ArticleService;
import ru.knowledgebase.dbmodule.dataservices.TokenService;
import ru.knowledgebase.dbmodule.dataservices.UserService;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;

/**
 * Created by root on 16.08.16.
 */
public class DataCollector {
    private ArticleService articleService;
    private TokenService tokenService;
    private UserService userService;

    public DataCollector() {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        articleService = (ArticleService) context.getBean("articleService");
        tokenService = (TokenService) context.getBean("tokenService");
        userService = (UserService) context.getBean("userService");
    }

    public boolean addUser(User user) throws Exception{
        return userService.create(user);
    }

    // to add
    public boolean addToken(Token token) throws Exception{
        tokenService.create(token);
        return true;
    }

    public void updateToken(Token token) throws Exception{
        tokenService.update(token);
    }

    public User findUserByCredentials(String login, String password) throws Exception{
        return userService.findByCredentials(login, password);
    }

    public Token getUserToken(User user) throws Exception{
        return tokenService.getUserToken(user);
    }



}
