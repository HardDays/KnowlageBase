package ru.knowledgebase.dbmodule.repositories.ES.articlerepositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.List;

/**
 * Created by Мария on 08.10.2016.
 */
public class ArticleESRepositoryImpl implements ArticleRepoFacade {


    @Autowired
    protected ElasticsearchTemplate elasticsearchTemplate;



    @Override
    public List<Article> findByTitle(String title) {
//        Criteria c = new Criteria("title").expression(title);//.fuzzy(title);
        Criteria c = new Criteria("title").fuzzy(title).or(new Criteria("title").expression(title));
        return elasticsearchTemplate.queryForList(new CriteriaQuery(c), Article.class);
    }

//    @Override
//    public List<Article> findByClearBody(String clearBody) {
//        return null;
//    }


}
