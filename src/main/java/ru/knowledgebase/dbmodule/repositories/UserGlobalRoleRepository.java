package ru.knowledgebase.dbmodule.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.GlobalRole;
import ru.knowledgebase.modelsmodule.UserArticleRole;
import ru.knowledgebase.modelsmodule.UserGlobalRole;

/**
 * Created by vova on 20.08.16.
 */
public interface UserGlobalRoleRepository  extends CrudRepository<UserGlobalRole, Integer> {
}
