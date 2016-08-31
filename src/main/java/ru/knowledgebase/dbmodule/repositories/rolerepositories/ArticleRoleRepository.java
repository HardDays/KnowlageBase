package ru.knowledgebase.dbmodule.repositories.rolerepositories;

/**
 * Created by vova on 19.08.16.
 */
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;


public interface ArticleRoleRepository extends CrudRepository<ArticleRole, Integer> {

    @Query("from ArticleRole")
    public List<ArticleRole> getAll() throws Exception;

    @Query("select r from ArticleRole r where r.name = ?1")
    public List<ArticleRole> findByName(String name) throws Exception;

}