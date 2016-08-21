package ru.knowledgebase.dbmodule.dataservices.userservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.userrepositories.UserRepository;
import ru.knowledgebase.modelsmodule.usermodels.User;

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

    public User findByLogin(String login) throws Exception{
        List<User> res = userRepository.findByLogin(login);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    public User findById(int id) throws Exception{
        return userRepository.findOne(id);
    }

    @Transactional
    public void update(User user) throws Exception{
        //User oldUser = userRepository.findOne(new Long(user.getId()));
     //   User oldUser = userRepository.findOne1(user.getId()).get(0);
      //  oldUser.copy(user);
        userRepository.save(user);
    }

    @Transactional
    public void delete(User user) throws Exception{
        userRepository.delete(user);
    }
}
