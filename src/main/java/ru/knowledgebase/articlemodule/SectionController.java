package ru.knowledgebase.articlemodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.sectionexceptions.NoSectionsException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 30.09.16.
 */
public class SectionController {
    private DataCollector dataCollector = DataCollector.getInstance();

    private static SectionController instance;

    /**
     * Controller as thread-safe singleton
     * @return
     */
    public static SectionController getInstance() {
        SectionController localInstance = instance;
        if (localInstance == null) {
            synchronized (SectionController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SectionController();
                }
            }
        }
        return localInstance;
    }

    //BEGIN PUBLIC METHODS

    /**
     *
     * @param articleId
     * @return section id
     * @throws Exception
     */
    @Deprecated
    public Integer getArticleSection(int articleId) throws Exception {
        Article section = null;
        try {
            section = dataCollector.findArticle(articleId);
        }
        catch (Exception ex) {
            throw new NoSectionsException();
        }
        return section.getSectionId();
    }

    /**
     * Next level of sections
     * @param sectionId
     * @return list of section articles
     * @throws Exception
     */
    public List<Article> getNextLevelSections(int sectionId) throws Exception{
        List<Article> sections = null;
        try {
            boolean section = dataCollector.isArticleSection(sectionId);
            if (section) {
                sections = dataCollector.getNextLevelSections(sectionId);
            }
            else {
                throw new NoSectionsException();
            }
        }
        catch (Exception ex) {
            throw new NoSectionsException();
        }
        return sections;
    }

    public List<Article> getSectionTree(int sectionId) throws Exception {
        List<Article> sections = null;
        try {
            Article section = dataCollector.findArticle(sectionId);
            if (section.isSection()) {
                sections = dataCollector.getSectionTree(section.getId());
            }
            else {
                throw new NoSectionsException();
            }
        }
        catch (Exception ex) {
            throw new NoSectionsException();
        }
        return sections;
    }

    public HashMap<Integer, HashMap<Article, List<Article>>> getSectionHierarchy() throws Exception {
        HashMap<Integer, HashMap<Article, List<Article>>> sections = null;
        try {
            sections = dataCollector.getSectionHierarchy();
        }
        catch (Exception ex) {
            throw new NoSectionsException();
        }
        return sections;
    }
    //END PUBLIC METHODS
}
