package ru.knowledgebase.dbmodule.dataservices.roleservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.rolerepositories.RoleRepository;
import ru.knowledgebase.modelsmodule.rolemodels.Role;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by vova on 07.10.16.
 */
@Service("roleService")
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public Role create(Role role) throws Exception{
        return roleRepository.save(role);
    }

    public List<Role> getAll() throws Exception{
        return roleRepository.getAll();
    }

    public Role find(int id) throws Exception{
        return roleRepository.findOne(id);
    }

    public Role findByRoleId(int id) throws Exception{
        List<Role> res = roleRepository.findByRoleId(id);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    public void update(Role role) throws Exception{
        roleRepository.save(role);
    }

    public void delete(Role role) throws Exception{
        roleRepository.delete(role);
    }

    public void delete(int id) throws Exception{
        roleRepository.delete(id);
    }

}