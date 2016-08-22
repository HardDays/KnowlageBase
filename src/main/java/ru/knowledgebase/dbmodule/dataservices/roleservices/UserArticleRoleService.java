package ru.knowledgebase.dbmodule.dataservices.roleservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.rolerepositories.UserArticleRoleRepository;
import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by vova on 19.08.16.
 */
@Service("userArticleRoleService")
public class UserArticleRoleService {

    @Autowired
    private UserArticleRoleRepository userArticleRoleRepository;

    @Transactional
    public void create(UserArticleRole role) throws Exception{
        userArticleRoleRepository.save(role);
    }

    @Transactional
    public UserArticleRole find(User user, Article article) throws Exception{
        List<UserArticleRole> res = userArticleRoleRepository.find(user, article);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }
}