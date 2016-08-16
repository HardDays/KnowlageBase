package ru.knowledgebase.dbmodule.dataservices;

import ru.knowledgebase.dbmodule.models.*;

import java.util.List;

/**
 * Created by root on 10.08.16.
 */
public interface MediaService {

    public void save(Article a);
    public void save(Users a);

    public List<Article> getAll();
}
