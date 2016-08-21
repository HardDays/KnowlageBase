package ru.knowledgebase.dbmodule.repositories.userrepositories;

/**
 * Created by root on 17.08.16.
 */

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select u from users u where u.login = ?1")
    public List<User> findByLogin(String login) throws Exception;

}

