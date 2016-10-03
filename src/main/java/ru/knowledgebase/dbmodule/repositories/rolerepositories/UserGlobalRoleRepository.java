package ru.knowledgebase.dbmodule.repositories.rolerepositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by vova on 20.08.16.
 */
public interface UserGlobalRoleRepository  extends CrudRepository<UserGlobalRole, Integer> {
    @Query("select r from UserGlobalRole r where  r.user =?1")
    public List<UserGlobalRole> find(User user) throws Exception;

    @Query("select r from UserGlobalRole r where  r.user.id =?1")
    public List<UserGlobalRole> find(int userId) throws Exception;

    @Modifying
    @Transactional
    @Query("delete from UserGlobalRole where user.id = ?1")
    void deleteByUser(int userId);

}
