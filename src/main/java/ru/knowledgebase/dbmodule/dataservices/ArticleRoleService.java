package ru.knowledgebase.dbmodule.dataservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.ArticleRoleRepository;
import ru.knowledgebase.modelsmodule.ArticleRole;

import java.util.List;

/**
 * Created by vova on 19.08.16.
 */
@Service("articleRoleService")
public class ArticleRoleService {

    @Autowired
    private ArticleRoleRepository articleRoleRepository;

    @Transactional
    public void create(ArticleRole articleRole) throws Exception{
        articleRoleRepository.save(articleRole);
    }

    public List<ArticleRole> getAll() throws Exception{
        return articleRoleRepository.getAll();
    }

    public ArticleRole findByName(String name) throws Exception{
        List<ArticleRole> res = articleRoleRepository.findByName(name);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }
}
