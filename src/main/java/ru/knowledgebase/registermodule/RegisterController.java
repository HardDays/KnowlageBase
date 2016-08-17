package ru.knowledgebase.registermodule;

/**
 * Created by vova on 17.08.16.
 */

import org.apache.commons.codec.digest.DigestUtils;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.ldapmodule.LdapAnswer;
import ru.knowledgebase.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;

import java.sql.Date;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.ldapmodule.LdapAnswer;
import ru.knowledgebase.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Date;

/**
 * Created by vova on 17.08.16.
 */
public class RegisterController {
    public static void register(String login, String password){
        DataCollector collector = new DataCollector();
        try {
            boolean res = collector.addUser(new User(login, password));
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        LdapController.getInstance().createUser(login, password, "User");

    }
}
