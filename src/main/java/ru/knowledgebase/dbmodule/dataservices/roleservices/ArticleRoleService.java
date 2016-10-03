package ru.knowledgebase.dbmodule.dataservices.roleservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.rolerepositories.ArticleRoleRepository;
import ru.knowledgebase.modelsmodule.rolemodels.ArticleRole;

import java.util.List;

/**
 * Created by vova on 19.08.16.
 */
@Service("articleRoleService")
public class ArticleRoleService {

    @Autowired
    private ArticleRoleRepository articleRoleRepository;

    @Transactional
    public ArticleRole create(ArticleRole articleRole) throws Exception{
        return articleRoleRepository.save(articleRole);
    }

    public List<ArticleRole> getAll() throws Exception{
        return articleRoleRepository.getAll();
    }

    public ArticleRole find(String name) throws Exception{
        List<ArticleRole> res = articleRoleRepository.findByName(name);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    public ArticleRole find(int id) throws Exception{
        return articleRoleRepository.findOne(id);
    }

    public void update(ArticleRole role) throws Exception{
        articleRoleRepository.save(role);
    }

    public void delete(ArticleRole role) throws Exception{
        articleRoleRepository.delete(role);
    }

    public void delete(int id) throws Exception{
        articleRoleRepository.delete(id);
    }

}
