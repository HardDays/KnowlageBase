package ru.knowledgebase.dbmodule.repositories.rolerepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by vova on 20.08.16.
 */
public interface UserGlobalRoleRepository  extends CrudRepository<UserGlobalRole, Integer> {
    @Query("select r from UserGlobalRole r where  r.user =?1")
    public List<UserGlobalRole> find(User user) throws Exception;

}
