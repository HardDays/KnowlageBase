package ru.knowledgebase.analyticsmodule.reports;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by vova on 01.10.16.
 */
public class EmployeesActionsReport {
    private Timestamp date;
    private Integer userId;
    private HashMap<Integer, Integer> articles;

    public EmployeesActionsReport(Timestamp date, Integer userId) {
        this.date = date;
        this.userId = userId;
    }

    public void addArticle(Integer article){
        if (articles.containsKey(article)){
            articles.put(article, articles.get(article) + 1);
        }else{
            articles.put(article, 1);
        }
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public HashMap<Integer, Integer> getArticles() {
        return articles;
    }

    public void setArticles(HashMap<Integer, Integer> articles) {
        this.articles = articles;
    }
}