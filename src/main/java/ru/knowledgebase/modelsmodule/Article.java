package ru.knowledgebase.modelsmodule;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 10.08.16.
 */
@Entity
public class Article {

    @Id
    @SequenceGenerator(name="article_id_seq",
            sequenceName="article_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="article_id_seq")
    private int id;

    @Column(length = 256)
    private String title;

    @Column
    private String body;

    @ManyToOne
    private User author;

    @Column
    private String clearBody;

    //*
    @ManyToOne
    private Article parentArticle;
    //*/

    @OneToMany(mappedBy="parentArticle")
    @Cascade({CascadeType.ALL})
    private List<Article> children;

    @OneToMany(mappedBy="article")
    @Cascade({CascadeType.ALL})
    List<Image> images;

    //BEGIN CONSTRUCTORS
    public Article(){}

    public Article(int id) {
        this.id = id;
    }

    public Article(String title, String body, String clearBody,
                   User author, Article parentArticle, List<Image> images) {
        this.title         = title;
        this.body          = body;
        this.clearBody     = clearBody;
        this.author        = author;
        this.parentArticle = parentArticle;
        if (images != null) {
            this.images = images;
        }
        else {
            this.images = new LinkedList<Image>();
        }
    }

    //END CONSTRUCTORS


    //BEGIN SG METHODS

    public User getAuthor() {
        return author;
    }

    public List<Image> getImages() {
        return images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClearBody() {
        return clearBody;
    }

    public void setClearBody(String clearBody) {
        this.clearBody = clearBody;
    }

    public Article getParentArticle() {
        return parentArticle;
    }

    public void setParentArticle(Article parentArticle) {
        this.parentArticle = parentArticle;
    }

    //END SG METHODS


    //BEGIN SUPPORT METHODS
    public void copy(Article article) {
        if (article == null)
            return;
        this.author        = article.author;
        this.id            = article.id;
        this.title         = article.title;
        this.clearBody     = article.clearBody;
        this.body          = article.body;
        this.parentArticle = article.parentArticle;
        this.images        = article.images;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        boolean res= true;
        Article comp = (Article)other;
        res &= this.author.equals(comp.author);
        res &= this.id == comp.id;
        res &= this.title.equals(comp.title);
        res &= this.clearBody.equals(comp.clearBody);
        res &= this.body.equals(comp.body);
        if (this.images == comp.images && comp.images != null)
            res &= this.images.equals(comp.images);
        if (this.parentArticle == comp.parentArticle && comp.parentArticle != null)
            res &= this.parentArticle.getId() == comp.parentArticle.getId();
        return res;
    }

    //END SUPPORT METHODS

}
