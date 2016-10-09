package ru.knowledgebase.modelsmodule.articlemodels;

import javax.persistence.*;

/**
 * Created by root on 11.09.16.
 */
@Entity
public class ArticleConnection {
    @Id
    @SequenceGenerator(name="articlecon_id_seq",
            sequenceName="articlecon_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="articlecon_id_seq")
    private int id;

    @Column
    private int parentId;

    @Column
    private int childId;

    public ArticleConnection() {}

    public ArticleConnection(int parentId, int childId) {
        this.parentId = parentId;
        this.childId = childId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }
}
