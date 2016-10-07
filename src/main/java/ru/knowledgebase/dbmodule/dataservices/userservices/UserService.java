package ru.knowledgebase.dbmodule.dataservices.userservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.dataservices.chunkrequest.ChunkRequest;
import ru.knowledgebase.dbmodule.repositories.userrepositories.UserRepository;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 17.08.16.
 */

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User user) throws Exception{
        return userRepository.save(user);
    }

    public List<User> getAll() throws Exception{
        return userRepository.getAll();
    }

    public List<User> getAll(int offset, int limit) throws Exception{
        return userRepository.getWithOffset(new ChunkRequest(offset, limit));
    }

    public User find(String login) throws Exception{

        List<User> res = userRepository.findByLogin(login);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    public User find(int id) throws Exception{
        return userRepository.findOne(id);
    }

    public List<User> findBySuperVisor(int superVisorId) throws Exception{
        return userRepository.findBySuperVisor(superVisorId);
    }

    public List<User> findBySuperVisor(int superVisorId, int offset, int limit) throws Exception{
        return userRepository.findBySuperVisorOffset(superVisorId, new ChunkRequest(offset, limit));
    }

    @Transactional
    public void update(User user) throws Exception{
        userRepository.save(user);
    }

    @Transactional
    public Integer updateSuperVisor(Integer oldId, Integer newId) throws Exception{
        return userRepository.updateSuperVisor(oldId, newId);
    }

    @Transactional
    public void delete(User user) throws Exception{
        userRepository.delete(user);
    }

    @Transactional
    public void delete(int id) throws Exception{
        userRepository.delete(id);
    }
}
