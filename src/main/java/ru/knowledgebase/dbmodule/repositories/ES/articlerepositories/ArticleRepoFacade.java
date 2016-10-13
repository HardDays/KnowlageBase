package ru.knowledgebase.dbmodule.repositories.ES.articlerepositories;

import org.springframework.data.repository.NoRepositoryBean;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.util.List;

/**
 * Created by Мария on 08.10.2016.
 */
@NoRepositoryBean
public interface ArticleRepoFacade {

    public List<Article> findByTitle(String title);
//    public List<Article> findByClearBody(String clearBody);

}
