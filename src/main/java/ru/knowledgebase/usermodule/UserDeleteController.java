package ru.knowledgebase.usermodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.modelsmodule.User;
import ru.knowledgebase.usermodule.ldapmodule.LdapController;

/**
 * Created by vova on 18.08.16.
 */
public class UserDeleteController {
    public static void delete(String login) throws Exception{
        DataCollector collector = new DataCollector();
        User user = collector.findUserByLogin(login);
        collector.deleteToken(collector.getUserToken(user));
        collector.deleteUser(user);
        LdapController.getInstance().deleteUser(login);
    }
}
