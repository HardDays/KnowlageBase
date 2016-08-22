package ru.knowledgebase.modelsmodule.rolemodels;

import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;
import java.io.Serializable;

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

    @OneToOne
    private User user;

    @OneToOne
    private Article article;

    @OneToOne
    private ArticleRole articleRole;

    public UserArticleRole(){

    }

    public UserArticleRole(User user, ArticleRole articleRole, Article article){
        this.user = user;
        this.articleRole = articleRole;
        this.article = article;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
