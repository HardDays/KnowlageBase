package ru.knowledgebase.dbmodule.repositories.rolerepositories;

import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;

/**
 * Created by vova on 19.08.16.
 */
public interface UserArticleRoleRepository extends CrudRepository<UserArticleRole, Integer> {

}
