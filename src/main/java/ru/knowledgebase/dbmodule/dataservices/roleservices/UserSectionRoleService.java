package ru.knowledgebase.dbmodule.dataservices.roleservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.rolerepositories.UserSectionRoleRepository;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.UserSectionRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by vova on 07.10.16.
 */
@Service("userSectionRoleService")
public class UserSectionRoleService {

    @Autowired
    private UserSectionRoleRepository userSectionRoleRepository;

    private HashMap<Integer, HashSet<Integer>> userSections;

    public List<UserSectionRole> getAll() throws Exception{
        return userSectionRoleRepository.getAll();
    }

    @Transactional
    public void create(UserSectionRole role) throws Exception{
        userSectionRoleRepository.save(role);
    }

    @Transactional
    public UserSectionRole find(User user, Article article) throws Exception{
        List<UserSectionRole> res = userSectionRoleRepository.find(user, article);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    @Transactional
    public UserSectionRole find(int userId, int articleId) throws Exception{
        List<UserSectionRole> res = userSectionRoleRepository.find(userId, articleId);
        if (res.size() == 1){
            return res.get(0);
        }
        return null;
    }

    @Transactional
    public List <User> findMistakeViewers(Article article) throws Exception{
        return userSectionRoleRepository.findMistakeViewers(article);
    }

    @Transactional
    public void delete(UserSectionRole role) throws Exception{
        userSectionRoleRepository.delete(role);
    }

    public List<UserSectionRole> findByArticle(int articleId){
        return userSectionRoleRepository.findByArticle(articleId);
    }

    @Transactional
    public void delete(int id) throws Exception{
        userSectionRoleRepository.delete(id);
    }

    @Transactional
    public void delete(int userId, int articleId) throws Exception{
        userSectionRoleRepository.delete(userId, articleId);
    }
}