package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.dbmodule.repositories.UserRepository;
import ru.knowledgebase.modelsmodule.User;

import java.util.List;

/**
 * Created by root on 17.08.16.
 */

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean create(User user) throws Exception{
        userRepository.save(user);
        return true;
    }

    public User findByCredentials(String login, String password) throws Exception{
        List<User> res = userRepository.findByCredentials(login, password);
        for (User u : res){
            System.out.println(u.getId());
        }
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }
}
