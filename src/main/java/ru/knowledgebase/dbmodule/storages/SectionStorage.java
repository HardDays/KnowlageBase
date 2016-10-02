package ru.knowledgebase.dbmodule.storages;

import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 01.10.16.
 */
public class SectionStorage {
    private static SectionStorage instance;

    /**
     * Controller as thread-safe singleton
     * @return
     */
    public static SectionStorage getInstance() {
        SectionStorage localInstance = instance;
        if (localInstance == null) {
            synchronized (SectionStorage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SectionStorage();
                }
            }
        }
        return localInstance;
    }

    private HashMap<Integer, LinkedList<Integer>> sectionMap = new HashMap<>();
    private HashMap<Integer, Article> cache = new HashMap<>();

    //BEGIN SECTION MAP PUBLIC METHODS
    public List<Integer> getNextLevel(Integer section) {
        List<Integer> sections = this.sectionMap.get(section);
        if (sections == null) {
            return new LinkedList<>();
        }
        else {
            return sections;
        }
    }

    public void addSectionToMap(Integer parentSection, Integer currentSection) {
        LinkedList<Integer> sections = this.sectionMap.get(parentSection);
        if (sections == null) {
            sections = new LinkedList<>();
        }
        sections.add(currentSection);
        this.sectionMap.put(parentSection, sections);
    }

    public void addSectionsToMap(Integer parentSection, List<Integer> currentSection) {
        LinkedList sections = this.sectionMap.get(parentSection);
        sections.addAll(currentSection);
        this.sectionMap.put(parentSection, sections);
    }

    public void setSectionsToMap(HashMap<Integer, LinkedList<Integer>> sections) {
        this.sectionMap = sections;
    }

    /**
     * Delete keys from root to leafs
     * Delete root section id from root parent list
     * @param parent
     * @param sections
     */
    public void deleteSectionsFromMap(Integer parent, List<Integer> sections) {
        for (Integer s : sections) {
            this.sectionMap.remove(s);
        }
        Integer rootSection = sections.get(0);
        if (parent == null) {
            //its base article
            return;
        }
        List<Integer> parentList = sectionMap.get(parent);
        int toDelete = -1;
        for (int i = 0; i < parentList.size(); i++) {
            if (parentList.get(i).equals(rootSection)) {
                toDelete = i;
                break;
            }
        }
        parentList.remove(toDelete);
        this.sectionMap.put(parent, (LinkedList<Integer>)parentList);
    }


    //END SECTION MAP PUBLIC METHODS

    //BEGIN CACHE METHODS
    public void deleteSectionsFromCache(List<Article> sections) {
        for (Article a : sections) {
            this.cache.remove(a.getId());
        }
    }

    public void addToCache(Article section) {
        this.cache.put(section.getId(), section);
    }

    public void addSectionsToCache(List<Article> sections) {
        for (Article a : sections) {
            this.cache.put(a.getId(), a);
        }
    }

    public Article getSectionFromCache(int articleId) {
        return this.cache.get(articleId);
    }

    public void updateArticleInCache(Article article) {
        this.cache.remove(article.getId());
        this.cache.put(article.getId(), article);
    }
    //END CACHE METHODS
}
