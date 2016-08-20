package ru.knowledgebase.dbmodule.repositories;

/**
 * Created by vova on 19.08.16.
 */
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.GlobalRole;
import ru.knowledgebase.modelsmodule.User;


public interface GlobalRoleRepository extends CrudRepository<GlobalRole, Integer> {

    @Query("from GlobalRole")
    public List<GlobalRole> getAll() throws Exception;

    @Query("select g from GlobalRole g where g.name = ?1")
    public List<GlobalRole> findByName(String name) throws Exception;
}