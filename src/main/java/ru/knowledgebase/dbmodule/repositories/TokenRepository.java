package ru.knowledgebase.dbmodule.repositories;

/**
 * Created by root on 17.08.16.
 */

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, Integer> {
    @Query("from Token where user = ?1")
    public List<Token> getUserToken(User user) throws Exception;
}
