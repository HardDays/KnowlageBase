package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.GlobalRoleRepository;
import ru.knowledgebase.dbmodule.repositories.SectionRoleRepository;
import ru.knowledgebase.modelsmodule.GlobalRole;
import ru.knowledgebase.modelsmodule.SectionRole;

import java.util.List;

/**
 * Created by vova on 19.08.16.
 */
@Service("globalRoleService")
public class GlobalRoleService {

    @Autowired
    private GlobalRoleRepository globalRoleRepository;

    @Transactional
    public void create(GlobalRole globalRole) throws Exception{
        globalRoleRepository.save(globalRole);
    }

    public List<GlobalRole> getAll() throws Exception{
        return globalRoleRepository.getAll();
    }
}
