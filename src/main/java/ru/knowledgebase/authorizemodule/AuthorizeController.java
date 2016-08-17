package ru.knowledgebase.authorizemodule;

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
public class AuthorizeController {
    public static Token authorize(String login, String password){
        User user = null;
        DataCollector collector = new DataCollector();
        try {
            user = collector.findUserByCredentials(login, password);
            if (user == null){
                throw new Exception("User not found");
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Token authorizeLdap(String login, String password) {
        LdapAnswer res = LdapController.getInstance().authorize(login, password);
        if (res == LdapAnswer.OK){
            return authorize(login, password);
        }
        return null;
    }
}
