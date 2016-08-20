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
public class UserEditController{
    public static void changePassword(User user, String newPass) throws Exception {
        if (newPass.length() == 0)
            throw new WrongUserDataException();
        newPass = DigestUtils.md5Hex(newPass);
        DataCollector collector = new DataCollector();
        if (user == null)
            throw new UserNotFoundException();
        user.setPassword(newPass);
        collector.updateUser(user);
        LdapController.getInstance().changePass(user.getLogin(), newPass);
    }

    public static void changePassword(int id, String newPass) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUserById(id);
        changePassword(user, newPass);
    }

    public static void changePassword(String login, String newPass) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUserByLogin(login);
        changePassword(user, newPass);
    }
}
