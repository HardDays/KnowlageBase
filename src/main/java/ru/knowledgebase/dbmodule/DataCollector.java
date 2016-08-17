package ru.knowledgebase.dbmodule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.knowledgebase.dbmodule.dataservices.ArticleService;
import ru.knowledgebase.dbmodule.dataservices.TokenService;
import ru.knowledgebase.dbmodule.dataservices.UserService;
import ru.knowledgebase.modelsmodule.User;

/**
 * Created by root on 16.08.16.
 */
public class DataCollector {
    private ArticleService as;
    private TokenService ts;
    private UserService us;

    public DataCollector() {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
        as = (ArticleService) context.getBean("articleService");
        ts = (TokenService) context.getBean("tokenService");
        us = (UserService) context.getBean("userService");
    }

    public boolean addUser(User user) {
        return us.create(user);
    }


}
