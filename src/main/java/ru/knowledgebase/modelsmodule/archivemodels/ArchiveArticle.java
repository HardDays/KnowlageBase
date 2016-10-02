package ru.knowledgebase.modelsmodule.archivemodels;

import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by root on 12.09.16.
 */

@Entity
public class ArchiveArticle {


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

    private int sectionId;

    //BEGIN CONSTRUCTORS
    public ArchiveArticle(){
    }

    public ArchiveArticle(String title, String body, String clearBody,
                          User author, Timestamp lifeTime) {
        this.title         = title;
        this.body          = body;
        this.clearBody     = clearBody;
        this.author        = author;

    }

    //END CONSTRUCTORS

    //BEGIN SG METHODS
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getClearBody() {
        return clearBody;
    }

    public void setClearBody(String clearBody) {
        this.clearBody = clearBody;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }
    //END SG METHODS

    //BEGIN SUPPORT METHODS
    public void copy(ArchiveArticle article) {
        if (article == null)
            return;
        this.author        = article.author;
        this.id            = article.id;
        this.title         = article.title;
        this.clearBody     = article.clearBody;
        this.body          = article.body;
        this.sectionId      = article.sectionId;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        boolean res= true;
        ArchiveArticle comp = (ArchiveArticle)other;
        res &= this.author.equals(comp.author);
        res &= this.id == comp.id;
        res &= this.title.equals(comp.title);
        res &= this.clearBody.equals(comp.clearBody);
        res &= this.body.equals(comp.body);
        res &= this.sectionId == comp.sectionId;
        return res;
    }

    //END SUPPORT METHODS


}
