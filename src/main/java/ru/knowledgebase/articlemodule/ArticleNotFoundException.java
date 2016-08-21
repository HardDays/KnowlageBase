package ru.knowledgebase.articlemodule;

/**
 * Created by vova on 21.08.16.
 */
public class ArticleNotFoundException extends Exception {
    public ArticleNotFoundException(){
        super("Article not found!");
    }
}
