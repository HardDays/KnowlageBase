package ru.knowledgebase.usermodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.usermodule.exceptions.UserNotFoundException;
import ru.knowledgebase.usermodule.exceptions.WrongPasswordException;
import ru.knowledgebase.usermodule.ldapmodule.LdapController;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Date;

/**
 * Created by vova on 17.08.16.
 */
public class AuthorizeController {
    public static Token authorize(String login, String password) throws Exception{
        User user = null;
        DataCollector collector = new DataCollector();
        password = DigestUtils.md5Hex(password);
        user = collector.findUserByLogin(login);
        if (user == null){
            throw new UserNotFoundException();
        }else if (!user.getPassword().equals(password)){
            throw new WrongPasswordException();
        }
        Date date = new Date(new java.util.Date().getTime());
        String tokenStr = DigestUtils.md5Hex(user.getLogin() + date.toString());
        Token token = new Token(user, tokenStr, date);
        Token oldToken = collector.getUserToken(user);
        if (oldToken == null){
            collector.addToken(token);
        }else{
            token.setId(oldToken.getId());
            collector.updateToken(token);
        }
        return token;
    }

    public static Token authorizeLdap(String login, String password) throws Exception{
        LdapController.getInstance().authorize(login, DigestUtils.md5Hex(password));
        return authorize(login, password);
    }
}
