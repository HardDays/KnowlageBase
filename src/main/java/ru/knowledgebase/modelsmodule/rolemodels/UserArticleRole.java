package ru.knowledgebase.modelsmodule.rolemodels;

import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;

/**
 * Created by root on 17.08.16.
 */


@Entity
public class UserArticleRole {

    @Id
    @SequenceGenerator(name="user_article_role_id_seq",
            sequenceName="user_article_role_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="user_article_role_id_seq")
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    @ManyToOne
    private ArticleRole articleRole;

    public UserArticleRole(){

    }

    public UserArticleRole(User user, Article article, ArticleRole articleRole){
        this.user = user;
        this.articleRole = articleRole;
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public ArticleRole getArticleRole() {
        return articleRole;
    }

    public void setArticleRole(ArticleRole articleRole) {
        this.articleRole = articleRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
