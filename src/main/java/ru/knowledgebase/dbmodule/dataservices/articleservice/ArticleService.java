package ru.knowledgebase.dbmodule.dataservices.articleservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.ES.articlerepositories.ArticleESRepository;
import ru.knowledgebase.dbmodule.repositories.ES.articlerepositories.ArticleESRepositoryImpl;
import ru.knowledgebase.dbmodule.repositories.JPA.articlerepository.ArticleConnectionRepository;
import ru.knowledgebase.dbmodule.repositories.JPA.articlerepository.ArticleJPARepository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.articlemodels.ArticleConnection;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Transactional(propagation = Propagation.REQUIRED)
@Service
public class ArticleService {

    @Autowired
    private ArticleJPARepository articleRepository;
    @Autowired
    private ArticleESRepository articleESRepository;


    @Autowired
    private ArticleConnectionRepository articleConnectionRepository;

    public Article create(Article article) {
        Article ar = articleRepository.save(article);
        ArticleConnection ac = new ArticleConnection(ar.getParentArticle(),
                    ar.getId());
        articleConnectionRepository.save(ac);
        articleESRepository.save(ar);
        return ar;
    }


    public Article findById(int articleId){
        return articleRepository.findOne(articleId);
    }

    public Article update(Article article) {
        Article oldActicle = articleRepository.findOne(article.getId());
        oldActicle.copy(article);
        articleESRepository.save(oldActicle);
        return articleRepository.save(oldActicle);
    }

    public void delete(Integer id) {
        List<Integer> deleteArticleOrder = getArticleHierarchyTree(id);
        List<ArticleConnection> deleteArticleConnection = getAllConnectionsOfList(deleteArticleOrder);

        for (int i = deleteArticleOrder.size()-1; i >= 0; i--) {
            articleRepository.delete(deleteArticleOrder.get(i));
            articleESRepository.delete(deleteArticleOrder.get(i));
        }

        for (ArticleConnection ac : deleteArticleConnection) {
            articleConnectionRepository.delete(ac);
        }
    }

    public boolean exists(int id) {
        return articleRepository.exists(id);
    }

    public List<Article> findByTitle(String title) {
        return articleESRepository.findByTitle(title);
    }

    public List<Article> getAll(){ return (List)articleRepository.findAll(); }

    /** Should be optimized!
     * Get all first-line children for current article
     * @param articleId
     * @return
     */

    public List<Integer> getChildrenIds(int articleId) {
        List<ArticleConnection> ids = articleConnectionRepository.findByParentId(articleId);
        List<Integer> articles = new LinkedList<>();
        for (ArticleConnection i : ids) {
            articles.add(i.getChildId());
        }
        return articles;
    }

    public Article getParentArticle(Article article) {
        return articleRepository.findOne(article.getId());
    }

    private List<ArticleConnection> getAllConnections(int articleId) {
        return articleConnectionRepository.findConnectionsById(articleId);
    }

    /**
     * Get all connections, where ids in list are children
     * @param ids
     * @return list of parents for ids articles
     */
    private List<ArticleConnection> getAllConnectionsOfList(List<Integer> ids) {
        List<ArticleConnection> connections = new LinkedList<>();
        for (Integer id : ids) {
            List<ArticleConnection> ac = articleConnectionRepository.findConnectionsById(id);
            if (ac != null) {
                connections.addAll(ac);
            }
        }
        return connections;
    }

    public List<Integer> getArticleHierarchyTree(int rootId) {
        List<Integer> tree = new LinkedList<>();
        Queue<Integer> queue = new LinkedList<>();

        tree.add(rootId);
        queue.add(rootId);

        //BFS to get order for delete operation
        while(!queue.isEmpty()) {
            Integer curId = queue.poll();
            List<Integer> articleChildren = getChildrenIds(curId);
            if (articleChildren != null) {
                tree.addAll(articleChildren);
                queue.addAll(articleChildren);
            }
        }

        return tree;
    }

    public Article getBaseArticle() {
        return articleRepository.findOne(-1);
    }

    public boolean isSection(int articleId) throws Exception {
        return articleRepository.isSection(articleId);
    }
}