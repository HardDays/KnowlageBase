package ru.knowledgebase.dbmodule.repositories;

/**
 * Created by root on 17.08.16.
 */

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select u from users u where u.login = ?1 and u.password = ?2")
    public List<User> findByCredentials(String login, String password) throws Exception;

}

