package ru.knowladgebase.dbmodule.DAO;

import org.springframework.stereotype.Repository;
import ru.knowladgebase.dbmodule.models.Article;
import ru.knowladgebase.dbmodule.models.Users;

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
