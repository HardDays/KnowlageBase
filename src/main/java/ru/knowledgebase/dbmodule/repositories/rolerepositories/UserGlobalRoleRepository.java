package ru.knowledgebase.dbmodule.repositories.rolerepositories;

import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;

/**
 * Created by vova on 20.08.16.
 */
public interface UserGlobalRoleRepository  extends CrudRepository<UserGlobalRole, Integer> {
}
