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


public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select u from users u where u.login = ?1")
    public List<User> findByLogin(String login) throws Exception;

    @Query("select u from users u where u.id = ?1")
    public List<User> findOne1(int id) throws Exception;
}

