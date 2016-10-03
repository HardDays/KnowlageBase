package ru.knowledgebase.dbmodule.dataservices.roleservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.rolerepositories.UserGlobalRoleRepository;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

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

    @Transactional
    public UserGlobalRole find(User user) throws Exception{
        List<UserGlobalRole> res = userGlobalRoleRepository.find(user);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    @Transactional
    public UserGlobalRole find(int userId) throws Exception{
        List<UserGlobalRole> res = userGlobalRoleRepository.find(userId);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    @Transactional
    public void delete(UserGlobalRole role) throws Exception{
        userGlobalRoleRepository.delete(role);
    }

    @Transactional
    public void delete(int id) throws Exception{
        userGlobalRoleRepository.delete(id);
    }

    @Transactional
    public void deleteByUser(int userId) throws Exception{
        userGlobalRoleRepository.deleteByUser(userId);
    }
}