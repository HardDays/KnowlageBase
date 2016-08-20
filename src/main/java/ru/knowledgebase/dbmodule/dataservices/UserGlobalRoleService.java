package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.UserGlobalRoleRepository;
import ru.knowledgebase.modelsmodule.UserGlobalRole;

/**
 * Created by vova on 20.08.16.
 */
@Service("userGlobalRoleService")
public class UserGlobalRoleService {

    @Autowired
    private UserGlobalRoleRepository userGlobalRoleRepository;

    @Transactional
    public void create(UserGlobalRole role) throws Exception{
        userGlobalRoleRepository.save(role);
    }
}