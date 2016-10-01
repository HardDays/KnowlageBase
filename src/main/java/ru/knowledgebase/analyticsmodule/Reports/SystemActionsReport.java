package ru.knowledgebase.analyticsmodule.Reports;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by vova on 01.10.16.
 */
public class SystemActionsReport {
    private Integer count;
    private HashMap<Integer, HashSet<Integer>> sectionArticles;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public HashMap<Integer, HashSet<Integer>> getSectionArticles() {
        return sectionArticles;
    }

    public void addSectionArticles(Integer section, HashSet<Integer> articles) {
        this.sectionArticles.put(section, articles);
    }
}
