package ru.knowledgebase.usermodule;

import org.apache.commons.codec.digest.DigestUtils;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.User;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongUserDataException;
import ru.knowledgebase.usermodule.ldapmodule.LdapController;

/**
 * Created by vova on 18.08.16.
 */
public class UserEditController {
    static void changePassword(String login, String newPass) throws Exception{
        if (newPass.length() == 0)
            throw new WrongUserDataException();
        newPass = DigestUtils.md5Hex(newPass);
        DataCollector collector = new DataCollector();
        User user = collector.findUserByLogin(login);
        if (user == null)
            throw new UserNotFoundException();
        user.setPassword(newPass);
        collector.updateUser(user);
        LdapController.getInstance().changePass(login, newPass);
    }
}
