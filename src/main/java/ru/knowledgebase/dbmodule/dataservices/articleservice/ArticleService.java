package ru.knowledgebase.dbmodule.dataservices.articleservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.articlerepositories.ArticleRepository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;


import javax.persistence.EntityManagerFactory;
import java.util.List;
@Service("articleService")
public class ArticleService {


    @Autowired
    private ArticleRepository articleRepository;


    public Article create(Article article) {
        return articleRepository.save(article);
    }

    public Article findById(int articleId){
        return articleRepository.findOne(articleId);
    }


    public Article update(Article article) {
        Article oldActicle = articleRepository.findOne(article.getId());
        oldActicle.copy(article);
        return articleRepository.save(oldActicle);
    }


    public void delete(Integer id) {
        articleRepository.delete(id);
    }


    public boolean exists(int id) {
        return articleRepository.exists(id);
    }


    public List<Article> findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }


    public List<Article> getAll(){ return articleRepository.getAll(); }
}