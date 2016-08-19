package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.UserSectionRoleRepository;
import ru.knowledgebase.modelsmodule.UserSectionRole;

/**
 * Created by vova on 19.08.16.
 */
@Service("userSectionRoleService")
public class UserSectionRoleService {

    @Autowired
    private UserSectionRoleRepository userSectionRoleRepository;

    @Transactional
    public void create(UserSectionRole category) throws Exception{
        userSectionRoleRepository.save(category);
    }
}