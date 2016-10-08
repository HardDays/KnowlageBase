package ru.knowledgebase.modelsmodule.articlemodels;

import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;

/**
 * Created by root on 12.09.16.
 */

@Entity
public class News {
    @Id
    @SequenceGenerator(name="article_id_seq",
            sequenceName="article_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="article_id_seq")
    private int id;

    @Column(length = 256)
    private String title;

    @Column(length = 10000)
    private String body;

    @Column(length = 10000)
    private String clearBody;

    private int sectionId;

    @ManyToOne
    private User author;

    public News(){}

    public News(String title, String body, String clearBody, User author, int sectionId) {
        this.title = title;
        this.body = body;
        this.clearBody = clearBody;
        this.author = author;
        this.sectionId = sectionId;
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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
