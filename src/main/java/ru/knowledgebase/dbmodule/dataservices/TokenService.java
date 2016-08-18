package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.TokenRepository;
import ru.knowledgebase.dbmodule.repositories.UserRepository;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;

import java.util.List;

/**
 * Created by root on 17.08.16.
 */

@Service("tokenService")
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public void create(Token token) throws Exception{
        if (!exist(token)) {
            tokenRepository.save(token);
        }
    }

    public boolean exist(Token token) throws Exception{
        return tokenRepository.exists(token.getId());
    }

    public Token getUserToken(User user) throws Exception{
        List<Token> tokens = tokenRepository.getUserToken(user);
        if (tokens.size() == 1)
            return tokens.get(0);
        return null;
    }

    @Transactional
    public void update(Token token) throws Exception{
        Token oldToken = tokenRepository.findOne(token.getId());
        oldToken.copy(token);
        tokenRepository.save(oldToken);
    }

    @Transactional
    public void delete(Token token) throws Exception{
        tokenRepository.delete(token);
    }
}
