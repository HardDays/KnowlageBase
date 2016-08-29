package ru.knowledgebase.exceptionmodule.articleexceptions;

/**
 * Created by root on 29.08.16.
 */
public class ParentArticleNotFoundException extends Exception {
    public ParentArticleNotFoundException(int parentArticle) {
        super(new Integer(parentArticle).toString());
    }

    public ParentArticleNotFoundException(){}
}
