package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.data.jpa.repository.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.ArticleRepository;
import ru.knowledgebase.modelsmodule.Article;


import java.util.List;

@Service("articleService")
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Transactional
    public Article create(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public Article findById(int articleId){
        return articleRepository.findOne(articleId);
    }

    @Transactional
    public Article update(Article article) {
        Article oldActicle = articleRepository.findOne(article.getId());
        oldActicle.copy(article);
        return articleRepository.save(oldActicle);
    }

    @Transactional
    public void delete(Integer id) {
        articleRepository.delete(id);
    }

    @Transactional
    public boolean exists(int id) {
        return articleRepository.exists(id);
    }

    @Transactional
    public List<Article> findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Transactional
    public List<Article> getAll(){ return articleRepository.getAll(); }
}