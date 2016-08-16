package ru.knowledgebase.dbmodule.DAO;

import org.springframework.stereotype.Repository;
import ru.knowledgebase.dbmodule.models.Article;
import ru.knowledgebase.dbmodule.models.Users;

import java.util.List;

/**
 * Created by root on 10.08.16.
 */

@Repository
public interface MediaDAO {
    public void save(Article a);
    public void save(Users u);
    public List<Article> getAll();
}
