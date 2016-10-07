package ru.knowledgebase.dbmodule.repositories.userrepositories;

/**
 * Created by root on 17.08.16.
 */

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select u from users u where u.login = ?1")
    public List<User> findByLogin(String login) throws Exception;

    @Query("from users")
    public List<User> getAll() throws Exception;

    @Transactional
    @Modifying
    @Query("update users set superVisorId = ?2 where superVisorId = ?1 and superVisorId != id")
    public Integer updateSuperVisor(Integer oldId, Integer newId) throws Exception;
}

