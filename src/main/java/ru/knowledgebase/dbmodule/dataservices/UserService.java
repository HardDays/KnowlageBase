package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.dbmodule.repositories.UserRepository;
import ru.knowledgebase.modelsmodule.User;

/**
 * Created by root on 17.08.16.
 */

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean create(User user) {
        try {
            userRepository.save(user);
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }
}
