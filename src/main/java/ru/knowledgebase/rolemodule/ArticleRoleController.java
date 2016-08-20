package ru.knowledgebase.rolemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.ArticleRole;
import ru.knowledgebase.modelsmodule.User;
import ru.knowledgebase.modelsmodule.UserArticleRole;

/**
 * Created by vova on 20.08.16.
 */
public class ArticleRoleController {
    public static void createRole(ArticleRole role) throws Exception{
        DataCollector coll = new DataCollector();
        coll.addArticleRole(role);
    }

    public static void assignUserRole(User user, ArticleRole role, Article article) throws Exception{
        DataCollector coll = new DataCollector();
        coll.addUserArticleRole(new UserArticleRole(user, role, article));
    }
}
