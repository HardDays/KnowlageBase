package ru.knowledgebase.modelsmodule;

import javax.persistence.*;

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

    @Column(length = 100, nullable = false)
    private String title;
    @Column
    private String body;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private User author;

    public Article(){}

    public Article(int id){
        this.id = id;
    }

    public Article(String t, String s, User u) {
        title = t;
        body = s;
        author = u;
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




}
