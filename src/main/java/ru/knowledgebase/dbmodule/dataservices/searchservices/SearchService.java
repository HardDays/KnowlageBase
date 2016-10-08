package ru.knowledgebase.dbmodule.dataservices.searchservices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.articlemodule.TextProcessor;
import ru.knowledgebase.dbmodule.repositories.articlerepositories.ArticleRepository;
import ru.knowledgebase.exceptionmodule.analyticsexceptions.ParseKeywordException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.*;

/**
 * Created by Мария on 13.09.2016.
 */
@Service("searchService")
public class SearchService {
    private TextProcessor textProcessor = new TextProcessor();

    @Autowired
    ArticleRepository articleRepository;

    public List<Article> searchByBody(String searchRequest, List<Integer> sections, int numArticles) throws ParseKeywordException {
        HashMap<String, ArticleCount> titleArticle = new HashMap<>();
        for(String word : textProcessor.getKeywords(searchRequest)){
            for(Article article : articleRepository.findByClearBody(word)){
                saveArticle(sections, titleArticle, article);
            }
        }
        return getSortedListOfArticles(numArticles, titleArticle);
    }

    public List<Article> searchByTitle(String searchRequest, List<Integer> sections, int numArticles) throws ParseKeywordException {
        HashMap<String, ArticleCount> titleArticle = new HashMap<>();
        for(String word : textProcessor.getKeywords(searchRequest)){
            for(Article article : articleRepository.findByTitle(word)){
                saveArticle(sections, titleArticle, article);
            }
        }
        return getSortedListOfArticles(numArticles, titleArticle);
    }

    private List<Article> getSortedListOfArticles(int numArticles, HashMap<String, ArticleCount> titleArticle) {
        List<ArticleCount> list = new LinkedList<>();
        list.addAll(titleArticle.values());
        Collections.sort(list);
        List<Article> articles = new LinkedList<>();
        for(ArticleCount ac : list.subList(0, numArticles)){
            articles.add(ac.getArticle());
        }
        return articles;
    }

    private void saveArticle(List<Integer> sections, HashMap<String, ArticleCount> titleArticle, Article article) {
        if(sections.contains(article.getSectionId())){
            String title = article.getTitle();
            if(titleArticle.containsKey(title)){
                titleArticle.get(title).increaseCount();
            }else{
                titleArticle.put(title, new ArticleCount(article, 1));
            }
        }
    }

    private class ArticleCount implements Comparable{
        private Article article;
        private Integer count;

        public ArticleCount(Article article, Integer count) {
            this.article = article;
            this.count = count;
        }

        public Article getArticle() {
            return article;
        }

        public void increaseCount() {
            this.count++;
        }

        public Integer getCount() {
            return count;
        }

        @Override
        public int compareTo(Object o) {
            if(this.count == ((ArticleCount)o).getCount()) return 0;
            if(this.count > ((ArticleCount)o).getCount()) return 1;
            else return -1;
        }
    }

//    @PersistenceContext(unitName = "EntityManagerFactory")
//    private EntityManager em;
//
//    @PersistenceContext
//    private FullTextEntityManager fullTextEntityManager;
//    public List<Article> searchByBody(String searchRequest) {
//        return hibernateSearchBy("clearBody", searchRequest);
//    }
//
//    public List<Article> searchByTitle(String searchRequest) {
//        return hibernateSearchBy("title", searchRequest);
//    }
//    private List hibernateSearchBy(String field, String searchRequest) {
//        FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();
//        QueryBuilder queryBuilder = getQueryBuilder(fullTextEntityManager);
//
//        // a very basic query by keywords
//        org.apache.lucene.search.Query query =
//                queryBuilder
//                        .keyword()
//                        .fuzzy()
//                            .withEditDistanceUpTo(2)
//                            .withPrefixLength(1)
//                        .onFields(field)
//                        .matching(searchRequest)
//                        .createQuery();
//
//        // wrap Lucene query in an Hibernate Query object
//        org.hibernate.search.jpa.FullTextQuery jpaQuery =
//                fullTextEntityManager.createFullTextQuery(query, Article.class);
//
//        // execute search and return results (sorted by relevance as default)
//        return jpaQuery.getResultList();
//    }
//
//    private QueryBuilder getQueryBuilder(FullTextEntityManager fullTextEntityManager) {
//        // create the query using Hibernate Search query DSL
//        return fullTextEntityManager.getSearchFactory()
//                .buildQueryBuilder().forEntity(Article.class).get();
//    }
//
//    private FullTextEntityManager getFullTextEntityManager(){
//        EntityManagerFactory factory = (EntityManagerFactory) (Config.getContext()).getBean("entityManagerFactory");
//        EntityManager entityManager = factory.createEntityManager();
//        // get the full text entity manager
//        return org.hibernate.search.jpa.Search.
//                getFullTextEntityManager(entityManager);
//    }
}
