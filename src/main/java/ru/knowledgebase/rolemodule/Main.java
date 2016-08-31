package ru.knowledgebase.rolemodule;

/**
 * Created by vova on 31.08.16.
 */
public class Main {
    public static void main(String [] argv) throws Exception{
        //ArticleRoleController.getInstance().assignUserRole(1, 1, 2);
        System.out.println(ArticleRoleController.getInstance().findUserRole(1, 4).getName());
    }
}
