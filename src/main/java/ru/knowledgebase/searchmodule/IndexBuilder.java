package ru.knowledgebase.searchmodule;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import ru.knowledgebase.configmodule.Config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created by Мария on 19.09.2016.
 */
public class IndexBuilder {
    public void buildIndices(){
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) (Config.getContext()).getBean("entityManagerFactory");
        EntityManager em = entityManagerFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.out.println("Unable to create indices");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        IndexBuilder indexBuilder = new IndexBuilder();
        indexBuilder.buildIndices();
    }
}
