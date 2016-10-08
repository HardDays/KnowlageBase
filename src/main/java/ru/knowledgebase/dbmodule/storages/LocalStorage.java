package ru.knowledgebase.dbmodule.storages;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 01.10.16.
 */
public class LocalStorage {
    private static LocalStorage instance;

    /**
     * Controller as thread-safe singleton
     * @return
     */
    public static LocalStorage getInstance() {
        LocalStorage localInstance = instance;
        if (localInstance == null) {
            synchronized (LocalStorage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new LocalStorage();
                }
            }
        }
        return localInstance;
    }

    private SectionStorage sectionStorage = SectionStorage.getInstance();
    private SectionRoleStorage sectionRoleStorage = SectionRoleStorage.getInstance();
    private Article baseArticle = null;

    //BEGIN SECTION METHODS

    public List<Integer> getNextLevelSection(Integer section) {
        return sectionStorage.getNextLevel(section);
    }

    public void addSection(Article section) {
        sectionStorage.addSectionToMap(section.getParentArticle(), section.getId());
        sectionStorage.addToCache(section);
    }

    public void addSections(Integer parentSection, List<Article> sections) {
        List<Integer> ids = new LinkedList<>();
        for (Article a : sections) {
            ids.add(a.getId());
        }
        sectionStorage.addSectionsToMap(parentSection, ids);
        sectionStorage.addSectionsToCache(sections);
    }

    /**
     * Get tree of section by root section (@id).
     * We need to delete all sections under root section and root section
     * from list of section of its parent
     * @param parent
     * @param sections
     */
    public void deleteSections(Integer parent, List<Article> sections) {
        List<Integer> ids = new LinkedList<>();
        for (Article a : sections) {
            ids.add(a.getId());
        }
        sectionStorage.deleteSectionsFromMap(parent, ids);
        sectionStorage.deleteSectionsFromCache(sections);
    }

    public Article getSectionFromCache(int articleId) {
        return sectionStorage.getSectionFromCache(articleId);
    }

    public void updateArticleInCache(Article article) {
        sectionStorage.updateArticleInCache(article);
    }

    //END SECTION METHODS

    //BEGIN SectionRole METHODS

    public void add(int userId, int sectionId) throws Exception {
        sectionRoleStorage.add(userId, sectionId);
    }

    public HashSet<Integer> getSections(int userId) throws Exception {
        return sectionRoleStorage.getSections(userId);
    }

    //END SectionRole METHODS

    //BEGIN LOCAL METHODS
    public Article getBaseArticle() {
        return baseArticle;
    }

    public void setBaseArticle(Article baseArticle) {
        this.baseArticle = baseArticle;
    }
    //END LOCAL METHODS


    //BEGIN INIT ACTIONS
    public void initSectionMapStorage(HashMap<Integer, LinkedList<Integer>> sectionMap) throws Exception{
        sectionStorage.setSectionsToMap(sectionMap);
    }

    public void initSectionRoleStorage(List<UserArticleRole> userArticleRoles) throws Exception{
        for (UserArticleRole role : userArticleRoles) {
            sectionRoleStorage.add(role.getUser().getId(), role.getArticle().getId());
        }
    }


    //END INIT ACTIONS
}
