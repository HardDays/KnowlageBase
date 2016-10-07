package ru.knowledgebase.dbmodule.repositories.rolerepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.rolemodels.Role;

import java.util.List;

/**
 * Created by vova on 07.10.16.
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {

    @Query("from Role")
    public List<Role> getAll() throws Exception;

    @Query("from Role where roleId = ?1")
    public List<Role> findByRoleId(int id);

}