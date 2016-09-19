package ru.knowledgebase.dbmodule.dataservices.searchservices;
import ru.knowledgebase.configmodule.Config;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by Мария on 13.09.2016.
 */
@Repository
public class SearchService {
    private static SearchService instance;

    /**
     * Searcher as thread-safe singleton
     * @return
     */
    public static SearchService getInstance() {
        SearchService localInstance = instance;
        if (localInstance == null) {
            synchronized (SearchService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SearchService();
                }
            }
        }
        return localInstance;
    }

    public List<Article> searchByBody(String searchRequest) {
        FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();
        QueryBuilder queryBuilder = getQueryBuilder(fullTextEntityManager);

        // a very basic query by keywords
        org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .fuzzy()
                            .withEditDistanceUpTo(2)
                            .withPrefixLength(1)
                        .onFields("clearBody")
                        .matching(searchRequest)
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Article.class);

        // execute search and return results (sorted by relevance as default)
        List results = jpaQuery.getResultList();

        return results;
    }

    public List<Article> searchByTitle(String searchRequest) {
        FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();
        QueryBuilder queryBuilder = getQueryBuilder(fullTextEntityManager);

        // a very basic query by keywords
        org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .fuzzy()
                            .withEditDistanceUpTo(2)
                            .withPrefixLength(1)
                        .onFields("title")
                        .matching(searchRequest)
                        .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Article.class);

        // execute search and return results (sorted by relevance as default)
        List results = jpaQuery.getResultList();

        return results;
    }

    private QueryBuilder getQueryBuilder(FullTextEntityManager fullTextEntityManager) {
        // create the query using Hibernate Search query DSL
        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Article.class).get();
    }

    private FullTextEntityManager getFullTextEntityManager(){
        EntityManagerFactory factory = (EntityManagerFactory) (Config.getContext()).getBean("entityManagerFactory");
        EntityManager entityManager = factory.createEntityManager();
        // get the full text entity manager
        return org.hibernate.search.jpa.Search.
                getFullTextEntityManager(entityManager);
    }
}
