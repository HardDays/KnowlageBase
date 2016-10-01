package ru.knowledgebase.modelsmodule.articlemodels;

import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

import java.sql.Timestamp;
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

    @Column
    private boolean isSection;

    private int parentId;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.REMOVE})
    private List<UserArticleRole> userArticleRole;

    /**
     * Date when article should be moved to Archive.
     */
    private Timestamp lifeTime;

    private Timestamp createdTime;

    private Timestamp updatedTime;

    private int sectionId;

    //BEGIN CONSTRUCTORS
    public Article(){
    }

    public Article(int id) {
        this.id = id;
    }

    public Article(String title, String body, String clearBody,
                   User author, int parentId, Timestamp createdDate,
                   Timestamp updatedTime, Timestamp lifeTime, boolean isSection) {
        this.title         = title;
        this.body          = body;
        this.clearBody     = clearBody;
        this.author        = author;
        this.parentId      = parentId;
        this.lifeTime      = lifeTime;
        this.isSection     = isSection;
        this.createdTime   = createdDate;
        this.updatedTime   = updatedTime;

    }

    //END CONSTRUCTORS


    //BEGIN SG METHODS

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdDate) {
        this.createdTime = createdDate;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setSection(boolean section) {
        isSection = section;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public int getParentArticle() {
        return parentId;
    }

    public void setParentArticle(int parentId) {
        this.parentId = parentId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getSectionId() {
        return this.sectionId;
    }

    public Timestamp getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(Timestamp lifeTime) {
        this.lifeTime = lifeTime;
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
        this.parentId      = article.parentId;
        this.lifeTime      = article.lifeTime;
        this.isSection     = article.isSection;
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
        res &= this.lifeTime == comp.lifeTime;
        res &= this.clearBody.equals(comp.clearBody);
        res &= this.body.equals(comp.body);
        res &= this.parentId == comp.parentId;
        res &= this.isSection == comp.isSection;
        return res;
    }


    //END SUPPORT METHODS

}
