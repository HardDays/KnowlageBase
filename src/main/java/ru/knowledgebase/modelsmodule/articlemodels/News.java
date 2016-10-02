package ru.knowledgebase.modelsmodule.articlemodels;

import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

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

    private Timestamp createDate;

    public News(){}

    public News(String title, String body, String clearBody, User author,
                                    int sectionId, Timestamp createDate) {
        this.title = title;
        this.body = body;
        this.clearBody = clearBody;
        this.author = author;
        this.sectionId = sectionId;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
