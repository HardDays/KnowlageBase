package ru.knowledgebase.usermodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.ldapmodule.LdapController;

/**
 * Created by vova on 18.08.16.
 */
public class UserDeleteController {
    public static void delete(String login) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUserByLogin(login);
        if (user == null)
            throw new UserNotFoundException();
        Token token = collector.getUserToken(user);
        if (token != null)
            collector.deleteToken(token);
        collector.deleteUser(user);
        LdapController.getInstance().deleteUser(login);
    }
}
