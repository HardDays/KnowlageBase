package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.UserArticleRoleRepository;
import ru.knowledgebase.modelsmodule.UserArticleRole;

/**
 * Created by vova on 19.08.16.
 */
@Service("userArticleRoleService")
public class UserArticleRoleService {

    @Autowired
    private UserArticleRoleRepository userArticleRoleRepository;

    @Transactional
    public void create(UserArticleRole role) throws Exception{
        userArticleRoleRepository.save(role);
    }
}