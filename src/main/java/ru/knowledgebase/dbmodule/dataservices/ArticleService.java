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
    public void create(Article a) throws Exception{
        articleRepository.save(a);
    }

    public List<Article> getAll() throws Exception{
        return articleRepository.getAll();
    }

    public Article find(int id) throws Exception{
        return articleRepository.findOne(id);
    }

    public void delete(Article article) throws Exception{
        articleRepository.delete(article);
    }
}