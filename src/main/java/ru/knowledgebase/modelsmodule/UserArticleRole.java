package ru.knowledgebase.modelsmodule;

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

    @OneToOne(cascade = {CascadeType.MERGE})
    private User user;

    @OneToOne(cascade = {CascadeType.MERGE})
    private Article article;

    @OneToOne(cascade = {CascadeType.MERGE})
    private ArticleRole articleRole;

    public UserArticleRole(){

    }

    public UserArticleRole(User user, ArticleRole articleRole, Article article){
        this.user = user;
        this.articleRole = articleRole;
        this.article = article;
    }

}
