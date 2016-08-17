package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.dbmodule.repositories.TokenRepository;
import ru.knowledgebase.dbmodule.repositories.UserRepository;
import ru.knowledgebase.modelsmodule.Token;
import ru.knowledgebase.modelsmodule.User;

/**
 * Created by root on 17.08.16.
 */

@Service("tokenService")
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public void create(Token token) {
        tokenRepository.save(token);
    }

    public boolean exist(Token token) {
        return tokenRepository.exists(token.getId());
    }
}
