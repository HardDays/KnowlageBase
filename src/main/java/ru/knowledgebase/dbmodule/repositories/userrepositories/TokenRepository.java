package ru.knowledgebase.dbmodule.repositories.userrepositories;

/**
 * Created by root on 17.08.16.
 */

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.knowledgebase.modelsmodule.usermodels.Token;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;
public interface TokenRepository extends CrudRepository<Token, Integer> {
    @Query("from Token where user = ?1")
    public List<Token> getUserToken(User user) throws Exception;
}
