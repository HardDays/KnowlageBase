package ru.knowledgebase.dbmodule.dataservices.articleservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.articlerepositories.ArticleConnectionRepository;
import ru.knowledgebase.dbmodule.repositories.articlerepositories.ArticleRepository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.articlemodels.ArticleConnection;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service("articleService")
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleConnectionRepository articleConnectionRepository;

    public Article create(Article article) throws Exception {
        Article ar = articleRepository.save(article);
        ArticleConnection ac = new ArticleConnection(ar.getParentArticle(),
                    ar.getId());
        articleConnectionRepository.save(ac);
        return ar;
    }

    public Article findById(int articleId)throws Exception {
        return articleRepository.findOne(articleId);
    }

    public Article update(Article article) throws Exception {
        Article oldActicle = articleRepository.findOne(article.getId());
        oldActicle.copy(article);
        return articleRepository.save(oldActicle);
    }

    public void delete(Integer id) throws Exception {
        List<Integer> deleteArticleOrder = getArticleHierarchyTree(id);
        List<ArticleConnection> deleteArticleConnection = getAllConnectionsOfList(deleteArticleOrder);

        for (int i = deleteArticleOrder.size()-1; i >= 0; i--) {
            articleRepository.delete(deleteArticleOrder.get(i));
        }

        for (ArticleConnection ac : deleteArticleConnection) {
            articleConnectionRepository.delete(ac);
        }
    }

    public boolean exists(int id) throws Exception  {
        return articleRepository.exists(id);
    }

    public Iterable<Article> findByTitle(String title) throws Exception {
        return articleRepository.findByTitle(title);
    }

    public List<Article> getAll() throws Exception { return articleRepository.getAll(); }

    /** Should be optimized!
     * Get all first-line children for current article
     * @param articleId
     * @return
     */
    public List<Integer> getChildrenIds(int articleId) throws Exception {
        List<ArticleConnection> ids = articleConnectionRepository.findByParentId(articleId);
        List<Integer> articles = new LinkedList<>();
        for (ArticleConnection i : ids) {
            articles.add(i.getChildId());
        }
        return articles;
    }

    public Article getParentArticle(Article article) throws Exception {
        return articleRepository.findOne(article.getId());
    }

    private List<ArticleConnection> getAllConnections(int articleId) throws Exception {
        return articleConnectionRepository.findConnectionsById(articleId);
    }

    /**
     * Get all connections, where ids in list are children
     * @param ids
     * @return list of parents for ids articles
     */
    private List<ArticleConnection> getAllConnectionsOfList(List<Integer> ids) throws Exception {
        List<ArticleConnection> connections = new LinkedList<>();
        for (Integer id : ids) {
            List<ArticleConnection> ac = articleConnectionRepository.findConnectionsById(id);
            if (ac != null) {
                connections.addAll(ac);
            }
        }
        return connections;
    }

    public List<Integer> getArticleHierarchyTree(int rootId) throws Exception {
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

    public Article getBaseArticle() throws Exception {
        return articleRepository.getBaseArticle();
    }

    public boolean isSection(int articleId) throws Exception {
        return articleRepository.isSection(articleId);
    }
}