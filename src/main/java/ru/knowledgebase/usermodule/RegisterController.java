package ru.knowledgebase.usermodule;

/**
 * Created by vova on 17.08.16.
 */

import org.apache.commons.codec.digest.DigestUtils;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.usermodule.exceptions.UserAlreadyExistsException;
import ru.knowledgebase.usermodule.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.User;

/**
 * Created by vova on 17.08.16.
 */
public class RegisterController {
    public static void register(String login, String password) throws Exception{
        DataCollector collector = new DataCollector();
        password = DigestUtils.md5Hex(password);
        try {
            collector.addUser(new User(login, password));
        }catch(org.springframework.dao.DataIntegrityViolationException e){
            throw new UserAlreadyExistsException();
        }catch (Exception e){
            throw e;
        }
        LdapController.getInstance().createUser(login, password, "User");

    }
}
