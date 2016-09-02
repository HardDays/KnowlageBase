package ru.knowledgebase.rolemodule;

import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.commentmodule.CommentController;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;
import ru.knowledgebase.usermodule.UserController;
import ru.knowledgebase.wrappermodule.CommentWrapper;
import ru.knowledgebase.wrappermodule.UserWrapper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vova on 31.08.16.
 */
public class Main {
    public static void main(String [] argv) throws Exception{
    //    createTest();
        CommentController.getInstance().add(1, 2, "test", "test");
        //ArticleRoleController.getInstance().assignUserRole(2, 2, 1);

    }


    public static void createTest() throws Exception{
        UserController.getInstance().register("test11", "test11");
        UserController.getInstance().register("test12", "test12");
        ArticleRole role = new ArticleRole();
        role.setName("SectionAdmin");
        role.setCanViewMistakes(true);
        ArticleRoleController.getInstance().create(role);
        ArticleController.getInstance().addBaseArticle("1", "1", 1, new LinkedList<String>());
        ArticleController.getInstance().addArticle("2", "2", 1, 1,false, new LinkedList<String>());
        ArticleController.getInstance().addArticle("3", "3", 1, 2, true, new LinkedList<String>());
        ArticleController.getInstance().addArticle("4", "4", 1, 3, false, new LinkedList<String>());
        ArticleRoleController.getInstance().assignUserRole(2, 2, 1);

    }
}
