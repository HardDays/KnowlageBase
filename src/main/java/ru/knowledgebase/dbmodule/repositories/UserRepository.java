package ru.knowledgebase.dbmodule.repositories;

/**
 * Created by root on 17.08.16.
 */

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.User;


public interface UserRepository extends CrudRepository<User, Integer> {
}
