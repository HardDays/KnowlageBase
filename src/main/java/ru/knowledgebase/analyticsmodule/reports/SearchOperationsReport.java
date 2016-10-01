package ru.knowledgebase.analyticsmodule.reports;

import ru.knowledgebase.analyticsmodule.rank.ArticleRank;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vova on 01.10.16.
 */
public class SearchOperationsReport {
    private Integer count;
    private HashMap<Integer, List<Integer>> popularArticles;
    private HashMap<Integer, HashMap<Integer, List<String>>>  userRequests;

    public void addArticles(Integer section, List<Integer> articles){
        popularArticles.put(section, articles);
    }

    public void addRequests(Integer section, HashMap<Integer, List<String>> requests){
        userRequests.put(section, requests);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public HashMap<Integer, List<Integer>> getPopularArticles() {
        return popularArticles;
    }

    public HashMap<Integer, HashMap<Integer, List<String>>> getUserRequests() {
        return userRequests;
    }

}