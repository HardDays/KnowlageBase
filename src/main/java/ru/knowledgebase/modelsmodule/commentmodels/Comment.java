package ru.knowledgebase.modelsmodule.commentmodels;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vova on 01.09.16.
 */

@Entity
public class Comment {
    @Id
    @SequenceGenerator(name="comment_id_seq",
            sequenceName="comment_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="comment_id_seq")
    private int id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String articleText;

    @ManyToOne
    private User commentator;

    @ManyToOne
    private User admin;

    @ManyToOne
    private Article article;

    public Comment(){

    }

    public Comment(User commentator, User admin, Article article, String comment, String articleText){
        this.comment = comment;
        this.articleText = articleText;
        this.commentator = commentator;
        this.admin = admin;
        this.article = article;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public User getCommentator() {
        return commentator;
    }

    public void setCommentator(User commentator) {
        this.commentator = commentator;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

}
