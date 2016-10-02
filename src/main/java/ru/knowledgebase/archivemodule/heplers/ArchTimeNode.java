package ru.knowledgebase.archivemodule.heplers;

import java.sql.Timestamp;

/**
 * Store this objects in collection of times to make process of moving to archive
 * easier
 * Created by root on 19.09.16.
 */
public class ArchTimeNode {

    private int articleId;
    private Timestamp time;

    public ArchTimeNode(int id, Timestamp time) {
        this.articleId = id;
        this.time = time;
    }

    public ArchTimeNode (){}

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
