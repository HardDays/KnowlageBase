package ru.knowledgebase.dbmodule.dataservices.roleservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.rolerepositories.GlobalRoleRepository;
import ru.knowledgebase.modelsmodule.rolemodels.GlobalRole;

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

    public GlobalRole findByName(String name) throws Exception{
        List<GlobalRole> res = globalRoleRepository.findByName(name);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    public GlobalRole findById(int id) throws Exception{
        return globalRoleRepository.findOne(id);
    }
}
