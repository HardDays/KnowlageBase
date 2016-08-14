package ru.knowladgebase.DataServices;

import ru.knowladgebase.models.*;

import java.util.List;

/**
 * Created by root on 10.08.16.
 */
public interface MediaService {

    public void save(Article a);
    public void save(User a);

    public List<Article> getAll();
}
